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
public class Vector2 {
    public double x;
    public double y;
    public Vector2(){};
    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }
    public double len2(){
        return this.x*this.x + this.y*this.y;
    }
}
