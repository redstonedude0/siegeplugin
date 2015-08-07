package redstonedude.plugins.siege.customdata;

import java.util.ArrayList;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import redstonedude.plugins.siege.src.Main;

public class MetadataValueHitters implements MetadataValue {
	
	public ArrayList<CustomHitterData> hitterDataArray = new ArrayList<CustomHitterData>();
	public int hitTotal = 0;
	
	@Override
	public Object value() {
		return null;
	}

	@Override
	public void invalidate() {
	}

	@Override
	public Plugin getOwningPlugin() {
		return Main.main;
	}

	@Override
	public String asString() {
		return null;
	}

	@Override
	public short asShort() {
		return 0;
	}

	@Override
	public long asLong() {
		return 0;
	}

	@Override
	public int asInt() {
		return 0;
	}

	@Override
	public float asFloat() {
		return 0;
	}

	@Override
	public double asDouble() {
		return 0;
	}

	@Override
	public byte asByte() {
		return 0;
	}

	@Override
	public boolean asBoolean() {
		return false;
	}
}
