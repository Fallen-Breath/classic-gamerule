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

import com.mojang.logging.LogUtils;
import me.fallenbreath.classicgamerule.translation.Translations;
import org.slf4j.Logger;

@net.minecraftforge.fml.common.Mod(ClassicGameruleMod.MOD_ID)
@net.neoforged.fml.common.Mod(ClassicGameruleMod.MOD_ID)
public class ClassicGameruleMod
{
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final String MOD_ID = "classicgamerule";

	public static void fabricInit()
	{
		//noinspection InstantiationOfUtilityClass
		new ClassicGameruleMod();
	}

	public ClassicGameruleMod()
	{
		Translations.load();
	}
}
