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
package ru.windcorp.crystalfarm.content.basic;

import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.content.basic.controls.PlayerMoveControl;
import ru.windcorp.crystalfarm.content.basic.entity.EntityTile;
import ru.windcorp.crystalfarm.content.basic.entity.PlayerEntity;
import ru.windcorp.crystalfarm.content.basic.floor.*;
import ru.windcorp.crystalfarm.content.basic.ground.*;
import ru.windcorp.crystalfarm.content.basic.object.*;
import ru.windcorp.crystalfarm.content.basic.test.*;
import ru.windcorp.crystalfarm.logic.BiomeRegistry;
import ru.windcorp.crystalfarm.logic.GridTile;
import ru.windcorp.crystalfarm.logic.IslandFactory;
import ru.windcorp.crystalfarm.logic.TileRegistries;
import ru.windcorp.crystalfarm.logic.TileRegistry;
import ru.windcorp.crystalfarm.logic.action.ActionRegistry;
import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;

public class JobRegisterContentBasic extends ModuleJob {

	public JobRegisterContentBasic(Module module) {
		super("RegisterContentBasic", "Registers basic content", module);
	}

	@Override
	protected void runImpl() {
		TileRegistry<GroundTile> groundLevelRegistry = new TileRegistry<>(InbuiltMod.INST, "GroundLevel", GroundTile.class);
		TileRegistries.register(groundLevelRegistry);
		
		TileRegistry<FloorTile> floorLevelRegistry = new TileRegistry<>(InbuiltMod.INST, "FloorLevel", FloorTile.class);
		TileRegistries.register(floorLevelRegistry);
		
		TileRegistry<ObjectTile> objectLevelRegistry = new TileRegistry<>(InbuiltMod.INST, "ObjectLevel", ObjectTile.class);
		TileRegistries.register(objectLevelRegistry);
		
		registerEntityLevel();
		
		/*
		 * Test
		 */
		ActionRegistry.IN_GAME.register(
				new PauseAction(),
				new UnpauseAction(),
				new OpenPauseMenuAction(),
				
				new TestBadaboomControl(),
				
				new PlayerMoveControl(PlayerMoveControl.NORTH),
				new PlayerMoveControl(PlayerMoveControl.EAST),
				new PlayerMoveControl(PlayerMoveControl.SOUTH),
				new PlayerMoveControl(PlayerMoveControl.WEST),
				
				new TestCameraMoveControl("Up",		  0, -40, "PRESS UP"),
				new TestCameraMoveControl("Down",	  0, +40, "PRESS DOWN"),
				new TestCameraMoveControl("Left",	-40,   0, "PRESS LEFT"),
				new TestCameraMoveControl("Right",	+40,   0, "PRESS RIGHT"),
				
				new TestCameraZoomControl("In",    1.25, "PRESS EQUAL"),
				new TestCameraZoomControl("Out", 1/1.25, "PRESS MINUS")
				);
		
		TileRegistry<TestTile> testLevelRegistry = new TileRegistry<>(InbuiltMod.INST, "TestLevel", TestTile.class);
		testLevelRegistry.register(new TestTile());
		TileRegistries.register(testLevelRegistry);
		
		TileRegistry<GridTile> treeLevelRegistry = new TileRegistry<>(InbuiltMod.INST, "TreeLevel", GridTile.class);
		treeLevelRegistry.register(new TestTreeTile());
		TileRegistries.register(treeLevelRegistry);
		
		IslandFactory.registerIslandLevelProvider(new TestLevelProvider());
		BiomeRegistry.register(new TestBiome());

		IslandFactory.registerIslandLevelProvider(new BasicLevelProvider());
	}

	private void registerEntityLevel() {
		TileRegistry<EntityTile> reg = new TileRegistry<>(InbuiltMod.INST, "EntityLevel", EntityTile.class);
		TileRegistries.register(reg);
		
		reg.register(new PlayerEntity());
	}

}
