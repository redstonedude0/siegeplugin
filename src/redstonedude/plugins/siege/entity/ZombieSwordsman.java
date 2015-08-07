package redstonedude.plugins.siege.entity;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.PathfinderGoalAvoidTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;

import redstonedude.plugins.siege.utils.FieldGetter;

public class ZombieSwordsman extends EntityZombie {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ZombieSwordsman(World world) {
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
		
		this.goalSelector.a(1, (PathfinderGoal)new PathfinderGoalFloat((EntityInsentient)this));
        this.goalSelector.a(2, (PathfinderGoal)new PathfinderGoalAvoidTarget((EntityCreature)this, (Class)EntityTNTPrimed.class, 10.0f, 1.0, 1.2));
        this.goalSelector.a(3, (PathfinderGoal)new PathfinderGoalAvoidTarget((EntityCreature)this, (Class)CreeperWallbreaker.class, 10.0f, 1.0, 1.2));
        this.goalSelector.a(4, (PathfinderGoal)new PathfinderGoalAvoidTarget((EntityCreature)this, (Class)ZombieBomb.class, 10.0f, 1.0, 1.2));
        this.goalSelector.a(5, (PathfinderGoal)new PathfinderGoalMeleeAttack((EntityCreature)this, (Class)EntityHuman.class, 1.0, false));
        this.goalSelector.a(6, (PathfinderGoal)new PathfinderGoalMoveTowardsRestriction((EntityCreature)this, 1.0));
        this.goalSelector.a(7, (PathfinderGoal)new PathfinderGoalRandomStroll((EntityCreature)this, 1.0));
        this.goalSelector.a(8, (PathfinderGoal)new PathfinderGoalLookAtPlayer((EntityInsentient)this, (Class)EntityHuman.class, 8.0f));
        this.goalSelector.a(8, (PathfinderGoal)new PathfinderGoalRandomLookaround((EntityInsentient)this));
        this.targetSelector.a(4, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityCreature)this, (Class)EntityHuman.class, true));
	}

}
