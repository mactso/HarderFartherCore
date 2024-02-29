package com.mactso.harderfarthercore.network;

import com.mactso.harderfarthercore.events.FogColorsEventHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent.Context;

public class SyncFogToClientsPacket  {
		private int fogSetting;
		private double red;
		private double green;
		private double blue;
	
		public SyncFogToClientsPacket ( int f, double r, double g, double b)
		{
			this.fogSetting = f;
			this.red = r;
			this.green = g;
			this.blue = b;
		}


		
		public static void processPacket(SyncFogToClientsPacket message, Context ctx)
		{
			ctx.enqueueWork( () -> 
				{
					FogColorsEventHandler.setFogRGB(message.red, message.green, message.blue);
				}
			);
			ctx.setPacketHandled(true);
		}
		
		public static SyncFogToClientsPacket readPacketData(FriendlyByteBuf buf) {
			int fogSetting = buf.readInt();
			double red = buf.readDouble();
			double green = buf.readDouble();
			double blue = buf.readDouble();
			return new SyncFogToClientsPacket(fogSetting, red, green, blue);
		}
		
		public static void writePacketData(SyncFogToClientsPacket msg, FriendlyByteBuf buf)
		{
			msg.encode(buf);
		}
		
		public void encode(FriendlyByteBuf buf)
		{
			buf.writeInt(fogSetting);	
			buf.writeDouble(this.red);
			buf.writeDouble(this.green);
			buf.writeDouble(this.blue);

		}
}
