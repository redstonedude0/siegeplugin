package redstonedude.plugins.siege.handler;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import redstonedude.plugins.siege.customdata.CustomInfiniteInventory;
import redstonedude.plugins.siege.customdata.CustomPlayerData;
import redstonedude.plugins.siege.src.Main;
import redstonedude.plugins.siege.utils.ChatHelper;
import redstonedude.plugins.siege.utils.Items;

public class InventoryHandlerListener implements Listener {

	public static Inventory shop1;
	public static ItemStack shop_back;
	public static ItemStack shop_yes;
	public static ItemStack shop_no;
	public static int items = 0;
	public static ItemStack errorItem;
	public static ArrayList<Integer> yesButtonLocations = new ArrayList<Integer>();

	public static void init() {
		yesButtonLocations.add(33);
		yesButtonLocations.add(34);
		yesButtonLocations.add(35);
		yesButtonLocations.add(42);
		yesButtonLocations.add(43);
		yesButtonLocations.add(44);
		yesButtonLocations.add(51);
		yesButtonLocations.add(52);
		yesButtonLocations.add(53);
		shop_yes = new ItemStack(Material.WOOL, 1, (short) 5);
		ItemMeta shop_yesm = shop_yes.getItemMeta();
		shop_yesm.setDisplayName("§aConfirm Purchase");
		ArrayList<String> shop_yesl = new ArrayList<String>();
		shop_yesl.add("§aConfirm Purchase");
		shop_yesm.setLore(shop_yesl);
		shop_yes.setItemMeta(shop_yesm);
		shop_no = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemMeta shop_nom = shop_no.getItemMeta();
		shop_nom.setDisplayName("§cCancel Purchase");
		ArrayList<String> shop_nol = new ArrayList<String>();
		shop_nol.add("§cCancel Purchase");
		shop_nom.setLore(shop_nol);
		shop_no.setItemMeta(shop_nom);
		errorItem = new ItemStack(Material.DIRT, 1, (short) 0);
		ItemMeta errorItemm = errorItem.getItemMeta();
		errorItemm.setDisplayName("ERROR");
		ArrayList<String> errorIteml = new ArrayList<String>();
		errorIteml.add("§cAn error has occured");
		errorItemm.setLore(errorIteml);
		errorItem.setItemMeta(errorItemm);
		shop_back = new ItemStack(Material.BARRIER, 1, (short) 0);
		ItemMeta shop_backm = shop_back.getItemMeta();
		shop_backm.setDisplayName("Back");
		ArrayList<String> shop_backl = new ArrayList<String>();
		shop_backl.add("§cClick to go back");
		shop_backm.setLore(shop_backl);
		shop_back.setItemMeta(shop_backm);
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

		ItemStack shop_i2 = new ItemStack(Material.DIAMOND_CHESTPLATE, 1, (short) 0);
		ItemMeta shop_im2 = shop_i2.getItemMeta();
		shop_im2.setDisplayName("Armor");
		ArrayList<String> shop_l2 = new ArrayList<String>();
		shop_l2.add("§cClick to shop for armor");
		shop_im2.setLore(shop_l2);
		shop_i2.setItemMeta(shop_im2);
		ItemStack shop_i3 = new ItemStack(Material.DIAMOND_SWORD, 1, (short) 0);
		ItemMeta shop_im3 = shop_i3.getItemMeta();
		shop_im3.setDisplayName("Weapons and Tools");
		ArrayList<String> shop_l3 = new ArrayList<String>();
		shop_l3.add("§cClick to shop for weapons or tools");
		shop_im3.setLore(shop_l3);
		shop_i3.setItemMeta(shop_im3);
		ItemStack shop_i4 = new ItemStack(Material.ARROW, 1, (short) 0);
		ItemMeta shop_im4 = shop_i4.getItemMeta();
		shop_im4.setDisplayName("Items");
		ArrayList<String> shop_l4 = new ArrayList<String>();
		shop_l4.add("§cClick to shop for items");
		shop_im4.setLore(shop_l4);
		shop_i4.setItemMeta(shop_im4);
		ItemStack shop_i5 = new ItemStack(Material.BRICK, 1, (short) 0);
		ItemMeta shop_im5 = shop_i5.getItemMeta();
		shop_im5.setDisplayName("Building materials");
		ArrayList<String> shop_l5 = new ArrayList<String>();
		shop_l5.add("§cClick to shop for building materials");
		shop_im5.setLore(shop_l5);
		shop_i5.setItemMeta(shop_im5);
		ItemStack shop_i6 = new ItemStack(Material.SLIME_BALL, 1, (short) 0);
		ItemMeta shop_im6 = shop_i6.getItemMeta();
		shop_im6.setDisplayName("Special tools");
		ArrayList<String> shop_l6 = new ArrayList<String>();
		shop_l6.add("§cClick to shop for special tools");
		shop_im6.setLore(shop_l6);
		shop_i6.setItemMeta(shop_im6);

		shop1 = Main.server.createInventory(null, 54, "Shop Menu");

		shop1.setItem(49, shop_back);

		shop1.setItem(0, shop_i2);
		shop1.setItem(1, shop_i3);
		shop1.setItem(2, shop_i4);
		shop1.setItem(3, shop_i5);
		shop1.setItem(4, shop_i6);

	}

