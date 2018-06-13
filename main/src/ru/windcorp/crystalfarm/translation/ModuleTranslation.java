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
package ru.windcorp.crystalfarm.translation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import ru.windcorp.crystalfarm.CrystalFarmResourceManagers;
import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.cfg.Setting;
import ru.windcorp.crystalfarm.struct.mod.ModRegistry;
import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.tge2.util.StringUtil;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.exceptions.SyntaxException;
import ru.windcorp.tge2.util.grh.Resource;
import ru.windcorp.tge2.util.jobs.JobManager;

public class ModuleTranslation extends Module {
	
	private static final Setting<String>
			LANGUAGE = new Setting<>("Language", "Launguage to use, as an IETF language tag", String.class, "en-US"),
			LANGUAGE_FALLBACK = new Setting<>("LanguageFallback", "Launguage to use when main language is missing a mapping, as an IETF language tag", String.class, "en-US");
	
	private static final Map<String, String>
			DICTIONARY = Collections.synchronizedMap(new HashMap<>()),
			DICTIONARY_FALLBACK = Collections.synchronizedMap(new HashMap<>());
	
	private static final Collection<WeakReference<TString>> STRING_SET = Collections.synchronizedCollection(new LinkedList<>());
	
	static boolean hasLoaded = false;

	public ModuleTranslation() {
		super("Translation", InbuiltMod.INST);
		
		addConfig(LANGUAGE);
		LANGUAGE.addListener(node -> reload());
		
		addConfig(LANGUAGE_FALLBACK);
		LANGUAGE_FALLBACK.addListener(node -> reload());
	}

	@Override
	public void registerJobs(JobManager<ModuleJob> manager) {
		manager.addJob(new JobLoadTranslation(this));
	}
	
	public static String getLanguageCode() {
		return LANGUAGE.get();
	}
	
	public static String getLanguageFallbackCode() {
		return LANGUAGE_FALLBACK.get();
	}
	
	public static String getValueForKey(String key) {
		if (hasLoaded) {
			
			synchronized (DICTIONARY) {
				if (DICTIONARY.containsKey(key)) {
					return DICTIONARY.get(key);
				}
			}
			
			synchronized (DICTIONARY_FALLBACK) {
				if (DICTIONARY_FALLBACK.containsKey(key)) {
					return DICTIONARY_FALLBACK.get(key);
				}
			}
			
			ExecutionReport.reportNotification(null, null,
					"Translation key %s is missing a mapping in languages %s/%s", key, getLanguageCode(), getLanguageFallbackCode());
			
		}
		
		return key;
	}
	
	static void register(TString tstring) {
		STRING_SET.add(new WeakReference<>(tstring));
		tstring.load();
	}
	
	static void clean() {
		STRING_SET.removeIf(ref -> ref.get() == null);
	}
	
	public static synchronized void reload() {
		reload(DICTIONARY, getLanguageCode());
		if (!getLanguageCode().equals(getLanguageFallbackCode())) reload(DICTIONARY_FALLBACK, getLanguageFallbackCode());
		STRING_SET.forEach(ref -> load(ref));
	}

	private static void load(WeakReference<TString> ref) {
		TString str = ref.get();
		if (str != null) {
			str.load();
		}
	}

	private static void reload(Map<String, String> dictionary, String languageCode) {
		synchronized (dictionary) {
			dictionary.clear();
			reloadFrom(dictionary, "tran/" + languageCode + ".cftran");
			ModRegistry.getMods().forEach((name, mod) -> {
				reloadFrom(dictionary, name + "/tran/" + languageCode + ".cftran");
			});
		}
	}

	private static void reloadFrom(Map<String, String> dictionary, String path) {
		Resource resource = CrystalFarmResourceManagers.RM_ASSETS.getResource(path);
		
		if (resource.canRead() != null) {
			return;
		}
		
		try {
			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							resource.getInputStream(),
							Charset.forName("UTF-8")
							)
					);
			
			reader.lines().forEachOrdered(line -> {
				
				line = line.trim();
				
				if (line.isEmpty() || line.startsWith("#")) {
					return;
				}
				
				String[] parts = StringUtil.split(line, '=', 2);
				if (parts[1] == null) {
					ExecutionReport.reportNotification(null,
							ExecutionReport.rscCorrupt(resource.toString(), "Line <%s> does not feature a separator '='", line),
							null);
					return;
				}
				parts[0] = parts[0].trim();
				parts[1] = parts[1].trim();
				
				if (parts[1].isEmpty()) {
					ExecutionReport.reportNotification(null,
							ExecutionReport.rscCorrupt(resource.toString(), "Key %s is assigned empty value. Use \"\" in case it is not a mistake", parts[0]),
							null);
					return;
				}
				
				if (parts[1].startsWith("\"") && parts[1].endsWith("\"") && parts[1].length() > 1) {
					parts[1] = parts[1].substring(1, parts[1].length() - 1);
				}
				
				try {
					parts[1] = new String(StringUtil.parseJavaEscapeCharacters(parts[1].toCharArray()));
				} catch (SyntaxException e) {
					ExecutionReport.reportNotification(e,
							ExecutionReport.rscCorrupt(resource.toString(), "Value <%s> for key %s has escape sequence syntax errors", parts[1], parts[0]),
							null);
					return;
				}
				
				dictionary.put(parts[0], parts[1]);
				
			});
			
		} catch (IOException e) {
			ExecutionReport.reportError(e,
					ExecutionReport.rscUnrch(resource.toString(), "Could not read translation file"),
					null);
			return;
		}
	}
	
}
