package redstonedude.plugins.siege.entity;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.PathfinderGoalAvoidTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;

import redstonedude.plugins.siege.entity.pathfinder.PathfinderGoalThrowTNT;
import redstonedude.plugins.siege.utils.FieldGetter;

public class IronGolemArtillery extends EntityIronGolem {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IronGolemArtillery(World world) {
		super(((CraftWorld) world).getHandle());

		UnsafeList goalB = (UnsafeList) FieldGetter.getPrivateField("b", PathfinderGoalSelector.class, goalSelector);
		goalB.clear();
		UnsafeList goalC = (UnsafeList) FieldGetter.getPrivateField("c", PathfinderGoalSelector.class, goalSelector);
		goalC.clear();
		UnsafeList targetB = (UnsafeList) FieldGetter.getPrivateField("b", PathfinderGoalSelector.class, targetSelector);
		targetB.clear();
		UnsafeList targetC = (UnsafeList) FieldGetter.getPrivateField("c", PathfinderGoalSelector.class, targetSelector);
		targetC.clear();
		
        this.setSize(1.4f, 2.9f);
        this.goalSelector.a(1, (PathfinderGoal)new PathfinderGoalFloat((EntityInsentient)this));
        this.goalSelector.a(2, (PathfinderGoal)new PathfinderGoalAvoidTarget((EntityCreature)this, (Class)EntityTNTPrimed.class, 10.0f, 1.0, 1.2));
        this.goalSelector.a(3, (PathfinderGoal)new PathfinderGoalAvoidTarget((EntityCreature)this, (Class)CreeperWallbreaker.class, 10.0f, 1.0, 1.2));
        this.goalSelector.a(4, (PathfinderGoal)new PathfinderGoalAvoidTarget((EntityCreature)this, (Class)ZombieBomb.class, 10.0f, 1.0, 1.2));
        //this.goalSelector.a(4, (PathfinderGoal)new PathfinderGoalAvoidTarget((EntityCreature)this, (Class)IronGolemArtillery.class, 10.0f, 1.0, 1.2));
        this.goalSelector.a(5, (PathfinderGoal)new PathfinderGoalThrowTNT(this));
        this.goalSelector.a(6, (PathfinderGoal)new PathfinderGoalRandomStroll((EntityCreature)this, 0.6));
        this.goalSelector.a(8, (PathfinderGoal)new PathfinderGoalRandomLookaround((EntityInsentient)this));

		
	}
	
}
