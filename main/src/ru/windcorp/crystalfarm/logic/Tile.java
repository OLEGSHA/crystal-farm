/**
 * Crystal Farm the game
 * Copyright (C) 2018  Crystal Farm Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ru.windcorp.crystalfarm.logic;

import static ru.windcorp.crystalfarm.logic.GameManager.TEXTURE_SIZE;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.ref.WeakReference;

import ru.windcorp.crystalfarm.client.ModuleClient;
import ru.windcorp.crystalfarm.client.View;
import ru.windcorp.crystalfarm.content.basic.Units;
import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.graphics.GraphicsInterface;
import ru.windcorp.crystalfarm.graphics.texture.ComplexTexture;
import ru.windcorp.crystalfarm.logic.server.World;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.crystalfarm.translation.TString;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public abstract class Tile extends Updateable implements ViewTarget {
	
	private static final Color COLLISION_BOUNDS_FILL_COLOR = new Color(0xFFFFFF77);
	
	private final Mod mod;
	private final String id;
	private TString name = null;
	
	private WeakReference<TileLevel<?>> level = null;
	private int nid;
	
	private boolean isTickable;

	public Tile(Mod mod, String id) {
		this.mod = mod;
		this.id = mod.getName() + ":" + id;
	}
	
	protected Tile(Mod mod, String id, String prefix) {
		this(mod, id);
	}
	
	protected void setLevel(TileLevel<?> level) {
		if (level == null) {
			if (this instanceof Collideable) {
				TileLevel<?> prevLevel = getLevel();
				if (prevLevel != null) {
					prevLevel.getCollideables().remove((Collideable) this);
				}
			}
			this.level = null;
			return;
		}
		
		this.level = new WeakReference<TileLevel<?>>(level);

		if (isTickable()) {
			level.getTickableTiles().add(this);
		}
		
		if (this instanceof Collideable && ((Collideable) this).canCollide()) {
			level.getCollideables().add((Collideable) this);
		}
	}
	
	public TileLevel<?> getLevel() {
		if (this.level == null) {
			return null;
		}
		
		return this.level.get();
	}
	
	public Island getIsland() {
		Level level = getLevel();
		if (level == null) {
			return null;
		}
		
		return level.getIsland();
	}
	
	public World getWorld() {
		Level level = getLevel();
		if (level == null) {
			return null;
		}
		
		return level.getWorld();
	}
	
	void setNid(int nid) {
		this.nid = nid;
	}
	
	int getNid() {
		return nid;
	}

	public Mod getMod() {
		return mod;
	}

	public String getId() {
		return id;
	}
	
	public String getRawId() {
		return id.substring(getMod().getName().length() + 1);
	}

	protected String getPrefix() {
		return "";
	}
	
	public String getResourceId() {
		return "tile." + getMod().getName() + "." + getPrefix() + getRawId();
	}
	
	public TString getName() {
		return name;
	}
	
	public Tile setName(TString name) {
		this.name = name;
		return this;
	}
	
	public Tile setName(String name) {
		if (name == null) {
			setName(TString.translated(getResourceId() + ".name"));
		} else {
			setName(TString.translated(getResourceId() + ".name." + name));
		}
		
		return this;
	}
	
	public Tile setDefaultName() {
		return setName((String) null);
	}
	
	public boolean isTickable() {
		return isTickable;
	}

	public void setTickable(boolean isTickable) {
		this.isTickable = isTickable;
	}
	
	public double getSize() {
		return 1 * Units.METERS;
	}

	@Override
	protected Tile clone() {
		return (Tile) super.clone();
	}
	
	@Override
	public void read(DataInput input, int change) throws IOException, SyntaxException {
		// Do nothing
	}
	
	@Override
	public void write(DataOutput output, int change) throws IOException {
		// Do nothing
	}
	
	public final void render(View view, int x, int y) {
		if (this instanceof Collideable && ModuleClient.DRAW_COLLISION_BOUNDS.get()) {
			Collideable c = (Collideable) this;
			GraphicsInterface.fillRectangle(
					(int) (c.getMinX()*TEXTURE_SIZE),
					(int) (c.getMinY()*TEXTURE_SIZE),
					(int) (c.getWidth()*TEXTURE_SIZE),
					(int) (c.getHeight()*TEXTURE_SIZE),
					COLLISION_BOUNDS_FILL_COLOR);
		}
		
		renderImpl(view, x, y);
	}
	
	protected abstract void renderImpl(View view, int x, int y);
	
	public void tick(World world, Island island, Level level, long length, long time) {
		// Do nothing
	}
	
	protected static ComplexTexture getTextureForTile(Tile tile, int... textureData) {
		return ComplexTexture.get(tile.getResourceId().replace('.', '/'), TEXTURE_SIZE, textureData);
	}

}
