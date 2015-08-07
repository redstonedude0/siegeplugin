package redstonedude.plugins.siege.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PlayerConnection;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBar {

	public static void sendActionBar(Player p, String Message) {
		if (Message == null) {
			Message = "null";
		}
		Message.replace('&', '§');

		PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;
		IChatBaseComponent chat = ChatSerializer.a("{\"text\": \"" + Message + "\"}");
		PacketPlayOutChat packet = new PacketPlayOutChat(chat, (byte) 2);

		con.sendPacket(packet);
	}

	public static void directcancelActionBar(Player p) {
		PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;
		PacketPlayOutChat packet = new PacketPlayOutChat(null, (byte) 2);

		con.sendPacket(packet);
	}
}
