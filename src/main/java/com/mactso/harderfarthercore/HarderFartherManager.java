package com.mactso.harderfarthercore;

import java.util.ArrayList;
import java.util.List;

import com.mactso.harderfarthercore.config.MyConfig;
import com.mactso.harderfarthercore.network.Network;
import com.mactso.harderfarthercore.network.SyncDifficultyToClientsPacket;
import com.mactso.harderfarthercore.utility.Utility;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.storage.LevelData;

public class HarderFartherManager {

	private static int grimRange = 1500;
	private static List<BlockPos> grimBlockPosList = new ArrayList<>();

	private static int lifeRange = 500;
	private static List<BlockPos> lifeBlockPosList = new ArrayList<>();

	public static BlockPos SURFACE_CENTER_POS = BlockPos.containing(0, 128, 0);

	private static BlockPos TEST_LIFE_POS = BlockPos.containing(10000, 128, -10000);
	private static BlockPos TEST_GRIM_POS = BlockPos.containing(3000, 128, -3000);

	private static BlockPos worldSpawnPos = SURFACE_CENTER_POS;

	public static void addGrimBlockPosListEntry(BlockPos pos, int range) {
		grimBlockPosList.add(pos);
		setGrimRange(range);
	}

	public static void addLifeBlockPosListEntry(BlockPos pos, int range) {
		lifeBlockPosList.add(pos);
		setLifeRange(range);

	}

	public static float calcBasicDistanceDifficulty(BlockPos pos) {
		return 0.68f;
	}

	public static void delGrimBlockPosListEntry(BlockPos pos) {
		grimBlockPosList.remove(pos);
	}

	public static void delLifeBlockPosListEntry(BlockPos pos) {
		lifeBlockPosList.remove(pos);
	}

	private static BlockPos getClosestPos(List<BlockPos> list, BlockPos pos) {
		BlockPos closePos = null;
		double dist = Double.MAX_VALUE;
		for (BlockPos p : list) {
			if (pos.distSqr(p) < dist) {
				dist = pos.distSqr(p);
				closePos = p;
			}
		}
		return closePos;
	}

	public static float getDifficulty(BlockPos pos) {
		Utility.debugMsg(2, "HarderFartherManager.getdifficulty top");

		double difficultyHere = 0.0d;

		double lifeDifficulty = 0.0d;
		double lifeDistance = Double.MAX_VALUE;
		BlockPos lifePos = getClosestPos(lifeBlockPosList, pos);

		double grimDifficulty = 0.0d;
		double grimDistance = 0.0d;
		BlockPos grimPos = getClosestPos(grimBlockPosList, pos);

		double worldSpawnDifficulty = 0.0d;
		double worldSpawnDistance = 0.0d;

		worldSpawnDistance = getWorldSpawnDistance(worldSpawnPos, pos);
		worldSpawnDifficulty = getWorldSpawnDifficulty(pos, worldSpawnDistance);

		difficultyHere = worldSpawnDifficulty;

		if (!lifeBlockPosList.isEmpty()) {
			lifeDistance = getLifeDistance(pos);
			lifePos = getLifePos(pos);
			if (lifeDistance < lifeRange) {
				lifeDifficulty = Math.max(0.0D, 0.8 - (lifeRange - lifeDistance) / lifeRange);
				if (lifeDifficulty < difficultyHere) {
					difficultyHere = lifeDifficulty;
				}

			}
		}

		if (!grimBlockPosList.isEmpty()) {
			grimDistance = getGrimDistance(pos);
			if (grimDistance < grimRange) {
				grimDifficulty = (grimRange - grimDistance) / grimRange;
				if (grimDifficulty > difficultyHere) {
					difficultyHere = grimDifficulty;
				}

			}
		}

		System.out.println("(pos) Diff: " + difficultyHere);
		Utility.debugMsg(2, "HarderFartherManager.getdifficulty end returning " + difficultyHere);
		return (float) difficultyHere;
	}

