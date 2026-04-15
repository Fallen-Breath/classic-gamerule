/*
 * This file is part of the Classic Gamerule project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package me.fallenbreath.classicgamerule.mixins.translation;

import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.classicgamerule.translation.ServerPlayerLanguageAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin
{
	@ModifyArg(
			method = "respawn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerPlayer;restoreFrom(Lnet/minecraft/server/level/ServerPlayer;Z)V"
			)
	)
	private ServerPlayer copyLanguageToTheNewPlayer(ServerPlayer newPlayer, @Local(argsOnly = true) ServerPlayer oldPlayer)
	{
		((ServerPlayerLanguageAccess)newPlayer).setLanguage$classicgamerule(((ServerPlayerLanguageAccess)oldPlayer).getLanguage$classicgamerule());
		return newPlayer;
	}
}
