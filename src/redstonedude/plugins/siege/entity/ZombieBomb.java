package redstonedude.plugins.siege.entity;

import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;

import redstonedude.plugins.siege.entity.pathfinder.PathfinderGoalBombAtWall;
import redstonedude.plugins.siege.utils.FieldGetter;

public class ZombieBomb extends EntityZombie {

	@SuppressWarnings({ "rawtypes" })
	public ZombieBomb(World world) {
		super(((CraftWorld) world).getHandle());

		UnsafeList goalB = (UnsafeList) FieldGetter.getPrivateField("b", PathfinderGoalSelector.class, goalSelector);
		goalB.clear();
		UnsafeList goalC = (UnsafeList) FieldGetter.getPrivateField("c", PathfinderGoalSelector.class, goalSelector);
		goalC.clear();
		UnsafeList targetB = (UnsafeList) FieldGetter.getPrivateField("b", PathfinderGoalSelector.class, targetSelector);
		targetB.clear();
		UnsafeList targetC = (UnsafeList) FieldGetter.getPrivateField("c", PathfinderGoalSelector.class, targetSelector);
		targetC.clear();
		
		this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(50);
		//this.getBukkitEntity().
		this.goalSelector.a(5, new PathfinderGoalBombAtWall(this));
		this.setBaby(true);
		this.setVillager(true);
	}

}