	public static float getDifficulty(LivingEntity le) {

		Utility.debugMsg(2, "HarderFartherManager.getdifficulty (le) top");
		ServerLevel serverLevel = (ServerLevel) le.level();
		if (serverLevel.isClientSide()) {
			return 0.0f;
		}
		BlockPos pos = le.blockPosition();
		long gameTime = serverLevel.getGameTime();

		String dimensionName = serverLevel.dimension().location().toString();
		if (MyConfig.isDimensionOmitted(dimensionName)) {
			return 0.0f;
		}
		if (dimensionName.equals(Level.OVERWORLD.location().toString())) {
			if (worldSpawnPos == SURFACE_CENTER_POS) {
				worldSpawnPos = getWorldSpawnPos(serverLevel);
			}
		} else if (MyConfig.isOnlyOverworld()) {
			return 0.0f;
		}

		// TESTING
		if (grimBlockPosList.size() == 0) {
			addGrimBlockPosListEntry(TEST_GRIM_POS, 1750);
		}
		if (lifeBlockPosList.size() == 0) {
			addLifeBlockPosListEntry(TEST_LIFE_POS, 500);
		}
		// TESTING

		double difficultyHere = 0.0d;

		double lifeDifficulty = 0.0d;
		double lifeDistance = Double.MAX_VALUE;
		BlockPos lifePos = getClosestPos(lifeBlockPosList, pos);

		double grimDifficulty = 0.0d;
		double grimDistance = 0.0d;
		BlockPos grimPos = getClosestPos(grimBlockPosList, pos);

		double worldSpawnDifficulty = 0.0d;
		double worldSpawnDistance = 0.0d;

		worldSpawnDistance = getWorldSpawnDistance(worldSpawnPos, pos);
		worldSpawnDifficulty = getWorldSpawnDifficulty(pos, worldSpawnDistance);

		difficultyHere = worldSpawnDifficulty;

		if (!lifeBlockPosList.isEmpty()) {
			lifeDistance = getLifeDistance(pos);
			lifePos = getLifePos(pos);
			if (lifeDistance < lifeRange) {
				lifeDifficulty = Math.max(0.0D, 0.8 - (lifeRange - lifeDistance) / lifeRange);
				if (lifeDifficulty < difficultyHere) {
					difficultyHere = lifeDifficulty;
				}
				makeLifePointMarker(le, serverLevel, pos, gameTime);
			}
		}

		if (!grimBlockPosList.isEmpty()) {
			grimDistance = getGrimDistance(pos);
			if (grimDistance < grimRange) {
				grimDifficulty = (grimRange - grimDistance) / grimRange;
				if (grimDifficulty > difficultyHere) {
					difficultyHere = grimDifficulty;
				}

				makeGrimPointMarker(le, serverLevel, pos, gameTime);
			}
		}

//		System.out.println (le.getDisplayName().getString());
		if (le instanceof ServerPlayer sp) {
			System.out.println("HFM sending difficulty, spawn:" + worldSpawnDifficulty + " life:" + lifeDifficulty
					+ " grim:" + grimDifficulty);
			Utility.debugMsg(2, "getdifficulty here network message");

			SyncDifficultyToClientsPacket msg = new SyncDifficultyToClientsPacket((float) difficultyHere,
					(float) grimDifficulty, (float) lifeDifficulty);
			Network.sendToClient(msg, sp);

		}

		System.out.println("Diff: " + difficultyHere);
		Utility.debugMsg(2, "HarderFartherManager.getdifficulty end returning " + difficultyHere);
		return (float) difficultyHere;

	}

	private static double getGrimDistance(BlockPos pos) {
		double grimDistance;
		grimDistance = Math.sqrt(pos.distSqr(getClosestPos(grimBlockPosList, pos)));
		return grimDistance;
	}

	public static BlockPos getGrimPos(BlockPos pos) {
		return getClosestPos(lifeBlockPosList, pos);
	}

