package com.mactso.harderfarthercore.config;

import java.util.Arrays;
import java.util.List;

//16.2 - 1.0.0.0 HarderFarther

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.harderfarthercore.Main;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MyConfig {




	private static final Logger LOGGER = LogManager.getLogger();
	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;

	static
	{
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}
	
	public static int getDebugLevel() {
		return debugLevel;
	}

	public static void setDebugLevel(int newValue) {
		if (newValue <0 || newValue > 2) // TODO: this should be redundant 
			newValue = 0;
		 debugLevel = newValue;
	}

	public static boolean isDimensionOmitted(String dimensionName) {
		return dimensionOmitList.contains(dimensionName);
	}
	
	public static boolean isHpMaxBoosted() {
		if (hpMaxBoost > 0) return true;
		return false;
	}

	public static boolean isSpeedBoosted() {
		if (speedBoost > 0) return true;
		return false;
	}

	public static boolean isAtkDmgBoosted() {
		if (atkDmgBoost > 0) return true;
		return false;
	}

	public static boolean isKnockBackBoosted() {
		if (knockbackBoost > 0) return true;
		return false;
	}

	public static int getBoostMaxDistance() {
		return boostMaxDistance;
	}

	public static int getBoostMinDistance() {
		return boostMinDistance;
	}
	
	public static int getBoostRange() {
		return Math.max(0, boostMaxDistance - boostMinDistance);
	}


	public static int getHpMaxBoost() {
		return hpMaxBoost;
	}

	public static int getSpeedBoost() {
		return speedBoost;
	}

	public static int getAtkDmgBoost() {
		return atkDmgBoost;
	}

	public static int getKnockBackMod() {
		return knockbackBoost;
	}

	public static float getHpMaxPercent() {
		return (float) (hpMaxBoost/100);
	}

	public static float getSpeedPercent()  {
		return ((float)speedBoost/100);
	}

	public static float getAtkPercent()  {
		return (float) (atkDmgBoost/100);
	}

	public static float getKnockBackPercent() {
		return (float) (knockbackBoost/100);
	}
	
	public static boolean isOnlyOverworld() {
		return onlyOverworld;
	}	
	
	
	public static double getFogRedPercent() {
		return fogRedPercent;
	}

	public static double getFogGreenPercent() {
		return fogGreenPercent;
	}

	public static double getFogBluePercent() {
		return fogBluePercent;
	}
	
	private static int      debugLevel;
	private static boolean  onlyOverworld;
	private static List<? extends String> dimensionOmitList;
	private static int      safeDistance;
	private static int 	    boostMaxDistance;
	private static int 	    boostMinDistance;
	private static int      minimumSafeAltitude;
	private static int      maximumSafeAltitude;
	private static int hpMaxBoost;
	private static int speedBoost;
	private static int atkDmgBoost;
	private static int knockbackBoost;
	private static double 	fogRedPercent;
	private static double 	fogGreenPercent;
	private static double 	fogBluePercent;

	@SubscribeEvent
	public static <ModConfig> void onModConfigEvent(final ModConfigEvent configEvent)
	{

		if (configEvent.getConfig().getSpec() == MyConfig.COMMON_SPEC)
		{
			bakeConfig();
		}
	}	
	
	public static void pushValues() {
		
	
		COMMON.debugLevel.set(debugLevel);

		COMMON.onlyOverworld.set(onlyOverworld);
		COMMON.dimensionOmitList.set(dimensionOmitList);
		
		COMMON.hpMaxBoost.set(hpMaxBoost);
		COMMON.speedBoost.set(speedBoost);
		COMMON.atkDmgBoost.set(atkDmgBoost);
		COMMON.knockbackBoost.set(knockbackBoost);
		
		COMMON.safeDistance.set(safeDistance );
		COMMON.minimumSafeAltitude.set(minimumSafeAltitude  );
		COMMON.maximumSafeAltitude.set(maximumSafeAltitude  );
		
		COMMON.fogRedPercent.set (fogRedPercent);
		COMMON.fogBluePercent.set (fogBluePercent);
		COMMON.fogGreenPercent.set (fogGreenPercent);
	}
	
	public static void bakeConfig()
	{
		debugLevel = COMMON.debugLevel.get();
		onlyOverworld = COMMON.onlyOverworld.get();

		dimensionOmitList = COMMON.dimensionOmitList.get();
		boostMaxDistance = COMMON.boostMaxDistance.get();
		boostMinDistance = COMMON.boostMinDistance.get();
		if (boostMinDistance >= boostMaxDistance) {
			LOGGER.error("ERROR: boostMinDistance should be less than boostMaxDistance.");
			LOGGER.error("ERROR: boostMinDistance will use (boostMaxDistance - 1).");
			boostMinDistance = boostMaxDistance-1;
			COMMON.boostMinDistance.set(boostMinDistance);
		}
		minimumSafeAltitude = COMMON.minimumSafeAltitude.get();
		maximumSafeAltitude = COMMON.maximumSafeAltitude.get();
		safeDistance =COMMON.safeDistance.get();

		
		
		hpMaxBoost=COMMON.hpMaxBoost.get();
		speedBoost=COMMON.speedBoost.get();
		atkDmgBoost=COMMON.atkDmgBoost.get();
		knockbackBoost=COMMON.knockbackBoost.get();


		


		
		fogRedPercent = COMMON.fogRedPercent.get();
		fogBluePercent = COMMON.fogBluePercent.get();
		fogGreenPercent = COMMON.fogGreenPercent.get();
		
		if (debugLevel > 0) {
			System.out.println("Harder Farther Debug Level: " + debugLevel );
		}
	}
	
	public static class Common {
		List<String> defDimensionOmitList = Arrays.asList(
				"minecraft:the_nether","minecraft:the_end");

		public final IntValue debugLevel;
		
		public final BooleanValue onlyOverworld;
		public final ConfigValue<List<? extends String>> dimensionOmitList;	
		public final IntValue boostMaxDistance;
		public final IntValue boostMinDistance;


		public final IntValue safeDistance;
		public final IntValue minimumSafeAltitude;
		public final IntValue maximumSafeAltitude;
		
		public final IntValue hpMaxBoost;
		public final IntValue speedBoost;
		public final IntValue atkDmgBoost;
		public final IntValue knockbackBoost;

		
		public final DoubleValue fogRedPercent;
		public final DoubleValue fogBluePercent;
		public final DoubleValue fogGreenPercent;

		public Common(ForgeConfigSpec.Builder builder) {
			
		builder.push("Harder Farther Control Values");
		builder.push("Debug Settings");			
		debugLevel = builder
				.comment("Debug Level: 0 = Off, 1 = Log, 2 = Chat+Log")
				.translation(Main.MODID + ".config." + "debugLevel")
				.defineInRange("debugLevel", () -> 0, 0, 2);
		builder.pop();
		builder.push("HarderFarther Settings");
		onlyOverworld= builder
				.comment("Only in minecraft Overworld (true) ")
				.translation(Main.MODID + ".config." + "onlyOverworld")
				.define ("onlyOverworld", () -> true);
		
		dimensionOmitList = builder
				.comment("Dimension Omit List")
				.translation(Main.MODID + ".config" + "dimensionOmitList")
				.defineList("dimensionOmitList", defDimensionOmitList, Common::isString);			
	
		boostMaxDistance = builder
				.comment("boostMaxDistance: Distance til Maximum Boost Values Applied")
				.translation(Main.MODID + ".config." + "boostMaxDistance")
				.defineInRange("boostMaxDistance", () -> 30000, 1000, 6000000);

		boostMinDistance = builder
				.comment("boostMinDistance: Distance til Boost Values Start.  Should be less than boostMaxDistance")
				.translation(Main.MODID + ".config." + "boostMinDistance")
				.defineInRange("boostMinDistance", () -> 1000, 64, 1000000);
		
		safeDistance = builder
				.comment("Worldspawn Safe Distance: No Hostile Creatures Will Spawn In this Range")
				.translation(Main.MODID + ".config." + "safeDistance")
				.defineInRange("safeDistance", () -> 64, 1, 1024);			

		minimumSafeAltitude = builder
				.comment("minimumSafeAltitude: Mobs are 6% tougher below this altitude. ")
				.translation(Main.MODID + ".config." + "minimumSafeAltitude")
				.defineInRange("minimumSafeAltitude", () -> 32, -32, 64);			

		maximumSafeAltitude = builder
				.comment("maximumSafeAltitude: Mobs are 9% tougher above this altitude.")
				.translation(Main.MODID + ".config." + "maximumSafeAltitude")
				.defineInRange("maximumSafeAltitude", () -> 99, 65, 256);			
		builder.pop();
		
		builder.push("Boost Settings");
		hpMaxBoost = builder
				.comment("Boost Max Hit Points (Percent) ")
				.translation(Main.MODID + ".config." + "hpMaxBoost")
				.defineInRange("hpMaxBoost", () -> 200, 0, 30000000);

		speedBoost = builder
				.comment("Boost Movement Speed (Percent). Very sensative setting. Over 50 kinda ridiculous ")
				.translation(Main.MODID + ".config." + "speedBoost")
				.defineInRange("speedBoost", () -> 20, 0, 999);
		
		atkDmgBoost = builder
				.comment("Boost Attack Damage (percent).  Mobs base damage is about 3 points + 2 for hard mode.")
				.translation(Main.MODID + ".config." + "atkDmgBoost")
				.defineInRange("atkDmgBoost", () -> 100, 0, 30000000);
		
		knockbackBoost = builder
				.comment("Boost Knockback Resistance (Percent) over 100 has no additional effect.")
				.translation(Main.MODID + ".config." + "knockbackBoost")
				.defineInRange("knockbackBoost", () -> 95, 0, 999);
		
		builder.pop();

		builder.push("Fog Color Settings");			
		fogRedPercent = builder
				.comment("fogRedPercent : Fog Red Component Multiplier")
				.translation(Main.MODID + ".config." + "fogRedPercent")
				.defineInRange("fogRedPercent", () -> 0.95, 0.0, 1.0);	

		fogBluePercent = builder
				.comment("fogBluePercent : Fog Blue Component Multiplier")
				.translation(Main.MODID + ".config." + "fogBluePercent")
				.defineInRange("fogBluePercent", () -> 0.05, 0.0, 1.0);	

		fogGreenPercent = builder
				.comment("fogGreenPercent : Fog Green Component Multiplier")
				.translation(Main.MODID + ".config." + "fogGreenPercent")
				.defineInRange("fogGreenPercent", () -> 0.05, 0.0, 1.0);	


		builder.pop();
		
	}
	

	public static boolean isString(Object o)
	{
		return (o instanceof String);
	}
}
		
}

