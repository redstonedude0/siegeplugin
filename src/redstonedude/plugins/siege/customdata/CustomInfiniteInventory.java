package redstonedude.plugins.siege.customdata;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import redstonedude.plugins.siege.src.Main;

public class CustomInfiniteInventory {
	
	public ArrayList<Inventory> inventories = new ArrayList<Inventory>();
	
	public boolean isIncluded(Inventory i) {
		int invLength = inventories.size();
		for (int j = 0; j < invLength; j++) {
			if (inventories.get(j).equals(i)) {
				return true;
			}
		}
		return false;
	}
	
	public Inventory getInventory(int page) {
		return inventories.get(page);
	}
	
	public Inventory getInventory(Inventory current, boolean next) {
		int inventoryNumber = -1;
		int invLength = inventories.size();
		for (int j = 0; j < invLength; j++) {
			if (inventories.get(j).equals(current)) {
				inventoryNumber = j;
			}
		}
		
		if (inventoryNumber == -1) {
			return null;
		}
		if (inventoryNumber == 0 && !next) {
			return null;
		}
		if (inventoryNumber == (invLength-1) && next) {
			return null;
		}
		return inventories.get(inventoryNumber + (next?1:-1));
	}
	
	public void addItem(ItemStack item, CustomPlayerData cpd) {
		int invLength = inventories.size();
		for (int j = 0; j < invLength; j++) {
			if (inventories.get(j).firstEmpty() != -1) {
				inventories.get(j).addItem(item);
				return;
			}
		}
		ItemStack istack = new ItemStack(Material.GOLD_BLOCK, 1, (short) 0);
		ItemMeta im = istack.getItemMeta();
		im.setDisplayName("Shop");
		ArrayList<String> lores = new ArrayList<String>();
		lores.add("§cClick to go to shop");
		im.setLore(lores);
		istack.setItemMeta(im);
		ItemStack shop_next = new ItemStack(Material.WOOL, 1, (short) 5);
		ItemMeta shop_nextm = shop_next.getItemMeta();
		shop_nextm.setDisplayName("Next");
		ArrayList<String> shop_nextl = new ArrayList<String>();
		shop_nextl.add("§cNext Page");
		shop_nextm.setLore(shop_nextl);
		shop_next.setItemMeta(shop_nextm);
		ItemStack shop_prev = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemMeta shop_prevm = shop_prev.getItemMeta();
		shop_prevm.setDisplayName("Previous");
		ArrayList<String> shop_prevl = new ArrayList<String>();
		shop_prevl.add("§cPrevious Page");
		shop_prevm.setLore(shop_prevl);
		shop_prev.setItemMeta(shop_prevm);
		UUID u = cpd.u;
		Inventory inv = Main.server.createInventory(null, 54, Main.server.getPlayer(u).getName() + "'s Chest (Page " + (invLength+1) + ")");
		inv.setItem(49, istack);
		inv.setItem(53, shop_next);
		inv.setItem(45, shop_prev);
		inventories.add(inv);
	}
	
}
