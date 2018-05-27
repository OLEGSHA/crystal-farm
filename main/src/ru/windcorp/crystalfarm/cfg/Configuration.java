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
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import ru.windcorp.tge2.util.debug.er.ExecutionReport;
import ru.windcorp.tge2.util.grh.Resource;

public class Configuration extends Section {

	private Document document;
	
	private final DocumentBuilder builder;
	private final Transformer writer;
	
	private final Resource resource;
	
	public Configuration(Resource resource, Transformer writer, DocumentBuilder builder, String name, String description) {
		super(name, description);
		this.resource = resource;
		
		this.builder = builder;
		this.writer = writer;
	}
	
	public DocumentBuilder getBuilder() {
		return this.builder;
	}
	
	public Transformer getWriter() {
		return this.writer;
	}
	
	public Resource getResource() {
		return resource;
	}
	
	public synchronized Document getDocument() {
		return document;
	}
	
	public synchronized void read() throws SAXException, IOException, ConfigurationSyntaxException {
		String problem = getResource().canRead();
		Element root;
		
		if (problem != null) {
			Exception e = new IOException(problem);
			
			ExecutionReport.reportNotification(e,
					ExecutionReport.rscUnrch("Main configuration", "Could not read main configuration, loading default instead"),
					null);
			
			this.document = getBuilder().newDocument();
			getDocument().appendChild(root = getDocument().createElement(getName()));
			
		} else {
			this.document = getBuilder().parse(getResource().getInputStream());
			root = (Element) getDocument().getElementsByTagName(getName()).item(0);
			
			if (root == null) {
				throw new ConfigurationSyntaxException("Cannot find root element by tag " + getName(), this);
			}
		}
		
		load(root);
	}
	
	public synchronized void write() throws IOException, TransformerException, IllegalStateException {
		if (getDocument() == null) {
			throw new IllegalStateException("No " + Document.class.getName() + " is loaded in " + this);
		}
		
		String problem = getResource().canWrite();
		
		if (problem != null) {
			throw new IOException(problem);
		}
		
		Source source = new DOMSource(getDocument());
		Result result = new StreamResult(getResource().getOutputStream());
		
		getWriter().transform(source, result);
	}

}
