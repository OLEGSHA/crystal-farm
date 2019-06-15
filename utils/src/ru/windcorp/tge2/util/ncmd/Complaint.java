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

public class Complaint extends CommandExceptions {

	private static final long serialVersionUID = -2995909392418618737L;

	public Complaint(Invocation inv, String message) {
		super(inv, message);
	}
	
	/**
	 * Attempts to localize the message by using the message as the input for the context's translator.
	 * If the translator is not set, returns the message plainly.
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	@Override
	public String getLocalizedMessage() {
		if (getInvocation().getContext().getTranslator() != null) {
			return getInvocation().getContext().getTranslator().apply(getMessage());
		}
		return getMessage();
	}

}
