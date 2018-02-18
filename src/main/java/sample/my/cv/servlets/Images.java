package sample.my.cv.servlets;


import sample.my.cv.util.ImageManager;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/images")
public class Images extends HttpServlet {

    private static final long serialVersionUID = 8406015696157225147L;
    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String key = request.getParameter("key");
        if (key == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }
        try (BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE)) {
            final BufferedImage bufferedImage;
            try {
                bufferedImage = ImageIO.read(new File(ImageManager.IMAGES_FOLDER + key + ".png"));
            } catch (IOException ex) {
                throw new ServletException("error reading image", ex);
            }
            ImageIO.write(bufferedImage, "png", output);
        }
    }

}
