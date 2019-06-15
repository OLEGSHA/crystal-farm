/**
 * Copyright (C) 2019 OLEGSHA
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
package ru.windcorp.tge2.util.ncmd;

import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Context {
	
	private final CommandRegistry globals = new CommandRegistry("__GLOBALS", "Global commands");
	private final Map<Class<? extends CommandExceptions>, Consumer<? extends CommandExceptions>> exceptionHandlers = new WeakHashMap<>();
	private BiConsumer<Invocation, Exception> fallbackExceptionHandler;
	private Function<String, String> translator = null;

	public CommandRegistry getGlobals() {
		return globals;
	}
	
	@SuppressWarnings("unchecked")
	public void handle(CommandExceptions exception) {
		Class<? extends CommandExceptions> mostSpecificClass = null;
		Consumer<? extends CommandExceptions> mostSpecificHandler = null;
		
		synchronized (exceptionHandlers) {
			for (Entry<Class<? extends CommandExceptions>, Consumer<? extends CommandExceptions>> entry
					: exceptionHandlers.entrySet()) {
				
				if (entry.getKey().equals(exception.getClass())) {
					mostSpecificHandler = entry.getValue();
					break;
				}
				
				if (entry.getKey().isAssignableFrom(exception.getClass())) {
					if (mostSpecificClass == null || entry.getKey().isAssignableFrom(mostSpecificClass)) {
						mostSpecificClass = entry.getKey();
						mostSpecificHandler = entry.getValue();
					}
				}
			}
		}
			
		if (mostSpecificHandler == null) {
			fallbackExceptionHandler.accept(exception.getInvocation(), exception);
			return;
		}
		
		((Consumer<CommandExceptions>) mostSpecificHandler).accept(exception);
	}
	
	/**
	 * @return the fallbackExceptionHandler
	 */
	public BiConsumer<Invocation, Exception> getFallbackExceptionHandler() {
		return fallbackExceptionHandler;
	}
	
	/**
	 * @param fallbackExceptionHandler the fallbackExceptionHandler to set
	 */
	public void setFallbackExceptionHandler(BiConsumer<Invocation, Exception> fallbackExceptionHandler) {
		this.fallbackExceptionHandler = fallbackExceptionHandler;
	}
	
	public synchronized <E extends CommandExceptions> void addExceptionHandler(Class<E> clazz, Consumer<E> handler) {
		exceptionHandlers.put(clazz, handler);
	}
	
	public synchronized void addDefaultExceptionHandlers(String fallbackComplaint, String cmdNotFoundComplaint) {
		setFallbackExceptionHandler((Invocation i, Exception e) -> {
			CommandRunner runner = i.getRunner();
			runner.complain(fallbackComplaint, i.getFullCommand());
			runner.reportException(e);
		});
		
		addExceptionHandler(
				CommandNotFoundException.class, e -> 
				e.getRunner().complain(cmdNotFoundComplaint, e.getMessage(), e.getFullCommand())
		);
		
		addExceptionHandler(
				Complaint.class, e ->
				e.getRunner().complain(e.getLocalizedMessage())
		);
	}

	public Function<String, String> getTranslator() {
		return translator;
	}

	public void setTranslator(Function<String, String> translator) {
		this.translator = translator;
	}

}
