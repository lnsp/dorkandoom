package de.mooxmirror.dorkandoom.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;

public class ResourceManager {
	private Properties resourceData;
	private Properties stringData;
	public ResourceManager(String path) {
		resourceData = new Properties();
		File resourceFile = new File(path);
		try {
			resourceData.load(new FileInputStream(resourceFile));
			stringData = new Properties();
			stringData.load(new FileInputStream(new File(resourceData.getProperty("stringResources"))));
			System.out.println("@ResourceManager:21 Resources succesfully loaded.");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public BufferedImage getImage(String imageKey) throws IOException {
		BufferedImage keyImage = null;
		keyImage = ImageIO.read(new File(resourceData.getProperty(imageKey)));
		System.out.println("@ResourceManager:31 Resource '" + imageKey + "' succesfully found.");
		return keyImage;
	}
}
