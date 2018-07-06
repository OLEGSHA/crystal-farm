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

import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.*;
import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.*;

import org.lwjgl.glfw.GLFW;

import ru.windcorp.crystalfarm.gui.listener.ComponentKeyInputListener;
import ru.windcorp.crystalfarm.gui.listener.ComponentScrollInputListener;

public class Scroller extends Component {

	private class ScrollerLayout implements Layout {

		@Override
		public void layout(Component c) {
			c.getChildren().forEach(child -> {
				Size size = child.getPreferredSize();
				
				size.width = Math.max(c.getWidth() - 2*margin*gdGetLine(), size.width);
				size.height = Math.max(c.getHeight() - 2*margin*gdGetLine(), size.height);
				
				child.setBounds(getX() + margin*gdGetLine(), (int) (getY() - position) + margin*gdGetLine(), size);
			});
		}

		@Override
		public Size calculatePreferredSize(Component c) {
			Size result = new Size(0, gdGetLine());
			c.getChildren().forEach(child -> {
				Size size = child.getPreferredSize();
				result.width = Math.max(result.width, size.width);
				result.height = Math.max(result.height, size.height);
			});
			
			result.width += 2 * margin*gdGetLine();
			result.height += 2 * margin*gdGetLine();
			
			return result;
		}

	}
	
	private static final int SCROLL_AMOUNT = 30;
	
	private double position = 0;
	private int margin;

	public Scroller(int margin, Component child) {
		super(child.getName() + ".scroller");
		this.margin = margin;
		setLayout(new ScrollerLayout());
		super.addChild(child, 0);
		
		addInputListener((ComponentScrollInputListener) (comp, input) -> {
			scroll(input.getAmount() * -SCROLL_AMOUNT);
			invalidate();
			input.consume();
		});
		
		addInputListener((ComponentKeyInputListener) (comp, input) -> {
			if (input.isReleased()) {
				return;
			}
			
			if (input.getKey() == GLFW.GLFW_KEY_UP) {
				scroll(-SCROLL_AMOUNT);
				invalidate();
				input.consume();
			}
			
			else if (input.getKey() == GLFW.GLFW_KEY_PAGE_UP) {
				scroll(-(getHeight() - 2*margin*gdGetLine()));
				invalidate();
				input.consume();
			}
			
			else if (input.getKey() == GLFW.GLFW_KEY_HOME) {
				scroll(Integer.MIN_VALUE);
				invalidate();
				input.consume();
			}
			
			else if (input.getKey() == GLFW.GLFW_KEY_DOWN) {
				scroll(SCROLL_AMOUNT);
				invalidate();
				input.consume();
			}
			
			else if (input.getKey() == GLFW.GLFW_KEY_PAGE_DOWN) {
				scroll(getHeight() - 2*margin*gdGetLine());
				invalidate();
				input.consume();
			}
			
			else if (input.getKey() == GLFW.GLFW_KEY_END) {
				scroll(Integer.MAX_VALUE);
				invalidate();
				input.consume();
			}
		});
	}
	
	public Scroller(Component child) {
		this(0, child);
	}
	
	public Component getContents() {
		return getChild(0);
	}
	
	public void scroll(int mod) {
		position = Math.max(0, Math.min(getContents().getHeight() - getHeight(), position + mod));
	}
	
	@Override
	public Component addChild(Component child, int index) {
		throw new UnsupportedOperationException("Cannot add child to scroller");
	}
	
	@Override
	public Component removeChild(Component child) {
		throw new UnsupportedOperationException("Cannot remove child from scroller");
	}
	
	@Override
	protected synchronized void layoutSelf() {
		scroll(0);
		super.layoutSelf();
	}
	
	@Override
	protected void renderChildren() {
		try {
			setMask(
					getX() + margin*gdGetLine(),
					getY() + margin*gdGetLine(),
					getWidth() - 2*margin*gdGetLine(),
					getHeight() - 2*margin*gdGetLine());
			super.renderChildren();
		} finally {
			resetMask();
		}
	}
	
	@Override
	protected void postRenderSelf() {
		if (getContents().getHeight() > getHeight()) {
			fillRectangle(
					getX() + getWidth() - gdGetLine(),
					getY() + (int) Math.round(getHeight() * position / getContents().getHeight()),
					gdGetLine(),
					(int) Math.round(getHeight() * (double) getHeight() / getContents().getHeight()),
					gdGetForegroundColor());
		}
	}

}
