package sample.my.cv.util;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageManager {

    public static final String IMAGES_FOLDER = "/home/jurasikasan/Pictures/";//System.getenv("OPENSHIFT_DATA_DIR");

    public static synchronized URL uploadImage(byte[] image, String name) throws IOException {
        File f = new File(IMAGES_FOLDER + name + ".png");
        BufferedImage image1 = ImageIO.read(new ByteArrayInputStream(image));
        ImageIO.write(image1, "png", f);
        return f.toURI().toURL();
    }

}
