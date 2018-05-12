package ru.windcorp.tge2.util.interfaces;

public class Link<T> {
	
	public T contents;
	
	public Link() { this(null); }
	
	public Link(T contents) {
		this.contents = contents;
	}

	public void delete() {
		contents = null;
	}

}
