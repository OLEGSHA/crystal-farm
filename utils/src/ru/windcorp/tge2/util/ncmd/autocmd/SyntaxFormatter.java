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
package ru.windcorp.tge2.util.ncmd.autocmd;

public interface SyntaxFormatter {
	
	public static final SyntaxFormatter PLAIN = new SyntaxFormatter() {
		
		@Override
		public void appendType(StringBuilder sb, String type) {
			sb.append(type);
		}
		
		@Override
		public void appendTrailing(StringBuilder sb) {
			sb.append("...");
		}
		
		@Override
		public void appendStructureChar(StringBuilder sb, char c) {
			sb.append(c);
		}
		
		@Override
		public void appendId(StringBuilder sb, String id) {
			sb.append(id);
		}
		
		@Override
		public void appendLiteral(StringBuilder sb, String contents) {
			sb.append(contents);
		}
	};
	
	void appendType(StringBuilder sb, String type);
	void appendId(StringBuilder sb, String id);
	void appendStructureChar(StringBuilder sb, char c);
	void appendTrailing(StringBuilder sb);
	void appendLiteral(StringBuilder sb, String contents);

}
