 
import com.sun.image.codec.jpeg.JPEGCodec;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;
 
public class ImageServlet extends HttpServlet {
private static final int WIDTH = 450;
private static final int HEIGHT = 450;
private static final Color BACKGROUND_COLOR =  new Color (56, 89, 94);
private static final Color COLOR = new Color(190, 214, 97);
private static final Color ISLAND_COLOR = new Color (255,255,255);



@Override
public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
   
  /*  response.setContentType("ship/jpeg");

		String pathToWeb = getServletContext().getRealPath(File.separator);
		File f = new File(pathToWeb + "ship.jpg");
		BufferedImage bi = ImageIO.read(f);
		OutputStream out = response.getOutputStream();
		ImageIO.write(bi, "jpg", out);
		out.close();
    
    response.setContentType("image/jpg");
  */
   ServletOutputStream out = response.getOutputStream();
   BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_INDEXED);
     
             
   Graphics graphics = image.getGraphics();
   graphics.setColor(BACKGROUND_COLOR);
   graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
   graphics.setColor(ISLAND_COLOR);
   graphics.drawOval(20, 20, 50, 50);
   graphics.drawOval(280, 100, 50, 50);
   graphics.drawOval(120, 50, 50, 50);
   graphics.drawOval(140, 200, 50, 50);
   graphics.drawOval(40, 250, 50, 50);
   graphics.drawOval(200, 300, 50, 50);
   graphics.drawOval(350, 180, 50, 50);
   graphics.drawOval(250, 380, 50, 50);
   graphics.drawOval(300, 300, 50, 50);
   
   graphics.setColor(COLOR);
   
   JPEGCodec.createJPEGEncoder(out).encode(image);
}


}