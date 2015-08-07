package redstonedude.plugins.siege.customdata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import redstonedude.plugins.siege.handler.PlayerEntityHandlerListener;
import redstonedude.plugins.siege.sql.MySQL;
import redstonedude.plugins.siege.src.Main;
import redstonedude.plugins.siege.utils.ChatHelper;

public class DataLoader {

	public static boolean loadAllData() {
		try {
			MySQL sql = new MySQL(Main.main, "localhost", "3306", "Siege", "siege", "RubyPuppy72");
			Connection c = sql.openConnection();
			PreparedStatement ps = c.prepareStatement("CREATE TABLE IF NOT EXISTS siege_userData (`id` INT(64) NOT NULL AUTO_INCREMENT PRIMARY KEY, `uuid` VARCHAR(256), `money` INT);");
			ps.executeUpdate();
			String where = "";
			int players = PlayerEntityHandlerListener.players.size();
			int pm1 = players-1;
			
			ArrayList<String> playerIds = new ArrayList<String>();
			for (int i = 0; i < players; i++) {
				String uuid = PlayerEntityHandlerListener.players.get(i).toString();
				playerIds.add(uuid);
				where += "`uuid`=\"" +uuid+"\" ";
				if (i < pm1) {
					where += "OR ";
				}
			}
			
			Statement statement = c.createStatement();
			String query = "SELECT * FROM siege_userData WHERE " + where + ";";
			ResultSet r = statement.executeQuery(query);
			//r.next();
			//ChatHelper.broadcast("query " + query);
			//ChatHelper.broadcast("r2 " + r.toString());
			while (r.next()) {
				//ChatHelper.broadcast("doing " + r.toString());
				String uuid = r.getString("uuid");
				//ChatHelper.broadcast("doing " + uuid);
				playerIds.remove(uuid);
				UUID u = UUID.fromString(uuid);
				CustomPlayerData cpd = PlayerEntityHandlerListener.getPlayerData(u);
				cpd.points = r.getInt("money");
			}
			int playersLeft = playerIds.size();
			for (int i = 0; i < playersLeft; i++) {
				String id = playerIds.get(i);
				UUID u = UUID.fromString(id);
				CustomPlayerData cpd = PlayerEntityHandlerListener.getPlayerData(u);
				cpd.points = 0;
				PreparedStatement ps1 = c.prepareStatement("INSERT INTO siege_userData SET `money` = \"0\", `uuid` = \"" + id + "\";");
				ps1.executeUpdate();
				//ChatHelper.broadcast("inserting: " + ps1.toString());
			}
			c.close();
			sql.closeConnection();
			return true;
		} catch (Exception e) {
			ChatHelper.broadcast("ERROR LOADING DATA");
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean saveAllData() {
		try {
			MySQL sql = new MySQL(Main.main, "localhost", "3306", "Siege", "siege", "RubyPuppy72");
			Connection c = sql.openConnection();
			//PreparedStatement ps = c.prepareStatement("CREATE TABLE IF NOT EXISTS siege_userData (`id` INT(64) NOT NULL AUTO_INCREMENT PRIMARY KEY, `uuid` VARCHAR(256), `money` INT);");
			//PreparedStatement ps = c.prepareStatement("UPDATE siege_userData SET `money` = \";");
			//ps.executeUpdate();
			int players = PlayerEntityHandlerListener.players.size();
			for (int i = 0; i < players; i++) {
				String set = "";
				String where = "";
				UUID uuid = PlayerEntityHandlerListener.players.get(i);
				CustomPlayerData cpd = PlayerEntityHandlerListener.getPlayerData(uuid);
				set = cpd.points + "";
				where = uuid.toString();
				PreparedStatement ps = c.prepareStatement("UPDATE siege_userData SET `money` = \"" + set + "\" WHERE `uuid` = \"" + where + "\";");
				ps.executeUpdate();
				//ChatHelper.broadcast("doing " + uuid);
				//ChatHelper.broadcast("ps " + ps.toString());
			}
			c.close();
			sql.closeConnection();
			Main.server.getLogger().info("[SIEGE] SAVE DATA SUCCESS");
			Main.server.getLogger().info("[SIEGE] SAVE DATA SUCCESS");
			Main.server.getLogger().info("[SIEGE] SAVE DATA SUCCESS");
			return true;
		} catch (Exception e) {
			Main.server.getLogger().severe("[SIEGE] SAVE DATA FAILURE");
			Main.server.getLogger().severe("[SIEGE] SAVE DATA FAILURE");
			Main.server.getLogger().severe("[SIEGE] SAVE DATA FAILURE");
			e.printStackTrace();
			return false;
		}
	}

}
