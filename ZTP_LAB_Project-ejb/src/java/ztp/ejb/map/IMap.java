package ztp.ejb.map;


import javax.ejb.Remote;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
@Remote
public interface IMap {
    /**
     * @return array with ships positions {x1,y1,x2,y2,x3...}
     */
    public double[] getShips();
    public double[] updateShips();
    /**
     * @return array of integer representing [x][y] coordinates of map
     */
    public int[][] getMap();
    public void init();
}
