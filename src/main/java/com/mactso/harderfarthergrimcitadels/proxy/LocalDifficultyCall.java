package com.mactso.harderfarthergrimcitadels.proxy;

import net.minecraft.core.BlockPos;

public class LocalDifficultyCall implements IDifficultyCallProxy {

	public float getDifficulty (BlockPos pos) { 
		return (0.33f);
	}
	
}
