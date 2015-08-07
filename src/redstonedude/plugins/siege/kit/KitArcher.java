package redstonedude.plugins.siege.kit;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import redstonedude.plugins.siege.handler.PurchasesHandler;
import redstonedude.plugins.siege.src.Main;
import redstonedude.plugins.siege.utils.MathHelper;

public class KitArcher extends Kit {

	Random rand = new Random();

	public ItemStack genStack(Material m, int i) {
		if (i == 0) {
			return null;
		}
		return new ItemStack(m, i, (short) 0);
	}

	@Override
	public ArrayList<ItemStack> generateItems(Player p) {
		ArrayList<ItemStack> returning = new ArrayList<ItemStack>();
		if (Main.wave == 0) {
			if (p.hasPermission("siege.sword_1")) {
				returning.add(PurchasesHandler.getItem("sword_1b"));
			} else {
				returning.add(new ItemStack(Material.WOOD_SWORD, 1, (short) 0));
			}
			if (p.hasPermission("siege.pickaxe_1")) {
				returning.add(PurchasesHandler.getItem("pickaxe_1"));
			} else {
				returning.add(new ItemStack(Material.STONE_PICKAXE, 1, (short) 0));
			}
			if (p.hasPermission("siege.bricks")) {
				returning.add(PurchasesHandler.getItem("bricks"));
			}
			returning.add(new ItemStack(Material.CAKE, 1, (short) 0));
			returning.add(new ItemStack(Material.COBBLESTONE, 64, (short) 0));
			returning.add(new ItemStack(Material.COBBLESTONE, 32, (short) 0));
			returning.add(new ItemStack(Material.LADDER, 16, (short) 0));
			returning.add(new ItemStack(Material.LOG, 8, (short) 0));
			returning.add(new ItemStack(Material.COOKED_BEEF, 12, (short) 0));
			returning.add(new ItemStack(Material.BOW, 1, (short) 0));
			returning.add(new ItemStack(Material.ARROW, 24, (short) 0));
			returning.add(new ItemStack(Material.WOOD_SPADE, 1, (short) 0));
			returning.add(new ItemStack(Material.WOOD_AXE, 1, (short) 0));
			returning.add(new ItemStack(Material.TORCH, 1, (short) 0));
			returning.add(new ItemStack(Material.CHEST, 1, (short) 0));

		} else {
			ItemStack i = genStack(Material.ARROW, MathHelper.cap(Main.wave >> 1, 16) + rand.nextInt(8));
			if (i != null) {
				returning.add(i);
			}
			i = genStack(Material.IRON_INGOT, rand.nextInt(1));
			if (i != null) {
				returning.add(i);
			}
			i = genStack(Material.COOKED_BEEF, rand.nextInt(3));
			if (i != null) {
				returning.add(i);
			}
			i = genStack(Material.COBBLESTONE, 16 + rand.nextInt(24));
			if (i != null) {
				returning.add(i);
			}
			i = genStack(Material.LADDER, 2 + rand.nextInt(4));
			if (i != null) {
				returning.add(i);
			}
			i = genStack(Material.BOW, rand.nextInt(5) == 2 ? 1 : 0);
			if (i != null) {
				returning.add(i);
			}
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
