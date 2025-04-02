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
package me.brandonli.murderrun.game.gadget.survivor.trap;

import static java.util.Objects.requireNonNull;

import me.brandonli.murderrun.game.Game;
import me.brandonli.murderrun.game.GameProperties;
import me.brandonli.murderrun.game.player.GamePlayer;
import me.brandonli.murderrun.game.player.GamePlayerManager;
import me.brandonli.murderrun.game.scheduler.GameScheduler;
import me.brandonli.murderrun.game.scheduler.reference.NullReference;
import me.brandonli.murderrun.locale.Message;
import me.brandonli.murderrun.utils.RandomUtils;
import me.brandonli.murderrun.utils.item.ItemFactory;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Sheep;

public final class JebTrap extends SurvivorTrap {

  public JebTrap() {
    super(
      "jeb_trap",
      GameProperties.JEB_COST,
      ItemFactory.createGadget("jeb_trap", GameProperties.JEB_MATERIAL, Message.JEB_NAME.build(), Message.JEB_LORE.build()),
      Message.JEB_ACTIVATE.build(),
      GameProperties.JEB_COLOR
    );
  }

  @Override
  public void onTrapActivate(final Game game, final GamePlayer murderer, final Item item) {
    final Location location = murderer.getLocation();
    final World world = requireNonNull(location.getWorld());
    for (int i = 0; i < GameProperties.JEB_SHEEP_COUNT; i++) {
      world.spawn(location, Sheep.class, sheep -> sheep.setCustomName("jeb_"));
    }

    final NullReference reference = NullReference.of();
    final GameScheduler scheduler = game.getScheduler();
    scheduler.scheduleRepeatedTask(() -> this.spawnRainbowParticles(location), 0, 5, GameProperties.JEB_DURATION, reference);

    final GamePlayerManager manager = game.getPlayerManager();
    manager.playSoundForAllParticipants(GameProperties.JEB_SOUND);
  }

  private void spawnRainbowParticles(final Location location) {
    final World world = requireNonNull(location.getWorld());
    final int r = RandomUtils.generateInt(255);
    final int g = RandomUtils.generateInt(255);
    final int b = RandomUtils.generateInt(255);
    final org.bukkit.Color color = org.bukkit.Color.fromRGB(r, g, b);
    world.spawnParticle(Particle.DUST, location, 15, 3, 3, 3, new DustOptions(color, 4));
  }
}
