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
import ru.windcorp.crystalfarm.content.basic.test.*;
import ru.windcorp.crystalfarm.logic.BiomeRegistry;
import ru.windcorp.crystalfarm.logic.DynamicTile;
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
		//IslandFactory.registerIslandLevelProvider(new BasicLevelProvider());
		TileRegistry<GroundTile> groundLevelRegistry = new TileRegistry<>(InbuiltMod.INST, "GroundLevel", GroundTile.class);
		TileRegistries.register(groundLevelRegistry);
		
		/*
		 * Test
		 */
		ActionRegistry.IN_GAME.register(
				new PauseAction(),
				new UnpauseAction(),
				new OpenPauseMenuAction(),
				
				new TestCharControl("Up",		"UP",		 0,		-0.003),
				new TestCharControl("Down", 	"DOWN",		 0,		+0.003),
				new TestCharControl("Left", 	"LEFT",		-0.003,		 0),
				new TestCharControl("Right", 	"RIGHT",	+0.003,		 0),
				
				new TestBadaboomControl(),
				
				new TestCameraMoveControl("Up",		  0, -40, "PRESS W"),
				new TestCameraMoveControl("Down",	  0, +40, "PRESS S"),
				new TestCameraMoveControl("Left",	-40,   0, "PRESS A"),
				new TestCameraMoveControl("Right",	+40,   0, "PRESS D"),
				
				new TestCameraZoomControl("In",    2, "PRESS EQUAL"),
				new TestCameraZoomControl("Out", 0.5, "PRESS MINUS")
				);
		
		TileRegistry<TestTile> testLevelRegistry = new TileRegistry<>(InbuiltMod.INST, "TestLevel", TestTile.class);
		testLevelRegistry.register(new TestTile());
		TileRegistries.register(testLevelRegistry);
		
		TileRegistry<DynamicTile> testDynLevelRegistry = new TileRegistry<>(InbuiltMod.INST, "TestDynLevel", DynamicTile.class);
		testDynLevelRegistry.register(new TestCharTile());
		TileRegistries.register(testDynLevelRegistry);
		
		TileRegistry<GridTile> treeLevelRegistry = new TileRegistry<>(InbuiltMod.INST, "TreeLevel", GridTile.class);
		treeLevelRegistry.register(new TestTreeTile());
		TileRegistries.register(treeLevelRegistry);
		
		IslandFactory.registerIslandLevelProvider(new TestLevelProvider());
		BiomeRegistry.register(new TestBiome());
	}

}
