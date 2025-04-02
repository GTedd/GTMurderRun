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
package me.brandonli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public final class PropertiesTest {

  public static void main(final String[] args) throws IOException {
    final Properties enUs = loadProperties("src/main/resources/locale/murderrun_en_us.properties");
    final Properties zhCn = loadProperties("src/main/resources/locale/murderrun_zh_cn.properties");
    final Properties zhHk = loadProperties("src/main/resources/locale/murderrun_zh_hk.properties");

    final Set<String> enUsKeys = enUs.stringPropertyNames();
    final Set<String> zhCnKeys = zhCn.stringPropertyNames();
    final Set<String> zhHkKeys = zhHk.stringPropertyNames();

    final Set<String> missingZhCn = enUsKeys.stream().filter(key -> !zhCnKeys.contains(key)).collect(Collectors.toSet());
    final Set<String> missingZhHk = enUsKeys.stream().filter(key -> !zhHkKeys.contains(key)).collect(Collectors.toSet());

    System.out.println("Missing keys in murderrun_zh_cn.properties: " + missingZhCn);
    System.out.println("Missing keys in murderrun_zh_hk.properties: " + missingZhHk);
  }

  private static Properties loadProperties(final String path) throws IOException {
    final Properties properties = new Properties();
    try (final var reader = Files.newBufferedReader(Path.of(path))) {
      properties.load(reader);
    }
    return properties;
  }
}
