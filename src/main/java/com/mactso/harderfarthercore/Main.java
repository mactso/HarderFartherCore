package com.mactso.harderfarthercore;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.harderfarthercore.command.MyCommands;
import com.mactso.harderfarthercore.config.MyConfig;
import com.mactso.harderfarthercore.events.LivingEventMovementHandler;
import com.mactso.harderfarthercore.events.WorldTickHandler;
import com.mactso.harderfarthercore.network.Register;
import com.mactso.harderfarthercore.utility.Utility;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.coremod.api.ASMAPI;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod("harderfarthercore")
public class Main {

	    public static final String MODID = "harderfarthercore"; 
		private static final Logger LOGGER = LogManager.getLogger();
		private static final int MAX_USABLE_VALUE = 16000000;  // you can subtract 1 from this number.	    

		public Main()
	    {
	    	Utility.debugMsg(0, MODID + ": Registering Mod.");
	  		FMLJavaModLoadingContext.get().getModEventBus().register(this);
 	        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,MyConfig.COMMON_SPEC );	    	

			
	    }

		@OnlyIn(Dist.CLIENT)
	    @SubscribeEvent
	    public void setupClient(final FMLClientSetupEvent event) {
	    	
//			MinecraftForge.EVENT_BUS.register(new FogColorsEventHandler());
// Grim	code // ModBlocks.setRenderLayer();
			
	    }
		
	    @SubscribeEvent
	    public void setupCommon(final FMLCommonSetupEvent event)
	    {
	        Register.initPackets();
	    }
	    
	    
		@SubscribeEvent 
		public void preInit (final FMLCommonSetupEvent event) {
			
				Utility.debugMsg(0, MODID + ": Registering Handlers");
				MinecraftForge.EVENT_BUS.register(new WorldTickHandler());
				MinecraftForge.EVENT_BUS.register(new LivingEventMovementHandler());
				
//				MinecraftForge.EVENT_BUS.register(new MonsterDropEventHandler());
//				MinecraftForge.EVENT_BUS.register(new ExperienceDropEventHandler());
//				MinecraftForge.EVENT_BUS.register(new ChunkEvent());
//				MinecraftForge.EVENT_BUS.register(new PlayerLoginEventHandler());
//				MinecraftForge.EVENT_BUS.register(new PlayerTickEventHandler());
//				MinecraftForge.EVENT_BUS.register(new PlayerTeleportHandler());
//				MinecraftForge.EVENT_BUS.register(new BlockEvents());
				
				fixAttributeMax();
 		} 
		
		private void fixAttributeMax() {
			// don't care about speed and knockback.
			// speed becomes too fast very quickly.
			// knockback maxes at 100% resistance to knockback.
			
				try {
					String name = ASMAPI.mapField("f_22308_");
					Field max = RangedAttribute.class.getDeclaredField(name);
					max.setAccessible(true);

					max.set(Attributes.MAX_HEALTH, (double) MAX_USABLE_VALUE);
					max.set(Attributes.ATTACK_DAMAGE, (double) MAX_USABLE_VALUE);

				} catch (Exception e) {
					LOGGER.error("XXX Unexpected Reflection Failure changing attribute maximum");
				}
				
		}

		@Mod.EventBusSubscriber(bus = Bus.FORGE)
		public static class ForgeEvents
		{
			@SubscribeEvent 		
			public static void onCommandsRegistry(final RegisterCommandsEvent event) {
				Utility.debugMsg(0,"Harder Farther Core: Registering Command Dispatcher");
				MyCommands.register(event.getDispatcher());			
			}			
		}
		

}
