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
package me.brandonli.murderrun.gui.arena;

import static net.kyori.adventure.text.Component.empty;

import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.components.util.GuiFiller;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import me.brandonli.murderrun.MurderRun;
import me.brandonli.murderrun.game.arena.Arena;
import me.brandonli.murderrun.game.arena.ArenaManager;
import me.brandonli.murderrun.locale.Message;
import me.brandonli.murderrun.utils.ComponentUtils;
import me.brandonli.murderrun.utils.ContainerUtils;
import me.brandonli.murderrun.utils.immutable.Keys;
import me.brandonli.murderrun.utils.item.Item;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public final class ArenaListGui extends PaginatedGui {

  private final MurderRun plugin;
  private final Player watcher;
  private final Consumer<InventoryClickEvent> consumer;

  public ArenaListGui(final MurderRun plugin, final Player watcher, final Consumer<InventoryClickEvent> consumer) {
    super(ContainerUtils.createChestContainer(Message.CHOOSE_ARENA_GUI_TITLE.build(), 6), 45, InteractionModifier.VALUES);
    this.plugin = plugin;
    this.watcher = watcher;
    this.consumer = consumer;
  }

  @Override
  public void update() {
    super.update();
    this.updatePane();
    this.createNavigationPane();
  }

  private void updatePane() {
    this.clearPageItems();
    this.getArenas().stream().map(stack -> new GuiItem(stack, this.consumer::accept)).forEach(this::addItem);
  }

  private void createNavigationPane() {
    final GuiFiller filler = this.getFiller();
    final GuiItem back = this.createBackStack();
    final GuiItem next = this.createForwardStack();
    final GuiItem close = this.createCloseStack();
    final GuiItem border = this.createBorderStack();
    filler.fillBottom(border);
    this.setItem(6, 1, back);
    this.setItem(6, 9, next);
    this.setItem(6, 5, close);
  }

  private GuiItem createBorderStack() {
    return new GuiItem(Item.builder(Material.GRAY_STAINED_GLASS_PANE).name(empty()).build());
  }

  private List<ItemStack> getArenas() {
    final ArenaManager manager = this.plugin.getArenaManager();
    final Map<String, Arena> arenas = manager.getArenas();
    final List<ItemStack> items = new ArrayList<>();
    for (final Entry<String, Arena> entry : arenas.entrySet()) {
      final String name = entry.getKey();
      final Arena arena = entry.getValue();
      final Location spawn = arena.getSpawn();
      final ItemStack item = this.constructArenaItem(name, spawn);
      items.add(item);
    }
    return items;
  }

  private ItemStack constructArenaItem(final String name, final Location spawn) {
    final Component title = Message.CHOOSE_ARENA_GUI_ARENA_DISPLAY.build(name);
    final Component lore = ComponentUtils.createLocationComponent(Message.CHOOSE_ARENA_GUI_ARENA_LORE, spawn);
    return Item.builder(Material.WHITE_BANNER).name(title).lore(lore).pdc(Keys.ARENA_NAME, PersistentDataType.STRING, name).build();
  }

  private GuiItem createCloseStack() {
    return new GuiItem(Item.builder(Material.BARRIER).name(Message.SHOP_GUI_CANCEL.build()).build(), event -> this.close(this.watcher));
  }

  private GuiItem createForwardStack() {
    return new GuiItem(Item.builder(Material.GREEN_WOOL).name(Message.SHOP_GUI_FORWARD.build()).build(), event -> this.next());
  }

  private GuiItem createBackStack() {
    return new GuiItem(Item.builder(Material.RED_WOOL).name(Message.SHOP_GUI_BACK.build()).build(), event -> this.previous());
  }
}
