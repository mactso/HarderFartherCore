package com.mactso.harderfarthercore.command;

import java.util.Iterator;
import java.util.List;

import com.mactso.harderfarthercore.config.MyConfig;
import com.mactso.harderfarthercore.network.Network;
import com.mactso.harderfarthercore.network.SyncFogToClientsPacket;
import com.mactso.harderfarthercore.utility.Utility;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class MyCommands {


	private static void printColorInfo(ServerPlayer p) {

		String chatMessage = "\nFog Color Current Values";
		Utility.sendBoldChat(p, chatMessage, ChatFormatting.DARK_GREEN);
		chatMessage = "R (" + MyConfig.getFogRedPercent() + ")" + " G (" + MyConfig.getFogGreenPercent() + ")"
				+ " B (" + MyConfig.getFogBluePercent() + ")";
		Utility.sendChat(p, chatMessage, ChatFormatting.GREEN);

	}




	
	
	
	private static void printInfo(ServerPlayer p) {

		String dimensionName = p.level().dimension().location().toString();

		String chatMessage = "\nDimension: " + dimensionName + "\n Current Values";
		Utility.sendBoldChat(p, chatMessage, ChatFormatting.DARK_GREEN);

		chatMessage = "  Harder Max Distance From Spawn....: " + MyConfig.getBoostMaxDistance() + " blocks."
				+ " blocks." + "\n  Debug Level .......................................................: "
				+ MyConfig.getDebugLevel() + "\n  Only In Overworld .........................................: "
				+ MyConfig.isOnlyOverworld()  ;
		Utility.sendChat(p, chatMessage, ChatFormatting.GREEN);

	}
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("harderfarthercore").requires((source) -> 
			{
				return source.hasPermission(3);
			}
		)
		.then(Commands.literal("setDebugLevel").then(
				Commands.argument("debugLevel", IntegerArgumentType.integer(0,2)).executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					return setDebugLevel(p, IntegerArgumentType.getInteger(ctx, "debugLevel"));
				}))).then(Commands.literal("chunkReport").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					p.level().gatherChunkSourceStats();
					Utility.sendChat(p, "\nChunk\n" + p.level().gatherChunkSourceStats(), ChatFormatting.GREEN);
					return 1;
				})).then(Commands.literal("info").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					printInfo(p);
					return 1;
				})).then(Commands.literal("colorInfo").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					printColorInfo(p);
					return 1;
				})).then(Commands.literal("boostInfo").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					String chatMessage = "\nHarder Farther Maximum Monster Boosts";
					Utility.sendBoldChat(p, chatMessage, ChatFormatting.DARK_GREEN);

					chatMessage = "  Monster Health ..........................: " + MyConfig.getHpMaxBoost() + " %."
							+ "\n  Damage ..............................................: " + MyConfig.getAtkDmgBoost()
							+ " %." + "\n  Movement .........................................: "
							+ MyConfig.getSpeedBoost() + " %." + "\n  KnockBack Resistance .........: "
							+ MyConfig.getKnockBackMod() + " %.";
					Utility.sendChat(p, chatMessage, ChatFormatting.GREEN);
					return 1;
				})).then(Commands.literal("setFogColors")
						.then(Commands.argument("R", IntegerArgumentType.integer(0,100))
						.then(Commands.argument("G", IntegerArgumentType.integer(0,100))
						.then(Commands.argument("B", IntegerArgumentType.integer(0,100))
						.executes(ctx -> {
							ServerPlayer p = ctx.getSource().getPlayerOrException();
							int r = IntegerArgumentType.getInteger(ctx,"R");
							int g = IntegerArgumentType.getInteger(ctx,"G");
							int b = IntegerArgumentType.getInteger(ctx,"B");
							return setFogColors(p, r, g, b);
						}))))
				));

	}



	public static int setDebugLevel(ServerPlayer p, int newDebugLevel) {
		MyConfig.setDebugLevel(newDebugLevel);
		printInfo(p);
		return 1;
	}


	private static int setFogColors(ServerPlayer p, int r, int g, int b) {
		MyConfig.setFogRedPercent(r);
		MyConfig.setFogGreenPercent(g);
		MyConfig.setFogBluePercent(b);
		ServerLevel sl = (ServerLevel) p.level();
		updateGCFogToAllClients (sl, (double)r/100, (double)g/100, (double)b/100);
		printColorInfo(p);
		return 1;
	}
	
	private static void updateGCFogToAllClients(ServerLevel level, double r , double g , double b) {
		List<ServerPlayer> allPlayers = level.getServer().getPlayerList().getPlayers();
		Iterator<ServerPlayer> apI = allPlayers.iterator();
		SyncFogToClientsPacket msg = new SyncFogToClientsPacket(MyConfig.getFogSetting(),r,g,b);
		while (apI.hasNext()) { // sends to all players online.
			Network.sendToClient(msg, apI.next());
		}
	}
	
	String subcommand = "";
	
	String value = "";

}
