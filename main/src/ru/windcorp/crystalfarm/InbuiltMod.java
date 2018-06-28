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
package ru.windcorp.crystalfarm;

import ru.windcorp.crystalfarm.audio.ModuleAudioInterface;
import ru.windcorp.crystalfarm.cfg.ModuleConfiguration;
import ru.windcorp.crystalfarm.client.ModuleClient;
import ru.windcorp.crystalfarm.content.basic.ModuleContentBasic;
import ru.windcorp.crystalfarm.graphics.ModuleGraphicsInterface;
import ru.windcorp.crystalfarm.graphics.notifier.ModuleNotifier;
import ru.windcorp.crystalfarm.struct.mod.Mod;
import ru.windcorp.crystalfarm.struct.mod.ModMeta;
import ru.windcorp.crystalfarm.struct.mod.ModuleModLoader;
import ru.windcorp.crystalfarm.struct.modules.ModuleRegistry;
import ru.windcorp.crystalfarm.translation.ModuleTranslation;

public class InbuiltMod extends Mod {
	
	public static final InbuiltMod INST = new InbuiltMod();

	public InbuiltMod() {
		super(new ModMeta(
				"Inbuilt", "Inbuilt", CrystalFarm.VERSION,
				CrystalFarm.DEVELOPERS,
				CrystalFarm.LICENSE, true,
				null,
				InbuiltMod.class));
	}

	@Override
	public void registerModules() {
		ModuleRegistry.register(new ModuleNotifier());
		ModuleRegistry.register(new ModuleConfiguration());
		ModuleRegistry.register(new ModuleModLoader());
		ModuleRegistry.register(new ModuleAudioInterface());
		ModuleRegistry.register(new ModuleTranslation());
		ModuleRegistry.register(new ModuleGraphicsInterface());
		ModuleRegistry.register(new ModuleClient());
		
		ModuleRegistry.register(new ModuleContentBasic());
	}

}
