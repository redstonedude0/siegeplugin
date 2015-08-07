package redstonedude.plugins.siege.kit;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import redstonedude.plugins.siege.handler.PurchasesHandler;
import redstonedude.plugins.siege.src.Main;
import redstonedude.plugins.siege.utils.MathHelper;

public class KitSwordsman extends Kit {
	
	
	Random rand = new Random();
	
	public ItemStack genStack(Material m, int i) {
		if (i == 0) return null;
		return new ItemStack(m,i,(short)0);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ArrayList<ItemStack> generateItems(Player p) {
		ArrayList<ItemStack> returning = new ArrayList<ItemStack>();
		if (Main.wave == 0) {
			if (p.hasPermission("siege.sword_1")) {
				returning.add(PurchasesHandler.getItem("sword_1a"));
			} else {
				returning.add(new ItemStack(Material.STONE_SWORD, 1, (short) 0));
			}
			if (p.hasPermission("siege.pickaxe_1")) {
				returning.add(PurchasesHandler.getItem("pickaxe_1"));
			} else {
				returning.add(new ItemStack(Material.STONE_PICKAXE, 1, (short) 0));
			}
			returning.add(new ItemStack(Material.CAKE, 1, (short) 0));
			returning.add(new ItemStack(Material.COBBLESTONE, 48, (short) 0));
			returning.add(new ItemStack(Material.WOOD_DOOR, 1, (short) 0));
			returning.add(new ItemStack(Material.LOG, 8, (short) 0));
			returning.add(new ItemStack(Material.COOKED_BEEF, 8, (short) 0));
			returning.add(new ItemStack(Material.LEATHER_CHESTPLATE, 1, (short) 0));
			returning.add(new ItemStack(Material.LEATHER_LEGGINGS, 1, (short) 0));
			returning.add(new ItemStack(Material.WOOD_SPADE, 1, (short) 0));
			returning.add(new ItemStack(Material.WOOD_AXE, 1, (short) 0));
			returning.add(new ItemStack(Material.TORCH, 1, (short) 0));
			Potion pot = new Potion(PotionType.STRENGTH,1,true,false);
			returning.add(pot.toItemStack(1));
		} else {
			ItemStack i = genStack(Material.COOKED_BEEF, MathHelper.cap(Main.wave>>2,3) + rand.nextInt(2));
			if (i != null) returning.add(i);
			i = genStack(Material.IRON_INGOT, rand.nextInt(3));
			if (i != null) returning.add(i);
			i = genStack(Material.LEATHER, MathHelper.mincap((6-Main.wave),1) + rand.nextInt(4));
			if (i != null) returning.add(i);
			i = genStack(Material.COBBLESTONE, 24 + rand.nextInt(16));
			if (i != null) returning.add(i);
			i = genStack(Material.IRON_INGOT, MathHelper.cap(0.2*Main.wave,2) + rand.nextInt(2));
			if (i != null) returning.add(i);
			i = genStack(Material.STONE_SWORD, rand.nextInt(4) == 2? 1 : 0);
			if (i != null) returning.add(i);
			if (p.hasPermission("siege.frag_grenade")) {
				i = PurchasesHandler.getItem("frag_grenade");
				int j = 0;
				j = rand.nextInt(2);
				if (j != 0) {
					i.setAmount(j);
					returning.add(i);
				}
			}
		}
		return returning;
	}

}
