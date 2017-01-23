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
        
        double baX = v2.x - v1.x;
        double baY = v2.y - v1.y;
        double caX = this.position.x + this.r - v1.x;
        double caY = this.position.y + this.r - v1.y;

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - this.r * this.r;

        double pBy2 = bBy2 / a;
        double q = c / a;

        double disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return null;
        }
        
        double tmpSqrt = Math.sqrt(disc);
        double abScalingFactor1 = -pBy2 + tmpSqrt;
        double abScalingFactor2 = -pBy2 - tmpSqrt;

        Vector2 p1 = new Vector2(v1.x - baX * abScalingFactor1, v1.y
                - baY * abScalingFactor1);
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            return new Vector2[] {p1, p1};
        }
        Vector2 p2 = new Vector2(v1.x - baX * abScalingFactor2, v1.y
                - baY * abScalingFactor2);
        return new Vector2[] {p1, p2};
    }

}
