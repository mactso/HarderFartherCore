package com.mactso.harderfarthercore.utility;

import java.lang.reflect.Field;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.harderfarthercore.HarderFartherManager;
import com.mactso.harderfarthercore.config.MyConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.coremod.api.ASMAPI;

public class Boosts {

	private static Field fieldXpReward = null;
	private static final Logger LOGGER = LogManager.getLogger();

	static {
		// FD: net/minecraft/world/entity/Mob/f_21364_
		// net/minecraft/world/entity/Mob/xpReward
		try {
			String name = ASMAPI.mapField("f_21364_");
			fieldXpReward = Mob.class.getDeclaredField(name);
			fieldXpReward.setAccessible(true);
		} catch (Exception e) {
			LOGGER.error("XXX Unexpected Reflection Failure xpReward in Mob");
		}
	}

	private static UUID HF_HEALTH_BOOST_UUID = UUID.fromString("9ea04686-ff0c-4252-8de7-19b973c8567e");
	private static final ResourceLocation HF_HEALTH_BOOST_ID = ResourceLocation.tryBuild("harderfarthercore", "healthboost");

	private static UUID HF_MOVE_BOOST_UUID = UUID.fromString("5d5d890a-7e40-4214-b86a-34640749c334");
	private static final ResourceLocation HF_MOVE_BOOST_ID = ResourceLocation.tryBuild("harderfarthercore", "moveboost");

	private static UUID HF_ATK_BOOST_UUID  = UUID.fromString("18781eb6-da89-4b2e-b47b-a961ab7fafac");
	private static final ResourceLocation HF_ATK_BOOST_ID = ResourceLocation.tryBuild("harderfarthercore", "atkboost");

	private static UUID HF_KNOCKBACK_BOOST_UUID  = UUID.fromString("834d99ba-f10c-4cdf-ab86-580a08b8ac55");
	private static final ResourceLocation HF_KNOCKBACK_BOOST_ID = ResourceLocation.tryBuild("harderfarthercore", "kbboost");

	private static void boostHealth(LivingEntity le, String eDsc, float difficulty, Attribute attr,
			ResourceLocation boostId, UUID boostUUID) {

		if (!isEntityAttributeBoostable(le, eDsc, attr, boostId, boostUUID)) {
			return;
		}

		double originalValue = le.getHealth();
		double valueBoost = (MyConfig.getHpMaxPercent() * difficulty);
		valueBoost = limitHealthBoostByMob(valueBoost, le);
		le.getAttribute(attr)
				.addPermanentModifier(new AttributeModifier(boostUUID, boostId.toString(), valueBoost, Operation.MULTIPLY_BASE));
		le.setHealth(le.getMaxHealth());

		reportBoost(le, eDsc, boostId, originalValue, le.getMaxHealth());

	}

	private static void boostAtkDmg(LivingEntity le, String eDsc, float difficulty, Attribute attr,
			ResourceLocation boostId, UUID boostUUID) {

		if (!isEntityAttributeBoostable(le, eDsc, attr, boostId, boostUUID)) {
			return;
		}

		double originalValue = (float) le.getAttribute(attr).getValue();
		double valueBoost = (MyConfig.getAtkPercent() * difficulty);

		le.getAttribute(attr)
				.addPermanentModifier(new AttributeModifier(boostUUID, boostId.toString(), valueBoost, Operation.MULTIPLY_BASE));

		reportBoost(le, eDsc, boostId, originalValue, le.getAttribute(attr).getValue());

	}
	
	// note KnockBack Resistance ranges from 0 to 100% (0.0f to 1.0f)
		private static void boostKnockbackResistance(LivingEntity le, String eDsc, float difficulty, Attribute attr,
				ResourceLocation boostId, UUID boostUUID) {

			if (!isEntityAttributeBoostable(le, eDsc, attr, boostId, boostUUID)) {
				return;
			}

			double originalValue = (float) le.getAttribute(attr).getValue();
			if (originalValue == 0) { // assign a base KBR to mobs that don't have one to start with.
				originalValue = getKBRBoostByMob(le);
				le.getAttribute(attr).setBaseValue(originalValue);
			}
			double valueBoost = (MyConfig.getKnockBackPercent() * difficulty);

			le.getAttribute(attr)
					.addPermanentModifier(new AttributeModifier(boostUUID, boostId.toString(), valueBoost, Operation.MULTIPLY_BASE));

			reportBoost(le, eDsc, boostId, originalValue, le.getAttribute(attr).getValue());

		}
		
		
		private static void boostSpeed(LivingEntity le, String eDsc, float difficulty, Attribute attr,
				ResourceLocation boostId, UUID boostUUID) {

			if (!isEntityAttributeBoostable(le, eDsc, attr, boostId, boostUUID)) {
				return;
			}

			double originalValue = (float) le.getAttribute(attr).getValue();
			double valueBoost = (MyConfig.getSpeedPercent() * difficulty);
			if (le instanceof Zombie) {
				Zombie z = (Zombie) le;
				if (z.isBaby()) {
					valueBoost *= 0.5d;
				}
			}
			le.getAttribute(attr)
					.addPermanentModifier(new AttributeModifier(boostUUID, boostId.toString(), valueBoost, Operation.MULTIPLY_BASE));

			reportBoost(le, eDsc, boostId, originalValue, le.getAttribute(attr).getValue());

		}
		


	private static void reportBoost(LivingEntity le, String eDsc, ResourceLocation boostId, double originalValue,
			double newValue) {
		Utility.debugMsg(1, le,
				"--Boost " + eDsc + " " + boostId.getPath() + " from " + originalValue + " to " + newValue + ".");
	}

