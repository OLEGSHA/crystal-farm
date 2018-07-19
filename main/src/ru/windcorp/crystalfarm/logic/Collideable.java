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

public interface Collideable {
	
	double getCollisionMinX();
	double getCollisionMinY();
	
	default boolean canCollide() {
		return true;
	}
	
	default double getCollisionMaxX() {
		return getCollisionMinX() + getCollisionWidth();
	}
	
	default double getCollisionMaxY() {
		return getCollisionMinY() + getCollisionHeight();
	}
	
	default double getCollisionCenterX() {
		return (getCollisionMinX() + getCollisionMaxX()) / 2;
	}
	
	default double getCollisionCenterY() {
		return (getCollisionMinY() + getCollisionMaxY()) / 2;
	}
	
	default double getCollisionWidth() {
		return getCollisionMaxX() - getCollisionMinX();
	}
	
	default double getCollisionHeight() {
		return getCollisionMaxY() - getCollisionMinY();
	}
	
	default boolean collides(Collideable other) {
		if (other == this) {
			return false;
		}
		
		if (
				getCollisionMinX() >= other.getCollisionMaxX() ||
				getCollisionMinY() >= other.getCollisionMaxY() ||
				getCollisionMaxX() <= other.getCollisionMinX() ||
				getCollisionMaxY() <= other.getCollisionMinY()) {
			return false;
		}
		
		return true;
	}

}
