/*
 * Decompiled with CFR 0_101.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.AxisAlignedBB
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityCreature
 *  net.minecraft.server.v1_8_R3.MathHelper
 *  net.minecraft.server.v1_8_R3.NavigationAbstract
 *  net.minecraft.server.v1_8_R3.PathEntity
 *  net.minecraft.server.v1_8_R3.PathfinderGoal
 *  net.minimine.bandits.bandits
 *  net.minimine.bandits.newmobs.TestZombie
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity
 */
package redstonedude.plugins.siege.entity.pathfinder;

import java.util.Random;

import net.minecraft.server.v1_8_R3.PathfinderGoal;

import org.bukkit.Location;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import redstonedude.plugins.siege.entity.IronGolemArtillery;
import redstonedude.plugins.siege.handler.InfiniteInventoryHandler;
import redstonedude.plugins.siege.src.Main;

public class PathfinderGoalThrowTNT extends PathfinderGoal {
	private IronGolemArtillery golem;
	private int counter = 0;
	private Random r = new Random();

	public PathfinderGoalThrowTNT(IronGolemArtillery iga) {
		this.golem = iga;
		this.a(1);
	}

	public boolean a() {
		counter++;
		if (counter >= 50) {
			counter = 0;
		}
		if (counter == 0) {
			//Main.server.broadcastMessage("blowing shit up");
			// pick target
			golem.world.broadcastEntityEffect(golem, (byte) 4);
			// calculate vector
			Location from = golem.getBukkitEntity().getLocation().clone().add(0.5, 3, 0.5);
			Vector fromVec = from.toVector();
			Vector to = InfiniteInventoryHandler.chest.clone().add(0.5, 1, 0.5).toVector();
			Vector v = calculateVelocity(fromVec, to, 10);
			
			double multiplier = 0.8 + (r.nextInt(5)/10);
			v.setX(v.getX()*multiplier);
			multiplier = 0.8 + (r.nextInt(5)/10);
			v.setY(v.getY()*multiplier);
			multiplier = 0.8 + (r.nextInt(5)/10);
			v.setZ(v.getZ()*multiplier);
			// offset vector
			// summon tnt
			TNTPrimed tp = Main.server.getWorld("world").spawn(from, TNTPrimed.class);
			tp.setFuseTicks(80);
			tp.setVelocity(v);
			tp.setYield(3F);
			//Main.server.broadcastMessage("veclo: " + v.serialize().toString());

		}
		return (counter == 0);
	}

	public static Vector calculateVelocity(Vector from, Vector to, int heightGain) {
		//Main.server.broadcastMessage("from: " + from.serialize().toString());
		//Main.server.broadcastMessage("to: " + to.serialize().toString());
		// Gravity of a potion
		double gravity = 0.115;

		// Block locations
		int endGain = to.getBlockY() - from.getBlockY();
		double horizDist = Math.sqrt(distanceSquared(from, to));

		// Height gain
		int gain = heightGain;

		double maxGain = gain > (endGain + gain) ? gain : (endGain + gain);

		maxGain = maxGain == 0 ? 0.1 : maxGain;
		
		//Main.server.broadcastMessage("maxgain: " + maxGain);
		//Main.server.broadcastMessage("hz: " + horizDist);
		
		// Solve quadratic equation for velocity
		double a = -horizDist * horizDist / (4 * maxGain);
		double b = horizDist;
		double c = -endGain;

		double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);

		//Main.server.broadcastMessage("slope: " + slope);
		//Main.server.broadcastMessage("slopea: " + a);
		//Main.server.broadcastMessage("slopeb: " + b);
		//Main.server.broadcastMessage("slopec: " + c);
		
		
		// Vertical velocity
		double vy = Math.sqrt(maxGain * gravity);
		
		//Main.server.broadcastMessage("vy: " + vy);

		// Horizontal velocity
		double vh = vy / slope;
		
		//Main.server.broadcastMessage("vh: " + vh);

		// Calculate horizontal direction
		int dx = to.getBlockX() - from.getBlockX();
		int dz = to.getBlockZ() - from.getBlockZ();
		double mag = Math.sqrt(dx * dx + dz * dz);
		double dirx = dx / mag;
		double dirz = dz / mag;

		// Horizontal velocity components
		double vx = vh * dirx;
		double vz = vh * dirz;

		return new Vector(vx, vy, vz);
	}

	private static double distanceSquared(Vector from, Vector to) {
		double dx = to.getBlockX() - from.getBlockX();
		double dz = to.getBlockZ() - from.getBlockZ();

		return dx * dx + dz * dz;
	}

	public boolean b() {
		return false;
	}

	public void c() {

	}
}