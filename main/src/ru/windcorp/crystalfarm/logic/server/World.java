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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import ru.windcorp.crystalfarm.content.basic.entity.EntityLevel;
import ru.windcorp.crystalfarm.content.basic.entity.PlayerEntity;
import ru.windcorp.crystalfarm.logic.Data;
import ru.windcorp.crystalfarm.logic.Island;
import ru.windcorp.crystalfarm.logic.IslandFactory;
import ru.windcorp.crystalfarm.logic.exception.UnknownVersionException;
import ru.windcorp.crystalfarm.logic.exception.UnknownWorldDataException;
import ru.windcorp.crystalfarm.logic.exception.WrongMagicValueException;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.grh.Resource;
import ru.windcorp.tge2.util.stream.CountingDataInput;
import ru.windcorp.tge2.util.stream.CountingDataOutput;

public class World {
	
	public static final long MAGIC_VALUE = 0xEDA_ACE_FEED_CABl;
	public static final byte VERSION_EARTH_PONY = 0;
	
	private final Resource resource;
	
	private final WorldMeta meta = new WorldMeta();
	private final Collection<Island> islands = Collections.synchronizedCollection(new ArrayList<>());
	private final Map<String, Data> data = Collections.synchronizedMap(new HashMap<>());
	private final Map<String, PlayerProfile> players = Collections.synchronizedMap(new HashMap<>());
	
	protected World(Resource resource) {
		this.resource = resource;
	}
	
	public WorldMeta getMeta() {
		return meta;
	}
	
	public Map<String, Data> getData() {
		return data;
	}
	
	public Data getData(String name) {
		return getData().get(name);
	}
	
	public void addData(Collection<Data> data) {
		data.forEach(datum -> getData().put(datum.getId(), datum));
	}
	
	public Collection<Island> getIslands() {
		return islands;
	}
	
	public Island getIsland(String name) {
		synchronized (getIslands()) {
			for (Island island : getIslands()) {
				if (island.getName().equals(name)) {
					return island;
				}
			}
		}
		
		return null;
	}
	
	public void addIsland(Island island) {
		island.setWorld(this);
		getIslands().add(island);
	}
	
	public Map<String, PlayerProfile> getPlayerProfiles() {
		return players;
	}
	
	public Stream<PlayerEntity> getPlayers() {
		return getPlayerProfiles().values().stream().map(profile -> profile.getEntity()).filter(entity -> entity != null);
	}

	public PlayerProfile getPlayerProfile(String login) {
		return getPlayerProfiles().get(login);
	}
	
	public PlayerEntity getPlayer(String login) {
		PlayerProfile profile = getPlayerProfile(login);
		
		if (profile == null) {
			return null;
		}
		
		return profile.getEntity();
	}
	
	public PlayerProfile addPlayer(String login) {
		PlayerProfile profile = new PlayerProfile(login, this);
		getPlayerProfiles().put(login, profile);
		return profile;
	}

	public void spawnPlayer(Island island, PlayerProfile profile) {
		if (profile.hasEntity()) {
			return;
		}
		
		PlayerEntity entity = new PlayerEntity();
		island.getLevel("Inbuilt:EntityLevel", EntityLevel.class).spawnPlayer(entity);
		
		entity.move(island.getSize() / 2, island.getSize() / 2);

		profile.setEntity(entity);
	}
	
	public long getTime() {
		return getMeta().getTime();
	}
	
	public void read(InputStream is) throws IOException, SyntaxException {
		CountingDataInput input = new CountingDataInput(is);
		
		readMeta(input, getMeta());
		
		int amount;
		
		amount = input.readInt();
		if (amount < 0) {
			throw new SyntaxException("Data amount is negative (0x" + Integer.toHexString(amount) + ")");
		}
		
		synchronized (getData()) {
			for (int i = 0; i < amount; ++i) {
				String name = input.readUTF();
				Data data = getData(name);
				if (data == null) {
					throw new UnknownWorldDataException("Data with name \"" + name + "\" has not been registered", name, this);
				}
				
				input.pushCounter();
				data.readAll(input);
				
				int read = (int) input.popCounter();
				int length = input.readInt();
				if (read != length) {
					throw new SyntaxException("Island Data " + name + " corrupted: expected length 0x" + Integer.toHexString(length) + ", got length 0x" + Integer.toHexString(read));
				}
			}
		}
		
		amount = input.readInt();
		if (amount < 0) {
			throw new SyntaxException("Player amount is negative (0x" + Integer.toHexString(amount) + ")");
		}
		
		synchronized (getPlayerProfiles()) {
			for (int i = 0; i < amount; ++i) {
				String login = input.readUTF();
				PlayerProfile profile = addPlayer(login);
				profile.read(input);
			}
		}
		
		amount = input.readInt();
		if (amount < 0) {
			throw new SyntaxException("Island amount is negative (0x" + Integer.toHexString(amount) + ")");
		}
		
		synchronized (getIslands()) {
			for (int i = 0; i < amount; ++i) {
				String name = input.readUTF();
				int size = input.readInt();
				if (size < 0) {
					throw new SyntaxException("Island size is negative (0x" + Integer.toHexString(size) + ")");
				}
				
				Island island = IslandFactory.createIsland(name, size);
				addIsland(island);
				
				input.pushCounter();
				island.read(input);
				
				int read = (int) input.popCounter();
				int length = input.readInt();
				if (read != length) {
					throw new SyntaxException("Island " + name + " corrupted: expected length 0x" + Integer.toHexString(length) + ", got length 0x" + Integer.toHexString(read));
				}
			}
		}
	}
	
