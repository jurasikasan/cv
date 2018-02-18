package sample.my.cv.beans;


import sample.my.cv.util.ImageManager;
import java.io.IOException;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.event.CaptureEvent;
import org.slf4j.LoggerFactory;

@Named
@SessionScoped
public class PhotoCamView implements Serializable {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private String filename;

    private static String getRandomImageName() {
        int i = (int) (Math.random() * 10000000);
        return String.valueOf(i);
    }

    public String getFilename() {
        return filename;
    }

    public void oncapture(CaptureEvent captureEvent) {
        filename = getRandomImageName();
        byte[] data = captureEvent.getData();
        try {
            ImageManager.uploadImage(data, filename);
            LOG.info("File [{}] saved", filename);
        } catch (IOException ex) {
            LOG.error("Error saving file [{}]", filename, ex);
        }
    }
}
