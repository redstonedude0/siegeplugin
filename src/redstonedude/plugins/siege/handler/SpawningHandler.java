package redstonedude.plugins.siege.handler;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_8_R3.PlayerConnection;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import redstonedude.plugins.siege.entity.CreeperWallbreaker;
import redstonedude.plugins.siege.entity.EntityBlankSlime;
import redstonedude.plugins.siege.entity.IronGolemArtillery;
import redstonedude.plugins.siege.entity.SuperGiant;
import redstonedude.plugins.siege.entity.ZombieBomb;
import redstonedude.plugins.siege.entity.ZombieMiner;
import redstonedude.plugins.siege.entity.ZombieSwordsman;
import redstonedude.plugins.siege.src.Main;
import redstonedude.plugins.siege.utils.ChatHelper;
import redstonedude.plugins.siege.utils.MathHelper;

public class SpawningHandler {

	static ArrayList<Integer> waveBuffer = new ArrayList<Integer>();
	public static ArrayList<Location> spawnLocations = new ArrayList<Location>();
	static Random rand = new Random();
	public static int golemCount = 0;
	public static int creeperCount = 0;
	
	public static Type type = Type.BURST;
	public static org.bukkit.Color c1 = Color.RED;
	public static Color c2 = Color.BLACK;
	public static FireworkEffect fwe = FireworkEffect.builder().flicker(false).withColor(c1).with(type).withFade(c2).trail(true).build();
	public static Vector nulVec = new Vector(0, 0, 0);
	
	public static boolean handleSpawning() {
		// handle mob spawning here
		// Main.server.broadcastMessage("Handling mob spawning");
		if (waveBuffer != null) {
			int bufferSize = waveBuffer.size();
			int locationsSize = spawnLocations.size();
			// Main.server.broadcastMessage("Buffersize: " + bufferSize);
			// Main.server.broadcastMessage("Locations: " + locationsSize);
			// Main.server.broadcastMessage("Buffer: " + waveBuffer.toString());
			// Main.server.broadcastMessage("Buffersize: " + bufferSize);
			if (Main.wave % 10 == 0 && (Main.wave) != 0) {
				String subtitle = "Bosswave";
				ChatHelper.broadcast("A boss wave has arrived!");
				switch (Main.bossWave) {
				case 0:
					ChatHelper.broadcast("Huge armies of golems and wallbreakers storm your base");
					subtitle = "Explosive";
					break;
				case 1:
					ChatHelper.broadcast("The groans of zombie swordsman wash over your base");
					subtitle = "Swordsmen";
					break;
				case 2:
					subtitle = "Missile";
					ChatHelper.broadcast("A missile strike has been detected, take cover");
					Main.server.getScheduler().scheduleSyncDelayedTask(Main.main, new Runnable() {
						public void run() {
							// strike
							int missiles = golemCount * 2;
							Location loc1 = InfiniteInventoryHandler.chest;
							for (int i = 0; i < missiles; i++) {
								int x = rand.nextInt(30) - 15;
								int z = rand.nextInt(30) - 15;
								Location vec1 = loc1.clone().add(x, 50, z);
								Vector v = new Vector(0, -3, 0);
								TNTPrimed tp = Main.server.getWorld("world").spawn(vec1, TNTPrimed.class);
								tp.setFuseTicks(80);
								tp.setVelocity(v);
								tp.setYield(MathHelper.cap((Main.wave/2),15));
							}
						}
					}, 60L);
					int playersQty = PlayerEntityHandlerListener.players.size();
					for (int i = 0; i < playersQty; i++) {
						UUID u = PlayerEntityHandlerListener.players.get(i);
						Player p = Main.server.getPlayer(u);
						p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
					}
					break;
				case 3:
					ChatHelper.broadcast("That zombie seems a bit larger than normal...");
					subtitle = "Giant";
					for (int i = 0; i < golemCount; i++) {
						int r = rand.nextInt(locationsSize);
						Location l = spawnLocations.get(r);
						spawnEntity(l, 5);
					}
					break;
				}
				String title = "Boss wave";
				IChatBaseComponent titleJSON = ChatSerializer.a("{'text': '" + title + "','color':'red'}");
				IChatBaseComponent subtitleJSON = ChatSerializer.a("{'text': '" + subtitle + "','color':'orange'}");
				int players = PlayerEntityHandlerListener.players.size();
				PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleJSON, 1, 4, 1);
				PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitleJSON);
				for (int i = 0; i < players; i++) {
					CraftPlayer craftplayer = (CraftPlayer) Main.server.getPlayer(PlayerEntityHandlerListener.players.get(i));
					PlayerConnection connection = craftplayer.getHandle().playerConnection;
					connection.sendPacket(titlePacket);
					connection.sendPacket(subtitlePacket);
				}
			}
			
