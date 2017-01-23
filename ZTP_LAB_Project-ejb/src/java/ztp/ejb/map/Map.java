package ztp.ejb.map;


import static java.lang.Math.ceil;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.ejb.Startup;
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
@Startup
@Singleton
public class Map implements IRadarMap, IMap {

    public static double SIZE_X = 1024.0;
    public static double SIZE_Y = 1024.0;
    private static ArrayList<Obstacle> obstacles;
    private static ArrayList<IMapShip> ships;
    

    @Override
    public double[] hit(double x, double y, double dx, double dy) {
        double ret[] = {
            Double.NaN,Double.NaN,
            Double.NaN,Double.NaN
        };
        Vector2 v[], v1, v2;
        for(Obstacle o : obstacles){
            v1 = new Vector2(x, y);
            v2 = new Vector2(dx, dy);
            v = o.hit(v1, v2);
            if(v!=null){
                //checking for map borders and single points
                if(!overflow(v[0]) && v[0].between(v1, v2)){
                    ret[0] = v[0].x;
                    ret[1] = v[0].y;
                } else{
                    ret[0] = Double.NaN;
                    ret[1] = Double.NaN;
                }
                if(!overflow(v[1]) && v[1].between(v1, v2) && !(v[0].x==v[1].x&&v[0].y==v[1].y)){
                    ret[2] = v[1].x;
                    ret[3] = v[1].y;
                }
                else{
                    ret[2] = Double.NaN;
                    ret[3] = Double.NaN;
                }
                if(ret[1]!=ret[3]&&(ret[1]!=Double.NaN||ret[3]!=Double.NaN)){
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
        int[][] ret = new int[(int)ceil(SIZE_X)][(int)ceil(SIZE_Y)];
        for(int i=0;i<obstacles.size();i++){
            drawObstacle(obstacles.get(i), ret);
        }
        return ret;
    }

    @Override
    public void init() {
        ships = new ArrayList<>();
        obstacles = new ArrayList<>();
        Random rand = new Random();
        
        // create a context passing these properties
        /*Context ctx;
        try {
            ctx = loadProperties("192.168.43.48", "3700");
            IMapShip ship = (IMapShip) ctx.lookup("java:global/Ship-lab/Ship!ztp.ejb.ship.IMapShip");
            System.out.println("connected");
            ships.add(ship);
            ship.init();
        } catch (Exception ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        int obstaclesAmmount = 4;
        for(int i=0;i < obstaclesAmmount;i++){
            obstacles.add(new Circle(getRandomVector(SIZE_X, SIZE_Y), 
                getRandomVector(SIZE_X/4, SIZE_Y/4)));
        }
        int shipsAmmount = 4;
        IMapShip ship;
        double hit[];
        for(int i=0;i < shipsAmmount;i++){
            ship = new ShipTest();
            do{
                ship.init();
                hit = hit(ship.position()[0], ship.position()[1],ship.position()[0]+1, ship.position()[1]);
            } while(!Double.isNaN(hit[0])&&!Double.isNaN(hit[1]));
            ships.add(ship);
        }
    }
    
    private void drawObstacle(Obstacle obstacle, int[][] ret){
        int[][] item = obstacle.draw();
        for(int i=0;i<item.length;i++){
            if(i+(int)(obstacle.position.x)<ceil(SIZE_X)){
                for(int j=0;j<item[0].length;j++){
                    if(j+ceil(obstacle.position.y)<ceil(SIZE_Y)){
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
 
            /*props.setProperty("java.naming.factory.initial",
                    "com.sun.enterprise.naming.SerialInitContextFactory");
            props.setProperty("java.naming.factory.url.pkgs",
                    "com.sun.enterprise.naming");
            props.setProperty("java.naming.factory.state",
                    "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
            props.setProperty("org.omg.CORBA.ORBInitialHost", h);
            props.setProperty("org.omg.CORBA.ORBInitialPort", p);*/
            
            Properties jndiProps = new Properties();
            jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
            jndiProps.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
            jndiProps.setProperty("java.naming.factory.state","com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
            jndiProps.put(Context.PROVIDER_URL, h + ":" + p);
            
            ic = new InitialContext(jndiProps);
            
        } catch (NamingException ex) {
            Logger.getLogger("Fail");
        }
        
        return ic;
    }

    private Vector2 getRandomVector(double rangeX, double rangeY){
        Random random = new Random();
        return new Vector2(random.nextDouble()*rangeX, random.nextDouble()*rangeY);
    }
    private boolean overflow(Vector2 v) {
        return v.x > SIZE_X || v.x < 0 || v.y > SIZE_Y || v.y < 0;
    }
}
