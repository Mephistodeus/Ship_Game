/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ztp.ejb.map;

import java.util.Random;

/**
 *
 * @author User
 */
public class ShipTest implements IMapShip{

    public static double SIZE_X = 1024.0;
    public static double SIZE_Y = 1024.0;
    
    private double x;
    private double y;
    
    @Override
    public void init() {
        Random random = new Random();
        this.x = random.nextDouble()*SIZE_X;
        this.y = random.nextDouble()*SIZE_Y;
    }

    @Override
    public double[] position() {
        double ret[] = new double[2];
        ret[0] = x;
        ret[1] = y;
        return ret;
    }
    
}
