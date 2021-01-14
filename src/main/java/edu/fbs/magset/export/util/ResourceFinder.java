package edu.fbs.magset.export.util;

import java.io.InputStream;

public class ResourceFinder {

	public static InputStream getResourceAsStream(String name) {
		InputStream resource = ResourceFinder.class.getClassLoader().getResourceAsStream(name);
		if (resource != null) {
			return resource;
		}
		return ResourceFinder.class.getClassLoader().getResourceAsStream("resources/" + name);
	}
}
