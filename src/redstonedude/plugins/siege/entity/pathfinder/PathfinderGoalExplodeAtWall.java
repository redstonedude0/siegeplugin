package redstonedude.plugins.siege.entity.pathfinder;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.PathfinderGoal;

import org.bukkit.block.Block;

import redstonedude.plugins.siege.entity.CreeperWallbreaker;
import redstonedude.plugins.siege.handler.InfiniteInventoryHandler;

public class PathfinderGoalExplodeAtWall extends PathfinderGoal {

	private EntityCreature a;
	private Block b;
	double PrevX;
	double PrevY;
	double PrevZ;
	int counter = 0;

	public PathfinderGoalExplodeAtWall(Object obj) {

		this.a = (EntityCreature) obj;
		this.a(1);
	}

	public boolean a() {

		this.b = InfiniteInventoryHandler.chest.getBlock();
		// Main.server.broadcastMessage("a method");

		return this.b != null;
	}

	public boolean b() {
		// Main.server.broadcastMessage("b method");

		return false;

	}

	public void c() {
		// Main.server.broadcastMessage("c method");
		// Main.server.broadcastMessage("path method");
		this.a.getNavigation().a((double) this.b.getX(), (double) this.b.getY(), (double) this.b.getZ(), 1.0D);
	}

	public void e() {
		// a.getNBTTag().
		// a.getNBTTag().setBoolean("ignited", true);
		counter++;
		// Main.server.broadcastMessage("counter: " + counter);
		
		if (counter % 10 == 0) {
			counter = 0;
			if (Math.round(a.locX) == Math.round(PrevX)) {
				if (Math.round(a.locZ) == Math.round(PrevZ)) {
					if (Math.round(a.locY) == Math.round(PrevY)) {
						((CreeperWallbreaker) a).co();
						// ExplosionPrimeEvent event= new
						// ExplosionPrimeEvent(CraftEntity.getEntity(a.world.getServer(),a),3F,false);
						// a.world.getServer().getPluginManager().callEvent(event);
						// if(!event.isCancelled()){
						// a.world.createExplosion(a,a.locX,a.locY,a.locZ,event.getRadius(),event.getFire(),true);
						// a.die();
						// } else{
						// a.die();
						// }
					}
				}
			}
			PrevX = a.locX;
			PrevZ = a.locZ;
			PrevY = a.locY;
		}
	}
}