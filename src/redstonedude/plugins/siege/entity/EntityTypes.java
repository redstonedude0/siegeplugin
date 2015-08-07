package redstonedude.plugins.siege.entity;

import java.util.Map;

import net.minecraft.server.v1_8_R3.Entity;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import redstonedude.plugins.siege.utils.FieldGetter;

public enum EntityTypes {

	ZOMBIE_MINER("ZombieMiner", 54, ZombieMiner.class),
	ZOMBIE_SWORDSMAN("ZombieSwordsman", 54, ZombieSwordsman.class),
	IRON_GOLEM_ARTILLERY("IronGolemArtillery", 99, IronGolemArtillery.class),
	CREEPER_WALLBREAKER("CreeperWallbreaker", 50, CreeperWallbreaker.class),
	ZOMBIE_BOMB("ZombieBomb",54,ZombieBomb.class),
	SUPER_GIANT("SuperGiant",53,SuperGiant.class),
	BLANK_SLIME("BlankSlime",65,EntityBlankSlime.class);

	private EntityTypes(String name, int id, Class<? extends Entity> custom) {
		addToMaps(custom, name, id);
	}

	public static void spawnEntity(Entity entity, Location loc) {
		entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		((CraftWorld) loc.getWorld()).getHandle().addEntity(entity);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void addToMaps(Class<? extends Entity> clazz, String name, int id) {
		// getPrivateField is the method from above.
		// Remove the lines with // in front of them if you want to override
		// default entities (You'd have to remove the default entity from the
		// map first though).
		((Map) FieldGetter.getPrivateField("c", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, clazz);
		((Map) FieldGetter.getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, name);
		// ((Map)getPrivateField("e",
		// net.minecraft.server.v1_7_R4.EntityTypes.class,
		// null)).put(Integer.valueOf(id), clazz);
		((Map) FieldGetter.getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, Integer.valueOf(id));
		// ((Map)getPrivateField("g",
		// net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(name,
		// Integer.valueOf(id));
		
	}

}
