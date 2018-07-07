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
	
	double getMinX();
	double getMinY();
	
	default boolean canCollide() {
		return true;
	}
	
	default double getMaxX() {
		return getMinX() + getWidth();
	}
	
	default double getMaxY() {
		return getMinY() + getHeight();
	}
	
	default double getCenterX() {
		return (getMinX() + getMaxX()) / 2;
	}
	
	default double getCenterY() {
		return (getMinY() + getMaxY()) / 2;
	}
	
	default double getWidth() {
		return getMaxX() - getMinX();
	}
	
	default double getHeight() {
		return getMaxY() - getMinY();
	}
	
	default boolean collides(Collideable other) {
		if (other == this) {
			return false;
		}
		
		if (
				getMinX() >= other.getMaxX() ||
				getMinY() >= other.getMaxY() ||
				getMaxX() <= other.getMinX() ||
				getMaxY() <= other.getMinY()) {
			return false;
		}
		
		return true;
	}
	
	default void pushOutside(DynamicCollideable other) {
		if (collides(other)) {
			double dX = other.getCenterX() - getCenterX();
			double dY = other.getCenterY() - getCenterY();
			double k = getHeight() / getWidth();
			
			if (dY > k*dX) {
				if (dY > -k*dX) {
					other.setXY(other.getMinX(), getMaxY());
				} else {
					other.setXY(getMinX() - other.getWidth(), other.getMinY());
				}
			} else {
				if (dY > -k*dX) {
					other.setXY(getMaxX(), other.getMinY());
				} else {
					other.setXY(other.getMinX(), getMinY() - other.getHeight());
				}
			}
		}
	}

}
