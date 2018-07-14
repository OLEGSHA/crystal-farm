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

import java.lang.ref.WeakReference;

import ru.windcorp.crystalfarm.logic.GameManager;
import ru.windcorp.crystalfarm.logic.Island;
import ru.windcorp.crystalfarm.logic.server.PlayerAgent;
import ru.windcorp.crystalfarm.logic.server.PlayerProfile;
import ru.windcorp.crystalfarm.logic.server.Server;

public class LocalClientAgent extends PlayerAgent {
	
	private final Server server;
	private WeakReference<LocalProxy> proxy;

	public LocalClientAgent(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
	}
	
	@Override
	public void setIsland(Island island) {
		super.setIsland(island);
		
		Proxy proxy = getProxy();
		if (proxy != null) {
			proxy.setIsland(island);
		}
	}
	
	@Override
	public void onServerShutdown() {
		if (GameManager.getLocalClient() != null) {
			GameManager.shutdownClient();
		}
	}

	public LocalProxy getProxy() {
		if (proxy == null) {
			return null;
		}
		
		return proxy.get();
	}

	public void setProxy(LocalProxy proxy) {
		this.proxy = new WeakReference<LocalProxy>(proxy);
		if (getIsland() != null) proxy.setIsland(getIsland());
	}

	@Override
	public String getLogin() {
		return "Frozenfield";
	}
	
	@Override
	public void setProfile(PlayerProfile profile) {
		super.setProfile(profile);
		
		Proxy proxy = getProxy();
		if (proxy != null) {
			proxy.getView().setTarget(profile.getEntity());
		}
	}

}
