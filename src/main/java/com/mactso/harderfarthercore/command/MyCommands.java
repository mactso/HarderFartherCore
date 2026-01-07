package com.mactso.harderfarthercore.command;

import com.mactso.harderfarthercore.HarderFartherManager;
import com.mactso.harderfarthercore.config.MyConfig;
import com.mactso.harderfarthercore.utility.MyUtilities;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class MyCommands {

	private static void printInfo(ServerPlayer p) {

		String dimensionName = p.level().dimension().identifier().toString();
		int printDifficulty = (int) (100 * HarderFartherManager.getDifficulty(p));

		String chatMessage = "\nDimension: " + dimensionName + "\n Current Values";
		MyUtilities.sendBoldChat(p, chatMessage, ChatFormatting.DARK_GREEN);

		chatMessage = "  Boosts Reach Maximum Value....: " + MyConfig.getBoostMaxDistance() + " blocks from Spawn."
				+ "\n  Boosts Start .........................................: " + MyConfig.getBoostMinDistance()
				+ " blocks from Spawn." + "\n  Difficulty here is ..............................: " + printDifficulty
				+ "%" + "\n  Only In Overworld ...........................: " + MyConfig.isOnlyOverworld()
				+ "\n  Debug Level ..........................................: " + MyConfig.getDebugLevel();
		MyUtilities.sendChat(p, chatMessage, ChatFormatting.GREEN);

	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("harderfarthercore")
				.requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))

				// -----------------------------------------------------------------
				// / setDebugLevel <0..2>
				// -----------------------------------------------------------------
				.then(Commands.literal("setDebugLevel")
						.then(Commands.argument("debugLevel", IntegerArgumentType.integer(0, 2)).executes(ctx -> {
							ServerPlayer p = ctx.getSource().getPlayerOrException();
							return setDebugLevel(p, IntegerArgumentType.getInteger(ctx, "debugLevel"));
						}) // executes setDebugLevel
						) // then argument debugLevel
				) // then setDebugLevel

				// -----------------------------------------------------------------
				// / chunkReport
				// -----------------------------------------------------------------
				.then(Commands.literal("chunkReport").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					p.level().gatherChunkSourceStats();
					MyUtilities.sendChat(p, "\nChunk\n" + p.level().gatherChunkSourceStats(), ChatFormatting.GREEN);
					return 1;
				}) // executes chunkReport
				) // then chunkReport

				// -----------------------------------------------------------------
				// / info
				// -----------------------------------------------------------------
				.then(Commands.literal("info").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					printInfo(p);
					return 1;
				}) // executes info
				) // then info

				// -----------------------------------------------------------------
				// / boostInfo
				// -----------------------------------------------------------------
				.then(Commands.literal("boostInfo").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					printBoostInfo(p);
					return 1;
				}) // executes boostInfo
				) // then boostInfo

		); // register harderfarthercore
	}

	private static void printBoostInfo(ServerPlayer p) {
		String chatMessage = "\nHarder Farther Maximum Monster Boosts";
		MyUtilities.sendBoldChat(p, chatMessage, ChatFormatting.DARK_GREEN);

		float df1 = HarderFartherManager.getDifficulty(p);
		int hitBoostHere = (int) (df1 * MyConfig.getHpMaxBoost());
		int dmgBoostHere = (int) (df1 * MyConfig.getAtkDmgBoost());
		int movBoostHere = (int) (df1 * MyConfig.getSpeedBoost());
		int kbrBoostHere = (int) (df1 * MyConfig.getKnockBackMod());

		chatMessage = "  Monster Health ...................: " + MyConfig.getHpMaxBoost() + " %. ( +" + hitBoostHere
				+ "% here)" + "\n  Damage .......................................: " + MyConfig.getAtkDmgBoost()
				+ " %.  ( +" + dmgBoostHere + "% here)" + "\n  Movement ..................................: "
				+ MyConfig.getSpeedBoost() + " %.  ( +" + movBoostHere + "% here)" + "\n  KnockBack Resistance ..: "
				+ MyConfig.getKnockBackMod() + " %.  ( +" + kbrBoostHere + "% here)";
		MyUtilities.sendChat(p, chatMessage, ChatFormatting.GREEN);
	}

	public static int setDebugLevel(ServerPlayer p, int newDebugLevel) {
		MyConfig.setDebugLevel(newDebugLevel);
		printInfo(p);
		return 1;
	}

	String subcommand = "";

	String value = "";

}
