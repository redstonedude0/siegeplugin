package redstonedude.plugins.siege.handler;

import java.util.ArrayList;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftGiant;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftIronGolem;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import redstonedude.plugins.siege.customdata.CustomHitterData;
import redstonedude.plugins.siege.customdata.CustomPlayerData;
import redstonedude.plugins.siege.customdata.MetadataValueHitters;
import redstonedude.plugins.siege.kit.KitArcher;
import redstonedude.plugins.siege.kit.KitBuilder;
import redstonedude.plugins.siege.kit.KitSwordsman;
import redstonedude.plugins.siege.src.Main;
import redstonedude.plugins.siege.utils.ChatHelper;

public class PlayerEntityHandlerListener implements Listener {
	
	public static Location lobbyLocation;
	public static ArrayList<CustomPlayerData> customPlayerData = new ArrayList<CustomPlayerData>();

	public static CustomPlayerData getPlayerData(UUID u) {
		int dataSize = customPlayerData.size();
		for (int i = 0; i < dataSize; i++) {
			if (customPlayerData.get(i).u.equals(u)) {
				return customPlayerData.get(i);
			}
		}
		return null;
	}

	// public static ArrayList<Integer> mobs = new ArrayList<Integer>();
	public static ArrayList<Integer> golems = new ArrayList<Integer>();
	public static ArrayList<Integer> wallbreakers = new ArrayList<Integer>();
	public static ArrayList<Integer> bombs = new ArrayList<Integer>();
	public static ArrayList<Integer> miners = new ArrayList<Integer>();
	public static ArrayList<Integer> swordsmen = new ArrayList<Integer>();
	public static ArrayList<Integer> giants = new ArrayList<Integer>();
	public static ArrayList<Integer> ideas = new ArrayList<Integer>();
	// public static ArrayList<Integer> bombs = new ArrayList<Integer>();

	public static ArrayList<UUID> players = new ArrayList<UUID>();
	public static ArrayList<UUID> ghostplayers = new ArrayList<UUID>();

	@EventHandler
	public static void onQuit(PlayerQuitEvent event) {
		disconnectPlayer(event.getPlayer());
	}

	@EventHandler
	public static void onKicked(PlayerKickEvent event) {
		disconnectPlayer(event.getPlayer());
	}

	public static void disconnectPlayer(Player p) {
		if (players.contains(p.getUniqueId())) {
			removePlayer(p);
		} else {
			removeGhostPlayer(p);
		}
	}

	public static void removePlayer(Player p) {
		players.remove(p.getUniqueId());
		Main.playerCount--;
		int cpdSize = customPlayerData.size();
		for (int i = 0; i < cpdSize; i++) {
			if (customPlayerData.get(i).u.equals(p.getUniqueId())) {
				customPlayerData.remove(i);
			}
		}
		if (Main.wave == -1) {
			ChatHelper.broadcast("Player '" + p.getName() + "' quit (" + Main.playerCount + "/10)");
		} else if (Main.wave == -2) {
			ChatHelper.broadcast("Player '" + p.getName() + "' quit the gamemode");
		} else {
			ChatHelper.broadcast("Player '" + p.getName() + "' has deserted you! Items have been added to the chest");
		}
	}

	public static void removeGhostPlayer(Player p) {
		ChatHelper.sendMessage(p, "You left ghost mode, use /ghost to re-enter ghost mode");
		if (Main.wave != -1 && Main.wave != -2) {
			p.teleport(lobbyLocation);
		} else {
			respawnPlayer(p);
		}
		ghostplayers.remove(p.getUniqueId());
	}

	public static void addPlayer(Player p) {

		Objective objective = Main.infoboard.registerNewObjective("info_" + Main.playerCount + "_" + Main.ticker, "dummy");
		objective.setDisplayName("§cSiege");
		Score score = objective.getScore(ChatColor.AQUA + "Welcome to Siege");
		score.setScore(2);
		score = objective.getScore(ChatColor.GREEN + "Server: " + ChatColor.RED + Main.server.getMotd());
		score.setScore(1);
		score = objective.getScore(ChatColor.GREEN + "Status: " + ChatColor.RED + "READY");
		score.setScore(0);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		p.setScoreboard(Main.infoboard);
		players.add(p.getUniqueId());
		Main.playerCount++;
		if (Main.playerCount < 10) {
			ChatHelper.sendMessage(p, "Wait for 10 players to join");
			ChatHelper.broadcast("Player '" + p.getName() + "' joined (" + Main.playerCount + "/10)");
		} else {
			ChatHelper.broadcast("10 Players have joined, starting game...");
			Main.startGame();
		}
		p.teleport(lobbyLocation);
		CustomPlayerData cpd = new CustomPlayerData();
		UUID u = p.getUniqueId();
		cpd.u = u;
		cpd.kit = new KitSwordsman();
		cpd.points = 0;
		cpd.purchasing = "";
		cpd.shopType = 0;
		cpd.attachment = p.addAttachment(Main.main);
		cpd.board = Main.sbm.getNewScoreboard();
		customPlayerData.add(cpd);
	}

