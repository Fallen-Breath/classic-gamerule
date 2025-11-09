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
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.slf4j.Logger;
//#elseif FORGE
//$$ import net.minecraftforge.fml.ModList;
//$$ import net.minecraftforge.fml.common.Mod;
//$$ import net.minecraftforge.forgespi.language.IModInfo;
//#elseif NEOFORGE
//$$ import net.neoforged.fml.ModList;
//$$ import net.neoforged.fml.common.Mod;
//$$ import net.neoforged.neoforgespi.language.IModInfo;
//#endif

//#if FORGE_LIKE
//$$ @Mod(ClassicGameruleMod.MOD_ID)
//#endif
public class ClassicGameruleMod
		//#if FABRIC
		implements ModInitializer
		//#endif
{
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final String MOD_ID = "classicgamerule";
	public static String MOD_VERSION = "unknown";
	public static String MOD_NAME = "unknown";

	//#if FABRIC
	@Override
	public void onInitialize()
	{
		ModMetadata metadata = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata();
		MOD_NAME = metadata.getName();
		MOD_VERSION = metadata.getVersion().getFriendlyString();
		this.init();
	}
	//#elseif FORGE_LIKE
	//$$ public ClassicGameruleMod()
	//$$ {
	//$$ 	IModInfo modInfo = ModList.get().getModContainerById(MOD_ID).orElseThrow(RuntimeException::new).getModInfo();
	//$$ 	MOD_NAME = modInfo.getDisplayName();
	//$$ 	MOD_VERSION = modInfo.getVersion().toString();
	//$$ 	this.init();
	//$$ }
	//#endif

	private void init()
	{
		Translations.load();
	}
}
