package redstonedude.plugins.siege.utils;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import redstonedude.plugins.siege.handler.InventoryHandlerListener;

public enum Items {

	//0 armor
	//1 weapon/tool
	//2 item
	//3 building
	//4 special
	//-1 inventory return
	
	//:COLOR: is replaced with color
	//:N: for lore newline
	
	//add item, add in shop, add in kit
	
	DIAMOND_CHEST("diamond_chest", 0, "Diamond Chestplate", Material.DIAMOND_CHESTPLATE, 0, "Get a free diamond chestplate each game", 5000),
	DIAMOND_LEG("diamond_leg", 0, "Diamond Leggings", Material.DIAMOND_LEGGINGS, 0, "Get a free pair of diamond leggings each game", 4000),
	DIAMOND_HELM("diamond_helm", 0, "Diamond Helmet", Material.DIAMOND_HELMET, 0, "Get a free diamond helmet each game", 3000),
	DIAMOND_BOOT("diamond_boot", 0, "Diamond Boots", Material.DIAMOND_BOOTS, 0, "Get a free pair of diamond boots each game", 3000),
	SWORD_1("sword_1", 1, "Sword upgrade 1", Material.DIAMOND_SWORD, 0, "Upgrade your sword:N:Swordsman -> Diamond:N:Others -> Iron", 8000),
	PICKAXE_1("pickaxe_1", 1, "Pickaxe upgrade 1", Material.DIAMOND_PICKAXE, 0, "Upgrade your pickaxe", 5000),
	BRICKS("bricks", 3, "Bricks", Material.BRICK, 0, "Get free bricks each game:N:Builder -> 2 stacks:N:Others -> 1 stack", 5000),
	WATER("water", 2, "Water bucket", Material.WATER_BUCKET, 0, "Get a free water bucket", 10000),
	GRENADE("frag_grenade", 4, "Frag grenade", Material.SLIME_BALL, 0, "Get free frag grenades each wave", 10000),
	TURRET("turret", 4, "Automatic Turret", Material.SKULL_ITEM, 1, "Get a free turret each game:N::COLOR:Automatic", 15000),
	HEAL("heal", 4, "Full heal", Material.POTION, 8261, "Get a full heal:N:", 1000);

	public String permission;
	public String readableName;
	public Material display;
	public int cost;
	public int shopType;
	public String[] lore;
	public short meta;

	// public Lore lore;

	private Items(String permission, int itemType, String readableName, Material display, int meta, String description, int cost) {
		this.permission = permission;
		this.readableName = readableName;
		this.display = display;
		InventoryHandlerListener.items++;
		this.lore = description.split(":N:");
		this.cost = cost;
		this.shopType = itemType;
		this.meta = (short) meta;
	}
	
	public static String[] getLore(String name) {
		for (Items item : Items.values()) {
			if (item.permission.equals(name)) {
				return item.lore;
			}
		}
		String[] error = {"ERROR","ERROR"};
		return error;
	}

	public static String getName(String name) {
		for (Items item : Items.values()) {
			if (item.permission.equals(name)) {
				return item.readableName;
			}
		}
		return "ERROR";
	}
	
	public static int getCost(String name) {
		for (Items item : Items.values()) {
			if (item.permission.equals(name)) {
				return item.cost;
			}
		}
		return 0;
	}
	
	public static int getShopType(String name) {
		for (Items item : Items.values()) {
			if (item.permission.equals(name)) {
				return item.shopType;
			}
		}
		return -1;
	}
	
	public static ItemStack getDisplay(String name, int balance, Player p) {
		Items targetItem = null;
		for (Items item : Items.values()) {
			if (item.permission.equals(name)) {
				targetItem = item;
			}
		}
		if (targetItem == null) {
			return InventoryHandlerListener.errorItem;
		} else {
			if (p.hasPermission("siege."+name)) {
				ItemStack returnItem = new ItemStack(targetItem.display, 1, targetItem.meta);
				ItemMeta returnItemm = returnItem.getItemMeta();
				returnItemm.setDisplayName(targetItem.readableName);
				ArrayList<String> returnIteml = new ArrayList<String>();
				String[] lore = targetItem.lore;
				for(String s: lore) {
					returnIteml.add(s.replace(":COLOR:", "§a"));
				}
				returnIteml.add("§a[Purchased]");
				returnItemm.setLore(returnIteml);
				returnItem.setItemMeta(returnItemm);
				return returnItem;
			} else {
				if (balance >= targetItem.cost) {
					ItemStack returnItem = new ItemStack(targetItem.display, 1, targetItem.meta);
					ItemMeta returnItemm = returnItem.getItemMeta();
					returnItemm.setDisplayName(targetItem.readableName);
					ArrayList<String> returnIteml = new ArrayList<String>();
					String[] lore = targetItem.lore;
					for(String s: lore) {
						returnIteml.add(s.replace(":COLOR:", "§e"));
					}
					returnIteml.add("§e[Click to purchase]");
					returnIteml.add("§eCost: £" + targetItem.cost);
					returnIteml.add("§eBalance: " + balance);
					returnItemm.setLore(returnIteml);
					returnItem.setItemMeta(returnItemm);
					return returnItem;
				} else {
					ItemStack returnItem = new ItemStack(targetItem.display, 1, targetItem.meta);
					ItemMeta returnItemm = returnItem.getItemMeta();
					returnItemm.setDisplayName(targetItem.readableName);
					ArrayList<String> returnIteml = new ArrayList<String>();
					String[] lore = targetItem.lore;
					for(String s: lore) {
						returnIteml.add(s.replace(":COLOR:", "§c"));
					}
					returnIteml.add("§c[Not enough money]");
					returnIteml.add("§cCost: £" + targetItem.cost);
					returnIteml.add("§cBalance: " + balance);
					returnItemm.setLore(returnIteml);
					returnItem.setItemMeta(returnItemm);
					return returnItem;
				}
			}
		}
	}

}