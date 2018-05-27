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
package ru.windcorp.crystalfarm.cfg;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.xml.sax.SAXException;

import ru.windcorp.crystalfarm.CrystalFarmResourceManagers;
import ru.windcorp.crystalfarm.InbuiltMod;
import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.grh.Resource;
import ru.windcorp.tge2.util.jobs.JobManager;

public class ConfigurationModule extends Module {
	
	private static Configuration mainConfig = null;
	
	private static DocumentBuilder defaultBuilder = null;
	private static Transformer defaultTransformer = null;
	
	public static Configuration getMainConfiguration() {
		if (mainConfig == null) {
			IllegalStateException e = new IllegalStateException("Main configuration not loaded");
			
			ExecutionReport.reportError(e, null,
					"%s.getMainConfiguration() method was invoked before main configuration has been loaded",
					ConfigurationModule.class.getName());
			throw e;
		}
		
		return mainConfig;
	}

	public static DocumentBuilder getDefaultBuilder() {
		if (defaultBuilder == null) {
			IllegalStateException e = new IllegalStateException("Default builder not initialized");
			
			ExecutionReport.reportError(e, null,
					"%s.getDefaultBuilder() method was invoked before default builder has been initialized",
					ConfigurationModule.class.getName());
			throw e;
		}
		
		return defaultBuilder;
	}

	public static Transformer getDefaultTransformer() {
		if (defaultTransformer == null) {
			IllegalStateException e = new IllegalStateException("Default transformer not initialized");
			
			ExecutionReport.reportError(e, null,
					"%s.getDefaultTransformer() method was invoked before default transformer has been initialized",
					ConfigurationModule.class.getName());
			throw e;
		}
		
		return defaultTransformer;
	}

	public ConfigurationModule() {
		super("Configuration", InbuiltMod.INST);
	}

	@Override
	public void registerJobs(JobManager<ModuleJob> manager) {
		manager.addJob(new ModuleJob("Load Config", "Loads main configuration", this) {

			@Override
			protected void runImpl() {
				Resource resource = CrystalFarmResourceManagers.RM_FILE_WD.getResource("config.xml");
				
				Log.info("Initializing XML");
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				try {
					defaultBuilder = documentBuilderFactory.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
					ExecutionReport.reportCriticalError(e, null,
							"Could not get XML DocumentBuilder");
					return;
				}
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer;
				try {
					transformer = transformerFactory.newTransformer();
				} catch (TransformerConfigurationException e) {
					ExecutionReport.reportCriticalError(e, null,
							"Could not get (XML) Transformer");
					return;
				}
				
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				
				defaultTransformer = transformer;
				
				mainConfig = new Configuration(resource, getDefaultTransformer(), getDefaultBuilder(),
						"MainConfig", "Main configuration root");
				
				Log.info("Loading configuration");
				try {
					getMainConfiguration().read();
				} catch (ConfigurationSyntaxException e) {
					ExecutionReport.reportCriticalError(e,
							ExecutionReport.rscCorrupt("Main configuration", "Could not parse main configuration; XML OK"),
							null);
					return;
				} catch (SAXException e) {
					ExecutionReport.reportCriticalError(e,
							ExecutionReport.rscCorrupt("Main configuration", "Could not parse main configuration; XML damaged"),
							null);
					return;
				} catch (IOException e) {
					ExecutionReport.reportCriticalError(e,
							ExecutionReport.rscUnrch("Main configuration", "Could not read main configuration"),
							null);
					return;
				}
			}
			
		});
	}

}
