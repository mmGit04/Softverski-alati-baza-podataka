/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sab_projekat;

import connection.Connect;
import java.math.BigDecimal;
import java.util.List;
import operations.CityOperations;
import operations.CourierOperations;
import operations.DistrictOperations;
import operations.PackageOperations;
import operations.PackageOperations.Pair;
import operations.UserOperations;
import operations.VehicleOperations;
import student.MyCityOperations;
import student.MyCourierOperations;
import student.MyDistrictOperations;
import student.MyPackageOperations;
import student.MyUserOperations;
import student.MyVehicleOperations;

/**
 *
 * @author Minjoza
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Connect con = new Connect();
        CityOperations cityOperations = new MyCityOperations();
        DistrictOperations districtOperations = new MyDistrictOperations();
        UserOperations userOperations = new MyUserOperations();
        VehicleOperations vehicleoperation=new MyVehicleOperations();
        CourierOperations courierOperations=new MyCourierOperations();
        PackageOperations packageOperations=new MyPackageOperations();
        String licencePlateNumber = "B34DU";
        String username="rope";
        int fuelType = 1;
        
       // int ret=packageOperations.insertPackage(9, 10, "crno.dete", 0, BigDecimal.ONE);
       // boolean idP=packageOperations.changeType(2, 2);
     //  boolean ret=packageOperations.acceptAnOffer(1);
     java.sql.Date datum=packageOperations.getAcceptanceTime(1);
        
          System.out.println("Ponuda id  : " + datum);
    }

}
