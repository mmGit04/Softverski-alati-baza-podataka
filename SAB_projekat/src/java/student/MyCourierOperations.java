/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.CourierOperations;

/**
 *
 * @author Minjoza
 */
public class MyCourierOperations implements CourierOperations{
      Connection con;

    public MyCourierOperations() {
        con = connection.Connect.getConnection();
    }
    @Override
    public boolean insertCourier(String string, String string1) {
          try {
              String query="select * from KORISNIK where username=?";
              PreparedStatement ps=con.prepareStatement(query);
              ps.setString(1, string);
              ResultSet rs=ps.executeQuery();
              int idK=0;
              if(rs.next()){
                  idK=rs.getInt("idKorisnik");
              }
              else {
                  System.out.println("Ne postoji korisnik sa zadatim usernameom.");
                  ps.close();
                  return false;
              }
              ps.close();
              query="select * from VOZILO where reg_broj=?";
              ps=con.prepareStatement(query);
              ps.setString(1, string1);
              rs=ps.executeQuery();
              int idV=0;
              if(rs.next()){
                  idV=rs.getInt("idVozilo");
              }
              else {
                  System.out.println("Ne postoji vozilo sa zadatim registracionim brojem.");
                  ps.close();
                  return false;
              }
              ps.close();
              
              query="insert into KURIR(idKorisnik,idVozilo,ispo_paketi,profit,status) values(?,?,?,?,?)";
              ps=con.prepareStatement(query);
              ps.setInt(1, idK);
              ps.setInt(2, idV);
              ps.setInt(3, 0);
              ps.setBigDecimal(4, BigDecimal.ZERO);
              ps.setInt(5, 0);
              int ret=ps.executeUpdate();
              ps.close();
              if(ret==1) return true;
              else return false;
              
              
          } catch (SQLException ex) {
              Logger.getLogger(MyCourierOperations.class.getName()).log(Level.SEVERE, null, ex);
              return false;
          }
        
    }

    @Override
    public boolean deleteCourier(String string) {
          try {
              String query = "select * from KORISNIK where username=?";
              PreparedStatement ps = con.prepareStatement(query);
              ps.setString(1, string);
              ResultSet rs = ps.executeQuery();
              int idK = 0;
              if (rs.next()) {
                  idK = rs.getInt("idKorisnik");
              } else {
                  System.out.println("Ne postoji korisnik sa zadatim usernameom.");
                  ps.close();
                  return false;
              }
              ps.close();
              query="DELETE from KURIR where idKorisnik=?";
              ps=con.prepareStatement(query);
              ps.setInt(1, idK);
              int ret=ps.executeUpdate();
              if(ret==1) return true;
              else return false;
              
          } catch (SQLException ex) {
              Logger.getLogger(MyCourierOperations.class.getName()).log(Level.SEVERE, null, ex);
              return false;
          }
    }

    @Override
    public List<String> getCouriersWithStatus(int i) {
          try {
              List<String> lista=new ArrayList<>();
              String query="select idKorisnik from KURIR where status=?";
              PreparedStatement ps=con.prepareStatement(query);
              ps.setInt(1, i);
              ResultSet rs=ps.executeQuery();
              
              while(rs.next()){
                  int pom = rs.getInt(1);
                  System.out.println("Id korisnika je  " + pom);
                  String  query1 = "select username from KORISNIK where idKorisnik=?";
                  PreparedStatement ps1 = con.prepareStatement(query1);
                  ps1.setInt(1, pom);
                  ResultSet rs1 = ps1.executeQuery();
                  String username = "";
                  if (rs1.next()) {
                      username = rs1.getString(1);
                      System.out.println("USERNAME JE" + username);
                  } 
                  ps1.close();
                  lista.add(username);
              }
              rs.close();
              ps.close();
              return lista;
          } catch (SQLException ex) {
              Logger.getLogger(MyCourierOperations.class.getName()).log(Level.SEVERE, null, ex);
              return null;
          }
    }

    @Override
    public List<String> getAllCouriers() {
         try {
              List<String> lista=new ArrayList<>();
              String query="select idKorisnik from KURIR order by Profit DESC";
              PreparedStatement ps=con.prepareStatement(query);
              
              ResultSet rs=ps.executeQuery();
              
              while(rs.next()){
                  int pom = rs.getInt(1);
                  System.out.println("Id korisnika je  " + pom);
                  String  query1 = "select username from KORISNIK where idKorisnik=?";
                  PreparedStatement ps1 = con.prepareStatement(query1);
                  ps1.setInt(1, pom);
                  ResultSet rs1 = ps1.executeQuery();
                  String username = "";
                  if (rs1.next()) {
                      username = rs1.getString(1);
                      System.out.println("USERNAME JE" + username);
                  } 
                  ps1.close();
                  lista.add(username);
              }
              rs.close();
              ps.close();
              return lista;
          } catch (SQLException ex) {
              Logger.getLogger(MyCourierOperations.class.getName()).log(Level.SEVERE, null, ex);
              return null;
          }
    }

    @Override
    public BigDecimal getAverageCourierProfit(int i) {
          try {
              String query="select profit from KURIR where ispo_paketi>= ?";
              PreparedStatement ps=con.prepareStatement(query);
              ps.setInt(1, i);
              ResultSet rs=ps.executeQuery();
              int broj=0;
              BigDecimal profit=new BigDecimal(BigInteger.ZERO);
              System.out.println("Profit"+ profit);
              while(rs.next()){
                  broj++;
                  profit=profit.add(rs.getBigDecimal(1));
              }
              ps.close();
              System.out.println("Broj : " + broj);
              System.out.println("Profit : " + profit);
              BigDecimal res=profit.divide(new BigDecimal(broj));
              return res;
          } catch (SQLException ex) {
              Logger.getLogger(MyCourierOperations.class.getName()).log(Level.SEVERE, null, ex);
              return null;
          }
        
    }
    
}
