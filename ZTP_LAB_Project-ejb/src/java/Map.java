
import ztp.ejb.ship.IMapShip;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Damian Gancarz
 */
@Stateful
public class Map implements IRadarMap, IMap {

    public static double SIZE_X = 1024.0;
    public static double SIZE_Y = 1024.0;
    private static ArrayList<Obstacle> obstacles;
    private static ArrayList<IMapShip> ships;
    

    @Override
    public double[] hit(double x, double y, double dx, double dy) {
        double ret[] = {
            Double.MAX_VALUE,Double.MAX_VALUE,
            Double.MAX_VALUE,Double.MAX_VALUE
        };
        Vector2[] v;
        for(Obstacle o : obstacles){
            v = o.hit(new Vector2(x, y), new Vector2(dx, dy));
            if(v!=null){
                //checking for map borders and single points
                if(!overflow(v[0])){
                    ret[0] = v[0].x;
                    ret[1] = v[0].y;
                } else{
                    ret[0] = Double.MAX_VALUE;
                    ret[1] = Double.MAX_VALUE;
                }
                if(!overflow(v[1]) && !(v[0].x==v[1].x&&v[0].y==v[1].y)){
                    ret[2] = v[1].x;
                    ret[3] = v[1].y;
                }
                else{
                    ret[2] = Double.MAX_VALUE;
                    ret[3] = Double.MAX_VALUE;
                }
                if(ret[1]!=ret[3]&&(ret[1]!=Double.MAX_VALUE||ret[3]!=Double.MAX_VALUE)){
                    break;
                }
            }
        }
        return ret;
    }

    @Override
    public double[] getShips() {
        double[] ret = new double[ships.size()*2];
        double[] position;
        for( int i=0;i<ships.size();i++){
            position = ships.get(i).position();
            if(position.length == 2){
                ret[i*2] = position[0];
                ret[i*2+1] = position[1];
            }
        }
        return ret;
    }

    @Override
    public double[] updateShips() {
        return getShips();
    }

    @Override
    public int[][] getMap() {
        int[][] ret = new int[(int)(SIZE_X + 0.5)][(int)(SIZE_Y + 0.5)];
        obstacles.stream().forEach((obstacle) -> {
            drawObstacle(obstacle, ret);
        });
        return ret;
    }

    @Override
    public void init() {
        ships = new ArrayList<>();
        obstacles = new ArrayList<>();
        Random rand = new Random();
        // create a context passing these properties
        Context ctx;
        try {
            ctx = loadProperties("192.168.43.110", "3700");
            IMapShip ship = (IMapShip) ctx.lookup("java:global/Ship/Ship!ztp.ejb.ship.IMapShip");
            ships.add(ship);
            ship.init();
        } catch (NamingException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        }
        obstacles.add(new Circle(new Vector2(SIZE_X/2, SIZE_Y/2),
            new Vector2(rand.nextDouble()*SIZE_X/4 + SIZE_X/4,
                    rand.nextDouble()*SIZE_Y/4 + SIZE_Y/4)));
    }
    
    private void drawObstacle(Obstacle obstacle, int[][] ret){
        int[][] item = obstacle.draw();
        for(int i=0;i<item.length;i++){
            if(i+(int)(obstacle.position.y)<(int)(SIZE_X+0.5)){
                for(int j=0;j<item[0].length;j++){
                    if(j+(int)(obstacle.position.y)<(int)(SIZE_Y+0.5)){
                        ret[i+(int)obstacle.getPosition().x][j+(int)obstacle.getPosition().y] |= item[i][j];
                    }
                }
            }
        }
    }
    public InitialContext loadProperties(String h, String p) {
        InitialContext ic = null;
        try {
            Properties props = new Properties();
 
            System.out.println("h: " + h + " p: " + p);
 
            props.setProperty("java.naming.factory.initial",
                    "com.sun.enterprise.naming.SerialInitContextFactory");
            props.setProperty("java.naming.factory.url.pkgs",
                    "com.sun.enterprise.naming");
            props.setProperty("java.naming.factory.state",
                    "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
            props.setProperty("org.omg.CORBA.ORBInitialHost", h);
            props.setProperty("org.omg.CORBA.ORBInitialPort", p);
            
            ic = new InitialContext(props);
            
        } catch (NamingException ex) {
            Logger.getLogger("Fail");
        }
        
        return ic;
    }

    private boolean overflow(Vector2 v) {
        return v.x > SIZE_X || v.x < 0 || v.y > SIZE_Y || v.y < 0;
    }
}
