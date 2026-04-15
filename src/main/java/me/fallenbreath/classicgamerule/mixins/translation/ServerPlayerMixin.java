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

package me.fallenbreath.classicgamerule.mixins.translation;

import me.fallenbreath.classicgamerule.translation.ServerPlayerLanguageAccess;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements ServerPlayerLanguageAccess
{
	@Shadow
	private String language;

	@Unique
	private String language$classicgamerule;

	@Inject(method = "updateOptions", at = @At("HEAD"))
	private void recordLang(ClientInformation clientInformation, CallbackInfo ci)
	{
		this.language$classicgamerule = clientInformation.language();
	}

	@Override
	public String getLanguage$classicgamerule()
	{
		if (this.language$classicgamerule != null)
		{
			return this.language$classicgamerule;
		}
		return this.language;
	}

	@Override
	public void setLanguage$classicgamerule(String language)
	{
		this.language$classicgamerule = language;
	}
}
