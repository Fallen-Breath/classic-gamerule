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

import com.google.common.collect.Sets;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.fallenbreath.classicgamerule.ClassicGameruleMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.flag.FeatureFlagSet;

import java.util.Set;

public class ClassicGameRuleCommandModifier
{
	public static void addClassicGameRuleNodes(LiteralArgumentBuilder<CommandSourceStack> rootNode, FeatureFlagSet featureFlagSet)
	{
		Set<String> existingArgs = Sets.newHashSet();
		for (var existingNode : rootNode.getArguments())
		{
			if (existingNode instanceof LiteralCommandNode<?> literalCommandNode)
			{
				existingArgs.add(literalCommandNode.getLiteral());
			}
		}

		for (GameRuleMapping<?, ?> mapping : GameRuleMappings.MAPPINGS)
		{
			if (!mapping.rule().requiredFeatures().isSubsetOf(featureFlagSet))
			{
				continue;
			}
			if (existingArgs.contains(mapping.classicName()))
			{
				ClassicGameruleMod.LOGGER.warn("Found existing literal node when registering classic gamerule '{}', skipped", mapping.classicName());
				continue;
			}

			rootNode.then(Commands.literal(mapping.classicName()).
					executes(mapping::queryRule).
					then(Commands.argument("value", mapping.getArgument()).
							executes(mapping::setRule)
					)
			);
			existingArgs.add(mapping.classicName());
		}
	}
}
