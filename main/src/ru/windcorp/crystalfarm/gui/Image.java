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
package ru.windcorp.crystalfarm.gui;

import ru.windcorp.crystalfarm.graphics.texture.Texture;

public class Image extends Component {

	private Texture texture = null;
	
	public Image(String name, Texture texture) {
		super(name);
		setTexture(texture);
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public Image setTexture(Texture texture) {
		this.texture = texture;
		if (texture != null) {
			setPreferredSize(texture.getWidth(), texture.getHeight());
		}
		
		return this;
	}
	
	@Override
	protected void renderSelf() {
		if (getTexture() == null) {
			return;
		}
		
		texture.render(
				getX() + (getWidth() - getTexture().getWidth())/2,
				getY() + (getHeight() - getTexture().getHeight())/2);
	}

}
