/*
 * This file is part of the Classic Gamerule project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
 *
 * Classic Gamerule is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Classic Gamerule is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Classic Gamerule.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.fallenbreath.classicgamerule.core;

import com.mojang.brigadier.arguments.BoolArgumentType;
import me.fallenbreath.classicgamerule.translation.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GameRuleMappings
{
	public static final List<GameRuleMapping<?, ?>> MAPPINGS = Lists.newArrayList();

	private static <T> int performQuery(String classicName, GameRule<@NotNull T> gameRule, CommandSourceStack source, T modernValue, T classicValue)
	{
		source.sendSuccess(() -> Component.translatable("commands.gamerule.query", gameRule.id(), gameRule.serialize(modernValue)), false);
		source.sendSystemMessage(Translations.translatable(source, "classicgamerule.command.gamerule.query", classicName, gameRule.serialize(classicValue)).withStyle(ChatFormatting.GRAY));
		return gameRule.getCommandResult(classicValue);
	}

	private static <T> int performSet(String classicName, GameRule<@NotNull T> gameRule, CommandSourceStack source, T modernValue, T classicValue)
	{
		source.getLevel().getGameRules().set(gameRule, modernValue, source.getServer());
		source.sendSuccess(() -> Component.translatable("commands.gamerule.set", gameRule.id(), gameRule.serialize(modernValue)), true);
		source.sendSystemMessage(Translations.translatable(source, "classicgamerule.command.gamerule.set", classicName, gameRule.serialize(classicValue)).withStyle(ChatFormatting.GRAY));
		return gameRule.getCommandResult(classicValue);
	}

	private static <T> void registerDefaultMapping(String classicName, GameRule<@NotNull T> gameRule)
	{
		MAPPINGS.add(new GameRuleMapping<>(
				classicName, gameRule,
				(ctx) -> {
					CommandSourceStack source = ctx.getSource();
					T ruleValue = source.getLevel().getGameRules().get(gameRule);
					return performQuery(classicName, gameRule, source, ruleValue, ruleValue);
				},
				(ctx) -> {
					CommandSourceStack source = ctx.getSource();
					T argValue = ctx.getArgument("value", gameRule.valueClass());
					return performSet(classicName, gameRule, source, argValue, argValue);
				},
				gameRule::argument
		));
	}

	private static void registerInvertedBooleanMapping(String classicName, GameRule<@NotNull Boolean> gameRule)
	{
		MAPPINGS.add(new GameRuleMapping<>(
				classicName, gameRule,
				(ctx) -> {
					CommandSourceStack source = ctx.getSource();
					Boolean modernValue = source.getLevel().getGameRules().get(gameRule);
					Boolean classicValue = !modernValue;
					return performQuery(classicName, gameRule, source, modernValue, classicValue);
				},
				(ctx) -> {
					CommandSourceStack source = ctx.getSource();
					Boolean classicValue = ctx.getArgument("value", gameRule.valueClass());
					Boolean modernValue = !classicValue;
					return performSet(classicName, gameRule, source, classicValue, modernValue);
				},
				gameRule::argument
		));
	}

	private static void registerFireTickMapping()
	{
		// FIRE_SPREAD_RADIUS_AROUND_PLAYER       doFireTick   allowFireTicksAwayFromPlayer
		//              -1                   -->     true                 true
		//               0                   -->     false                false
		//          other (> 0)              -->     true                 false
		final var gameRule = GameRules.FIRE_SPREAD_RADIUS_AROUND_PLAYER;
		BiConsumer<String, Function<Integer, Boolean>> addRule = (classicName, classicValueMapper) -> MAPPINGS.add(new GameRuleMapping<>(
				classicName, GameRules.FIRE_SPREAD_RADIUS_AROUND_PLAYER,
				(ctx) -> {
					CommandSourceStack source = ctx.getSource();
					int modernValue = source.getLevel().getGameRules().get(gameRule);
					boolean classicValue = classicValueMapper.apply(modernValue);

					source.sendSuccess(() -> Component.translatable("commands.gamerule.query", gameRule.id(), gameRule.serialize(modernValue)), false);
					source.sendSystemMessage(Translations.translatable(source, "classicgamerule.command.gamerule.query", classicName, classicValue).withStyle(ChatFormatting.GRAY));
					return classicValue ? 1 : 0;
				},
				(ctx) -> {
					CommandSourceStack source = ctx.getSource();
					source.sendFailure(Translations.translatable(source, "classicgamerule.command.gamerule.set_disabled", classicName, gameRule.id()));
					return 0;
				},
				BoolArgumentType::bool
		));
		addRule.accept("doFireTick", modernValue -> modernValue != 0);
		addRule.accept("allowFireTicksAwayFromPlayer", modernValue -> modernValue == -1);
	}

	static
	{
		registerDefaultMapping("allowEnteringNetherUsingPortals", GameRules.ALLOW_ENTERING_NETHER_USING_PORTALS);
		registerDefaultMapping("announceAdvancements", GameRules.SHOW_ADVANCEMENT_MESSAGES);
		registerDefaultMapping("blockExplosionDropDecay", GameRules.BLOCK_EXPLOSION_DROP_DECAY);
		registerDefaultMapping("commandBlockOutput", GameRules.COMMAND_BLOCK_OUTPUT);
		registerDefaultMapping("commandBlocksEnabled", GameRules.COMMAND_BLOCKS_WORK);
		registerDefaultMapping("commandModificationBlockLimit", GameRules.MAX_BLOCK_MODIFICATIONS);
		registerInvertedBooleanMapping("disableElytraMovementCheck", GameRules.ELYTRA_MOVEMENT_CHECK);
		registerInvertedBooleanMapping("disablePlayerMovementCheck", GameRules.PLAYER_MOVEMENT_CHECK);
		registerInvertedBooleanMapping("disableRaids", GameRules.RAIDS);
		registerDefaultMapping("doDaylightCycle", GameRules.ADVANCE_TIME);
		registerDefaultMapping("doEntityDrops", GameRules.ENTITY_DROPS);
		registerDefaultMapping("doImmediateRespawn", GameRules.IMMEDIATE_RESPAWN);
		registerDefaultMapping("doInsomnia", GameRules.SPAWN_PHANTOMS);
		registerDefaultMapping("doLimitedCrafting", GameRules.LIMITED_CRAFTING);
		registerDefaultMapping("doMobLoot", GameRules.MOB_DROPS);
		registerDefaultMapping("doMobSpawning", GameRules.SPAWN_MOBS);
		registerDefaultMapping("doPatrolSpawning", GameRules.SPAWN_PATROLS);
		registerDefaultMapping("doTileDrops", GameRules.BLOCK_DROPS);
		registerDefaultMapping("doTraderSpawning", GameRules.SPAWN_WANDERING_TRADERS);
		registerDefaultMapping("doVinesSpread", GameRules.SPREAD_VINES);
		registerDefaultMapping("doWardenSpawning", GameRules.SPAWN_WARDENS);
		registerDefaultMapping("doWeatherCycle", GameRules.ADVANCE_WEATHER);
		registerDefaultMapping("drowningDamage", GameRules.DROWNING_DAMAGE);
		registerDefaultMapping("enderPearlsVanishOnDeath", GameRules.ENDER_PEARLS_VANISH_ON_DEATH);
		registerDefaultMapping("fallDamage", GameRules.FALL_DAMAGE);
		registerDefaultMapping("fireDamage", GameRules.FIRE_DAMAGE);
		registerDefaultMapping("forgiveDeadPlayers", GameRules.FORGIVE_DEAD_PLAYERS);
		registerDefaultMapping("freezeDamage", GameRules.FREEZE_DAMAGE);
		registerDefaultMapping("globalSoundEvents", GameRules.GLOBAL_SOUND_EVENTS);
		registerDefaultMapping("keepInventory", GameRules.KEEP_INVENTORY);
		registerDefaultMapping("lavaSourceConversion", GameRules.LAVA_SOURCE_CONVERSION);
		registerDefaultMapping("locatorBar", GameRules.LOCATOR_BAR);
		registerDefaultMapping("logAdminCommands", GameRules.LOG_ADMIN_COMMANDS);
		registerDefaultMapping("maxCommandChainLength", GameRules.MAX_COMMAND_SEQUENCE_LENGTH);
		registerDefaultMapping("maxCommandForkCount", GameRules.MAX_COMMAND_FORKS);
		registerDefaultMapping("maxEntityCramming", GameRules.MAX_ENTITY_CRAMMING);
		registerDefaultMapping("minecartMaxSpeed", GameRules.MAX_MINECART_SPEED);
		registerDefaultMapping("mobExplosionDropDecay", GameRules.MOB_EXPLOSION_DROP_DECAY);
		registerDefaultMapping("naturalRegeneration", GameRules.NATURAL_HEALTH_REGENERATION);
		registerDefaultMapping("playersNetherPortalCreativeDelay", GameRules.PLAYERS_NETHER_PORTAL_CREATIVE_DELAY);
		registerDefaultMapping("playersNetherPortalDefaultDelay", GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY);
		registerDefaultMapping("playersSleepingPercentage", GameRules.PLAYERS_SLEEPING_PERCENTAGE);
		registerDefaultMapping("projectilesCanBreakBlocks", GameRules.PROJECTILES_CAN_BREAK_BLOCKS);
		registerDefaultMapping("reducedDebugInfo", GameRules.REDUCED_DEBUG_INFO);
		registerDefaultMapping("sendCommandFeedback", GameRules.SEND_COMMAND_FEEDBACK);
		registerDefaultMapping("showDeathMessages", GameRules.SHOW_DEATH_MESSAGES);
		registerDefaultMapping("snowAccumulationHeight", GameRules.MAX_SNOW_ACCUMULATION_HEIGHT);
		registerDefaultMapping("spawnerBlocksEnabled", GameRules.SPAWNER_BLOCKS_WORK);
		registerDefaultMapping("spawnMonsters", GameRules.SPAWN_MONSTERS);
		registerDefaultMapping("spawnRadius", GameRules.RESPAWN_RADIUS);
		registerDefaultMapping("spectatorsGenerateChunks", GameRules.SPECTATORS_GENERATE_CHUNKS);
		registerDefaultMapping("tntExplodes", GameRules.TNT_EXPLODES);
		registerDefaultMapping("tntExplosionDropDecay", GameRules.TNT_EXPLOSION_DROP_DECAY);
		registerDefaultMapping("universalAnger", GameRules.UNIVERSAL_ANGER);
		registerDefaultMapping("waterSourceConversion", GameRules.WATER_SOURCE_CONVERSION);
		registerFireTickMapping();
	}
}
