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
import static ru.windcorp.crystalfarm.graphics.GraphicsInterface.*;

import java.util.function.Consumer;

import ru.windcorp.crystalfarm.audio.Sound;
import ru.windcorp.crystalfarm.audio.SoundManager;
import ru.windcorp.crystalfarm.graphics.Color;
import ru.windcorp.crystalfarm.graphics.fonts.FontString;
import ru.windcorp.crystalfarm.graphics.texture.SimpleTexture;
import ru.windcorp.crystalfarm.graphics.texture.Texture;
import ru.windcorp.crystalfarm.gui.Size;
import ru.windcorp.crystalfarm.translation.TString;

public class Notification {
	
	public static enum Type {
		
		/**
		 * Debug notifications are ignored unless Debug.allowDebug is true
		 */
		DEBUG ("work", "normal", Color.LIGHT_GRAY),
		
		/**
		 * Indicates a neutral notification
		 */
		INFO_NEUTRAL ("neutral", "normal", new Color(0xEE_EE_EE_FF)),
		
		/**
		 * Indicates a positive notification
		 */
		INFO_POSITIVE ("happy", "positive", new Color(0x93_E2_71_FF)),
		
		/**
		 * Indicates an alarm. Nothing 
		 */
		WARNING ("attention", "attention", new Color(0xFF_A0_60_FF)),
		
		/**
		 * Indicates an error message. Something has gone wrong
		 */
		ERROR ("broken", "error", new Color(0xFF_77_77_FF)),
		
		/**
		 * Indicates a question to the user
		 */
		QUESTION ("question", "attention", new Color(0xB5_C0_FF_FF));
		
		private final Texture icon;
		private final Sound sound;
		private final Color color;
		private final Color borderColor;
		
		Type(String iconName, String soundName, Color color) {
			this.icon = SimpleTexture.get("mascot/" + iconName);
			this.sound = SoundManager.get("ui/notification/" + soundName);
			this.color = color;
			this.borderColor = color.clone().multiply(0.75);
		}

		public Texture getIcon() {
			return icon;
		}
		
		public Sound getSound() {
			return sound;
		}

		public Color getColor() {
			return color;
		}

		public Color getBorderColor() {
			return borderColor;
		}
	}
	
	private static final double ACCELERATION = 0.01f;
	private static final double BOUNCINESS = 0.5f;
	
	private final Type type;
	private final boolean isModal;
	
	private FontString label = null;
	private final TString text;
	private final Consumer<?> action;
	
	private NotifierLayer layer;
	
	int x = 0,
		y = 0,
		width = 0,
		height = 0;
	
	private double vx = 0, vy = 0;
	private boolean die = false;
	private boolean soundPlayed = false;
	
	private double hideAt = -1;
	private double shakeAt = -1;
	
	public Notification(Type type, boolean isModal, Consumer<?> action, TString text) {
		this.type = type;
		this.text = text;
		this.isModal = isModal;
		this.action = action;
	}
	
	void show(NotifierLayer layer) {
		this.layer = layer;
		this.label = new FontString(text).setColor(Color.BLACK);
		this.hideAt = ModuleNotifier.SETTING_TIMEOUT.get() * 1000 + time();
		if (isModal()) this.shakeAt = ModuleNotifier.SETTING_SHAKE_INTERVAL.get() * 1000 + time();
		
		getText().addChangeListener(x -> layout());
		layout();
		
		x = -width;
		y = layer.nextY;
		layer.nextY += height;
	}
	
	private void layout() {
		Size textSize = getLabel().getBounds();
		
		width = 5*gdGetLine() + getType().getIcon().getWidth() + textSize.width;
		height = 4*gdGetLine() + Math.max(getType().getIcon().getHeight(), textSize.height);
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean isModal() {
		return isModal;
	}
	
	public FontString getLabel() {
		return label;
	}
	
	public TString getText() {
		return text;
	}
	
	public Consumer<?> getAction() {
		return action;
	}
	
	public boolean hasCursor() {
		return isCursorIn(x, y, width, height);
	}
	
	public void onClicked() {
		if (die) {
			return;
		}
		
		if (getAction() != null) {
			getAction().accept(null);
		}
		
		onKicked();
	}
	
	public void onKicked() {
		if (die) {
			return;
		}
		
		vy = -0.01;
		die = true;
	}
	
	public int render(int targetY) {
		x = Math.min(gdGetLine(), (int) (x + vx * frame()));
		
		if (x == gdGetLine()) {
			if (!soundPlayed) {
				getType().getSound().play(ModuleNotifier.SETTING_ALERT_GAIN.get());
				soundPlayed = true;
			}
			
			if (isModal() && shakeAt < time()) {
				this.shakeAt = ModuleNotifier.SETTING_SHAKE_INTERVAL.get() * 1000 + time();
				if (!hasCursor()) {
					vx = -2;
					soundPlayed = false;
				}
			} else {
				if (vx > 1) {
					vx *= -BOUNCINESS;
				} else {
					vx = 0;
				}
			}
		} else {
			vx += ACCELERATION * frame();
		}
		
		int advance;
		if (die || (!isModal() && hideAt < time())) {
			y -= vy * frame();
			vy += ACCELERATION * frame();
			
			advance = 0;
			
			if (y < -height) {
				layer.hide(this);
			}
			
			die = true;
		} else {
			y = Math.max(targetY, (int) (y - vy * frame()));
			if (y == targetY) {
				vy = 0;
			} else {
				vy += ACCELERATION * frame();
			}
			
			advance = height;
		}
		
		fillRectangle(x, y, width, height,
				getType().getColor(),
				getType().getBorderColor(),
				gdGetLine());
		
		if (hasCursor()) {
			fillRectangle(x, y, width, height, gdGetCoverColor());
		}
		
		getType().getIcon().render(
				x + 2*gdGetLine(),
				y + 2*gdGetLine());
		
		getLabel().render(x + 3*gdGetLine() + getType().getIcon().getWidth(), y + 2*gdGetLine());
		
		return advance;
	}

}
