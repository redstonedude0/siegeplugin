package redstonedude.plugins.siege.handler;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import redstonedude.plugins.siege.customdata.CustomPlayerData;
import redstonedude.plugins.siege.src.Main;
import redstonedude.plugins.siege.utils.ChatHelper;

public class CommandHandler implements Listener {

	public static boolean handleCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("start")) {
				if (sender.hasPermission("siege.start")) {
					ChatHelper.broadcast("Siege force-started by " + player.getName());
					Main.startGame();
					return true;
				}
			} else if (cmd.getName().equalsIgnoreCase("nextWave")) {
				if (sender.hasPermission("siege.next")) {
					Main.nextWaveTime = (Main.ticker * 20) + 20;
					ChatHelper.broadcast(ChatColor.BOLD + "" + ChatColor.GOLD + player.getName() + ChatColor.RESET + ChatColor.BLUE + " Forced the next wave");
					return true;
				}
			} else if (cmd.getName().equalsIgnoreCase("kit")) {
				if (args.length == 1) {
					PlayerEntityHandlerListener.setKit(player, Integer.parseInt(args[0]));
					ChatHelper.sendMessage(player, "Set your kit to kit " + args[0]);
				} else {
					Player p = Main.server.getPlayer(args[0]);
					PlayerEntityHandlerListener.setKit(p, Integer.parseInt(args[1]));
					ChatHelper.sendMessage(player, "Set " + p.getName() + "'s kit to kit " + args[1]);
					ChatHelper.sendMessage(p, player.getName() + " Set your kit to kit " + args[1]);
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase("ghost")) {
				if (PlayerEntityHandlerListener.ghostplayers.contains(player.getUniqueId())) {
					PlayerEntityHandlerListener.removeGhostPlayer(player);
					PlayerEntityHandlerListener.addPlayer(player);
				} else {
					PlayerEntityHandlerListener.addGhostPlayer(player);
					PlayerEntityHandlerListener.removePlayer(player);
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase("spawnshit")) {
				Location l = ((Player) sender).getLocation();
				SpawningHandler.spawnEntity(l, Integer.parseInt(args[0]));
				ChatHelper.sendMessage(player, "Shit has been spawned");
				return true;
			} else if (cmd.getName().equalsIgnoreCase("join")) {
				if (!PlayerEntityHandlerListener.players.contains(player.getUniqueId())) {
					PlayerEntityHandlerListener.addPlayer(player);
				} else {
					ChatHelper.sendMessage(player, "You are already in the game");
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase("boss")) {
				Main.bossWave = Integer.parseInt(args[0]);
				ChatHelper.sendMessage(player, "Boss wave set to " + args[0]);
				return true;
			} else if (cmd.getName().equalsIgnoreCase("joinghost")) {
				if (player.hasPermission("siege.ghost")) {
					PlayerEntityHandlerListener.addGhostPlayer(player);
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase("money")) {
				if (player.hasPermission("siege.money")) {
					Player p = Main.server.getPlayer(args[0]);
					String com = args[1];
					Integer money1 = Integer.parseInt(args[2]);
					if (p != null) {
						switch (com) {
						case "add":
							CustomPlayerData cpd = PlayerEntityHandlerListener.getPlayerData(p.getUniqueId());
							cpd.points += money1;
							ChatHelper.sendMessage(player, "Added £" + money1 + " to " + p.getName());
							ChatHelper.sendMessage(p, player.getName() + " Added £" + money1 + " to your money");
							break;
						case "set":
							CustomPlayerData cpd1 = PlayerEntityHandlerListener.getPlayerData(p.getUniqueId());
							cpd1.points = money1;			
							ChatHelper.sendMessage(player, "Set " + p.getName() + "'s money to £" + money1);
							ChatHelper.sendMessage(p, player.getName() + " Set your money to £" + money1);
							break;
						case "remove":
							CustomPlayerData cpd2 = PlayerEntityHandlerListener.getPlayerData(p.getUniqueId());
							cpd2.points -= money1;
							ChatHelper.sendMessage(player, "Removed £" + money1 + " from " + p.getName());
							ChatHelper.sendMessage(p, player.getName() + " Removed £" + money1 + " from your money");
							break;
						}
					}
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase("retain")) {
				if (PlayerEntityHandlerListener.retainingPlayers.contains(player.getUniqueId()) {
					PlayerEntityHandlerListener.removeRetainingPlayer(player);
				} else {
					PlayerEntityHandlerListener.addRetainingPlayer(player);
				}
			}
		}
		return false;
	}

}
