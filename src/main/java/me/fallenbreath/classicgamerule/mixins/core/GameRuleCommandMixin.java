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

package me.fallenbreath.classicgamerule.mixins.core;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.fallenbreath.classicgamerule.core.ClassicGameRuleCommandModifier;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.GameRuleCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRuleCommand.class)
public abstract class GameRuleCommandMixin
{
	@ModifyVariable(
			method = "register",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;"
			)
	)
	private static LiteralArgumentBuilder<CommandSourceStack> addClassicGameRuleNodes(
			LiteralArgumentBuilder<CommandSourceStack> node,
			@Local(argsOnly = true) CommandBuildContext commandBuildContext
	)
	{
		ClassicGameRuleCommandModifier.addClassicGameRuleNodes(node, commandBuildContext.enabledFeatures());
		return node;
	}
}
