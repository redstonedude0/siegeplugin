package redstonedude.plugins.siege.handler;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockHandlerListener implements Listener {

	public static ArrayList<Location> goldBlocks = new ArrayList<Location>();

	public static boolean isProtectedPlacement(Location l) {
		if (l.getChunk().equals(InfiniteInventoryHandler.chest.getChunk())) {
			int goldBlockSize = BlockHandlerListener.goldBlocks.size();
			for (int i = 0; i < goldBlockSize; i++) {
				Location l1 = BlockHandlerListener.goldBlocks.get(i).clone().add(0, 1, 0);
				Location l2 = l1.clone().add(0, 1, 0);
				Location l3 = l1.clone().add(0, 2, 0);
				if (l.equals(l1) || l.equals(l2) || l.equals(l3)) {
					return true;
				}
			}
		}
		int spawnsSize = SpawningHandler.spawnLocations.size();
		for (int i = 0; i < spawnsSize; i++) {
			Location l1 = SpawningHandler.spawnLocations.get(i).getBlock().getLocation();
			Location l2 = l1.clone().add(0, 1, 0);
			Location l3 = l1.clone().add(0, 2, 0);
			if (l.equals(l1) || l.equals(l2) || l.equals(l3)) {
				return true;
			}

		}
		return false;
	}

	/*
	 * 0 = Not protected
	 * 1 = Protected but will not end game if broken
	 * 2 = Protected and will end game if broken
	 */
	
	public static int isProtectedDestroy(Location l) {
		int spawnsSize = SpawningHandler.spawnLocations.size();
		for (int i = 0; i < spawnsSize; i++) {
			if (l.equals(SpawningHandler.spawnLocations.get(i).getBlock().getLocation().add(0, -1, 0))) {
				return 1;
			}
		}
		if (l.getChunk().equals(InfiniteInventoryHandler.chest.getChunk())) {
			int goldBlockSize = BlockHandlerListener.goldBlocks.size();
			for (int i = 0; i < goldBlockSize; i++) {
				if (l.equals(BlockHandlerListener.goldBlocks.get(i))) {
					return 2;
				}
			}
			if (l.equals(InfiniteInventoryHandler.chest)) {
				return 2;
			}
		}
		return 0;
	}

	@EventHandler
	public static void onBlockPlace(BlockPlaceEvent e) {
		if (isProtectedPlacement(e.getBlock().getLocation())) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.GREEN + "[Siege] " + ChatColor.BLUE + "You cannot build here");
		}
		if (e.getBlockAgainst().getLocation().equals(InfiniteInventoryHandler.chest)) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.GREEN + "[Siege] " + ChatColor.BLUE + "You cannot build here");
		}
	}

	@EventHandler
	public static void onBlockBreak(BlockBreakEvent e) {
		if (isProtectedDestroy(e.getBlock().getLocation()) != 0) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.GREEN + "[Siege] " + ChatColor.BLUE + "You cannot break this block");
		}
	}

	@EventHandler
	public static void onBlockBreakExplode(EntityExplodeEvent e) {
		int stop = isMultipleProtectedDestroy(e.blockList());
		if (stop == 1) {
			e.setCancelled(true);
		} else if (stop == 2) {
			e.setCancelled(true);
			if (!ControlHandler.gameEnded) {
				ControlHandler.endGame();
			}
		}
	}
	
	public static int isMultipleProtectedDestroy(List<Block> blocks) {
		int blockSize = blocks.size();
		int toReturn = 0;
		for (int i = 0; i < blockSize; i++) {
			if (toReturn != 2) {
				int thisBlock = isProtectedDestroy(blocks.get(i).getLocation());
				if (thisBlock > toReturn) {
					toReturn = thisBlock;
				}
			}
		}
		return toReturn;
	}

}