	public static int getGrimRange() {
		return grimRange;
	}

	private static double getLifeDistance(BlockPos pos) {
		double lifeDistance;
		lifeDistance = Math.sqrt(pos.distSqr(getClosestPos(lifeBlockPosList, pos)));
		return lifeDistance;
	}

	public static BlockPos getLifePos(BlockPos pos) {
		return getClosestPos(lifeBlockPosList, pos);
	}

	public static int getLifeRange() {
		return lifeRange;
	}

	private static double getWorldSpawnDifficulty(BlockPos pos, double worldSpawnDistance) {
		if (worldSpawnDistance > MyConfig.getBoostMinDistance()) {
			return (worldSpawnDistance - MyConfig.getBoostMinDistance()) / MyConfig.getBoostRange();
		}
		return 0.0d;
	}

	private static double getWorldSpawnDistance(BlockPos pos, BlockPos worldSpawnPos) {
		return Math.sqrt(worldSpawnPos.distSqr(pos));
	}

	// note there is no code to remove the old worldspawn.
	// but it will be lost when the game/server restarts.
	public static BlockPos getWorldSpawnPos(ServerLevel serverLevel) {

		LevelData winfo = serverLevel.getLevelData();
		return BlockPos.containing(winfo.getXSpawn(), winfo.getYSpawn(), winfo.getZSpawn());

	}

	private static void makeGrimPointMarker(LivingEntity le, ServerLevel serverLevel, BlockPos pos, long gameTime) {
		if ((gameTime % 20 == 2) && (le instanceof ServerPlayer)) {

			ServerPlayer sp = (ServerPlayer) le;
			BlockPos gpos = getClosestPos(grimBlockPosList, pos);
			double x = gpos.getX();
			double y = serverLevel.getHeightmapPos(Types.MOTION_BLOCKING_NO_LEAVES, pos).getY() + 8.0d;
			double z = gpos.getZ();
			boolean overrideLimiter = true;
			int count = 15;
			float xDist = 0.25f;
			float yDist = 0.25f;
			float zDist = 0.15f;
			float maxSpeed = 0.15f;
			serverLevel.sendParticles(sp, ParticleTypes.DRIPPING_LAVA, overrideLimiter, x, y, z, count, xDist, yDist,
					zDist, maxSpeed);

			count = 3;
			maxSpeed = 0.25f;

			serverLevel.sendParticles(sp, ParticleTypes.FALLING_OBSIDIAN_TEAR, overrideLimiter, x, y, z, count, xDist,
					yDist, zDist, maxSpeed);

		}
	}

	private static void makeLifePointMarker(LivingEntity le, ServerLevel serverLevel, BlockPos pos, long gameTime) {
		if ((gameTime % 20 == 2) && (le instanceof ServerPlayer)) {
			ServerPlayer sp = (ServerPlayer) le;
			BlockPos lpos = getClosestPos(lifeBlockPosList, pos);
			double x = lpos.getX();
			double y = serverLevel.getHeightmapPos(Types.MOTION_BLOCKING_NO_LEAVES, pos).getY() + 8.0d;
			double z = lpos.getZ();

			boolean overrideLimiter = true;

			int count = 15;

			float xDist = 0.05f;
			float yDist = 0.05f;
			float zDist = 0.05f;
			float maxSpeed = 0.05f;

			System.out.println("sent particles");
			serverLevel.sendParticles(sp, ParticleTypes.END_ROD, overrideLimiter, x, y, z, count, xDist, yDist, zDist,
					maxSpeed);
		}
	}

	public static void setGrimRange(int grimRange) {
		HarderFartherManager.grimRange = grimRange;
	}

	public static void setLifeRange(int lifeRange) {
		HarderFartherManager.lifeRange = lifeRange;
	}

	public static void setWorldSpawnPos(BlockPos pos) {
		worldSpawnPos = pos;
	}

}
