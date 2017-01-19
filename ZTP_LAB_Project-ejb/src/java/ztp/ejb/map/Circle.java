package ztp.ejb.map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class Circle extends Obstacle {

    private double r;

    public Circle(Vector2 position, Vector2 size) {
        this.position = position;
        this.size = size;
        this.r = size.x < size.y ? size.x / 2 : size.y / 2;
    }

    @Override
    public int[][] draw() {
        int arraySize = (int) (r * 2 + 0.5);
        int[][] ret = new int[arraySize][arraySize];
        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize; j++) {
                ret[i][j] = 
                    (i - r) * (i - r) + (j - r) * (j - r) <= r * r ? 1 : 0;
            }
        }
        return ret;
    }

    @Override
    public Vector2[] hit(Vector2 v1, Vector2 v2) {
        double[] ret = new double[4];
        try{
            //line params
            double a = (v1.y - v2.y)/(v1.x - v2.x);
            double b = 1;
            double c = a*v1.x - v1.y;
            //circle params
            double d = this.position.x;
            double e = this.position.y;
            //quadratic equation params
            double qa = a*a + 1;
            double qb = -2*d-2*a*c+2*a*e;
            double qc = -d*d+c*c+e*e-r*r-2*c*e;
            //solving equation
            ret[0] = (-qb-Math.sqrt(qb*qb-4*qa*qc))/2*qa;
            ret[2] = (-qb+Math.sqrt(qb*qb-4*qa*qc))/2*qa;
            ret[1] = a*ret[0] - c;
            ret[3] = a*ret[2] - c;
        } catch(Exception e){
            return null;
        }
        return new Vector2[] {new Vector2(ret[0],ret[1]), new Vector2(ret[2], ret[3])};
    }

}
