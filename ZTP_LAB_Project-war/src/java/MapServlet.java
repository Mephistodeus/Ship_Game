/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ztp.ejb.map.IMap;
import com.sun.image.codec.jpeg.JPEGCodec;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ztp.ejb.map.IRadarMap;

/**
 *
 * @author User
 */
@WebServlet(urlPatterns = {"/MapServlet"})
public class MapServlet extends HttpServlet {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 1024;
    private static final int SHIP_SIZE = 20;
    private static final Color BACKGROUND_COLOR = new Color(56, 89, 94);
    private static final Color COLOR = new Color(190, 214, 97);
    private static final Color ISLAND_COLOR = new Color(255, 255, 128);
    private static final Color SHIP_COLOR = new Color(255, 255, 255);
    @EJB
    IMap map;
    @EJB
    IRadarMap radar;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("image/jpeg");

        map.init();

        ServletOutputStream out = response.getOutputStream();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_INDEXED);

        Graphics graphics = image.getGraphics();
        graphics.setColor(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.setColor(ISLAND_COLOR);
        int islands[][] = map.getMap();
        for (int i = 0; i < islands.length; i++) {
            for (int j = 0; j < islands[0].length; j++) {
                if (islands[i][j] > 0) {
                    graphics.drawLine(i, j, i, j);
                }
            }
        }
        graphics.setColor(SHIP_COLOR);
        double ships[] = map.getShips();
        double hit[];
        for (int i = 0; (i + 1) < ships.length; i += 2) {
            graphics.drawOval((int) ships[i] - SHIP_SIZE/2, (int) ships[i + 1] - SHIP_SIZE/2, SHIP_SIZE, SHIP_SIZE);
            graphics.setColor(Color.RED);
            graphics.drawOval((int) ships[i] + 50 - SHIP_SIZE/2, (int) ships[i + 1] + 50 - SHIP_SIZE/2, SHIP_SIZE, SHIP_SIZE);
            hit = radar.hit(ships[i], ships[i + 1], ships[i] + 50, ships[i + 1] + 50);
            for (int j = 0; (j + 1) < hit.length; j += 2) {
                if(!Double.isNaN(hit[j]) && !Double.isNaN(hit[j+1])){
                    graphics.setColor(Color.RED);
                    graphics.drawRect((int)hit[j]-SHIP_SIZE/4, (int)hit[j+1]-SHIP_SIZE/4, SHIP_SIZE/2, SHIP_SIZE/2);
                    //System.out.println("x: " + hit[j] + " y: " + hit[j+1]);
                    
                }
            }
            graphics.setColor(SHIP_COLOR);
        }
        graphics.setColor(COLOR);

        JPEGCodec.createJPEGEncoder(out).encode(image);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
