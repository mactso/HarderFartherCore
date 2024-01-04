package com.mactso.harderfarthergrimcitadels;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.harderfarthercore.HarderFartherManager;
import com.mactso.harderfarthergrimcitadels.proxy.CoreDifficultyCall;
import com.mactso.harderfarthergrimcitadels.proxy.IDifficultyCallProxy;
import com.mactso.harderfarthergrimcitadels.proxy.LocalDifficultyCall;

import net.minecraft.core.BlockPos;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod("harderfarthergrimcitadels")
public class Main {

	    public static final String MODID = "harderfarthergrimcitadels"; 
	    public static IDifficultyCallProxy difficultyCallProxy;
	    public static IDifficultyCallProxy tryDifficultyCallProxy;
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
	    	
	    	LOGGER.warn("1.Difficult is : " + difficultyCallProxy.getDifficulty(pos));
	    	
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

}
