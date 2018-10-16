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
package ru.windcorp.crystalfarm.client;

import static org.lwjgl.opengl.GL11.*;
import static java.lang.Math.*;

import ru.windcorp.crystalfarm.graphics.GraphicsInterface;
import ru.windcorp.crystalfarm.logic.Units;
import ru.windcorp.crystalfarm.logic.ViewTarget;

public class View {
	
	public static final double MAX_SCALE = 4;
	public static final double MIN_SCALE = 1.0/200;
	
	private double scale;

	private double x, y;
	private int minX, maxX, minY, maxY;
	
	private int islandSize;
	
	private ViewTarget target;
	
	public View(int x, int y, double scale) {
		this.x = x;
		this.y = y;
		this.scale = scale;
	}

	public void move(int xMod, int yMod) {
		this.x += xMod;
		this.y += yMod;
	}

	public void zoom(double factor) {
		this.scale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, this.scale*factor));
	}
	
	public void update() {
		if (getTarget() != null) {
			x = getTarget().getViewX();
			y = getTarget().getViewY();
		}
		
		this.x = max(
				(int) ceil(GraphicsInterface.getWindowWidth()/2/scale - Units.PX_PER_TILE/2),
				min(
						getIslandSize() - (int) floor(GraphicsInterface.getWindowWidth()/2/scale + Units.PX_PER_TILE/2),
						this.x));
		
		this.y = max(
				(int) ceil(GraphicsInterface.getWindowHeight()/2/scale) - Units.PX_PER_TILE/2,
				min(
						getIslandSize() - (int) floor(GraphicsInterface.getWindowHeight()/2/scale + Units.PX_PER_TILE/2),
						this.y));
	}
	
	public int getIslandSize() {
		return islandSize;
	}

	public void setIslandSize(int islandSize) {
		this.islandSize = islandSize;
	}

	public void pushMatrix() {
		update();
		
		glPushMatrix();
		glMatrixMode(GL_MODELVIEW);
		glTranslated(GraphicsInterface.getWindowWidth()/2, GraphicsInterface.getWindowHeight()/2, 0);
		if (!ModuleClient.DEBUG_OPTIMIZED_RENDER.get()) glScaled(scale, scale, 0);
		glTranslated(-x, -y, 0);
		
		minX = (int) floor(x - GraphicsInterface.getWindowWidth()/2/scale - 0.5);
		maxX = (int) ceil(x + GraphicsInterface.getWindowWidth()/2/scale - 0.5);
		minY = (int) floor(y - GraphicsInterface.getWindowHeight()/2/scale - 0.5);
		maxY = (int) ceil(y + GraphicsInterface.getWindowHeight()/2/scale - 0.5);
		
		GraphicsInterface.checkOpenGLErrors();
	}
	
	public void popMatrix() {
		if (ModuleClient.DEBUG_OPTIMIZED_RENDER.get()) {
			glBegin(GL_LINE_LOOP);
				glColor3d(0, 0, 0);
				glVertex2d(getMinX(), getMinY());
				glVertex2d(getMinX(), getMaxY());
				glVertex2d(getMaxX(), getMaxY());
				glVertex2d(getMaxX(), getMinY());
			glEnd();
		}
		
		glPopMatrix();
	}

	public int getMinX() {
		return minX;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public ViewTarget getTarget() {
		return target;
	}

	public void setTarget(ViewTarget target) {
		this.target = target;
	}

}