	public static Inventory generateArmorShop(Player p, int page) {
		Inventory shop = Main.server.createInventory(null, 54, "Armor Shop");
		shop.setItem(49, shop_back);
		CustomPlayerData cpd = PlayerEntityHandlerListener.getPlayerData(p.getUniqueId());
		int points = cpd.points;
		shop.setItem(0, Items.getDisplay("diamond_helm", points, p));
		shop.setItem(9, Items.getDisplay("diamond_chest", points, p));
		shop.setItem(18, Items.getDisplay("diamond_leg", points, p));
		shop.setItem(27, Items.getDisplay("diamond_boot", points, p));
		return shop;
	}

	public static Inventory generateWeaponShop(Player p, int page) {
		Inventory shop = Main.server.createInventory(null, 54, "Weapon and Tool Shop");
		shop.setItem(49, shop_back);
		CustomPlayerData cpd = PlayerEntityHandlerListener.getPlayerData(p.getUniqueId());
		int points = cpd.points;
		shop.setItem(0, Items.getDisplay("sword_1", points, p));
		shop.setItem(1, Items.getDisplay("pickaxe_1", points, p));
		return shop;
	}

	public static Inventory generateItemShop(Player p, int page) {
		Inventory shop = Main.server.createInventory(null, 54, "Item Shop");
		shop.setItem(49, shop_back);
		CustomPlayerData cpd = PlayerEntityHandlerListener.getPlayerData(p.getUniqueId());
		int points = cpd.points;
		shop.setItem(0, Items.getDisplay("water", points, p));
		return shop;
	}

	public static Inventory generateBuildingShop(Player p, int page) {
		Inventory shop = Main.server.createInventory(null, 54, "Building Supplies Shop");
		shop.setItem(49, shop_back);
		CustomPlayerData cpd = PlayerEntityHandlerListener.getPlayerData(p.getUniqueId());
		int points = cpd.points;
		shop.setItem(0, Items.getDisplay("bricks", points, p));
		return shop;
	}

	public static Inventory generateSpecialShop(Player p, int page) {
		Inventory shop = Main.server.createInventory(null, 54, "Special Shop");
		shop.setItem(49, shop_back);
		CustomPlayerData cpd = PlayerEntityHandlerListener.getPlayerData(p.getUniqueId());
		int points = cpd.points;
		shop.setItem(0, Items.getDisplay("frag_grenade", points, p));
		shop.setItem(1, Items.getDisplay("turret", points, p));
		shop.setItem(8, Items.getDisplay("heal", points, p));
		return shop;
	}

