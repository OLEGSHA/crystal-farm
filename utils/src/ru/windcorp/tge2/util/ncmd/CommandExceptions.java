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

public abstract class CommandExceptions extends Exception {

	private static final long serialVersionUID = -8088208351838104573L;
	
	private final Invocation inv;
	
	protected CommandExceptions(Invocation inv) {
		this.inv = inv;
	}
	
	protected CommandExceptions(
			Invocation inv,
			String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.inv = inv;
	}
	
	protected CommandExceptions(Invocation inv, String message, Throwable cause) {
		super(message, cause);
		this.inv = inv;
	}
	
	protected CommandExceptions(Invocation inv, String message) {
		super(message);
		this.inv = inv;
	}
	
	/**
	 * @return the invocation
	 */
	public Invocation getInvocation() {
		return inv;
	}

	public Context getContext() {
		return inv.getContext();
	}

	public String getFullCommand() {
		return inv.getFullCommand();
	}

	public CommandRunner getRunner() {
		return inv.getRunner();
	}

	public CommandRegistry getParentCommand() {
		return inv.getParent();
	}

	public Command getThrower() {
		return inv.getCurrent();
	}

	public String getArgs() {
		return inv.getArgs();
	}

}
