package com.mactso.harderfarthergrimcitadels;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.harderfarthercore.HarderFartherManager;
import com.mactso.harderfarthergrimcitadels.events.LivingEventMovementHandler;
import com.mactso.harderfarthergrimcitadels.proxy.CoreDifficultyCall;
import com.mactso.harderfarthergrimcitadels.proxy.IHarderFartherCoreProxy;
import com.mactso.harderfarthergrimcitadels.proxy.LocalDifficultyCall;

import net.minecraft.core.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod("harderfarthergrimcitadels")
public class Main {

	    public static final String MODID = "harderfarthergrimcitadels"; 
	    public static IHarderFartherCoreProxy difficultyCallProxy;
	    public static IHarderFartherCoreProxy tryDifficultyCallProxy;
		private static final Logger LOGGER = LogManager.getLogger();
	    public Main()
	    {
	    	System.out.println(MODID + ": Registering Mod.");
	    	
	    	
	    	if (ModList.get().isLoaded("harderfarthercore")) {
	    		difficultyCallProxy = new CoreDifficultyCall ();
	    	} else {
	    		difficultyCallProxy = new LocalDifficultyCall();
	    	}
	    	
	    	BlockPos pos = new BlockPos(1000, 1200, 1000);
	    	
	    	LOGGER.warn("1.Difficulty is : " + difficultyCallProxy.getDifficulty(pos));
	    	
	    	// forge/fabric independent approach
	    	
	    	try {
	    		Class<HarderFartherManager> c = HarderFartherManager.class;
	    		// create core proxy
	    		tryDifficultyCallProxy = new CoreDifficultyCall ();
	    	} catch (Throwable e) {
	    		// create local proxy
	    		tryDifficultyCallProxy = new LocalDifficultyCall();
	    	}

	    	LOGGER.warn("2.Difficult is : " + tryDifficultyCallProxy.getDifficulty(pos));
	    	int x = 3;
	    }
	    
		@SubscribeEvent 
		public void preInit (final FMLCommonSetupEvent event) {
			
				Utility.debugMsg(0, MODID + ": Registering Handlers");
//				MinecraftForge.EVENT_BUS.register(new WorldTickHandler());
				MinecraftForge.EVENT_BUS.register(new LivingEventMovementHandler());
				
//				MinecraftForge.EVENT_BUS.register(new MonsterDropEventHandler());
//				MinecraftForge.EVENT_BUS.register(new ExperienceDropEventHandler());
//				MinecraftForge.EVENT_BUS.register(new ChunkEvent());
//				MinecraftForge.EVENT_BUS.register(new PlayerLoginEventHandler());
//				MinecraftForge.EVENT_BUS.register(new PlayerTickEventHandler());
//				MinecraftForge.EVENT_BUS.register(new PlayerTeleportHandler());
//				MinecraftForge.EVENT_BUS.register(new BlockEvents());
//				fixAttributeMax();
				
 		} 

}
