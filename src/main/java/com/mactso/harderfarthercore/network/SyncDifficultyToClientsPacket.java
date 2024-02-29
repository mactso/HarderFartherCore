package com.mactso.harderfarthercore.network;

import com.mactso.harderfarthercore.events.FogColorsEventHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent.Context;

public class SyncDifficultyToClientsPacket  {
		private float spawnDifficulty;
		private float lifeDifficulty;
		private float grimDifficulty;
	
		public SyncDifficultyToClientsPacket ( float spawn, float grim, float life)
		{
			this.spawnDifficulty = spawn;
			this.lifeDifficulty = life;
			this.grimDifficulty = grim;
		}

		public static void processPacket(SyncDifficultyToClientsPacket message, Context ctx)
		{
			ctx.enqueueWork( () -> 
				{
					FogColorsEventHandler.setLocalDifficulty(message.spawnDifficulty, message.grimDifficulty, message.lifeDifficulty);
				}
			);
			ctx.setPacketHandled(true);
		}
		
		public static SyncDifficultyToClientsPacket readPacketData(FriendlyByteBuf buf) {
			float hard = buf.readFloat();
			float grim = buf.readFloat();
			float time = buf.readFloat();
			return new SyncDifficultyToClientsPacket(hard, grim, time);
		}
		
		public static void writePacketData(SyncDifficultyToClientsPacket msg, FriendlyByteBuf buf)
		{
			msg.encode(buf);
		}
		
		public void encode(FriendlyByteBuf buf)
		{
			buf.writeFloat(this.spawnDifficulty);
			buf.writeFloat(this.lifeDifficulty);
			buf.writeFloat(this.grimDifficulty);

		}
}
