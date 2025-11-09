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

package me.fallenbreath.classicgamerule;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.gamerules.GameRule;
import org.jetbrains.annotations.NotNull;

public record GameRuleMapping<T>(
		String classicName,
		GameRule<@NotNull T> rule,
		RuleQuerier<T> ruleQuerier,
		RuleSetter<T> ruleSetter
)
{
	public int queryRule(CommandContext<CommandSourceStack> ctx)
	{
		return this.ruleQuerier.queryRule(ctx);
	}

	public int setRule(CommandContext<CommandSourceStack> ctx)
	{
		return this.ruleSetter.setRule(ctx);
	}

	@NotNull
	public ArgumentType<T> getArgument()
	{
		return this.rule.argument();
	}

	@FunctionalInterface
	public interface RuleQuerier<T>
	{
		int queryRule(CommandContext<CommandSourceStack> source);
	}

	@FunctionalInterface
	public interface RuleSetter<T>
	{
		int setRule(CommandContext<CommandSourceStack> ctx);
	}
}