			for (int i = 0; i < bufferSize; i++) {
				int numberToSpawn = waveBuffer.get(i);
				// Main.server.broadcastMessage("Spawning mobs: " +
				// numberToSpawn);
				// Main.server.broadcastMessage("numberToSpawn: " +
				// numberToSpawn);
				for (int j = 0; j < numberToSpawn; j++) {
					int r = rand.nextInt(locationsSize);
					Location l = spawnLocations.get(r);
					// Main.server.broadcastMessage("l: " + l);
					Location l1 = l.clone();
					l1.add(0.5, 0, 0.5);
					Firework fw = (Firework) Main.server.getWorld("world").spawnEntity(l1, EntityType.FIREWORK);
					FireworkMeta fwm = fw.getFireworkMeta();
					fwm.addEffect(fwe);
					fwm.setPower(0);
					fw.setFireworkMeta(fwm);
					fw.setVelocity(nulVec);
					// Main.server.broadcastMessage("l: " + l.toString());
					// Main.server.broadcastMessage("i: " + i);
					spawnEntity(l1, i);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public static void bufferSpawning() {
		int nextWave = Main.wave + 1;
		golemCount = (int) Math.floor(nextWave / 10);
		creeperCount = (int) Math.floor(nextWave / 6);
		waveBuffer = new ArrayList<Integer>();
		if (nextWave % 10 == 9) {
			// Main.server.broadcastMessage("restwave buffered");
			waveBuffer = new ArrayList<Integer>();
		} else if (nextWave % 10 == 0 && nextWave != 0) {
			// Main.server.broadcastMessage("bosswave next");
			waveBuffer = new ArrayList<Integer>();
			int switchForBoss = rand.nextInt(4);
			switch (switchForBoss) {
			case 0:
				waveBuffer.add(0);
				waveBuffer.add(creeperCount * 3);
				waveBuffer.add(golemCount * 3);
				waveBuffer.add(0);
				Main.bossWave = 0;
				break;
			case 1:
				waveBuffer.add(4 + (nextWave << 2));
				waveBuffer.add(0);
				waveBuffer.add(0);
				waveBuffer.add(0);
				Main.bossWave = 1;
				break;
			case 2:
				Main.bossWave = 2;
				break;
			case 3:
				waveBuffer.add(0);
				waveBuffer.add(0);
				waveBuffer.add(0);
				waveBuffer.add(0);
				Main.bossWave = 3;
				break;
			}
		} else if (nextWave < 5) {
			switch (nextWave) {
			case 1:
				waveBuffer.add(0);
				waveBuffer.add(1);
				waveBuffer.add(0);
				waveBuffer.add(0);
				break;
			case 2:
				waveBuffer.add(4);
				waveBuffer.add(0);
				waveBuffer.add(0);
				waveBuffer.add(0);
				break;
			case 3:
				waveBuffer.add(0);
				waveBuffer.add(0);
				waveBuffer.add(0);
				waveBuffer.add(4);
				break;
			case 4:
				waveBuffer.add(0);
				waveBuffer.add(0);
				waveBuffer.add(1);
				waveBuffer.add(0);
				break;
			}
		} else {
			waveBuffer.add(4 + nextWave);
			waveBuffer.add(creeperCount);
			waveBuffer.add(golemCount);
			waveBuffer.add(2 + (int) Math.floor(nextWave >> 1));
		}
	}

	public static void spawnEntity(Location l, int id) {
		switch (id) {
		case 0: // heavy zombie, sword and normal AI
			//net.minecraft.server.v1_8_R3.World world = ((CraftWorld) Main.server.getWorld("world")).getHandle();
			//EntityBlankSlime eA = new EntityBlankSlime(world);
			//redstonedude.plugins.siege.entity.EntityTypes.spawnEntity(eA, l);
			
			//ItemStack stack = new ItemStack(Material.DIAMOND_PICKAXE);
			//Item item2 = Main.server.getWorld("world").dropItem(l, stack);
			
			//LivingEntity eAB = (LivingEntity) eA.getBukkitEntity();
			//eAB.setPassenger(item2);
			
			ZombieSwordsman e = new ZombieSwordsman(Main.server.getWorld("world"));
			redstonedude.plugins.siege.entity.EntityTypes.spawnEntity(e, l);
			ItemStack item = new ItemStack(Material.IRON_SWORD, 1);
			CraftItemStack cis = CraftItemStack.asCraftCopy(item);
			net.minecraft.server.v1_8_R3.ItemStack sta = CraftItemStack.asNMSCopy(cis);
			e.setEquipment(0, sta);
			e.setBaby(false);
			e.setVillager(false);
			PlayerEntityHandlerListener.swordsmen.add(e.getId());
			
			//LivingEntity eB = (LivingEntity) e.getBukkitEntity();
			//eB.setPassenger(eA.getBukkitEntity());
			
			//item2.setPickupDelay(32767);
			//item2.setTicksLived(32767);
			
			//PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY, 6000, 1, true, false);
			//eAB.addPotionEffect(effect);
			
			break;
		case 1:
			CreeperWallbreaker e1 = new CreeperWallbreaker(Main.server.getWorld("world"));
			redstonedude.plugins.siege.entity.EntityTypes.spawnEntity(e1, l);
			PlayerEntityHandlerListener.wallbreakers.add(e1.getId());
			break;
		case 2:
			IronGolemArtillery e2 = new IronGolemArtillery(Main.server.getWorld("world"));
			redstonedude.plugins.siege.entity.EntityTypes.spawnEntity(e2, l);
			PlayerEntityHandlerListener.golems.add(e2.getId());
			break;
		case 3:
			ZombieMiner e3 = new ZombieMiner(Main.server.getWorld("world"));
			ItemStack item3 = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			LeatherArmorMeta lam3 = (LeatherArmorMeta) item3.getItemMeta();
			lam3.setColor(Color.fromRGB(139, 69, 19));
			redstonedude.plugins.siege.entity.EntityTypes.spawnEntity(e3, l);
			CraftItemStack cis3 = CraftItemStack.asCraftCopy(item3);
			net.minecraft.server.v1_8_R3.ItemStack sta3 = CraftItemStack.asNMSCopy(cis3);
			e3.setEquipment(3, sta3);
			e3.setBaby(false);
			e3.setVillager(false);
			PlayerEntityHandlerListener.miners.add(e3.getId());
			break;
		case 4:
			ZombieBomb zb = new ZombieBomb(Main.server.getWorld("world"));
			redstonedude.plugins.siege.entity.EntityTypes.spawnEntity(zb, l);
			zb.setHealth(1F);
			zb.motX = (rand.nextInt(5) - 2) >> 1;
			zb.motZ = (rand.nextInt(5) - 2) >> 1;
			zb.motY = rand.nextInt(8) >> 3;
			zb.setCustomName("10");
			zb.setCustomNameVisible(true);
			PlayerEntityHandlerListener.bombs.add(zb.getId());
			break;
		case 5:
			SuperGiant giant = new SuperGiant(Main.server.getWorld("world"));
			redstonedude.plugins.siege.entity.EntityTypes.spawnEntity(giant, l);
			// giant.setHealth(1F);
			PlayerEntityHandlerListener.giants.add(giant.getId());
			break;
		}
		Main.updateScoreboard(Main.nextWaveTime - (Main.ticker * 20));
	}

}
