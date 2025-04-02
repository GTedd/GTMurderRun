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
import me.brandonli.murderrun.game.gadget.GadgetManager;
import me.brandonli.murderrun.game.gadget.packet.GadgetDropPacket;
import me.brandonli.murderrun.game.gadget.survivor.SurvivorGadget;
import me.brandonli.murderrun.game.player.GamePlayer;
import me.brandonli.murderrun.game.player.PlayerAudience;
import me.brandonli.murderrun.locale.Message;
import me.brandonli.murderrun.utils.item.ItemFactory;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Item;

public final class MagnetMode extends SurvivorGadget {

  public MagnetMode() {
    super(
      "magnet_mode",
      GameProperties.MAGNET_MODE_COST,
      ItemFactory.createGadget(
        "magnet_mode",
        GameProperties.MAGNET_MODE_MATERIAL,
        Message.MAGNET_MODE_NAME.build(),
        Message.MAGNET_MODE_LORE.build()
      )
    );
  }

  @Override
  public boolean onGadgetDrop(final GadgetDropPacket packet) {
    final Game game = packet.getGame();
    final GamePlayer player = packet.getPlayer();
    final Item item = packet.getItem();
    item.remove();

    final GadgetManager gadgetManager = game.getGadgetManager();
    final double current = gadgetManager.getActivationRange();
    gadgetManager.setActivationRange(current * GameProperties.MAGNET_MODE_MULTIPLIER);

    final PlayerAudience audience = player.getAudience();
    final Component message = Message.MAGNET_MODE_ACTIVATE.build();
    audience.sendMessage(message);
    audience.playSound(GameProperties.MAGNET_MODE_SOUND);

    return false;
  }
}
