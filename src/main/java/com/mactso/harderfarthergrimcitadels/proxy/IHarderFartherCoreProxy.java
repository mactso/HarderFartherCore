package com.mactso.harderfarthergrimcitadels.proxy;

import net.minecraft.core.BlockPos;

public interface IHarderFartherCoreProxy {
	public float getDifficulty (BlockPos pos);
	public void  addGrimBlockPosListEntry(BlockPos pos, int grimRange);
	public void  addLifeBlockPosListEntry(BlockPos pos, int lifeRange);
}
