/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import com.google.common.base.Strings;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.CityOperations;

/**
 *
 * @author Minjoza
 */
public class MyCityOperations implements CityOperations{
     Connection con;

    public MyCityOperations() {
        con=connection.Connect.getConnection();
    }
     
     
    @Override
    public int insertCity(String string, String string1) {
        try {
            String query1="select COUNT(*) FROM GRAD where naziv=?";
            PreparedStatement ps1=con.prepareStatement(query1);
            ps1.setString(1, string);
            ResultSet res=ps1.executeQuery();
            int count=0;
            while (res.next()){
                    count = res.getInt(1);
            }
            ps1.close();
            if(count!=0) {
                System.out.println("Vec postoji grad sa tim nazivom");
                return -1;
            }
            String query2="select COUNT(*) FROM GRAD where post_broj=?";
            PreparedStatement ps2=con.prepareStatement(query2);
            ps2.setString(1, string1);
            ResultSet res1=ps2.executeQuery();
            int count1=0;
            while (res1.next()){
                    count1 = res1.getInt(1);
            }
            ps2.close();
            if(count1!=0) {
                System.out.println("Vec postoji grad sa tim postanskim brojem.");
                return -1;
            }
            
            String query = "insert into GRAD(naziv,post_broj) values (?,?)";
            PreparedStatement ps =con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, string);
            ps.setString(2, string1);
            int ret = 0; 
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                 ret = rs.getInt(1);
            }
            ps.close();
            System.out.println("RowId " + ret);
            return ret;
            
        } catch (SQLException ex) {
            Logger.getLogger(MyCityOperations.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        
    }

  
    @Override
    public boolean deleteCity(int i) {
         try {
             String query ="delete GRAD where idGrad=?";
             PreparedStatement ps=con.prepareStatement(query);   
             ps.setInt(1, i);  
             int ret=ps.executeUpdate();
             ps.close();
             if(ret==0) return false;
             else return true;
         } catch (SQLException ex) {
             Logger.getLogger(MyCityOperations.class.getName()).log(Level.SEVERE, null, ex);
             return false;
         }
        
    }

    @Override
    public List<Integer> getAllCities() {
         try {
             List<Integer> ret = new ArrayList();
             String query ="select * from GRAD ";  
             PreparedStatement ps=con.prepareStatement(query);
             ResultSet rs=ps.executeQuery();
             while(rs.next()){
                 ret.add(rs.getInt("idGrad"));
             }
             ps.close();
             return ret;
         } catch (SQLException ex) {
             Logger.getLogger(MyCityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }
         return null;
    }

    @Override
    public int deleteCity(String... strings) {
        try {
             int ret=0;
             for(String pom:strings){
                String query ="delete GRAD where naziv=?";
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
    
}
