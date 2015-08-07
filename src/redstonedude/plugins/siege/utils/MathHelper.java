package redstonedude.plugins.siege.utils;

import org.bukkit.Location;

import redstonedude.plugins.siege.handler.BlockHandlerListener;
import redstonedude.plugins.siege.handler.InfiniteInventoryHandler;
import redstonedude.plugins.siege.handler.SpawningHandler;

public class MathHelper {

	public static int cap(double in, int cap) {
		if (in > cap) {
			return cap;
		} else {
			return (int) in;
		}
	}

	public static int mincap(double in, int cap) {
		if (in < cap) {
			return cap;
		} else {
			return (int) in;
		}
	}

	public static boolean sphereIllegal(Location loc, float radius) {
		int sqrRadius = (int) (radius*radius);
		int spawns = SpawningHandler.spawnLocations.size();
		for (int i = 0; i < spawns; i++) {
			int xDiff = (int) (loc.getX()-SpawningHandler.spawnLocations.get(i).getX());
			int yDiff = (int) (loc.getY()-SpawningHandler.spawnLocations.get(i).getY());
			int zDiff = (int) (loc.getZ()-SpawningHandler.spawnLocations.get(i).getZ());
			int sqrDist = xDiff*xDiff+yDiff*yDiff+zDiff*zDiff;
			if (sqrDist <= sqrRadius) {
				return true;
			}
		}
		int gBlocks = BlockHandlerListener.goldBlocks.size();
		for (int i = 0; i < gBlocks; i++) {
			int xDiff = (int) (loc.getX()-BlockHandlerListener.goldBlocks.get(i).getX());
			int yDiff = (int) (loc.getY()-BlockHandlerListener.goldBlocks.get(i).getY());
			int zDiff = (int) (loc.getZ()-BlockHandlerListener.goldBlocks.get(i).getZ());
			int sqrDist = xDiff*xDiff+yDiff*yDiff+zDiff*zDiff;
			if (sqrDist <= sqrRadius) {
				return true;
			}
		}
		int xDiff = (int) (loc.getX()-InfiniteInventoryHandler.chest.getX());
		int yDiff = (int) (loc.getY()-InfiniteInventoryHandler.chest.getY());
		int zDiff = (int) (loc.getZ()-InfiniteInventoryHandler.chest.getZ());
		int sqrDist = xDiff*xDiff+yDiff*yDiff+zDiff*zDiff;
		if (sqrDist <= sqrRadius) {
			return true;
		}
		
		
		return false;
	}

}
