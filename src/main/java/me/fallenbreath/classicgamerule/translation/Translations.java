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

package me.fallenbreath.classicgamerule.translation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import me.fallenbreath.classicgamerule.ClassicGameruleMod;
import me.fallenbreath.classicgamerule.mixins.translation.ServerPlayerAccessor;
import me.fallenbreath.classicgamerule.mixins.translation.TranslatableContentsAccessor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Translations
{
	private static final String DEFAULT_LANGUAGE = "en_us";
	private static final String RESOURCE_DIR = String.format("assets/%s/lang", ClassicGameruleMod.MOD_ID);
	private static final Map<String, Map<String, String>> translations = Maps.newLinkedHashMap();  // lang -> (key -> formatter)

	public static MutableComponent translatable(CommandSourceStack source, String key, Object... args)
	{
		String formatter = translateKey(source, key);
		if (formatter == null)
		{
			ClassicGameruleMod.LOGGER.warn("Unknown translation key {}", key);
			return Component.translatable(key, args);
		}

		TranslatableContents translatableContents = (TranslatableContents)Component.translatable(formatter, args).getContents();
		List<FormattedText> segments = Lists.newArrayList();
		((TranslatableContentsAccessor)translatableContents).invokeDecomposeTemplate(formatter, segments::add);

		MutableComponent msg = Component.literal("");
		for (FormattedText segment : segments)
		{
			if (segment instanceof Component)
			{
				msg.append((Component)segment);
			}
			else
			{
				msg.append(Component.literal(segment.getString()));
			}
		}
		return msg;
	}

	@Nullable
	private static String translateKey(CommandSourceStack source, String key)
	{
		String lang = DEFAULT_LANGUAGE;
		if (source.getEntity() instanceof ServerPlayer player)
		{
			lang = ((ServerPlayerAccessor)player).getLanguage();
		}

		String formatter = translations.getOrDefault(lang, Collections.emptyMap()).get(key);
		if (formatter == null && !lang.equals(DEFAULT_LANGUAGE))
		{
			formatter = translations.getOrDefault(DEFAULT_LANGUAGE, Collections.emptyMap()).get(key);
		}
		return formatter;
	}

	private static String readResourceFileAsString(String path) throws IOException
	{
		InputStream inputStream = Translations.class.getClassLoader().getResourceAsStream(path);
		if (inputStream == null)
		{
			throw new IOException("Null input stream from path " + path);
		}
		return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
	}

	public static void load()
	{
		try
		{
			var languageList = new Gson().fromJson(readResourceFileAsString("%s/meta/languages.json".formatted(RESOURCE_DIR)), LanguageList.class);
			for (String lang : languageList)
			{
				var oneTranslation = new Gson().fromJson(readResourceFileAsString("%s/%s.json".formatted(RESOURCE_DIR, lang)), TranslationMapping.class);
				translations.put(lang, oneTranslation);
			}
		}
		catch (IOException e)
		{
			ClassicGameruleMod.LOGGER.error("FATAL: Failed to load translations: {}", e.toString());
			throw new RuntimeException(e);
		}
	}

	private static class LanguageList extends ArrayList<String>
	{
	}

	private static class TranslationMapping extends LinkedHashMap<String, String>
	{
	}
}
