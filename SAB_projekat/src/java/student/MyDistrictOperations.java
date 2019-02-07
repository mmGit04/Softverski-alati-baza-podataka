/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.DistrictOperations;

/**
 *
 * @author Minjoza
 */
public class MyDistrictOperations implements DistrictOperations{
     Connection con;

    public MyDistrictOperations() {
        con=connection.Connect.getConnection();
    }
     
     
    @Override
    public int insertDistrict(String string, int i, int i1, int i2) {
         try {
             String query="select COUNT(*) FROM OPSTINA where naziv=? AND idGrad=?";
             
             PreparedStatement ps=con.prepareStatement(query);
             ps.setString(1, string);
             ps.setInt(2, i);
             ResultSet res=ps.executeQuery();
             int count=0;
             while (res.next()){
                 count = res.getInt(1);
             }
             ps.close();
             if(count!=0) {
                 System.out.println("Vec postoji opstina sa tim nazivom u zadatom gradu.");
                 return -1;
             }
            query = "insert into OPSTINA(naziv,x_cord,y_cord,idGrad) values (?,?,?,?)";
            ps =con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, string);
            ps.setInt(2, i1);
            ps.setInt(3,i2);
            ps.setInt(4, i);
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
             Logger.getLogger(MyDistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
             return -1;
         }
    }

    @Override
    public int deleteDistricts(String... strings) {
         try {
             int ret=0;
             for(String pom:strings){
                String query ="delete OPSTINA where naziv=?";
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
    public boolean deleteDistrict(int i) {
         try {
             String query ="delete OPSTINA where idOpstina=?";
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
    public int deleteAllDistrictsFromCity(String string) {
         try {
             String query="select idGrad from GRAD where naziv='" + string + "'";
             Statement s=con.createStatement();
             ResultSet rs=s.executeQuery(query);
             int idCity=-1;
             while(rs.next()){
                 
                 idCity=rs.getInt(1);
                 System.out.println("IdCity: " + idCity);
             }
             if(idCity==-1){
                 System.out.println("Ne postoji grad sa tim nazivom.");
                 return 0;
             }
             else {
             query ="delete OPSTINA where idGrad=?";
             PreparedStatement ps=con.prepareStatement(query);   
             ps.setInt(1, idCity);  
             int ret=ps.executeUpdate();
             ps.close();
             return ret;
             }
         } catch (SQLException ex) {
             Logger.getLogger(MyCityOperations.class.getName()).log(Level.SEVERE, null, ex);
             return 0;
         }
    }

    @Override
    public List<Integer> getAllDistrictsFromCity(int i) {
        try {
             List<Integer> ret = new ArrayList();
             String query ="select idOpstina from OPSTINA where idGrad=" + i;  
             Statement ps=con.createStatement();
             ResultSet rs=ps.executeQuery(query);
             while(rs.next()){
                 ret.add(rs.getInt("idOpstina"));
             }
             ps.close();
             return ret;
         } catch (SQLException ex) {
             Logger.getLogger(MyCityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }
         return null;
    }

    @Override
    public List<Integer> getAllDistricts() {
         try {
             List<Integer> ret = new ArrayList();
             String query ="select idOpstina from OPSTINA";  
             Statement ps=con.createStatement();
             ResultSet rs=ps.executeQuery(query);
             while(rs.next()){
                 ret.add(rs.getInt("idOpstina"));
             }
             ps.close();
             return ret;
         } catch (SQLException ex) {
             Logger.getLogger(MyCityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }
         return null;
    }
    
}
