package com.mactso.harderfarthergrimcitadels.events;

import com.mactso.harderfarthercore.HarderFartherManager;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;;

@Mod.EventBusSubscriber()
public class LivingEventMovementHandler {

	/**
	 * @param event
	 */
	@SubscribeEvent
	public void onLivingUpdate(LivingTickEvent event) {

		LivingEntity le = event.getEntity();
		if (le instanceof ServerPlayer) {
			float difficultyHere = HarderFartherManager.getDifficulty(le);
			System.out.println("Player: " + le.getDisplayName().getString() + " Difficulty Here: " + difficultyHere);
		}

	}

}
