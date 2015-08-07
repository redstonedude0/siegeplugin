package redstonedude.plugins.siege.customdata;


import java.util.UUID;

import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scoreboard.Scoreboard;

import redstonedude.plugins.siege.kit.Kit;

public class CustomPlayerData {
	
	public UUID u;
	public CustomInfiniteInventory inventory;
	//public Inventory i;
	public Kit kit;
	public Integer points;
	//public ArrayList<Boolean> unlocks;
	public Inventory shopOpen;
	public int shopType;
	public String purchasing;
	public PermissionAttachment attachment;
	public Scoreboard board;
	
}
