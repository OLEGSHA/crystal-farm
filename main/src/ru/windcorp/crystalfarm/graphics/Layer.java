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
package ru.windcorp.crystalfarm.graphics;

import ru.windcorp.tge2.util.IndentedStringBuilder;
import ru.windcorp.tge2.util.Nameable;

public abstract class Layer extends Nameable {

	public Layer(String name) {
		super(name);
	}
	
	public abstract void render();
	
	public void dump(IndentedStringBuilder sb) {
		sb.append("Layer " + toString() + " (" + this.getClass() + ")");
		sb.breakLine();
	}
	
	public void show() {
		GraphicsInterface.addLayer(this);
	}
	
	public void close() {
		GraphicsInterface.removeLayer(this);
	}

}
