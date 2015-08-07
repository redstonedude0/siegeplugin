package redstonedude.plugins.siege.handler;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import redstonedude.plugins.siege.customdata.CustomPlayerData;

public class PurchasesHandler {

	static ItemStack diamondHelm;
	static ItemStack diamondChest;
	static ItemStack diamondLeg;
	static ItemStack diamondBoot;
	static ItemStack sword1a;
	static ItemStack sword1b;
	static ItemStack pickaxe1;
	static ItemStack bricks;
	static ItemStack water;
	static ItemStack grenade5;
	static ItemStack turret;

	public static void init() {
		diamondHelm = new ItemStack(Material.DIAMOND_HELMET, 1, (short) 0);
		ItemMeta im1 = diamondHelm.getItemMeta();
		im1.setDisplayName("§bDiamond Helmet");
		im1.setLore(Arrays.asList("§5Purchased for £3,000", "§cArmor"));
		diamondHelm.setItemMeta(im1);
		//
		diamondChest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1, (short) 0);
		ItemMeta im2 = diamondChest.getItemMeta();
		im2.setDisplayName("§bDiamond Chestplate");
		im2.setLore(Arrays.asList("§5Purchased for £5,000", "§cArmor"));
		diamondChest.setItemMeta(im2);
		//
		diamondLeg = new ItemStack(Material.DIAMOND_LEGGINGS, 1, (short) 0);
		ItemMeta im3 = diamondLeg.getItemMeta();
		im3.setDisplayName("§bDiamond Leggings");
		im3.setLore(Arrays.asList("§5Purchased for £4,000", "§cArmor"));
		diamondLeg.setItemMeta(im3);
		//
		diamondBoot = new ItemStack(Material.DIAMOND_BOOTS, 1, (short) 0);
		ItemMeta im4 = diamondBoot.getItemMeta();
		im4.setDisplayName("§bDiamond Boots");
		im4.setLore(Arrays.asList("§5Purchased for £3,000", "§cArmor"));
		diamondBoot.setItemMeta(im4);
		//
		sword1a = new ItemStack(Material.DIAMOND_SWORD, 1, (short) 0);
		ItemMeta im5a = sword1a.getItemMeta();
		im5a.setDisplayName("§bDiamond Sword");
		im5a.setLore(Arrays.asList("§5Upgraded for £8,000", "§cWeapon"));
		sword1a.setItemMeta(im5a);
		sword1b = new ItemStack(Material.IRON_SWORD, 1, (short) 0);
		ItemMeta im5b = sword1b.getItemMeta();
		im5b.setDisplayName("§bDiamond Sword");
		im5b.setLore(Arrays.asList("§5Upgraded for £8,000", "§cWeapon"));
		sword1b.setItemMeta(im5b);
		
		//
		pickaxe1 = new ItemStack(Material.DIAMOND_PICKAXE, 1, (short) 0);
		ItemMeta im6 = pickaxe1.getItemMeta();
		im6.setDisplayName("§bDiamond Pickaxe");
		im6.setLore(Arrays.asList("§5Purchased for £5,000", "§cTool"));
		pickaxe1.setItemMeta(im6);
		//
		water = new ItemStack(Material.WATER_BUCKET, 1, (short) 0);
		ItemMeta im7 = water.getItemMeta();
		im7.setDisplayName("§bWater Bucket");
		im7.setLore(Arrays.asList("§5Purchased for £10,000", "§cItem"));
		water.setItemMeta(im7);
		//
		bricks = new ItemStack(Material.BRICK, 64, (short) 0);
		ItemMeta im8 = bricks.getItemMeta();
		im8.setDisplayName("§bBricks");
		im8.setLore(Arrays.asList("§5Purchased for £5,000", "§cBuilding Material"));
		bricks.setItemMeta(im8);
		//
		grenade5 = new ItemStack(Material.SLIME_BALL, 5, (short) 0);
		ItemMeta im9 = grenade5.getItemMeta();
		im9.setDisplayName("§bFrag Grenade");
		im9.setLore(Arrays.asList("§5Purchased for £10,000", "§cSpecial"));
		grenade5.setItemMeta(im9);
		//
		turret = new ItemStack(Material.SKULL_ITEM, 1, (short) 1);
		ItemMeta im10 = turret.getItemMeta();
		im10.setDisplayName("§bTurret");
		im10.setLore(Arrays.asList("§5Purchased for £10,000", "§cSpecial", "§cAutomatic"));
		turret.setItemMeta(im10);
		//
	}

	public static ItemStack getItem(String s) {
		switch (s) {
		case "diamond_helm":
			return diamondHelm;
		case "diamond_chest":
			return diamondChest;
		case "diamond_leg":
			return diamondLeg;
		case "diamond_boot":
			return diamondBoot;
		case "sword_1a":
			return sword1a;
		case "sword_1b":
			return sword1b;
		case "pickaxe_1":
			return pickaxe1;
		case "bricks":
			return bricks;
		case "water":
			return water;
		case "frag_grenade":
			return grenade5;
		case "turret":
			return turret;
		}
		return InventoryHandlerListener.errorItem;
	}

	public static ArrayList<ItemStack> generateInitialItems(Player p) {
		ArrayList<ItemStack> returning = new ArrayList<ItemStack>();
		if (p.hasPermission("siege.diamond_helm")) {
			returning.add(diamondHelm);
		}
		if (p.hasPermission("siege.diamond_chest")) {
			returning.add(diamondChest);
		}
		if (p.hasPermission("siege.diamond_leg")) {
			returning.add(diamondLeg);
		}
		if (p.hasPermission("siege.diamond_boot")) {
			returning.add(diamondBoot);
		}
		if (p.hasPermission("siege.water")) {
			returning.add(water);
		}
		if (p.hasPermission("siege.frag_grenade")) {
			returning.add(grenade5);
		}
		if (p.hasPermission("siege.turret")) {
			returning.add(turret);
		}
		return returning;
	}
	
	public static void purchase(Player p, CustomPlayerData cpd) {
		String s = cpd.purchasing;
		if (s.equals("heal")) {
			cpd.attachment.setPermission("siege.heal", false);
			p.setHealth(20);
		} else {
			cpd.inventory.addItem(getItem(s), cpd);
		}
	}

}
