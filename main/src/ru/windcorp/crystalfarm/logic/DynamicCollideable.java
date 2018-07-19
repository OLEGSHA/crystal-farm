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

public interface DynamicCollideable extends Collideable {

	void setCollisionMinXY(double x, double y);
	
	default void pushOutside(Collideable obstacle) {
		if (obstacle.collides(this)) {
			double dX = getCollisionCenterX() - obstacle.getCollisionCenterX();
			double dY = getCollisionCenterY() - obstacle.getCollisionCenterY();
			double k = obstacle.getCollisionHeight() / obstacle.getCollisionWidth();
			
			if (dY > k*dX) {
				if (dY > -k*dX) {
					setCollisionMinXY(getCollisionMinX(), obstacle.getCollisionMaxY());
				} else {
					setCollisionMinXY(obstacle.getCollisionMinX() - getCollisionWidth(), getCollisionMinY());
				}
			} else {
				if (dY > -k*dX) {
					setCollisionMinXY(obstacle.getCollisionMaxX(), getCollisionMinY());
				} else {
					setCollisionMinXY(getCollisionMinX(), obstacle.getCollisionMinY() - getCollisionHeight());
				}
			}
		}
	}
	
}
