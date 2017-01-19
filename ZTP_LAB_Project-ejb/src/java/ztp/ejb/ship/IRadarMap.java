package ztp.ejb.ship;


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
public interface IRadarMap{
    double[] hit(double x, double y, double dx, double dy);
}
