package com.mactso.harderfarthercore.command;

import com.mactso.harderfarthercore.HarderFartherManager;
import com.mactso.harderfarthercore.config.MyConfig;
import com.mactso.harderfarthercore.utility.Utility;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class MyCommands {


	private static void printInfo(ServerPlayer p) {

		String dimensionName = p.level().dimension().location().toString();
		int printDifficulty = (int)(100 * HarderFartherManager.getDifficulty(p) );
		
		String chatMessage = "\nDimension: " + dimensionName + "\n Current Values";
		Utility.sendBoldChat(p, chatMessage, ChatFormatting.DARK_GREEN);

		chatMessage = "  Harder Max Distance From Spawn....: " + MyConfig.getBoostMaxDistance() + " blocks."				+ " blocks." 
				+ "\n  Difficulty here is .......................:" + printDifficulty + "%"
				+ "\n  Debug Level ..................................................: "
				+ MyConfig.getDebugLevel() + "\n  Only In Overworld ....................................: "
				+ MyConfig.isOnlyOverworld()  ;
		Utility.sendChat(p, chatMessage, ChatFormatting.GREEN);

	}
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("harderfarthercore").requires((source) -> 
			{
				return source.hasPermission(3);
			}
		)
		.then(Commands.literal("setDebugLevel").then(
				Commands.argument("debugLevel", IntegerArgumentType.integer(0,2)).executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					return setDebugLevel(p, IntegerArgumentType.getInteger(ctx, "debugLevel"));
				}))).then(Commands.literal("chunkReport").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					p.level().gatherChunkSourceStats();
					Utility.sendChat(p, "\nChunk\n" + p.level().gatherChunkSourceStats(), ChatFormatting.GREEN);
					return 1;
				})).then(Commands.literal("info").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					printInfo(p);
					return 1;
				})).then(Commands.literal("boostInfo").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					printBoostInfo(p);
					return 1;
				}))

				);

	}

	private static void printBoostInfo(ServerPlayer p) {
		String chatMessage = "\nHarder Farther Maximum Monster Boosts";
		Utility.sendBoldChat(p, chatMessage, ChatFormatting.DARK_GREEN);
		
		float df1 = HarderFartherManager.getDifficulty(p);
		int hitBoostHere = (int) (df1 * MyConfig.getHpMaxBoost());    
		int dmgBoostHere = (int) (df1 * MyConfig.getAtkDmgBoost()); 
		int movBoostHere = (int) (df1 * MyConfig.getSpeedBoost()); 
		int kbrBoostHere = (int) (df1 * MyConfig.getKnockBackMod()); 
		

		chatMessage = "  Monster Health ...................: " + MyConfig.getHpMaxBoost() + " %. ( +"
				+ hitBoostHere + "% here)"
				+ "\n  Damage .......................................: " + MyConfig.getAtkDmgBoost() + " %.  ( +"  
				+ dmgBoostHere + "% here)"
				+ "\n  Movement ..................................: " + MyConfig.getSpeedBoost() + " %.  ( +" 	
				+ movBoostHere + "% here)"
				+ "\n  KnockBack Resistance ..: " + MyConfig.getKnockBackMod() + " %.  ( +"
			    + kbrBoostHere + "% here)";
		Utility.sendChat(p, chatMessage, ChatFormatting.GREEN);
	}



	public static int setDebugLevel(ServerPlayer p, int newDebugLevel) {
		MyConfig.setDebugLevel(newDebugLevel);
		printInfo(p);
		return 1;
	}

	
	String subcommand = "";
	
	String value = "";

}
