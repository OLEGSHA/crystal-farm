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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import ru.windcorp.crystalfarm.graphics.notifier.Notification.Type;

public class Notifier {

	private static NotifierLayer layer = new NotifierLayer();
	
	private static final Queue<Notification> QUEUE = new ConcurrentLinkedQueue<>();

	public static NotifierLayer getLayer() {
		return layer;
	}

	static void setLayer(NotifierLayer layer) {
		Notifier.layer = layer;
	}
	
	public static void postNotification(Notification notification) {
		if (getLayer() != null) {
			getLayer().show(notification);
		} else {
			synchronized (QUEUE) {
				QUEUE.add(notification);
			}
		}
	}
	
	public static void debug(String key, Object... args) {
		postNotification(new Notification(Type.DEBUG, false, null, key, args));
	}
	
	public static void info(String key, Object... args) {
		postNotification(new Notification(Type.INFO_NEUTRAL, false, null, key, args));
	}
	
	public static void positive(String key, Object... args) {
		postNotification(new Notification(Type.INFO_POSITIVE, false, null, key, args));
	}
	
	public static void warn(String key, Object... args) {
		postNotification(new Notification(Type.WARNING, false, null, key, args));
	}
	
	public static void error(String key, Object... args) {
		postNotification(new Notification(Type.ERROR, false, null, key, args));
	}
	
	static void postQueuedNotifications() {
		synchronized (QUEUE) {
			QUEUE.forEach(notification -> getLayer().show(notification));
		}
	}
	
}
