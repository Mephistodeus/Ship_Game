
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double[] getShips() {
        double[] ret = new double[ships.size()*2];
        double[] position;
        for( int i=0;i<ships.size();i++){
            position = ships.get(i).position();
            ret[i*2] = position[0];
            ret[i*2+1] = position[1];
        }
        return ret;
    }

    @Override
    public double[] updateShips() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        Properties jndiProps = new Properties();
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        jndiProps.put(Context.PROVIDER_URL,"remote://192.168.43.231:8686");
        // create a context passing these properties
        Context ctx;
        try {
            ctx = new InitialContext(jndiProps);
            IMapShip ship = (IMapShip) ctx.lookup("foo/bar");
            ships.add(ship);
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
}