	public static void addGhostPlayer(Player p) {
		ChatHelper.sendMessage(p, "You entered ghost mode, use /ghost to join the game");
		ChatHelper.broadcast("Player '" + p.getName() + "' entered ghost mode and so does not affect player count");
		p.setGameMode(GameMode.SPECTATOR);
		ghostplayers.add(p.getUniqueId());
	}

	@EventHandler
	public static void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if (p.hasPermission("siege.ghost")) {
			addGhostPlayer(p);
		} else {
			addPlayer(p);
		}
	}

	public static void setKit(Player p, int i) {
		UUID u = p.getUniqueId();
		int dataLen = PlayerEntityHandlerListener.customPlayerData.size();
		for (int j = 0; j < dataLen; j++) {
			if (PlayerEntityHandlerListener.customPlayerData.get(j).u.equals(u)) {
				switch (i) {
				case 0:
					PlayerEntityHandlerListener.customPlayerData.get(j).kit = new KitArcher();
					break;
				case 1:
					PlayerEntityHandlerListener.customPlayerData.get(j).kit = new KitSwordsman();
					break;
				case 2:
					PlayerEntityHandlerListener.customPlayerData.get(j).kit = new KitBuilder();
					break;
				}
			}
		}
	}

	@EventHandler
	public static void onBlockPlace(BlockPlaceEvent event) {
		SpecialWeaponHandler.blockPlace(event);
	}

	@EventHandler
	public static void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock().getLocation().equals(InfiniteInventoryHandler.chest)) {
				event.getPlayer().closeInventory();
				Inventory i = InfiniteInventoryHandler.getInventory(event.getPlayer().getUniqueId()).getInventory(0);
				event.getPlayer().openInventory(i);
				Player p = event.getPlayer();
				p.openInventory(i);

				event.setCancelled(true);
			} else {
				SpecialWeaponHandler.onThrow(event);
			}
		} else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
			SpecialWeaponHandler.onThrow(event);
		}
	}

	public static void respawnPlayer(Player p) {
		p.setGameMode(GameMode.SURVIVAL);
		int goldBlocksSize = BlockHandlerListener.goldBlocks.size();
		Location l = BlockHandlerListener.goldBlocks.get(Main.rand.nextInt(goldBlocksSize)).clone();
		l.add(0.5, 1, 0.5);
		p.teleport(l);
		ChatHelper.broadcast("Player '" + p.getName() + "' has respawned");
	}

	public static void clearInventory(Player p) {
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
	}

	@EventHandler
	public static void onPlayerSpawn(PlayerRespawnEvent e) {
		final Player p = e.getPlayer();
		clearInventory(p);
		// p.setGameMode(GameMode.SPECTATOR);
		p.teleport(InfiniteInventoryHandler.chest);
		Main.server.getScheduler().scheduleSyncDelayedTask(Main.main, new Runnable() {
			public void run() {
				p.setGameMode(GameMode.SPECTATOR);
				p.teleport(InfiniteInventoryHandler.chest);
			}
		}, 1L);
		Main.server.getScheduler().scheduleSyncDelayedTask(Main.main, new Runnable() {
			public void run() {
				respawnPlayer(p);
			}
		}, 200L);
	}

	@EventHandler
	public static void onPlayerDeath(PlayerDeathEvent e) {
		final Player p = (Player) e.getEntity();
		e.setKeepInventory(true);
		e.setDeathMessage(null);
		// p.setHealth(20);
		// p.setSaturation(20);
		ChatHelper.broadcast("Player '" + p.getName() + "' has died and will respawn in 10 seconds");
		// p.playSound(p.getLocation(), Sound.HURT_FLESH, 1, 1);
	}

	@EventHandler
	public static void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			int damage = (e.getDamage() > ((CraftLivingEntity) e.getEntity()).getHealth() ? (int) ((CraftLivingEntity) e.getEntity()).getHealth() : (int) e.getDamage());
			if (e.getEntity().getMetadata("hitters").isEmpty()) {
				MetadataValueHitters mvh = new MetadataValueHitters();
				mvh.hitTotal = damage;
				ArrayList<CustomHitterData> array = new ArrayList<CustomHitterData>();
				CustomHitterData chd = new CustomHitterData();
				chd.damageDealt = damage;
				chd.hitterID = e.getDamager().getUniqueId();
				array.add(chd);
				mvh.hitterDataArray = array;
				e.getEntity().setMetadata("hitters", mvh);
			} else {
				MetadataValueHitters mvh = (MetadataValueHitters) e.getEntity().getMetadata("hitters").get(0);
				mvh.hitTotal += damage;
				ArrayList<CustomHitterData> array = mvh.hitterDataArray;
				boolean found = false;
				for (CustomHitterData chd : array) {
					if (chd.hitterID.equals(e.getDamager().getUniqueId())) {
						found = true;
						chd.damageDealt += damage;
					}
				}
				if (!found) {
					CustomHitterData chd = new CustomHitterData();
					chd.hitterID = e.getDamager().getUniqueId();
					chd.damageDealt = damage;
					array.add(chd);
				}
			}
			// double pointsPer1Health = maxPoints / maxHealth;
			// ChatHelper.broadcast("" + pointsPer1Health);
			// double pointsToAdd = (e.getDamage() > ((CraftLivingEntity)
			// e.getEntity()).getHealth() ? (int) (((CraftLivingEntity)
			// e.getEntity()).getHealth() * pointsPer1Health) : (int)
			// (e.getDamage() * pointsPer1Health));
			// cpd.points += (int) pointsToAdd;
		}
	}

	public static void giveMoney(Entity e, double value) {
		MetadataValueHitters mvh = (MetadataValueHitters) e.getMetadata("hitters").get(0);
		ArrayList<CustomHitterData> array = mvh.hitterDataArray;
		double totalHits = mvh.hitTotal;
		double valuePerHit = value / totalHits;
		for (CustomHitterData chd : array) {
			UUID playerID = chd.hitterID;
			int toAdd = (int) (valuePerHit * chd.damageDealt);
			PlayerEntityHandlerListener.getPlayerData(playerID).points += toAdd;
		}
	}

	@EventHandler
	public static void onDeath(EntityDeathEvent e) {
		if (wallbreakers.contains(e.getEntity().getEntityId())) {
			wallbreakers.remove((Object) e.getEntity().getEntityId());
			giveMoney(e.getEntity(), 50);
			Main.updateScoreboard(Main.nextWaveTime - (Main.ticker * 20));
		} else if (bombs.contains(e.getEntity().getEntityId())) {
			bombs.remove((Object) e.getEntity().getEntityId());
			giveMoney(e.getEntity(), 5);
			Main.updateScoreboard(Main.nextWaveTime - (Main.ticker * 20));
		} else if (miners.contains(e.getEntity().getEntityId())) {
			miners.remove((Object) e.getEntity().getEntityId());
			giveMoney(e.getEntity(), 20);
			Main.updateScoreboard(Main.nextWaveTime - (Main.ticker * 20));
		} else if (swordsmen.contains(e.getEntity().getEntityId())) {
			//e.getEntity().getPassenger().getPassenger().remove();
			//e.getEntity().getPassenger().remove();
			swordsmen.remove((Object) e.getEntity().getEntityId());
			giveMoney(e.getEntity(), 40);
			Main.updateScoreboard(Main.nextWaveTime - (Main.ticker * 20));
		} else if (golems.contains(e.getEntity().getEntityId())) {
			golems.remove((Object) e.getEntity().getEntityId());
			giveMoney(e.getEntity(), 275);
			Main.updateScoreboard(Main.nextWaveTime - (Main.ticker * 20));
			final CraftIronGolem golem = (CraftIronGolem) e.getEntity();
			// golem is dying
			Location l = golem.getLocation();
			int bombs = 10 + Main.rand.nextInt(10);
			for (int i = 0; i < bombs; i++) {
				SpawningHandler.spawnEntity(l, 4);
			}
			Firework fw = (Firework) Main.server.getWorld("world").spawnEntity(l, EntityType.FIREWORK);
			FireworkMeta fwm = fw.getFireworkMeta();
			Type type = Type.BALL;
			org.bukkit.Color c1 = Color.RED;
			FireworkEffect fwe = FireworkEffect.builder().flicker(false).withColor(c1).with(type).build();
			fwm.addEffect(fwe);
			fwm.setPower(0);
			fw.setFireworkMeta(fwm);
		} else if (giants.contains(e.getEntity().getEntityId())) {
			giants.remove((Object) e.getEntity().getEntityId());
			giveMoney(e.getEntity(), 275);
			Main.updateScoreboard(Main.nextWaveTime - (Main.ticker * 20));
			final CraftGiant giant = (CraftGiant) e.getEntity();
			// golem is dying
			Location l = giant.getLocation();
			for (int i = 0; i < 30; i++) {
				SpawningHandler.spawnEntity(l, 0);
			}
			for (int i = 0; i < 30; i++) {
				SpawningHandler.spawnEntity(l, 4);
			}
			Firework fw = (Firework) Main.server.getWorld("world").spawnEntity(l, EntityType.FIREWORK);
			FireworkMeta fwm = fw.getFireworkMeta();
			Type type = Type.BALL;
			org.bukkit.Color c1 = Color.RED;
			FireworkEffect fwe = FireworkEffect.builder().flicker(false).withColor(c1).with(type).build();
			fwm.addEffect(fwe);
			fwm.setPower(0);
			fw.setFireworkMeta(fwm);
		}
	}

	@EventHandler
	public static void onPickup(PlayerPickupItemEvent event) {
		event.getItem().getEntityId();
		if (SpecialWeaponHandler.grenadeIDs.contains(event.getItem().getEntityId())) {
			event.setCancelled(true);
		}
	}

}
