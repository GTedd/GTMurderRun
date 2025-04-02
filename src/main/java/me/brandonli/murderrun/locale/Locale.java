/*
 * This file is part of Murder Run, a spin-off game-mode of Dead by Daylight
 * Copyright (C) Brandon Li <https://brandonli.me/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package me.brandonli.murderrun.locale;

import java.util.Map;

public enum Locale {
  EN_US,
  ZH_CN,
  ZH_HK;

  private static final Map<String, Locale> LOOKUP_TABLE = Map.of("EN_US", EN_US, "ZH_CN", ZH_CN, "ZH_HK", ZH_HK);

  public static Locale fromString(final String locale) {
    final String upper = locale.toUpperCase();
    return LOOKUP_TABLE.getOrDefault(upper, EN_US);
  }
}