	public static Inventory generatePurchase(Player p, CustomPlayerData cpd) {
		String purchasing = cpd.purchasing;
		// ChatHelper.broadcast("purchasing " + purchasing);
		if (p.hasPermission("siege." + purchasing)) {
			// ChatHelper.broadcast("no perms");
			return null;
		} else if (cpd.points < Items.getCost(purchasing)) {
			return null;
		} else {
			// ChatHelper.broadcast("yes perms");
			Inventory in = Main.server.createInventory(null, 54, "Purchase " + Items.getName(purchasing));
			in.setItem(13, Items.getDisplay(purchasing, cpd.points, p));
			in.setItem(27, shop_no);
			in.setItem(28, shop_no);
			in.setItem(29, shop_no);
			in.setItem(36, shop_no);
			in.setItem(37, shop_no);
			in.setItem(38, shop_no);
			in.setItem(45, shop_no);
			in.setItem(46, shop_no);
			in.setItem(47, shop_no);
			in.setItem(33, shop_yes);
			in.setItem(34, shop_yes);
			in.setItem(35, shop_yes);
			in.setItem(42, shop_yes);
			in.setItem(43, shop_yes);
			in.setItem(44, shop_yes);
			in.setItem(51, shop_yes);
			in.setItem(52, shop_yes);
			in.setItem(53, shop_yes);
			return in;
		}
	}

