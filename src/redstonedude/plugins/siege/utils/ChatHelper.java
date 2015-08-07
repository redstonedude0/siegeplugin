package redstonedude.plugins.siege.utils;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_8_R3.PlayerConnection;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import redstonedude.plugins.siege.src.Main;

public class ChatHelper {

	public static void broadcast(String s) {
		if (s != null) {
			Main.server.broadcastMessage(ChatColor.GREEN + "[Siege] " + ChatColor.BLUE + s);
		}
	}

	public static void sendMessage(Player p, String s) {
		p.sendMessage(ChatColor.GREEN + "[Siege] " + ChatColor.BLUE + s);
	}

	public static void sendTitle(Player p, String title, String subtitle, String color1, String color2, int i1, int i2, int i3) {
		IChatBaseComponent titleJSON = ChatSerializer.a("{'text': '" + title + "','color':'" + color1 + "'}");
		IChatBaseComponent subtitleJSON = ChatSerializer.a("{'text': '" + subtitle + "','color':'" + color2 + "'}");
		CraftPlayer craftplayer = (CraftPlayer) p;
		PlayerConnection connection = craftplayer.getHandle().playerConnection;
		PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleJSON, i1, i2, i3);
		PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitleJSON);
		connection.sendPacket(titlePacket);
		connection.sendPacket(subtitlePacket);
	}


}