	private static boolean isEntityAttributeBoostable(LivingEntity le, String eDsc, Attribute attr,
			 ResourceLocation boostId, UUID boostUUID) {
		if (le.getAttribute(attr) == null) {
			Utility.debugMsg(1, le, "erBoost " + eDsc + boostId.getPath() + " attribute is null  .");
			return false;
		}

		if (le.getAttribute(attr).getModifier(boostUUID) != null) {
			return false;
		}
		return true;
	}

	private static boolean boostXp(LivingEntity le, String eDsc, float distanceModifier) {
		try {
			int preXp = fieldXpReward.getInt(le);
			fieldXpReward.setInt(le, (int) (fieldXpReward.getInt(le) * (1.0f + distanceModifier)));
			Utility.debugMsg(2, le,
					"--Boost " + eDsc + " Xp increased from (" + preXp + ") to (" + fieldXpReward.getInt(le) + ")");
		} catch (Exception e) {
			LOGGER.error("XXX Unexpected Reflection Failure getting xpReward");
			return false;
		}
		return true;
	}

	public static boolean isBoostable(LivingEntity le) {

		Utility.debugMsg(2, "is entity boostable? (" + le.tickCount + " ticks old)");

		if ((le instanceof Monster) && (le.tickCount > 0) && le.tickCount < 120) {
			if (le.getAttribute(Attributes.MAX_HEALTH) == null) {
				return false;
			}
			if (le.getAttribute(Attributes.MAX_HEALTH).getModifier(HF_HEALTH_BOOST_UUID) != null) {
				return false;
			} 
			Utility.debugMsg(1, "entity can be boosted.");
			return true;
		}
		Utility.debugMsg(2, "entity not a monster or 0 ticks old.");
		return false;
	}

	private static double limitHealthBoostByMob(double valueBoost, LivingEntity entity) {

		// give some mobs more bonus hit points and some mobs less bonus hit points.
		if (entity instanceof Zombie) {
			Zombie z = (Zombie) entity;
			if (z.isBaby()) {
				valueBoost *= 0.6f;
			}
		} else if (entity instanceof CaveSpider) {
			valueBoost *= 0.5f; // lower boost
		} else if (entity instanceof Spider) {
			valueBoost *= 1.10f; // higher boost
		} else if (entity instanceof Creeper) {
			valueBoost *= 0.85f; // lower boost
		} else if (EntityType.getKey((entity.getType())).toString().equals("nasty:skeleton")) {
			valueBoost *= 0.1f; // much lower boost they are self boosted.
		} else if (entity instanceof AbstractSkeleton) {
			valueBoost *= 0.9f;
		}
		return valueBoost;
	}

	// UUID MAX_SPEED = UUID.fromString("5d5d890a-7e40-4214-b86a-34640749c334");
	// UUID MAX_ATTACK = UUID.fromString("18781eb6-da89-4b2e-b47b-a961ab7fafac");
	// UUID MAX_KNOCKBACK = UUID.fromString("834d99ba-f10c-4cdf-ab86-580a08b8ac55");

	public static void doBoostAbilities(LivingEntity le, String eDsc) {

		Utility.debugMsg(2, "doBoosts");

		float difficulty = HarderFartherManager.getDifficulty(le);
		if (difficulty == 0.0f) {
			return;
		}

		if (!isBoostable(le))  
			return;

		if (fieldXpReward == null) { // should not fail except when developing a new version or if someone removed
			// this field.
			return;
		}

		if (MyConfig.isHealthBoosted())
			boostHealth(le, eDsc, difficulty, Attributes.MAX_HEALTH, HF_HEALTH_BOOST_ID, HF_HEALTH_BOOST_UUID);

		if (MyConfig.isSpeedBoosted())
			boostSpeed(le, eDsc, difficulty, Attributes.MOVEMENT_SPEED, HF_MOVE_BOOST_ID,HF_MOVE_BOOST_UUID);

		if (MyConfig.isAtkDmgBoosted())
			boostAtkDmg(le, eDsc, difficulty, Attributes.ATTACK_DAMAGE, HF_ATK_BOOST_ID, HF_ATK_BOOST_UUID);

		if (MyConfig.isKnockBackBoosted())
			boostKnockbackResistance(le, eDsc, difficulty, Attributes.KNOCKBACK_RESISTANCE, HF_KNOCKBACK_BOOST_ID, HF_KNOCKBACK_BOOST_UUID);

		boostXp(le, eDsc, difficulty);
	}

	private static float getKBRBoostByMob(LivingEntity le) {

		float kbrBoost = 0;
		// give some mobs default knockback boosts.
		if (le instanceof Zombie) {
			kbrBoost = .45f;
		} else if (le instanceof CaveSpider) {
			kbrBoost = 0.05f; // lower boost
		} else if (le instanceof Spider) {
			kbrBoost = .6f; // higher boost
		} else if (le instanceof Creeper) {
			kbrBoost = 0.2f; // lower boost
		} else if (EntityType.getKey(le.getType()).toString().equals("nasty:skeleton")) {
			kbrBoost = 0.2f;
		} else if (le instanceof AbstractSkeleton) {
			kbrBoost = 0.3f;
		} else if (le.getMaxHealth() < 10) {
			kbrBoost = 0.05f;
		} else if (le.getMaxHealth() < 40) {
			kbrBoost = 0.2f;
		} else {
			kbrBoost = 0.35f;
		}
		return kbrBoost * 0.4f;
	}

}
