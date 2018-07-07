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
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;

public abstract class Layer extends Nameable {
	
	protected long lastFrame = -1;

	public Layer(String name) {
		super(name);
	}
	
	protected abstract void renderImpl();
	
	public final void render() {
		try {
			renderImpl();
			lastFrame = GraphicsInterface.getFrame();
		} catch (Exception e) {
			ExecutionReport.reportCriticalError(e, null,
					"Could not render layer %s",
					this);
		}
	}
	
	public void dump(IndentedStringBuilder sb) {
		sb.append("Layer " + toString() + " (" + this.getClass() + ") " + GraphicsInterface.dumpIsRendered(lastFrame));
		sb.breakLine();
	}
	
	public final void show(boolean addToBottom) {
		preShow();
		
		if (this instanceof StickyLayer) {
			Log.debug("Showing sticky layer " + this);
			GraphicsInterface.addStickyLayer(this);
		} else if (addToBottom) {
			Log.debug("Showing bottom layer " + this);
			GraphicsInterface.addLayerToBottom(this);
		} else {
			Log.debug("Showing top layer " + this);
			GraphicsInterface.addLayer(this);
		}
		
		postShow();
	}
	
	protected void preShow() {
		// To be overridden
	}
	
	protected void postShow() {
		// To be overridden
	}
	
	public final void close() {
		preClose();
		Log.debug("Closing layer " + this);
		GraphicsInterface.removeLayer(this);
		postClose();
	}
	
	protected void preClose() {
		// To be overridden
	}
	
	protected void postClose() {
		// To be overridden
	}

}
