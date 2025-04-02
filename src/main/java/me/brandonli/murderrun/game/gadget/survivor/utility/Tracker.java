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
package me.brandonli.murderrun.game.gadget.survivor.utility;

import me.brandonli.murderrun.game.Game;
import me.brandonli.murderrun.game.GameProperties;
import me.brandonli.murderrun.game.gadget.packet.GadgetDropPacket;
import me.brandonli.murderrun.game.gadget.survivor.SurvivorGadget;
import me.brandonli.murderrun.game.player.GamePlayer;
import me.brandonli.murderrun.game.player.GamePlayerManager;
import me.brandonli.murderrun.game.player.PlayerAudience;
import me.brandonli.murderrun.game.player.metadata.MetadataManager;
import me.brandonli.murderrun.locale.Message;
import me.brandonli.murderrun.utils.item.ItemFactory;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Item;

public final class Tracker extends SurvivorGadget {

  public Tracker() {
    super(
      "tracker",
      GameProperties.TRACKER_COST,
      ItemFactory.createGadget("tracker", GameProperties.TRACKER_MATERIAL, Message.TRACKER_NAME.build(), Message.TRACKER_LORE.build())
    );
  }

  @Override
  public boolean onGadgetDrop(final GadgetDropPacket packet) {
    final Game game = packet.getGame();
    final GamePlayer player = packet.getPlayer();
    final Item item = packet.getItem();
    item.remove();

    final GamePlayerManager manager = game.getPlayerManager();
    manager.applyToKillers(killer -> this.handleGlowing(killer, player));

    final PlayerAudience audience = player.getAudience();
    audience.playSound(GameProperties.TRACKER_SOUND);

    return false;
  }

  private void handleGlowing(final GamePlayer killer, final GamePlayer player) {
    final Location origin = player.getLocation();
    final Location killerLocation = killer.getLocation();
    final double distance = origin.distanceSquared(killerLocation);
    final MetadataManager metadata = player.getMetadataManager();
    final PlayerAudience audience = player.getAudience();
    final double radius = GameProperties.TRACKER_RADIUS;
    if (distance < radius * radius) {
      metadata.setEntityGlowing(killer, ChatColor.DARK_PURPLE, true);
      audience.sendMessage(Message.TRACKER_ACTIVATE.build());
    } else {
      audience.sendMessage(Message.TRACKER_DEACTIVATE.build());
    }
  }
}
