package redstonedude.plugins.siege.handler;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import redstonedude.plugins.siege.src.Main;
import redstonedude.plugins.siege.utils.ChatHelper;
import redstonedude.plugins.siege.utils.MathHelper;

public class SpecialWeaponHandler {

	public static int grenadeCount = 0;
	public static ArrayList<Integer> grenadeIDs = new ArrayList<Integer>();
	public static ArrayList<Location> turretLocations = new ArrayList<Location>();
	
	public static void blockPlace(final BlockPlaceEvent event) {
		if (event.getItemInHand().isSimilar(PurchasesHandler.turret)) {
			//turret placed
			//ChatHelper.broadcast(event.getBlock().getType().toString());
			final Block b = event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().clone().add(0,1,0));
			if (b.getType() == Material.AIR) {
				event.setCancelled(true);
				int num = event.getItemInHand().getAmount();
				num--;
				if (num == 0) {
					event.getPlayer().setItemInHand(null);
				} else {
					event.getItemInHand().setAmount(num);
				}
				final Location l1 = event.getBlock().getLocation();
				final Location l2 = b.getLocation();
				Main.server.getScheduler().scheduleSyncDelayedTask(Main.main, new Runnable() {
					@Override
					public void run() {
						Main.server.getWorld("world").getBlockAt(l1).setType(Material.OBSIDIAN);
						Block b2 = Main.server.getWorld("world").getBlockAt(l2);
						//b2.setType(Material.SKULL_ITEM);
						//((Skull) b2).setSkullType(SkullType.WITHER);
						//((CraftBlock) b2).getState();
						//MaterialData s2 =  b2.getState().getData();
						ItemStack item = new ItemStack(Material.SKULL,1,(byte)1);
						b2.setType(Material.SKULL);
						
						b2.getState().setData(item.getData());
						
						
					}
				}, 2L);
				
			} else {
				event.setCancelled(true);
				ChatHelper.sendMessage(event.getPlayer(),"Could not place turret here (Space required: 1x2x1)");
			}
		}
	}
	
	public static void onThrow(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.getItemInHand().isSimilar(PurchasesHandler.grenade5)) {
			int amount = player.getItemInHand().getAmount();
			amount--;
			if (amount != 0) {
				player.getItemInHand().setAmount(amount);
			} else {
				player.setItemInHand(null);
			}
			ItemStack stack = PurchasesHandler.getItem("frag_grenade").clone();
			stack.setAmount(1);
			ItemMeta im = stack.getItemMeta();
			im.setLore(Arrays.asList("Grenade_"+grenadeCount));
			grenadeCount++;
			stack.setItemMeta(im);
			final Item item = player.getWorld().dropItem(player.getEyeLocation(), stack);
			grenadeIDs.add(item.getEntityId());
			//item.setMetadata("Grenade", (MetadataValue) new FixedMetadataValue(Main.main, true));
			item.setVelocity(player.getEyeLocation().getDirection().multiply(2));
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main, new Runnable() {
				@Override
				public void run() {
					Location loc = item.getLocation();
					
					if (MathHelper.sphereIllegal(loc,5F)) {
						//cancelled
						item.remove();
					} else {
						item.remove();
						loc.getWorld().createExplosion(item.getLocation().getBlockX(), item.getLocation().getBlockY(), item.getLocation().getBlockZ(), 5F, false, true);
					}
					//loc.getWorld().createExplosion(item.getLocation().getBlockX(), item.getLocation().getBlockY(), item.getLocation().getBlockZ(), 5F, false, true);
				}
			}, 100L);
		}

	}

}
