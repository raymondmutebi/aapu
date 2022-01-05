package org.pahappa.systems.core.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class PropertiesLoader {
	private static String PROPERTIES_FILE = "EmailConnections.properties";
	private Properties properties;

	private PropertiesLoader() {
		try {
			loadProperties();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	private URL getPropertiesURL() {
		return getClass().getResource(PROPERTIES_FILE);
	}

	private void loadProperties() throws URISyntaxException, IOException {
		this.properties = new Properties();
		properties.load(new FileInputStream(
				new File(getPropertiesURL().toURI())));
	}

	public static class Util {

		private static PropertiesLoader instance;

		public static PropertiesLoader getInstance() {
			if (instance == null)
				instance = new PropertiesLoader();

			return instance;
		}

		private Util() {

		}
	}
}
