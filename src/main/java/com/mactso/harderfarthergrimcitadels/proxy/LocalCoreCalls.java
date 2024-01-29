package com.mactso.harderfarthergrimcitadels.proxy;

import com.mactso.harderfarthercore.HarderFartherManager;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;

public class LocalCoreCalls implements IHarderFartherCoreProxy {

	public float getDifficulty (LivingEntity le) {
		// return difficulty based on grim citadel list only.
		return 0.0f;
	}

	@Override
	public void addGrimBlockPosListEntry(BlockPos pos, int range) {
		// add position to list of grim citadel positions.
		HarderFartherManager.getDifficulty(null);
		
		
	}
	
	@Override
	public void addLifeBlockPosListEntry(BlockPos pos, int lifeRange) {
	}

	@Override
	public float getDifficulty(BlockPos pos) {
		// return difficulty based on grim citadel list only.
		return 0;
	}
	

	
}
