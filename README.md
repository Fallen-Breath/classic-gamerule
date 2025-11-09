## Classic Gamerule

[![License](https://img.shields.io/github/license/Fallen-Breath/classic-gamerule.svg)](http://www.gnu.org/licenses/lgpl-3.0.html)
[![workflow](https://github.com/Fallen-Breath/classic-gamerule/actions/workflows/gradle.yml/badge.svg)](https://github.com/Fallen-Breath/classic-gamerule/actions/workflows/gradle.yml)
[![MC Versions](https://cf.way2muchnoise.eu/versions/For%20MC_1381629_all.svg)](https://legacy.curseforge.com/minecraft/mc-mods/classic-gamerule)
[![CurseForge](https://cf.way2muchnoise.eu/full_1381629_downloads.svg)](https://legacy.curseforge.com/minecraft/mc-mods/classic-gamerule)
[![Modrinth](https://img.shields.io/modrinth/dt/FSWe8RZ3?label=Modrinth%20Downloads)](https://modrinth.com/mod/classic-gamerule)

A simple mod that brings back the classic gamerule names to MC 1.21.11+

Minecraft renamed all gamerules from camelCase to namespaced:snake_case in [25w44a](https://www.minecraft.net/en-us/article/minecraft-snapshot-25w44a) (1.21.11 snapshot),
and even reverted the values of some rules, which broke all existing usage of gamerules.
This is a poorly implemented incompatible change. Let's "revert" it by restoring the old names in Minecraft.

For example, classic gamerules like `doDaylightCycle` were changed to `minecraft:advance_time`, and `doMobSpawning` became `minecraft:spawn_mobs`

## What This Mod Does

1. Add all camelCase names as aliases for existing gamerules. The values of reverted rules will be correctly mapped
2. (To be determined) Provides camelCase aliases for new gamerules.

## Requirement

This mod is server-side only (including single-player worlds) and requires no additional dependencies.
It supports all Minecraft versions since 1.21.11

## Config

TODO
