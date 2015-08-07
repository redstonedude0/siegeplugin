package redstonedude.plugins.siege.src;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import redstonedude.plugins.siege.customdata.CustomPlayerData;
import redstonedude.plugins.siege.customdata.DataLoader;
import redstonedude.plugins.siege.handler.BlockHandlerListener;
import redstonedude.plugins.siege.handler.CommandHandler;
import redstonedude.plugins.siege.handler.ControlHandler;
import redstonedude.plugins.siege.handler.InfiniteInventoryHandler;
import redstonedude.plugins.siege.handler.InventoryHandlerListener;
import redstonedude.plugins.siege.handler.PlayerEntityHandlerListener;
import redstonedude.plugins.siege.handler.PurchasesHandler;
import redstonedude.plugins.siege.handler.SpawningHandler;
import redstonedude.plugins.siege.task.TaskZombieCheck;
import redstonedude.plugins.siege.utils.ActionBar;
import redstonedude.plugins.siege.utils.ChatHelper;
import redstonedude.plugins.siege.utils.MathHelper;

public class Main extends JavaPlugin implements Listener {
	public static FileConfiguration cfg;
	static int waveTime;
	public static int playerCount = 0;
	public static int wave = -1;
	public static Server server;

	public static PlayerEntityHandlerListener playerEntityHandlerListener = new PlayerEntityHandlerListener();
	public static BlockHandlerListener blockHandlerListener = new BlockHandlerListener();
	public static InventoryHandlerListener inventoryHandlerListener = new InventoryHandlerListener();

	public static Main main;

	public static Random rand = new Random();

	static TaskZombieCheck zombieCheck = new TaskZombieCheck();

	public static int bossWave = -1;
	// public static boolean startSkipped = false;

	public static ScoreboardManager sbm;
	public static Scoreboard infoboard;
	public static int mobsCounter = 0;
	public static int counter = 0;
	public static int ticker = 0;
	public static int nextWaveTime = 0;
	public static boolean startedYet = false;
	public static int timerUntilForceStart = 300;

