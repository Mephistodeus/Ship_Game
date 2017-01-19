package ztp.ejb.ship;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public abstract class Obstacle {
    protected Vector2 position;
    protected Vector2 size;
    
    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }
    /**
     * 
     * @return 
     */
    public abstract int[][] draw();
    /**
     * Finds points of collision between edge (v1,v2).
     * @param v1 
     * @param v2
     * @return Array of collision vectors.
     */
    public abstract Vector2[] hit(Vector2 v1, Vector2 v2);
}
