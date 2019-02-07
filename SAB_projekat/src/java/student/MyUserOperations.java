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
import operations.UserOperations;

/**
 *
 * @author Minjoza
 */
public class MyUserOperations implements UserOperations{
    Connection con;

    public MyUserOperations() {
        con = connection.Connect.getConnection();
    }

    @Override
    public boolean insertUser(String string, String string1, String string2, String string3) {
        try {
            String query1 = "select COUNT(*) FROM KORISNIK where username=?";
            PreparedStatement ps1 = con.prepareStatement(query1);
            ps1.setString(1, string);
            ResultSet res = ps1.executeQuery();
            int count = 0;
            while (res.next()) {
                count = res.getInt(1);
            }
            ps1.close();
            if (count != 0) {
                System.out.println("Vec je definisan taj username za nekog korinsika ,izaberite neki drugi username.");
                return false;
            }
            String query = "insert into KORISNIK(ime,prezime,username,lozinka) values (?,?,?,?)";
            PreparedStatement ps =con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, string1);
            ps.setString(2, string2);
            ps.setString(3,string);
            ps.setString(4, string3);
            int ret = ps.executeUpdate();; 
            ps.close();
            System.out.println("Insertovan je podatak: " + ret);
            if(ret!=0) return true;
            else return false;
        } catch (SQLException ex) {
            Logger.getLogger(MyUserOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    @Override
    public int declareAdmin(String string) {
        try {
            String query = "select idKorisnik FROM KORISNIK where username=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, string);
            ResultSet res = ps.executeQuery();
            int idKorisnik = -1;
            while (res.next()) {
                idKorisnik = res.getInt(1);
                System.out.println("Id korisnika sa tim username: " + idKorisnik);
            }
            ps.close();
            if (idKorisnik == -1) {
                System.out.println("Ne postoji korisnik kod koga je username = " + string);
                return 2;
            }
            //Sad treba proveriti da li je taj korisnik vec Admin.
            query="select COUNT(*) FROM ADMIN where idKorisnik=?";
            ps=con.prepareStatement(query);
            ps.setInt(1, idKorisnik);
            res=ps.executeQuery();
            int isAdmin=0;
            while(res.next()){
                isAdmin=res.getInt(1);
                System.out.println("Broj admina sa tim idKorisnikom je  : " + isAdmin);
                
            }
            ps.close();
            if(isAdmin!=0) {
                return 1;
            }
            
            query = "insert into ADMIN(idKorisnik) values (?)";
            ps=con.prepareStatement(query);
            ps.setInt(1, idKorisnik);
            ps.executeUpdate();
            ps.close();
            return 0;
        } catch (SQLException ex) {
            Logger.getLogger(MyUserOperations.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public Integer getSentPackages(String... strings) {
         int sent=0;
         boolean imaUser=false;
         for(String pom:strings){
             try {
                 String query = "select broj_poslatih from KORISNIK where username=?";
                 PreparedStatement ps = con.prepareStatement(query);
                 ps.setString(1, pom);
                 ResultSet rs=ps.executeQuery();
                 while(rs.next()){
                      sent=rs.getInt(1);
                      System.out.println("Broj poslatih za username : " + pom + " je : " + rs.getInt(1));
                      imaUser=true;
                 }
                 ps.close();
             } catch (SQLException ex) {
                 Logger.getLogger(MyUserOperations.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         if(imaUser==false) return null;
         return sent;
        
    }

    @Override
    public int deleteUsers(String... strings) {
        int ret=0;
        for (String user : strings) {
            try {
                String query = "delete KORISNIK where username=?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, user);
                ret += ps.executeUpdate();
                ps.close();
                
            } catch (SQLException ex) {
                Logger.getLogger(MyUserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ret;
    }

    @Override
    public List<String> getAllUsers() {
        try {
            List<String> ret=new ArrayList<>();
            String query = "select * from KORISNIK ";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ret.add(rs.getString("username"));
            }
            ps.close();
            return ret;
        } catch (SQLException ex) {
            Logger.getLogger(MyUserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
       return null;
    }
    
}
