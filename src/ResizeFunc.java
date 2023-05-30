import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ResizeFunc {
    public static BufferedImage reSize(BufferedImage inputStream, int width, int height) throws IOException {
        BufferedImage outputImage = new BufferedImage(width, height, inputStream.getType());

        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.drawImage(inputStream, 0, 0, width, height, null);
        graphics2D.dispose();

        return outputImage;
    }
}