	public static WorldMeta readMeta(InputStream is) throws IOException, SyntaxException {
		WorldMeta result = new WorldMeta();
		readMeta(new DataInputStream(is), result);
		return result;
	}
	
	private static void readMeta(DataInput input, WorldMeta meta) throws IOException, SyntaxException {
		long presentMagicValue = input.readLong();
		if (presentMagicValue != MAGIC_VALUE) {
			throw new WrongMagicValueException("Wrong magic value, probably not a save file. Expected 0x"
					+ Long.toHexString(MAGIC_VALUE) + ", got 0x" + Long.toHexString(presentMagicValue),
					presentMagicValue,
					MAGIC_VALUE);
		}
		
		byte version = input.readByte();
		if (version != VERSION_EARTH_PONY) {
			throw new UnknownVersionException("Unknown version 0x" + Integer.toHexString(version & 0xFF) +
					", 0x" + Integer.toHexString(VERSION_EARTH_PONY) + " (Earth Pony) known",
					version);
		}
		
		meta.readAll(input);
	}
	
	public void write(OutputStream os) throws IOException {
		Log.topic("Write World");
		Log.debug("Started writing world");
		
		try {
			CountingDataOutput output = new CountingDataOutput(os);
			
			Log.topic("Header & Meta");
			Log.debug("Writing header and meta");
			try {
				output.writeLong(MAGIC_VALUE);
				output.writeByte(VERSION_EARTH_PONY);
				getMeta().writeAll(output);
			} finally {
				Log.end("Header & Meta");
			}
			
			Log.topic("Data");
			Log.debug("Writing data");
			try {
				synchronized (getData()) {
					output.writeInt(getData().size());
					
					for (Data data : getData().values()) {
						output.writeUTF(data.getId());
						
						output.pushCounter();
						data.writeAll(output);
						output.writeInt((int) output.popCounter());
					}
				}
			} finally {
				Log.end("Data");
			}
			
			Log.topic("Profiles");
			Log.debug("Writing player profiles");
			
			try {
				synchronized (getPlayerProfiles()) {
					output.writeInt(getPlayerProfiles().size());
					
					for (PlayerProfile profile : getPlayerProfiles().values()) {
						output.writeUTF(profile.getLogin());
						profile.write(output);
					}
				}
			} finally {
				Log.end("Profiles");
			}
			
			Log.topic("Islands");
			Log.debug("Writing islands");
			
			try {
				synchronized (getIslands()) {
					output.writeInt(getIslands().size());
					
					for (Island island : getIslands()) {
						Log.topic(island.getName());
						Log.debug("Writing island " + island.getName());
						try {
							output.writeUTF(island.getName());
							output.writeInt(island.getSize());
							
							output.pushCounter();
							island.write(output);
							output.writeInt((int) output.popCounter());
						} finally {
							Log.end(island.getName());
						}
					}
				}
			} finally {
				Log.end("Islands");
			}
			
			Log.debug("Writing world done");
		} finally {
			Log.end("Write World");
		}
	}

	public Resource getResource() {
		return resource;
	}
	
	public void load() throws IOException, SyntaxException {
		getResource().checkRead(problem -> { throw new IOException(problem); });
		BufferedInputStream bis = new BufferedInputStream(getResource().getInputStream());
		read(bis);
		bis.close();
	}
	
	public void save() throws IOException {
		getResource().checkWrite(problem -> { throw new IOException(problem); });
		BufferedOutputStream bos = new BufferedOutputStream(getResource().getOutputStream());
		write(bos);
		bos.flush();
		bos.close();
	}

}