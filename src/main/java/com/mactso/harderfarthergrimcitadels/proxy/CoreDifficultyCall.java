package com.mactso.harderfarthergrimcitadels.proxy;

import com.mactso.harderfarthercore.HarderFartherManager;

import net.minecraft.core.BlockPos;

public class CoreDifficultyCall implements IDifficultyCallProxy  {
	
	public float getDifficulty (BlockPos pos) { 
		return HarderFartherManager.getDifficulty (pos);
		
	}

}
