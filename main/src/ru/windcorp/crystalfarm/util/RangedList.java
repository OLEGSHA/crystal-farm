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
package ru.windcorp.crystalfarm.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

/*
 * TODO: optimize with binsearch
 */
public class RangedList<T> {
	
	protected class Pair {
		private final double start;
		private final double end;
		private final T element;
		
		public Pair(double start, double end, T element) {
			this.start = start;
			this.end = end;
			this.element = element;
		}

		public double getStart() {
			return start;
		}

		public double getEnd() {
			return end;
		}

		public T getElement() {
			return element;
		}
		
		@Override
		public String toString() {
			return element + " [" + start + "; " + end + "]";
		}
	}
	
	private final SortedSet<Pair> elements = new TreeSet<>(Comparator.comparingDouble(pair -> pair.getStart()));
	
	public void add(T element, double start, double end) {
		if (start > end) {
			throw new IllegalArgumentException("start (" + start + ") > end (" + end + ")");
		}
		
		for (Pair pair : getPairs()) {
			if (pair != null) {
				if (pair.getStart() > end) {
					continue;
				}
				if (pair.getEnd() > start && pair.getStart() < end) {
					throw new IllegalStateException("Cannot add " + element + " [" + start + "; " + end + "]: collides with " + pair);
				}
			}
		}
		
		getPairs().add(new Pair(start, end, element));
	}
	
	public void remove(T element) {
		for (Iterator<Pair> iterator = getPairs().iterator(); iterator.hasNext(); ) {
			if (iterator.next().getElement() == element) {
				iterator.remove();
				break;
			}
		}
	}
	
	public void remove(double i) {
		getPairs().remove(getPair(i));
	}
	
	protected Pair getPair(double i) {
		for (Pair pair : getPairs()) {
			if (pair.getStart() > i) {
				return null;
			} else if (pair.getEnd() < i) {
				continue;
			} else {
				return pair;
			}
		}
		
		return null;
	}
	
	public T get(double i) {
		Pair pair = getPair(i);
		if (pair != null) {
			return pair.getElement();
		} else {
			return null;
		}
	}
	
	protected SortedSet<Pair> getPairs() {
		return elements;
	}
	
	public Stream<T> getElements() {
		return getPairs().stream().map(pair -> pair.getElement());
	}

}
