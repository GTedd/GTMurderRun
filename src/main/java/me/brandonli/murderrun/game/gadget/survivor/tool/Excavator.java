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
package me.brandonli.murderrun.game.gadget.survivor.tool;

import me.brandonli.murderrun.game.GameProperties;
import me.brandonli.murderrun.game.gadget.packet.GadgetDropPacket;
import me.brandonli.murderrun.game.gadget.survivor.SurvivorGadget;
import me.brandonli.murderrun.locale.Message;
import me.brandonli.murderrun.utils.item.ItemFactory;

public final class Excavator extends SurvivorGadget {

  public Excavator() {
    super(
      "excavator",
      GameProperties.EXCAVATOR_COST,
      ItemFactory.createExcavator(
        ItemFactory.createGadget(
          "excavator",
          GameProperties.EXCAVATOR_MATERIAL,
          Message.EXCAVATOR_NAME.build(),
          Message.EXCAVATOR_LORE.build()
        )
      )
    );
  }

  @Override
  public boolean onGadgetDrop(final GadgetDropPacket packet) {
    return true;
  }
}