	public static void updateScoreboard(int timeToNextWave) {
		mobsCounter++;
		int playerNum = PlayerEntityHandlerListener.players.size();
		for (int i = 0; i < playerNum; i++) {
			CustomPlayerData cpd = PlayerEntityHandlerListener.getPlayerData(PlayerEntityHandlerListener.players.get(i));
			Scoreboard board = cpd.board;
			Objective objective2 = board.getObjective(DisplaySlot.SIDEBAR);
			Objective objective = board.registerNewObjective("mob_" + mobsCounter + "_" + i, "dummy");
			if (wave % 10 == 9) {
				objective.setDisplayName("§aRest Wave");
				Score score = objective.getScore(ChatColor.GOLD + "Build your defences!");
				score.setScore(3);
			} else if (wave % 10 == 0 && wave != 0) {
				objective.setDisplayName("§cBoss Wave");
				Score score;
				switch (bossWave) {
				case 0:
					score = objective.getScore(ChatColor.GREEN + "Type: §c" + ChatColor.BOLD + "Explosive");
					score.setScore(6);
					score = objective.getScore(ChatColor.GREEN + "Wallbreakers: §c" + PlayerEntityHandlerListener.wallbreakers.size());
					score.setScore(5);
					score = objective.getScore(ChatColor.GREEN + "Artillery Golems: §c" + PlayerEntityHandlerListener.golems.size());
					score.setScore(4);
					score = objective.getScore(ChatColor.GREEN + "Bombs: §c" + PlayerEntityHandlerListener.bombs.size());
					score.setScore(3);
					break;
				case 1:
					score = objective.getScore(ChatColor.GREEN + "Type: §c" + ChatColor.BOLD + "Swordsmen");
					score.setScore(6);
					score = objective.getScore(ChatColor.GREEN + "Swordsmen: §c" + PlayerEntityHandlerListener.swordsmen.size());
					score.setScore(5);
					break;
				case 2:
					score = objective.getScore(ChatColor.GREEN + "Type: §c" + ChatColor.BOLD + "Missile");
					score.setScore(6);
					score = objective.getScore(ChatColor.GREEN + "Missiles: §c" + (SpawningHandler.golemCount*2));
					score.setScore(5);
					score = objective.getScore(ChatColor.GREEN + "Blast Size: §c" + MathHelper.cap((Main.wave/2),15));
					score.setScore(4);
					break;
				case 3:
					score = objective.getScore(ChatColor.GREEN + "Type: §c" + ChatColor.BOLD + "Giant");
					score.setScore(6);
					score = objective.getScore(ChatColor.GREEN + "Giants: " + PlayerEntityHandlerListener.giants.size());
					score.setScore(5);
					score = objective.getScore(ChatColor.GREEN + "Swordsmen: " + PlayerEntityHandlerListener.swordsmen.size());
					score.setScore(4);
					score = objective.getScore(ChatColor.GREEN + "Bombs: " + PlayerEntityHandlerListener.bombs.size());
					score.setScore(3);
					break;
				}
			} else if (wave == 0) {
				objective.setDisplayName("§cPreperation Stage");
				Score score = objective.getScore(ChatColor.GOLD + "Collect your items from the chest");
				score.setScore(7);
				score = objective.getScore(ChatColor.GOLD + "Build your defences as a team");
				score.setScore(6);
				score = objective.getScore(ChatColor.GOLD + "Buy items and upgrades in the shop");
				score.setScore(5);
				score = objective.getScore(ChatColor.GOLD + "Teamspeak server: <public ip>");
				score.setScore(4);
				score = objective.getScore(ChatColor.GOLD + "This games teamspeak: Siege Lounge #" + server.getMotd().substring(5, 6));
				score.setScore(3);
			} else {
				objective.setDisplayName("§aMobs");
				Score score = objective.getScore(ChatColor.GREEN + "Swordsman: " + PlayerEntityHandlerListener.swordsmen.size());
				score.setScore(7);
				score = objective.getScore(ChatColor.GREEN + "Wallbreakers: " + PlayerEntityHandlerListener.wallbreakers.size());
				score.setScore(6);
				score = objective.getScore(ChatColor.GREEN + "Artillery Golems: " + PlayerEntityHandlerListener.golems.size());
				score.setScore(5);
				score = objective.getScore(ChatColor.GREEN + "Miners: " + PlayerEntityHandlerListener.miners.size());
				score.setScore(4);
				score = objective.getScore(ChatColor.GREEN + "Bombs: " + PlayerEntityHandlerListener.bombs.size());
				score.setScore(3);
			}
			Score score = objective.getScore(ChatColor.GOLD + "Money: £" + ChatColor.BOLD + ChatColor.GREEN + cpd.points);
			score.setScore(2);
			score = objective.getScore(ChatColor.AQUA + "Time until next wave: " + ChatColor.BOLD + ChatColor.RED + getNextWaveTime(timeToNextWave));
			score.setScore(1);
			score = objective.getScore(ChatColor.AQUA + "Current Wave: " + ChatColor.BOLD + ChatColor.DARK_AQUA + Main.wave);
			score.setScore(0);
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			try {
				objective2.unregister();
			} catch (NullPointerException e) {
				// runs on first run was nothing is in sidebar
			}
			server.getPlayer(PlayerEntityHandlerListener.players.get(i)).setScoreboard(board);
		}
	}

	public static void updateActionBarGame(int timeToNextWave) {
		String bar = "§6>> §bTime to next wave: §e" + ChatColor.BOLD + getNextWaveTime(timeToNextWave);
		bar += ChatColor.RESET + "§c || §aYour money: £§2" + ChatColor.BOLD;
		String ender = ChatColor.RESET + " §6<<";
		int playerNum = PlayerEntityHandlerListener.players.size();
		for (int i = 0; i < playerNum; i++) {
			Player p = server.getPlayer(PlayerEntityHandlerListener.players.get(i));
			String message = bar + PlayerEntityHandlerListener.getPlayerData(p.getUniqueId()).points + ender;
			ActionBar.sendActionBar(p, message);
			// .setScoreboard(board);
		}
	}

	public static String getNextWaveTime(int ticks) {
		String time = ((int) Math.floor(ticks / 1200)) + ":";
		String seconds = "" + ((ticks % 1200) / 20);
		if (seconds.length() == 1) {
			seconds = "0" + seconds;
		}
		return time + seconds;
	}

	public static void checkGame(int timeToNextWave) {
		if (timeToNextWave == 0) {
			warning(0);
		}
		if (timeToNextWave == 20) {
			warning(20);
		}
		if (timeToNextWave == 40) {
			warning(40);
		}
		if (timeToNextWave == 60) {
			warning(60);
		}
	}

