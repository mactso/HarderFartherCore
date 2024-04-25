package com.mactso.harderfarthercore.network;

public class Register {
	public static void initPackets()
	{

//		Network.registerMessage(SyncFogToClientsPacket.class,
//				SyncFogToClientsPacket::writePacketData,
//				SyncFogToClientsPacket::readPacketData,
//				SyncFogToClientsPacket::processPacket);

		Network.registerMessage(SyncDifficultyToClientsPacket.class,
				SyncDifficultyToClientsPacket::writePacketData,
				SyncDifficultyToClientsPacket::readPacketData,
				SyncDifficultyToClientsPacket::processPacket);


	}
}
