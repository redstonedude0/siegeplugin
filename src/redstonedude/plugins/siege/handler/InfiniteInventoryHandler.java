package redstonedude.plugins.siege.handler;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import redstonedude.plugins.siege.customdata.CustomInfiniteInventory;
import redstonedude.plugins.siege.customdata.CustomPlayerData;
import redstonedude.plugins.siege.src.Main;
import redstonedude.plugins.siege.utils.MathHelper;

public class InfiniteInventoryHandler {

	public static Location chest;
	static Random rand = new Random();
	static ItemStack xpBottles;

	// public static ArrayList<CustomPlayerData> invs = new
	// ArrayList<CustomPlayerData>();

	public static void handleInventories() {
		if (Main.wave == 0) {
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
			int max = Main.playerCount;
			for (int i = 0; i < max; i++) {
				CustomPlayerData cpd = PlayerEntityHandlerListener.customPlayerData.get(i);
				UUID u = cpd.u;
				Inventory inv = Main.server.createInventory(null, 54, Main.server.getPlayer(u).getName() + "'s Chest (Page 1)");
				inv.setItem(49, istack);
				inv.setItem(53, shop_next);
				inv.setItem(45, shop_prev);
				CustomInfiniteInventory newInv =  new CustomInfiniteInventory();
				newInv.inventories.add(inv);
				ArrayList<ItemStack> items2 = PurchasesHandler.generateInitialItems(Main.server.getPlayer(cpd.u));
				int itemMax2 = items2.size();
				for (int j = 0; j < itemMax2; j++) {
					// Main.server.broadcastMessage("loading kitj" + j);
					newInv.addItem(items2.get(j),cpd);
				}
				cpd.inventory = newInv;
				// Main.server.broadcastMessage("setting " + u);
			}
		} else {
			Location l = chest.clone();
			l.add(0.5, 0, 0.5);
			Firework fw = (Firework) Main.server.getWorld("world").spawnEntity(l, EntityType.FIREWORK);
			FireworkMeta fwm = fw.getFireworkMeta();
			Type type = Type.BURST;
			org.bukkit.Color c1 = Color.GREEN;
			Color c2 = Color.BLUE;
			FireworkEffect fwe = FireworkEffect.builder().flicker(false).withColor(c1).with(type).withFade(c2).trail(true).build();
			fwm.addEffect(fwe);
			fwm.setPower(1);
			fw.setFireworkMeta(fwm);
		}
		// Main.server.broadcastMessage("max" + max);
		// Main.server.broadcastMessage("invs" + invs.toString());
		//for (int i = 0; i < max; i++) {
		xpBottles = new ItemStack(Material.EXP_BOTTLE,MathHelper.cap(2+rand.nextInt(Main.wave >> 1),32));
		for (CustomPlayerData cpd : PlayerEntityHandlerListener.customPlayerData) {
			//CustomPlayerData cpd = PlayerEntityHandlerListener.customPlayerData.get(i);
			CustomInfiniteInventory in = cpd.inventory;
			ArrayList<ItemStack> items = cpd.kit.generateItems(Main.server.getPlayer(cpd.u));
			int itemMax = items.size();
			for (int j = 0; j < itemMax; j++) {
				// Main.server.broadcastMessage("loading kitj" + j);
				in.addItem(items.get(j),cpd);
			}
			in.addItem(xpBottles, cpd);
			
			// in.addItem(invs.get(i).kit.)
		}
	}
	
	public static CustomInfiniteInventory getInventory(UUID u) {
		int j = PlayerEntityHandlerListener.customPlayerData.size();
		for (int i = 0; i < j; i++) {
			if (PlayerEntityHandlerListener.customPlayerData.get(i).u.equals(u)) {
				CustomInfiniteInventory in = PlayerEntityHandlerListener.customPlayerData.get(i).inventory;
				// Main.server.broadcastMessage("setting j" + ji.toString());
				return in;
			}
		}
		// Main.server.broadcastMessage("null inv");
		return null;
	}

}
