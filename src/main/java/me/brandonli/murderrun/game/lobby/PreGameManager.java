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
package me.brandonli.murderrun.game.lobby;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import me.brandonli.murderrun.MurderRun;
import me.brandonli.murderrun.game.Game;
import me.brandonli.murderrun.game.GameEventsListener;
import me.brandonli.murderrun.game.GameSettings;
import me.brandonli.murderrun.game.lobby.event.PreGameEvents;
import me.brandonli.murderrun.game.map.MapSchematicIO;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PreGameManager {

  private final MurderRun plugin;
  private final Game game;
  private final GameSettings settings;
  private final String id;
  private final GameEventsListener callback;
  private final GameManager gameManager;
  private final UUID uuid;

  private MapSchematicIO mapSchematicIO;
  private PreGamePlayerManager manager;
  private PreGameEvents events;

  public PreGameManager(final MurderRun plugin, final GameManager manager, final String id, final GameEventsListener callback) {
    this.plugin = plugin;
    this.gameManager = manager;
    this.callback = callback;
    this.id = id;
    this.game = new Game(plugin);
    this.settings = new GameSettings();
    this.uuid = UUID.randomUUID();
  }

  public CompletableFuture<Void> initialize(final CommandSender leader, final int min, final int max, final boolean quickJoinable) {
    this.manager = new PreGamePlayerManager(this, leader, min, max, quickJoinable);
    this.events = new PreGameEvents(this);
    this.mapSchematicIO = new MapSchematicIO(this.settings, this.uuid);
    this.events.registerEvents();
    this.manager.initialize();
    return this.mapSchematicIO.pasteMap();
  }

  public void startGame() {
    final Collection<Player> players = this.manager.getParticipants();
    final Collection<Player> killers = this.manager.getMurderers();
    this.manager.assignKiller();
    this.game.startGame(this.settings, killers, players, this.callback, this.mapSchematicIO, this.uuid);
    this.shutdown(false);
  }

  public void shutdown(final boolean forced) {
    final AtomicBoolean disabling = this.plugin.isDisabling();
    this.events.unregisterEvents();
    this.manager.shutdown();
    if (disabling.get()) {
      this.mapSchematicIO.resetMapShutdown();
    } else if (forced) {
      this.mapSchematicIO.resetMap();
    }
  }

  public MurderRun getPlugin() {
    return this.plugin;
  }

  public Game getGame() {
    return this.game;
  }

  public GameSettings getSettings() {
    return this.settings;
  }

  public String getId() {
    return this.id;
  }

  public PreGamePlayerManager getPlayerManager() {
    return this.manager;
  }

  public GameEventsListener getCallback() {
    return this.callback;
  }

  public PreGameEvents getEvents() {
    return this.events;
  }

  public GameManager getGameManager() {
    return this.gameManager;
  }

  public MapSchematicIO getMapSchematicIO() {
    return this.mapSchematicIO;
  }

  public UUID getUuid() {
    return this.uuid;
  }
}
