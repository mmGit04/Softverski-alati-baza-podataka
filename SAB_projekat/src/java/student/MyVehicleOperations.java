/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.VehicleOperations;

/**
 *
 * @author Minjoza
 */
public class MyVehicleOperations implements VehicleOperations{
     Connection con;

    public MyVehicleOperations() {
        con = connection.Connect.getConnection();
    }

    @Override
    public boolean insertVehicle(String string, int i, BigDecimal bd) {
         try {
            String query1 = "select COUNT(*) FROM VOZILO where reg_broj=?";
            PreparedStatement ps1 = con.prepareStatement(query1);
            ps1.setString(1, string);
            ResultSet res = ps1.executeQuery();
            int count = 0;
            while (res.next()) {
                count = res.getInt(1);
            }
            ps1.close();
            if (count != 0) {
                System.out.println("Vec je definisano vozilo sa istim registracionim brojem.");
                return false;
            }
             BigDecimal broj=bd.setScale(3, BigDecimal.ROUND_HALF_UP);
             String query="insert into VOZILO values(?,?,?)";
             PreparedStatement ps=con.prepareStatement(query);
             ps.setString(1, string);
             ps.setInt(2, i);
             ps.setBigDecimal(3, broj);
             int rez=ps.executeUpdate();
             ps.close();
             if(rez==0) return false;
             else return true;
             
         } catch (SQLException ex) {
             Logger.getLogger(MyVehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
             return false;
         }
        
        
    }

    @Override
    public int deleteVehicles(String... strings) {
         try {
             int ret=0;
             for(String pom:strings){
                String query ="delete VOZILO where reg_broj=?";
                PreparedStatement ps=con.prepareStatement(query); 
                ps.setString(1,pom);
                ret+=ps.executeUpdate();
                ps.close();
             }
             return ret;
             
         } catch (SQLException ex) {
             Logger.getLogger(MyCityOperations.class.getName()).log(Level.SEVERE, null, ex);
             return 0;
         }
    }

    @Override
    public List<String> getAllVehichles() {
         try {
             List<String> ret = new ArrayList();
             String query ="select * from VOZILO ";  
             PreparedStatement ps=con.prepareStatement(query);
             ResultSet rs=ps.executeQuery();
             while(rs.next()){
                 ret.add(rs.getString("reg_broj"));
             }
             ps.close();
             return ret;
         } catch (SQLException ex) {
             Logger.getLogger(MyCityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }
         return null;
    }

    @Override
    public boolean changeFuelType(String string, int i) {
         try {
            if(!(i==1 || i==0 || i==2)) return false;
            String query = "update VOZILO SET tip_goriva=? where reg_broj=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, i);
            ps.setString(2, string);
            int ret=ps.executeUpdate();
            ps.close();
            if(ret==1) return true;
            else return false;
        } catch (SQLException ex) {
            Logger.getLogger(MyVehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean changeConsumption(String string, BigDecimal bd) {
         try {
             BigDecimal broj=bd.setScale(3, BigDecimal.ROUND_HALF_UP);
             String query = "update VOZILO SET potrosnja=? where reg_broj=?";
             PreparedStatement ps = con.prepareStatement(query);
             ps.setBigDecimal(1, broj);
             ps.setString(2, string);
             int ret=ps.executeUpdate();
             ps.close();
             if(ret==1) return true;
             else return false;
         } catch (SQLException ex) {
             Logger.getLogger(MyVehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
             return false;
         }
    }
    
}
