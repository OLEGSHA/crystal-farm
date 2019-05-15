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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.ref.WeakReference;

import ru.windcorp.crystalfarm.client.ModuleClient;
import ru.windcorp.crystalfarm.client.View;
import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.graphics.GraphicsInterface;
import ru.windcorp.crystalfarm.graphics.texture.ComplexTexture;
import ru.windcorp.crystalfarm.logic.server.World;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.crystalfarm.translation.TString;
import ru.windcorp.tge2.util.exceptions.SyntaxException;

public abstract class Tile extends Updateable implements ViewTarget, Collideable {
	
	private static final Color COLLISION_BOUNDS_FILL_COLOR = new Color(0xFFFFFF77);
	
	private final Mod mod;
	private final String id;
	private TString name = null;
	
	private WeakReference<TileLevel<?>> level = null;
	private int nid;
	
	private double x, y;
	
	private boolean isTickable = false;
	private boolean canCollide = false;

	public Tile(Mod mod, String id) {
		this.mod = mod;
		this.id = mod.getName() + ":" + id;
	}
	
	protected Tile(Mod mod, String id, String prefix) {
		this(mod, id);
	}
	
	protected void setLevel(TileLevel<?> level) {
		if (level == null) {
			if (canCollide()) {
				TileLevel<?> prevLevel = getLevel();
				if (prevLevel != null) {
					prevLevel.getCollideables().remove(this);
				}
			}
				
			this.level = null;
			return;
		}
		
		this.level = new WeakReference<TileLevel<?>>(level);

		if (isTickable()) {
			level.getTickableTiles().add(this);
		}
		
		if (this.canCollide()) {
			level.getCollideables().add(this);
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
	
	@Override
	public boolean canCollide() {
		return canCollide;
	}
	
	public void setCanCollide(boolean canCollide) {
		if (canCollide == this.canCollide) {
			return;
		}
		
		this.canCollide = canCollide;
		onUpdateCanCollide();
	}
	
	protected void onUpdateCanCollide() {
		TileLevel<?> level = getLevel();
		if (level != null) {
			if (canCollide()) {
				level.getCollideables().add(this);
			} else {
				level.getCollideables().remove(this);
			}
		}
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	protected synchronized void setXY(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public double getCollisionMinX() {
		return getX() - getCollisionWidth() / 2;
	}
	
	@Override
	public double getCollisionMinY() {
		return getY() - getCollisionHeight() / 2;
	}
	
	@Override
	public double getCollisionWidth() {
		return getSize();
	}
	
	@Override
	public double getCollisionHeight() {
		return getSize();
	}
	
	@Override
	public double getViewX() {
		return getX() * Units.PX_PER_TILE;
	}
	
	@Override
	public double getViewY() {
		return getY() * Units.PX_PER_TILE;
	}
	
	protected double getTextureSize() {
		return 1;
	}
	
	protected int getTextureX() {
		return (int) ((getX() - getTextureSize() / 2) * Units.PX_PER_TILE);
	}
	
	protected int getTextureY() {
		return (int) ((getY() - getTextureSize() / 2) * Units.PX_PER_TILE);
	}
	
	public boolean isVisible(View view) {
		double x = getTextureX();
		double y = getTextureY();
		
		return
				x + getTextureSize() * Units.PX_PER_TILE	>= view.getMinX() &&
				x											<= view.getMaxX() &&
				y + getTextureSize() * Units.PX_PER_TILE	>= view.getMinY() &&
				y											<= view.getMaxY();
	}
	
	public double getSize() {
		return 1 * Units.METERS;
	}
	
	public double getMass() {
		return 80 * Units.KILOGRAMS;
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
	
	public final void render(View view) {
		if (ModuleClient.DRAW_COLLISION_BOUNDS.get() && canCollide()) {
			GraphicsInterface.fillRectangle(
					(int) (getCollisionMinX()	* Units.PX_PER_TILE),
					(int) (getCollisionMinY()	* Units.PX_PER_TILE),
					(int) (getCollisionWidth()	* Units.PX_PER_TILE),
					(int) (getCollisionHeight()	* Units.PX_PER_TILE),
					COLLISION_BOUNDS_FILL_COLOR);
		}
		
		renderImpl(view);
		
		if (ModuleClient.DEBUG_OPTIMIZED_RENDER.get()) {
			GraphicsInterface.fillRectangle(
					(int) (getX()	* Units.PX_PER_TILE) - 1,
					(int) (getY()	* Units.PX_PER_TILE) - 1,
					2,
					2,
					Color.BLUE);
			
			GraphicsInterface.fillRectangle(
					(int) (getViewX()	* Units.PX_PER_TILE) - 1,
					(int) (getViewY()	* Units.PX_PER_TILE) - 1,
					2,
					2,
					Color.BLACK);
		}
	}
	
	protected abstract void renderImpl(View view);
	
	public void tick(World world, Island island, Level level, long length, long time) {
		// Do nothing
	}
	
	protected static ComplexTexture getTextureForTile(Tile tile, int... textureData) {
		return ComplexTexture.get(tile.getResourceId().replace('.', '/'), Units.PX_PER_TILE, textureData);
	}

}
