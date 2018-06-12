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
package ru.windcorp.crystalfarm.graphics.notifier;

import static ru.windcorp.crystalfarm.graphics.GraphicsDesign.*;

import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;

import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.graphics.GraphicsInterface;
import ru.windcorp.crystalfarm.graphics.fonts.GString;
import ru.windcorp.crystalfarm.graphics.texture.SimpleTexture;
import ru.windcorp.crystalfarm.graphics.texture.Texture;
import ru.windcorp.crystalfarm.gui.Size;

public class Notification {
	
	public static enum Type {
		
		/**
		 * Debug notifications are ignored unless Debug.allowDebug is true
		 */
		DEBUG ("debug", Color.LIGHT_GRAY),
		
		/**
		 * Indicates a neutral notification
		 */
		INFO_NEUTRAL ("neutral", Color.LIGHT_GRAY),
		
		/**
		 * Indicates a positive notification
		 */
		INFO_POSITIVE ("positive", new Color(0x93_E2_71_FF)),
		
		/**
		 * Indicates an alarm. Nothing 
		 */
		WARNING ("warning", new Color(0xFF_A0_60_FF)),
		
		/**
		 * Indicates an error message. Something has gone wrong
		 */
		ERROR ("error", new Color(0xFF_77_77_FF)),
		
		/**
		 * Indicates a question to the user
		 */
		QUESTION ("question", new Color(0xB5_C0_FF_FF));
		
		private final Texture icon;
		private final Color color;
		private final Color borderColor;
		
		Type(String iconName, Color color) {
			this.icon = SimpleTexture.get("load/loadThread.work"/*"notification/" + iconName*/);
			this.color = color;
			this.borderColor = color.clone().multiply(0.75);
		}

		public Texture getIcon() {
			return icon;
		}

		public Color getColor() {
			return color;
		}

		public Color getBorderColor() {
			return borderColor;
		}
	}
	
	private final Type type;
	private final boolean isModal;
	
	private GString label;
	private final String key;
	private final Object[] params;
	private final Consumer<?> action;
	
	private NotifierLayer layer;
	
	private int x = 0,
			y = 0,
			width = 0,
			height = 0;
	
	private float vx = 0, vy = 0;
	
	private int liveTimer = ModuleNotifier.SETTING_LIVE_TIMER.get() * 1000;
	
	public Notification(Type type, boolean isModal, Consumer<?> action, String key, Object... params) {
		this.type = type;
		this.key = key;
		this.params = params;
		this.isModal = isModal;
		this.action = action;
	}
	
	void show(NotifierLayer layer) {
		this.layer = layer;
		this.label = new GString(key, params).setColor(Color.BLACK);
		
		Size textSize = getText().getBounds();
		
		width = 5*LINE_THICKNESS + getType().getIcon().getWidth() + textSize.width;
		height = 4*LINE_THICKNESS + Math.max(getType().getIcon().getHeight(), textSize.height);
		
		x = -width;
		y = layer.nextY;
		
		layer.nextY += height;
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean isModal() {
		return isModal;
	}
	
	public GString getText() {
		return label;
	}
	
	public Consumer<?> getAction() {
		return action;
	}
	
	public void render() {
		x = Math.min(0, x + (int) vx);
		
		if (x == 0) {
			vx *= -0.5f;
		} else {
			vx += 0.5;
		}
		
//		if (isModal()) {
//			if (liveTimer > 0) {
//				liveTimer -= GLFW.glfwGetTimerValue() * 1000 / GLFW.glfwGetTimerFrequency();
//			} else {
//				y = (int) (y - vy);
//				vy += 0.5;
//				
//				if (y < -height) {
//					layer.hide(this);
//				}
//			}
//		}
		
		GraphicsInterface.fillRectangle(x, y, width, height,
				getType().getColor(),
				getType().getBorderColor(),
				LINE_THICKNESS);
		
		GraphicsInterface.drawTexture(
				x + 2*LINE_THICKNESS,
				y + 2*LINE_THICKNESS,
				getType().getIcon());
		
		getText().render(x + 3*LINE_THICKNESS + getType().getIcon().getWidth(), y + 2*LINE_THICKNESS);
	}

}