	public static void updateLobbyActionBar(boolean started) {
		if (!started) {
			String bar = "§6>> §cSiege §6|| ";
			int playerNum = PlayerEntityHandlerListener.players.size();
			if (playerNum >= 4) {
				bar += "§2Players: " + ChatColor.BOLD + playerNum + "/10 §6|| §aGame starting in: §c" + ChatColor.BOLD + getNextWaveTime(timerUntilForceStart * 20);
			} else {
				bar += "§2Players: " + ChatColor.BOLD + playerNum + "/10 §6|| §cNot enough players to force-start";
			}
			String ender = ChatColor.RESET + " §6<<";
			for (int i = 0; i < playerNum; i++) {
				Player p = server.getPlayer(PlayerEntityHandlerListener.players.get(i));
				String message = bar + ender;
				ActionBar.sendActionBar(p, message);
				// .setScoreboard(board);
			}
		} else {
			String bar = "§6>> §cSiege §6|| §2Thank you for playing §6|| §4Server closing in: §2" + ChatColor.BOLD + getNextWaveTime(timerUntilForceStart*20);
			int playerNum = PlayerEntityHandlerListener.players.size();
			String ender = ChatColor.RESET + " §6<<";
			for (int i = 0; i < playerNum; i++) {
				Player p = server.getPlayer(PlayerEntityHandlerListener.players.get(i));
				String message = bar + ender;
				ActionBar.sendActionBar(p, message);
				// .setScoreboard(board);
			}
		}
	}

	public static void updateLobbyScoreboard(boolean started) {
		if (!started) {
			int playerNum = PlayerEntityHandlerListener.players.size();
			for (int i = 0; i < playerNum; i++) {
				CustomPlayerData cpd = PlayerEntityHandlerListener.getPlayerData(PlayerEntityHandlerListener.players.get(i));
				Scoreboard board = cpd.board;
				Objective objective2 = board.getObjective(DisplaySlot.SIDEBAR);
				Objective objective = board.registerNewObjective("info_" + i + "_" + ticker, "dummy");
				objective.setDisplayName("§cSiege");
				Score score = objective.getScore(ChatColor.AQUA + "Welcome to Siege");
				score.setScore(4);
				score = objective.getScore(ChatColor.GREEN + "Server: " + ChatColor.RED + Main.server.getMotd());
				score.setScore(3);
				score = objective.getScore(ChatColor.GREEN + "Status: " + ChatColor.RED + "READY");
				score.setScore(2);
				if (playerNum >= 4) {
					score = objective.getScore(ChatColor.GREEN + "Players: " + ChatColor.GREEN + playerNum + "/10");
					score.setScore(1);
					score = objective.getScore(ChatColor.GREEN + "Time until start: " + ChatColor.GREEN + getNextWaveTime(timerUntilForceStart*20));
					score.setScore(0);
				} else {
					score = objective.getScore(ChatColor.GREEN + "Players: " + ChatColor.RED + playerNum + "/10");
					score.setScore(1);
				}
				try {
					objective2.unregister();
				} catch (NullPointerException e) {
					// runs on first run as nothing is in sidebar
				}
				//ChatHelper.broadcast("scoreboarding");
				objective.setDisplaySlot(DisplaySlot.SIDEBAR);
				server.getPlayer(PlayerEntityHandlerListener.players.get(i)).setScoreboard(board);
			}
		} else {
			int playerNum = PlayerEntityHandlerListener.players.size();
			for (int i = 0; i < playerNum; i++) {
				CustomPlayerData cpd = PlayerEntityHandlerListener.getPlayerData(PlayerEntityHandlerListener.players.get(i));
				Scoreboard board = cpd.board;
				Objective objective2 = board.getObjective(DisplaySlot.SIDEBAR);
				Objective objective = board.registerNewObjective("info_" + i + "_" + ticker, "dummy");
				objective.setDisplayName("§cSiege");
				Score score = objective.getScore(ChatColor.AQUA + "Thank you for playing siege");
				score.setScore(4);
				score = objective.getScore(ChatColor.GREEN + "Server: " + ChatColor.RED + Main.server.getMotd());
				score.setScore(3);
				score = objective.getScore(ChatColor.GREEN + "The game will end in 10 seconds");
				score.setScore(2);
				score = objective.getScore(ChatColor.GREEN + "Type " + ChatColor.BOLD + "/again" + ChatColor.RESET + ChatColor.GREEN + " to join another game");
				score.setScore(1);
				score = objective.getScore(ChatColor.GREEN +"Made by Restonedude for "+ChatColor.BOLD + ChatColor.ITALIC + "Freedom!");
				score.setScore(0);
				if (playerNum >= 4) {
					score = objective.getScore(ChatColor.GREEN + "Players: " + ChatColor.GREEN + playerNum + "/10");
					score.setScore(1);
					score = objective.getScore(ChatColor.GREEN + "Time until start: " + ChatColor.GREEN + getNextWaveTime(timerUntilForceStart*20));
					score.setScore(0);
				} else {
					score = objective.getScore(ChatColor.GREEN + "Players: " + ChatColor.RED + playerNum + "/10");
					score.setScore(1);
				}
				try {
					objective2.unregister();
				} catch (NullPointerException e) {
					// runs on first run as nothing is in sidebar
				}
				//ChatHelper.broadcast("scoreboarding");
				objective.setDisplaySlot(DisplaySlot.SIDEBAR);
				server.getPlayer(PlayerEntityHandlerListener.players.get(i)).setScoreboard(board);
			}
		}
	}
	
