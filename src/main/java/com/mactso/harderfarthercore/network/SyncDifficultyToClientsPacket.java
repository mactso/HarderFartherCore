package com.mactso.harderfarthercore.network;

import java.util.function.Supplier;

import com.mactso.harderfarthercore.events.FogColorsEventHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

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

		public static void processPacket(SyncDifficultyToClientsPacket message, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork( () -> 
				{
					FogColorsEventHandler.setLocalDifficulty(message.spawnDifficulty, message.lifeDifficulty, message.grimDifficulty);
				}
			);
			ctx.get().setPacketHandled(true);
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
