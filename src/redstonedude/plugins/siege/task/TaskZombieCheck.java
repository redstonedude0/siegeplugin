package redstonedude.plugins.siege.task;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Cancellable;

import redstonedude.plugins.siege.customdata.DataLoader;
import redstonedude.plugins.siege.entity.ZombieMiner;
import redstonedude.plugins.siege.handler.BlockHandlerListener;
import redstonedude.plugins.siege.handler.ControlHandler;
import redstonedude.plugins.siege.handler.InfiniteInventoryHandler;
import redstonedude.plugins.siege.src.Main;

public class TaskZombieCheck implements Runnable, Cancellable {

	public TaskZombieCheck() {
	}

	public static boolean saved = false;

	public void run() {
		// Main.server.broadcastMessage("checking for zombies");
		if (!ControlHandler.gameEnded) {
			Entity[] entities = Main.server.getWorld("world").getChunkAt(InfiniteInventoryHandler.chest).getEntities();
			int length = entities.length;
			for (int i = 0; i < length; i++) {
				Location l1 = entities[i].getLocation();
				l1.add(0, -1, 0);
				int goldBlocksSize = BlockHandlerListener.goldBlocks.size();
				for (int j = 0; j < goldBlocksSize; j++) {
					if (Main.server.getWorld("world").getBlockAt(l1).getLocation().equals(BlockHandlerListener.goldBlocks.get(j))) {
						if (entities[i] instanceof Zombie || entities[i] instanceof Spider || entities[i] instanceof Skeleton || entities[i] instanceof ZombieMiner) {
							ControlHandler.endGame();
							j = goldBlocksSize;
							i = length;
						}
					}
				}
			}
		} else {
			if (!saved) {
				Main.server.getLogger().info("[SIEGE] SAVING ALL DATA");
				if (DataLoader.saveAllData()) {
					Main.server.getLogger().info("[SIEGE] SAVE DATA SUCCESS");
				} else {
					Main.server.getLogger().severe("[SIEGE] SAVE DATA FAILURE");
					Main.server.getLogger().severe("[SIEGE] SAVE DATA FAILURE");
					Main.server.getLogger().severe("[SIEGE] SAVE DATA FAILURE");
				}
				saved = true;
			}
		}
	}

	public boolean isCancelled() {
		return ControlHandler.gameEnded;
	}

	public void setCancelled(boolean arg0) {

	}

}