	@EventHandler
	public static void onInventoryClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		int dataLen = PlayerEntityHandlerListener.customPlayerData.size();
		CustomInfiniteInventory i = null;
		CustomPlayerData cpd = null;
		for (int j = 0; j < dataLen; j++) {
			if (PlayerEntityHandlerListener.customPlayerData.get(j).u.equals(player.getUniqueId())) {
				cpd = PlayerEntityHandlerListener.customPlayerData.get(j);
			}
		}
		Inventory i2 = e.getInventory();
		if (cpd.inventory != null) {
			i = cpd.inventory;
			if (i.isIncluded(i2)) {
				// e.setCancelled(true);
				// player.updateInventory();
				switch (e.getSlot()) {
				case 49:
					e.setCancelled(true);
					player.updateInventory();
					player.openInventory(shop1);
					break;
				case 53:
					e.setCancelled(true);
					player.updateInventory();
					Inventory i3 = i.getInventory(i2, true);
					if (i3 != null) {
						player.openInventory(i.getInventory(i2, true));
					}
					break;
				case 45:
					e.setCancelled(true);
					player.updateInventory();
					Inventory i3b = i.getInventory(i2, false);
					if (i3b != null) {
						player.openInventory(i.getInventory(i2, false));
					}
					break;
				// ChatHelper.broadcast("shop open");
				}
			} else if (e.getInventory().equals(shop1)) {
				e.setCancelled(true);
				player.updateInventory();
				switch (e.getSlot()) {
				case 0:
					Inventory in1 = generateArmorShop(player, 0);
					player.openInventory(in1);
					cpd.shopOpen = in1;
					cpd.shopType = 1;
					break;
				case 1:
					Inventory in2 = generateWeaponShop(player, 0);
					player.openInventory(in2);
					cpd.shopOpen = in2;
					cpd.shopType = 2;
					break;
				case 2:
					Inventory in3 = generateItemShop(player, 0);
					player.openInventory(in3);
					cpd.shopOpen = in3;
					cpd.shopType = 3;
					break;
				case 3:
					Inventory in4 = generateBuildingShop(player, 0);
					player.openInventory(in4);
					cpd.shopOpen = in4;
					cpd.shopType = 4;
					break;
				case 4:
					Inventory in5 = generateSpecialShop(player, 0);
					player.openInventory(in5);
					cpd.shopOpen = in5;
					cpd.shopType = 5;
					break;
				case 5:
					player.closeInventory();
					//sell flesh
					ItemStack[] items = player.getInventory().getContents();
					int flesh = 0;
					int itemLen = items.length;
					for (int j = 0; j < itemLen; j++) {
						if (items[j].getType() == Material.ROTTEN_FLESH) {
							flesh += items[j].getAmount();
						}
					}
					cpd.points += (flesh << 3) ;
					break;
				case 49:
					player.openInventory(i.getInventory(0));
					cpd.shopType = 0;
					break;
				}
			}
		}
		Inventory shopOpen = cpd.shopOpen;
		if (shopOpen != null) {
			if (e.getInventory().equals(shopOpen)) {
				if (cpd.shopType == 1) {
					e.setCancelled(true);
					player.updateInventory();
					switch (e.getSlot()) {
					case 0:
						// ChatHelper.broadcast("generating purchase");
						cpd.purchasing = "diamond_helm";
						Inventory in1 = generatePurchase(player, cpd);
						if (in1 != null) {
							// ChatHelper.broadcast("not null");
							player.openInventory(in1);
							cpd.shopOpen = in1;
							cpd.shopType = -1;// purchase
						} // else item already purchased
						break;
					case 9:
						// ChatHelper.broadcast("generating purchase");
						cpd.purchasing = "diamond_chest";
						Inventory in2 = generatePurchase(player, cpd);
						if (in2 != null) {
							// ChatHelper.broadcast("not null");
							player.openInventory(in2);
							cpd.shopOpen = in2;
							cpd.shopType = -1;// purchase
						} // else item already purchased
						break;
					case 18:
						// ChatHelper.broadcast("generating purchase");
						cpd.purchasing = "diamond_leg";
						Inventory in3 = generatePurchase(player, cpd);
						if (in3 != null) {
							// ChatHelper.broadcast("not null");
							player.openInventory(in3);
							cpd.shopOpen = in3;
							cpd.shopType = -1;// purchase
						} // else item already purchased or not enough money
						break;
					case 27:
						// ChatHelper.broadcast("generating purchase");
						cpd.purchasing = "diamond_boot";
						Inventory in4 = generatePurchase(player, cpd);
						if (in4 != null) {
							// ChatHelper.broadcast("not null");
							player.openInventory(in4);
							cpd.shopOpen = in4;
							cpd.shopType = -1;// purchase
						} // else item already purchased
						break;
					case 49:
						player.openInventory(shop1);
						break;
					}
				} else if (cpd.shopType == -1) {
					e.setCancelled(true);
					player.updateInventory();
					if (yesButtonLocations.contains(e.getSlot())) {
						if (!player.hasPermission("siege." + cpd.purchasing)) {
							cpd.attachment.setPermission("siege." + cpd.purchasing, true);
							cpd.points -= Items.getCost(cpd.purchasing);
							player.closeInventory();
							ChatHelper.sendMessage(player, "You purchased " + Items.getName(cpd.purchasing));
							PurchasesHandler.purchase(player, cpd);
							// player.playSound(l, Sound.FIREWORK_TWINKLE, arg2,
							// arg3)
							cpd.purchasing = "";
						}
					} else {
						switch (Items.getShopType(cpd.purchasing)) {
						case 0:
							Inventory in1 = generateArmorShop(player, 0);
							player.openInventory(in1);
							cpd.shopOpen = in1;
							cpd.shopType = 1;
							break;
						case 1:
							Inventory in2 = generateWeaponShop(player, 0);
							player.openInventory(in2);
							cpd.shopOpen = in2;
							cpd.shopType = 2;
							break;
						case 2:
							Inventory in3 = generateItemShop(player, 0);
							player.openInventory(in3);
							cpd.shopOpen = in3;
							cpd.shopType = 3;
							break;
						case 3:
							Inventory in4 = generateBuildingShop(player, 0);
							player.openInventory(in4);
							cpd.shopOpen = in4;
							cpd.shopType = 4;
							break;
						case 4:
							Inventory in5 = generateSpecialShop(player, 0);
							player.openInventory(in5);
							cpd.shopOpen = in5;
							cpd.shopType = 5;
							break;
						default:
							player.openInventory(shop1);
							cpd.shopType = 0;
							break;

						}
					}
				} else if (cpd.shopType == 2) {
					e.setCancelled(true);
					player.updateInventory();
					switch (e.getSlot()) {
					case 0:
						// ChatHelper.broadcast("generating purchase");
						cpd.purchasing = "sword_1";
						Inventory in1 = generatePurchase(player, cpd);
						if (in1 != null) {
							// ChatHelper.broadcast("not null");
							player.openInventory(in1);
							cpd.shopOpen = in1;
							cpd.shopType = -1;// purchase
						} // else item already purchased
						break;
					case 1:
						// ChatHelper.broadcast("generating purchase");
						cpd.purchasing = "pickaxe_1";
						Inventory in2 = generatePurchase(player, cpd);
						if (in2 != null) {
							// ChatHelper.broadcast("not null");
							player.openInventory(in2);
							cpd.shopOpen = in2;
							cpd.shopType = -1;// purchase
						} // else item already purchased
						break;
					case 49:
						player.openInventory(shop1);
						break;
					}
				} else if (cpd.shopType == 3) {
					e.setCancelled(true);
					player.updateInventory();
					switch (e.getSlot()) {
					case 0:
						// ChatHelper.broadcast("generating purchase");
						cpd.purchasing = "water";
						Inventory in1 = generatePurchase(player, cpd);
						if (in1 != null) {
							// ChatHelper.broadcast("not null");
							player.openInventory(in1);
							cpd.shopOpen = in1;
							cpd.shopType = -1;// purchase
						} // else item already purchased
						break;
					case 49:
						player.openInventory(shop1);
						break;
					}
				} else if (cpd.shopType == 4) {
					e.setCancelled(true);
					player.updateInventory();
					switch (e.getSlot()) {
					case 0:
						// ChatHelper.broadcast("generating purchase");
						cpd.purchasing = "bricks";
						Inventory in1 = generatePurchase(player, cpd);
						if (in1 != null) {
							// ChatHelper.broadcast("not null");
							player.openInventory(in1);
							cpd.shopOpen = in1;
							cpd.shopType = -1;// purchase
						} // else item already purchased
						break;
					case 49:
						player.openInventory(shop1);
						break;
					}
				} else if (cpd.shopType == 5) {
					e.setCancelled(true);
					player.updateInventory();
					switch (e.getSlot()) {
					case 0:
						// ChatHelper.broadcast("generating purchase");
						cpd.purchasing = "frag_grenade";
						Inventory in1 = generatePurchase(player, cpd);
						if (in1 != null) {
							// ChatHelper.broadcast("not null");
							player.openInventory(in1);
							cpd.shopOpen = in1;
							cpd.shopType = -1;// purchase
						} // else item already purchased
						break;
					case 1:
						// ChatHelper.broadcast("generating purchase");
						cpd.purchasing = "turret";
						Inventory in2 = generatePurchase(player, cpd);
						if (in2 != null) {
							// ChatHelper.broadcast("not null");
							player.openInventory(in2);
							cpd.shopOpen = in2;
							cpd.shopType = -1;// purchase
						} // else item already purchased
						break;
					case 8:
						// ChatHelper.broadcast("generating purchase");
						cpd.purchasing = "heal";
						Inventory in8 = generatePurchase(player, cpd);
						if (in8 != null) {
							// ChatHelper.broadcast("not null");
							player.openInventory(in8);
							cpd.shopOpen = in8;
							cpd.shopType = -1;// purchase
						} // else item already purchased
						break;
					case 49:
						player.openInventory(shop1);
						break;
					}
				}
			}
		}

	}

}
