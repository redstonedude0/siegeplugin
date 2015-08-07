package redstonedude.plugins.siege.entity.pathfinder;

import net.minecraft.server.v1_8_R3.PathfinderGoal;

import org.bukkit.block.Block;

import redstonedude.plugins.siege.entity.ZombieBomb;
import redstonedude.plugins.siege.handler.InfiniteInventoryHandler;
import redstonedude.plugins.siege.src.Main;

public class PathfinderGoalBombAtWall extends PathfinderGoal {

	private ZombieBomb a;
	private Block b;
	double PrevX;
	double PrevY;
	double PrevZ;
	int counter = 0;

	public PathfinderGoalBombAtWall(ZombieBomb obj) {

		this.a = obj;
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
		//Main.server.broadcastMessage("counter: " + counter);
		
		if (counter == 5) {
			a.setCustomName("9");
		}
		if (counter == 10) {
			a.setCustomName("8");
		}
		if (counter == 15) {
			a.setCustomName("7");
		}
		if (counter == 20) {
			a.setCustomName("6");
		}
		if (counter == 25) {
			a.setCustomName("5");
		}
		if (counter == 30) {
			a.setCustomName("4");
		}
		if (counter == 35) {
			a.setCustomName("3");
		}
		if (counter == 40) {
			a.setCustomName("2");
		}
		if (counter == 45) {
			a.setCustomName("1");
		}
		if (counter == 50) {
			Main.server.getWorld("world").createExplosion(a.locX,a.locY,a.locZ, 3F);
			a.die();
		}
		
		
		//if (counter % 10 == 0) {
			//counter = 0;
			//if (Math.round(a.locX) == Math.round(PrevX)) {
			//	if (Math.round(a.locZ) == Math.round(PrevZ)) {
			//		if (Math.round(a.locY) == Math.round(PrevY)) {
			//			ExplosionPrimeEvent event = new ExplosionPrimeEvent(CraftEntity.getEntity(a.world.getServer(), a), 1F, false);
			//			a.world.getServer().getPluginManager().callEvent(event);
			//			if (!event.isCancelled()) {
			//				//a.world.createExplosion(a, a.locX, a.locY, a.locZ, 3F, false, false);
			//				Main.server.getWorld("world").createExplosion(a.locX,a.locY,a.locZ, 2F);
			//				a.die();
			//			} else {
			//				a.die();
			//			}
			//		}
			//	}
			//}
			//PrevX = a.locX;
			//PrevZ = a.locZ;
			//PrevY = a.locY;
		//}
	}
}