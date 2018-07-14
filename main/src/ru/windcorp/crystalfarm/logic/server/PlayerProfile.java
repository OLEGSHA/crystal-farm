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
package ru.windcorp.crystalfarm.logic.server;

import java.io.IOException;
import java.lang.ref.WeakReference;

import ru.windcorp.crystalfarm.content.basic.entity.EntityLevel;
import ru.windcorp.crystalfarm.content.basic.entity.PlayerEntity;
import ru.windcorp.crystalfarm.logic.GameManager;
import ru.windcorp.tge2.util.NumberUtil;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.stream.CountingDataInput;
import ru.windcorp.tge2.util.stream.CountingDataOutput;

public class PlayerProfile {
	
	private String login;
	private final WeakReference<World> world;
	
	private long statId;
	private String island;
	
	private PlayerEntity entity;
	
	public PlayerProfile(String login, World world) {
		this.login = login;
		this.world = new WeakReference<>(world);
	}
	
	public boolean hasEntity() {
		return statId != -1;
	}

	public PlayerEntity getEntity() {
		if (statId == -1) {
			return null;
		}
		
		if (entity == null) {
			try {
				entity = (PlayerEntity) world.get()
						.getIsland(island)
						.getLevel("Inbuilt:EntityLevel", EntityLevel.class)
						.getTileByStatId(statId);
			} catch (NullPointerException | ClassCastException e) {
				GameManager.shutdownLocalServer(e, "Could not find player entity for login %s in %s->%s",
						getLogin(), island, new String(NumberUtil.toFullHex(statId)));
			}
		}
		
		return entity;
	}

	public void setEntity(PlayerEntity entity) {
		if (entity == null) {
			this.entity = null;
			this.island = "";
			this.statId = -1;
		} else {
			entity.setLogin(getLogin());
			this.entity = entity;
			this.island = entity.getIsland().getName();
			this.statId = entity.getStatId();
		}
	}

	public String getLogin() {
		return login;
	}
	
	public void read(CountingDataInput input) throws IOException, SyntaxException {
		island = input.readUTF();
		statId = input.readLong();
	}
	
	public void write(CountingDataOutput output) throws IOException {
		output.writeUTF(island);
		output.writeLong(statId);
	}

}
