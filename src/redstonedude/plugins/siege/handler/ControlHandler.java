package redstonedude.plugins.siege.handler;

import java.util.UUID;

import org.bukkit.Location;

import redstonedude.plugins.siege.src.Main;
import redstonedude.plugins.siege.utils.ChatHelper;

public class ControlHandler {
	
	public static boolean gameEnded = false;
	
	public static void endGame() {
		ChatHelper.broadcast("You have failed, you made it to wave " + Main.wave);
		ChatHelper.broadcast("You will be taken back to the lobby in 10 seconds");
		int playersQty = PlayerEntityHandlerListener.players.size();
		Location l = new Location(Main.server.getWorld("world"), 100, 65, 100);
		for (int k = 0; k < playersQty; k++) {
			UUID u = PlayerEntityHandlerListener.players.get(k);
			Main.server.getPlayer(u).teleport(l);
		}
		Main.wave = -2;
		Main.server.getScheduler().scheduleSyncDelayedTask(Main.main, new Runnable() {
			public void run() {
				ChatHelper.broadcast("I lied, I can't do that sort of code");
				Main.startedYet = false;
			}
		}, 200L);
		Main.timerUntilForceStart = 10;
		gameEnded = true;
	}
}
