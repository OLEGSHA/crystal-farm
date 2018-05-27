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

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.xml.sax.SAXException;

import ru.windcorp.crystalfarm.CrystalFarmResourceManagers;
import ru.windcorp.crystalfarm.struct.modules.Module;
import ru.windcorp.crystalfarm.struct.modules.ModuleJob;
import ru.windcorp.tge2.util.debug.Log;
import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.grh.Resource;

import static ru.windcorp.crystalfarm.cfg.ConfigurationModule.*;

public class JobLoadConfig extends ModuleJob {

	public static JobLoadConfig inst;
	
	public JobLoadConfig(Module module) {
		super("LoadConfig", "Loads main configuration", module);
	}

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

}