	public static void checkLobby() {
		if (timerUntilForceStart == 0) {
			ChatHelper.broadcast("Insert bungee pull-back command here");
			startedYet = false;
		}
	}

	public static void ticker() {
		ticker++;
		int timeToNextWave = nextWaveTime - (ticker * 20);
		if (!ControlHandler.gameEnded && startedYet) {
			// game running
			updateActionBarGame(timeToNextWave);
			updateScoreboard(timeToNextWave);
			checkGame(timeToNextWave);
		} else if (!ControlHandler.gameEnded && !startedYet) {
			updateLobbyActionBar(false);
			if (PlayerEntityHandlerListener.players.size() >= 4) {
			timerUntilForceStart--;
			} else {
				timerUntilForceStart = 300;
			}
			updateLobbyScoreboard(false);
			// in lobby (waiting for start)
		} else if (ControlHandler.gameEnded && startedYet) {
			timerUntilForceStart--;
			updateLobbyActionBar(true);
			updateLobbyScoreboard(true);
			checkLobby();
			// just ended
		} else if (ControlHandler.gameEnded && !startedYet) {
			// server shutting down in this stage, send bungee pullback blip
			// here
		}
	}

	@Override
	public void onEnable() {
		getLogger().info("Enabling Siege, loading config");
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		sbm = Bukkit.getScoreboardManager();
		main = this;
		server = getServer();
		cfg = getConfig();
		waveTime = cfg.getInt("wave-time");
		String chestLocation = cfg.getString("chest-location");
		Location l3 = deserializeLocation(chestLocation);
		InfiniteInventoryHandler.chest = l3.clone();
		String lobbyLocation = cfg.getString("lobby-location");
		PlayerEntityHandlerListener.lobbyLocation = deserializeLocation(lobbyLocation);
		List<String> mobLocations = cfg.getStringList("mob-locations");
		int locationNum = mobLocations.size();
		for (int i = 0; i < locationNum; i++) {
			Location l = deserializeLocation(mobLocations.get(i));
			l.add(0.5, 0, 0.5);
			SpawningHandler.spawnLocations.add(l);
			Location l2 = deserializeLocation(mobLocations.get(i));
			l2.add(0, -1, 0);
			server.getWorld("world").getBlockAt(l2).setType(Material.GLOWSTONE);
		}
		Chunk ch = l3.getChunk();
		//TODO optimise this loop
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (ch.getBlock(i, l3.getBlockY() - 1, j).getType() == Material.GOLD_BLOCK) {
					Location l1 = new Location(server.getWorld("world"), i + ch.getX() * 16, l3.getBlockY() - 1, j + ch.getZ() * 16);
					BlockHandlerListener.goldBlocks.add(l1);
				}
			}
		}
		getLogger().info("config loaded, registering events");
		PluginManager pm = server.getPluginManager();
		pm.registerEvents(main, main);
		pm.registerEvents(playerEntityHandlerListener, main);
		pm.registerEvents(blockHandlerListener, main);
		pm.registerEvents(inventoryHandlerListener, main);
		InventoryHandlerListener.init();
		PurchasesHandler.init();
		infoboard = sbm.getNewScoreboard();
		server.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			@Override
			public void run() {
				Main.ticker();
			}
		}, 0L, 20L);
		getLogger().info("Siege enabled");
		saveConfig();
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabling Siege");
	}

	public Location deserializeLocation(String s) {
		try {
			String[] s1 = s.split(":");
			int x = Integer.parseInt(s1[0]);
			int y = Integer.parseInt(s1[1]);
			int z = Integer.parseInt(s1[2]);
			return new Location(server.getWorld("world"), x, y, z);
		} catch (Exception e) {
			getLogger().severe("Incorrect location formatting");
			e.printStackTrace();
			return null;
		}
	}

	public static void startGame() {
		if (DataLoader.loadAllData()) {
			ticker = 0;
			wave = 0;
			int playersQty = PlayerEntityHandlerListener.players.size();
			int goldBlocksSize = BlockHandlerListener.goldBlocks.size();
			for (int i = 0; i < playersQty; i++) {
				UUID u = PlayerEntityHandlerListener.players.get(i);
				Player p = server.getPlayer(u);
				Location l1 = BlockHandlerListener.goldBlocks.get(rand.nextInt(goldBlocksSize));
				l1.add(0.5, 1, 0.5);
				PlayerEntityHandlerListener.clearInventory(p);
				p.setFoodLevel(20);
				p.setSaturation(20);
				String title = "Siege";
				String subtitle = "You have until nightfall to prepare";
				ChatHelper.sendTitle(p, title, subtitle, "red", "orange", 1, 4, 1);
				p.teleport(l1);
			}
			ChatHelper.broadcast("You have until nightfall to prepare...");
			server.getWorld("world").setFullTime(6000);
			SpawningHandler.bufferSpawning();
			InfiniteInventoryHandler.handleInventories();
			nextWaveTime = 7500;
			// zombieCheck.runTaskTimer(main, 0L, 1L);
			server.getScheduler().scheduleSyncRepeatingTask(main, zombieCheck, 0L, 40L);
			updateScoreboard(nextWaveTime - (ticker * 20));
			startedYet = true;
		} else {
			// TODO throw error
			ChatHelper.broadcast("SEVERE ERROR COULD NOT LOAD ALL DATA");
		}
	}

	public static void warning(int i) {
		if (wave == 0) {
			if (i == 1200) {
				ChatHelper.broadcast("As the sun sets a sense of urgency arises; you have 1 minute left.");
			} else if (i == 0) {
				ChatHelper.broadcast("The siege has begun");
				runWave();
			} else {
				ChatHelper.broadcast("The siege will begin in " + (int) (i / 20) + " seconds!");
			}
		} else if (i == 0) {
			ChatHelper.broadcast("The next wave has begun!");
			runWave();
		} else {
			ChatHelper.broadcast("The next wave arrives in " + (int) (i / 20) + " seconds!");
		}
	}

	public static void runWave() {
		if (wave != -2) {
			if (wave == 0) {
				// begin
				int playersQty = PlayerEntityHandlerListener.players.size();
				for (int i = 0; i < playersQty; i++) {
					UUID u = PlayerEntityHandlerListener.players.get(i);
					Player p = server.getPlayer(u);
					p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
				}
				server.broadcastMessage(ChatColor.GREEN + "[Siege]" + ChatColor.BLUE + " Midnight has fallen, wave 1 has begun.");
				wave = 1;
				SpawningHandler.handleSpawning();
				SpawningHandler.bufferSpawning();
				InfiniteInventoryHandler.handleInventories();
				nextWaveTime += waveTime;
				updateScoreboard(nextWaveTime - (ticker * 20));
			} else {
				// next wave
				int playersQty = PlayerEntityHandlerListener.players.size();
				for (int i = 0; i < playersQty; i++) {
					UUID u = PlayerEntityHandlerListener.players.get(i);
					Player p = server.getPlayer(u);
					p.playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 1.0F, 1.0F);
				}
				wave++;
				server.broadcastMessage(ChatColor.GREEN + "[Siege]" + ChatColor.BLUE + " Wave " + wave + " has arrived.");
				SpawningHandler.handleSpawning();
				SpawningHandler.bufferSpawning();
				int waitTime = waveTime;
				if (wave % 10 == 9) {
					// just before boss wave
					Main.server.broadcastMessage(ChatColor.GREEN + "[Siege]" + ChatColor.BLUE + " The mobs are building their forces, use this time to build your base");
					waitTime = waveTime * 2;
				} else if (wave % 10 == 0) {
					waitTime = waveTime * 2;
				}
				InfiniteInventoryHandler.handleInventories();
				nextWaveTime += waitTime;
				updateScoreboard(nextWaveTime - (ticker * 20));
			}
			// fireworks!!!
		} // siege has ended. yayyyy
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return CommandHandler.handleCommand(sender, cmd, commandLabel, args);
	}

}
