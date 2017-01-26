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
        ret[0] = this.x;
        ret[1] = this.y;
        return ret;
    }

    @Override
    public void update() {
        Random random = new Random();
        double moveX;
        double moveY;
        do{
            moveX = random.nextDouble() * 100 - 50;
            moveY = random.nextDouble() * 100 - 50;
        } while((this.x + moveX) < 0 || (this.x + moveX) > SIZE_X || 
                (this.y + moveY) < 0 || (this.y + moveY) > SIZE_Y );
        this.x += moveX;
        this.y += moveY;
    }
    
}
