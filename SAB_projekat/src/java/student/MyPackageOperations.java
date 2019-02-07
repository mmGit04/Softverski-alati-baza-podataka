/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.PackageOperations;

/**
 *
 * @author Minjoza
 */
public class MyPackageOperations implements PackageOperations {

    Connection con;

    public MyPackageOperations() {
        con = connection.Connect.getConnection();
    }

    public class MyPair implements Pair<Integer, BigDecimal> {

        private Integer l;
        private BigDecimal r;

        public MyPair(Integer l, BigDecimal r) {
            this.l = l;
            this.r = r;
        }

        @Override
        public Integer getFirstParam() {
            return l;
        }

        @Override
        public BigDecimal getSecondParam() {
            return r;
        }
    }
    @Override
    public int insertPackage(int i, int i1, String string, int i2, BigDecimal bd) {
        try {
            String query = "select * from OPSTINA where idOpstina=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();
            boolean post = false;
            while (rs.next()) {
                post = true;
            }
            ps.close();
            if (post == false) {
                System.out.println("Ne postoji opstina sa datim id1.");
                return -1;
            }
            query = "select * from OPSTINA where idOpstina=?";
            ps = con.prepareStatement(query);
            ps.setInt(1, i1);
            rs = ps.executeQuery();
            post = false;
            while (rs.next()) {
                post = true;
            }
            ps.close();
            if (post == false) {
                System.out.println("Ne postoji opstina sa datim id2.");
                return -1;
            }
            query = "select idKorisnik from KORISNIK where username=?";
            ps = con.prepareStatement(query);
            ps.setString(1, string);
            rs = ps.executeQuery();
            int idK = 0;
            while (rs.next()) {
                idK = rs.getInt(1);
            }
            ps.close();
            if (idK == 0) {
                System.out.println("Ne postoji korisnik sa zadatim usernameom.");
                return -1;
            }
            if (!(i2 == 0 || i2 == 1 || i2 == 2)) {
                System.out.println("Nije dobar tip paketa koji ste prosledili.");
                return -1;
            }
            query = "INSERT into PAKET VALUES (?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(query);
            ps.setInt(1, i);
            ps.setInt(2, i1);
            ps.setInt(3, idK);
            ps.setInt(4, i2);
            BigDecimal broj = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
            ps.setBigDecimal(5, broj);
            ps.setNull(6, java.sql.Types.NULL);
            ps.setInt(7, 0);
            ps.setBigDecimal(8, new BigDecimal(BigInteger.ZERO));
            ps.setNull(9, java.sql.Types.NULL);
            int ret = ps.executeUpdate();
            ps.close();
            if (ret != 1) {
                return -1;
            } else {
                query = "select MAX(idPaket) from PAKET";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                int id = 0;
                while (rs.next()) {
                    id = rs.getInt(1);
                }
                ps.close();
                query = "select broj_poslatih from KORISNIK where idKorisnik=?";
                ps = con.prepareStatement(query);
                ps.setInt(1, idK);
                int brojP = 0;
                rs = ps.executeQuery();
                while (rs.next()) {
                    brojP = rs.getInt(1);
                }
                ps.close();
                query = "update KORISNIK set broj_poslatih=? where idKorisnik=?";
                ps = con.prepareStatement(query);
                brojP++;
                ps.setInt(1, brojP);
                ps.setInt(2, idK);

                ps.executeUpdate();
                return id;
            }

        } catch (SQLException ex) {
            Logger.getLogger(MyPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }

    @Override
    public int insertTransportOffer(String string, int i, BigDecimal bd) {
        try {
            String query = "select idKorisnik from KORISNIK where username=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, string);
            ResultSet rs = ps.executeQuery();
            int idK = 0;
            while (rs.next()) {
                idK = rs.getInt(1);
            }
            ps.close();
            if (idK == 0) {
                System.out.println("Ne postoji korisnik sa datim usernamemom.");
                return -1;
            }
            query = "insert into PONUDA values(?,?,?)";
            ps = con.prepareStatement(query);
            ps.setInt(1, idK);
            ps.setInt(2, i);
            BigDecimal broj = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
            ps.setBigDecimal(3, broj);
            int ret = ps.executeUpdate();
            ps.close();
            if (ret == 0) {
                System.out.println("Neuspesno insertovanje u ponudu.");
                return -1;
            }
            query = "select MAX(idPonuda) from PONUDA";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            int id = 0;
            while (rs.next()) {
                id = rs.getInt(1);
            }
            ps.close();
            return id;
        } catch (SQLException ex) {
            Logger.getLogger(MyPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }

    private double euclidean_distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    //Ovo nisam testirala 
    @Override
    public boolean acceptAnOffer(int i) {
        try {
            String query = "select * from PONUDA where idPonuda=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();
            int idK = 0;
            int idP = 0;
            BigDecimal procenat = new BigDecimal(BigInteger.ONE);
            while (rs.next()) {
                idK = rs.getInt(2);
                idP = rs.getInt(3);
                procenat = rs.getBigDecimal(4);
            }
            ps.close();
            if (idK == 0) {
                System.out.println("Nismo pronasli ponudu sa tim ID");
                return false;
            }
            query = "select * from PAKET where idPaket=?";
            ps = con.prepareStatement(query);
            ps.setInt(1, idP);
            rs = ps.executeQuery();
            BigDecimal tezina = new BigDecimal(BigInteger.ZERO);
            int tip = 0;
            int idOpstinaOd = 0;
            int idOpstinaDo = 0;
            while (rs.next()) {
                idOpstinaOd = rs.getInt(2);
                idOpstinaDo = rs.getInt(3);
                tip = rs.getInt(5);
                tezina = rs.getBigDecimal(6);
            }
            ps.close();
            query = "select * FROM OPSTINA where idOpstina=?";
            ps = con.prepareStatement(query);
            ps.setInt(1, idOpstinaOd);
            rs = ps.executeQuery();
            int x1 = 0;
            int y1 = 0;
            while (rs.next()) {
                x1 = rs.getInt(3);
                y1 = rs.getInt(4);
            }
            ps.close();
            query = "select * FROM OPSTINA where idOpstina=?";
            ps = con.prepareStatement(query);
            ps.setInt(1, idOpstinaDo);
            rs = ps.executeQuery();
            int x2 = 0;
            int y2 = 0;
            while (rs.next()) {
                x2 = rs.getInt(3);
                y2 = rs.getInt(4);
            }
            ps.close();
            BigDecimal cena = new BigDecimal(BigInteger.ZERO);
            double distance = euclidean_distance(x1, y1, x2, y2);
            procenat = procenat.divide(new BigDecimal(100));
            switch (tip) {
                case 0:
                    cena = new BigDecimal(10.0D * distance).multiply(procenat.add(new BigDecimal(1)));
                case 1:
                    cena = new BigDecimal((25.0D + tezina.doubleValue() * 100.0D) * distance).multiply(procenat.add(new BigDecimal(1)));
                case 2:
                    cena = new BigDecimal((75.0D + tezina.doubleValue() * 600.0D) * distance).multiply(procenat.add(new BigDecimal(1)));
            }
            long trenutnV = System.currentTimeMillis();
            java.util.Date date = new Date(trenutnV);
            Object param = new java.sql.Timestamp(date.getTime());

            query = "update PAKET set idKurir=? ,status=?,cena=?,vreme=?";
            ps = con.prepareStatement(query);
            ps.setInt(1, idK);
            ps.setInt(2, 1);
            ps.setBigDecimal(3, cena);
            ps.setObject(4, param);
            int ret = ps.executeUpdate();
            ps.close();
            if (ret == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MyPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<Integer> getAllOffers() {
        try {
            String query="select idPonuda from PONUDA";
            List<Integer> lista=new ArrayList<>();
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                lista.add(rs.getInt(1));
            }
            ps.close();
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(MyPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
     }

    @Override
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int i) {
        try {
            List<Pair<Integer,BigDecimal>> lista=new ArrayList<>();
            String query = "select * from PONUDA where idPaket=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();
            int idP=0;
            BigDecimal procenat=new BigDecimal(BigInteger.ZERO);
            while (rs.next()) {
                idP=rs.getInt(1);
                procenat=rs.getBigDecimal(4);
                Pair<Integer,BigDecimal> p1=new MyPair(idP,procenat);
                lista.add(p1);
            }
            ps.close();
            return lista;
            
        } catch (SQLException ex) {
            Logger.getLogger(MyPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public boolean deletePackage(int i) {
        try {
            String query = "Delete PAKET where idPaket=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, i);
            int ret = ps.executeUpdate();
            ps.close();
            if (ret == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MyPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    @Override
    public boolean changeWeight(int i, BigDecimal bd) {
        try {
            String query = "update PAKET set tezina=? where idPaket=?";
            PreparedStatement ps = con.prepareStatement(query);
            BigDecimal broj = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
            ps.setBigDecimal(1, broj);
            ps.setInt(2, i);
            int ret = ps.executeUpdate();
            ps.close();
            if (ret != 1) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MyPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    @Override
    public boolean changeType(int i, int i1) {
        try {
            if (!(i1 == 0 || i1 == 1 || i1 == 2)) {
                return false;
            }
            String query = "update PAKET set tezina=? where idPaket=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, i1);
            ps.setInt(2, i);
            int ret = ps.executeUpdate();
            ps.close();
            if (ret != 1) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MyPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public Integer getDeliveryStatus(int i) {
        try {
            String query="select status from PAKET where idPaket=?";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setInt(1, i);
            ResultSet rs=ps.executeQuery();
            int status=-1;
            while(rs.next()){
                status=rs.getInt(1);
            }
            if(status==-1) {System.out.println("Ne postoji paket sa datim id.");return null;}
            else return status;
                
        } catch (SQLException ex) {
            Logger.getLogger(MyPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
     
    }
    //Ovde kaze ako cena nije jos izracuna treba da vrati null ,meni se kad se ubacuje ta vrednost vec postavlja na null.
    @Override
    public BigDecimal getPriceOfDelivery(int i) {
        try {
            String query="select cena from PAKET where idPaket=?";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setInt(1, i);
            ResultSet rs=ps.executeQuery();
            BigDecimal cena=new BigDecimal(BigInteger.ZERO);
            boolean postoji=false;
            while(rs.next()){
                postoji=true;
                cena=rs.getBigDecimal(1);
            }
           if(postoji==false) {System.out.println("Ne postoji paket sa datim id.");return null;}
            else return cena;
                
        } catch (SQLException ex) {
            Logger.getLogger(MyPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    //Ovde vraca tip podatka DATE ali sta je sa vremenom odnosno trenutkom kada je prihvacen neki zahtev.
    @Override
    public Date getAcceptanceTime(int i) {
          try {
            String query="select vreme from PAKET where idPaket=? and status!=0";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setInt(1, i);
            ResultSet rs=ps.executeQuery();
            java.sql.Date datum=null;
            boolean postoji=false;
            while(rs.next()){
                postoji=true;
                datum=rs.getDate(1);
            }
           if(postoji==false) {System.out.println("Ne postoji paket sa datim id. koji je prihvacen.");return null;}
           else return datum;
                
        } catch (SQLException ex) {
            Logger.getLogger(MyPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int i) {
         try {
            List<Integer> lista=new ArrayList<>();
            String query = "select idPaket from PAKET where tip_paketa=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();
            int idP=0;
            while (rs.next()) {
                idP=rs.getInt(1);
                lista.add(idP);
            }
            ps.close();
            return lista;
            
        } catch (SQLException ex) {
            Logger.getLogger(MyPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Integer> getAllPackages() {
        try {
            String query="select idPaket from PAKET";
            List<Integer> lista=new ArrayList<>();
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                lista.add(rs.getInt(1));
            }
            ps.close();
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(MyPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Integer> getDrive(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int driveNextPackage(String string) {
        try {
            String query="select idKorisnik from KORISNIK k1,KURIR k2 where k1.idKorisnik=k2.idKorisnik and k1.username=?";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setString(1, string);
            ResultSet rs=ps.executeQuery();
            int idK=0;
            while(rs.next()){
                idK=rs.getInt(1);
                
            }
            ps.close();
            if(idK==0) {System.out.println("Ne postoji kurir sa datim usernamom."); return -2;}
            query="select * from VOZNJA where idKurir=?";
            ps=con.prepareStatement(query);
            ps.setInt(1, idK);
            rs=ps.executeQuery();
            int idV=0;
            boolean postoji=false;
            Object vreme=null;
            int brojIsporuka=0;
            BigDecimal prihod=new BigDecimal(BigInteger.ZERO);
            
            while(rs.next()){
                postoji=true;System.out.println("Postoji trenutna voznja za definisanog kurira.");
                idV=rs.getInt(1);
                prihod=rs.getBigDecimal(4);
                brojIsporuka=rs.getInt(5);
                vreme=rs.getTimestamp(3);
            }
            ps.close();
            if(postoji==false){
                long trenutnV = System.currentTimeMillis();
                java.util.Date date = new Date(trenutnV);
                Object trenutno = new java.sql.Timestamp(date.getTime());//Trenutno vreme pocetka voznje.
                vreme=trenutno;
                query="insert into VOZNJA values(?,?,?,?)";
                ps=con.prepareStatement(query);
                ps.setInt(1, idK);
                ps.setObject(2, vreme);
                BigDecimal bd=new BigDecimal(BigInteger.ZERO);
                BigDecimal broj = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
                ps.setBigDecimal(3, broj);
                ps.setInt(4,0);
                int res=ps.executeUpdate();
                ps.close();
                query="select MAX(idVoznja) from VOZNJA";
                ps=con.prepareStatement(query);
                rs=ps.executeQuery();
                idV=0;
                if(rs.next()){
                   idV=rs.getInt(1);
                }
                ps.close();
                query="select idPaket from PAKET where idKurir=? and vreme<? and status=?";
                ps=con.prepareStatement(query);
                ps.setInt(1, idK);
                ps.setObject(2, vreme);
                ps.setInt(3, 1);
                rs=ps.executeQuery();
                List<Integer> paketi=new ArrayList<>();
                boolean imaPaketaZaVoznju=false;
                while(rs.next()){
                    imaPaketaZaVoznju=true;
                    paketi.add(rs.getInt(1));
                }
                ps.close();
                if(imaPaketaZaVoznju==false) {System.out.println("Ne postoje paketi koji su trenutno dostupni za razvozenje za tog kurira."); return -1;}
                for(int idP:paketi){
                query="insert into PREVOZI values(?,?)";
                ps=con.prepareStatement(query);
                ps.setInt(1, idP);
                ps.setInt(2, idV);
                ps.execute();
                ps.close();
                query="update PAKET set status=? where idKurir=? and vreme<?";//Svim onima koji pripadaju ovoj voznji setujem da su pokupljeni.
                ps=con.prepareStatement(query);
                ps.setInt(1, 2);
                ps.setInt(2, idK);
                ps.setObject(3, vreme);
                res=ps.executeUpdate();
                ps.close();
              }
            }
            //Sad ide standardno za obradu paketa.
            query="select p.idPaket,p.idOpstinaOd,p.idOpstinaDo,p.cena from PAKET p,PREVOZI pr where pr.idPaket=p.idPaket and v.idVoznja=? order by p.vreme";
            ps=con.prepareStatement(query);
            ps.setInt(1, idV);
            int idSlanje=0;
            rs=ps.executeQuery();
            boolean imaJosPaketa=false;
            int idOpstinaOd=0;
            int idOpstinaDo=0;
            BigDecimal cena=new BigDecimal(BigInteger.ZERO);
            if(rs.next()){
                imaJosPaketa=true;
                idSlanje=rs.getInt(1);
                idOpstinaOd=rs.getInt(2);
                idOpstinaDo=rs.getInt(3);
                cena=rs.getBigDecimal(4);
            }
            ps.close();
            if(imaJosPaketa==false){
                //Ovo je kraj voznje ,potrebno je izbrisati objekat voznja i azurirati prihod i broj isporucenih.
            query="update KURIR set ispo_paketi=?, profit=? where idKurir=? ";
            ps=con.prepareStatement(query);
            ps.setInt(1,brojIsporuka);
     //Ovde vrv treba srediti prihod da se smanji na 3 decimale jer ga je on mozda tamo prosirio.
            ps.setBigDecimal(2, prihod);
            ps.setInt(3, idK);
            int resU = ps.executeUpdate();
            System.out.println("Updatovali smo broj isprucenih i prihod kod kurira." + resU);
            ps.close();
            query="delete from VOZNJA where idKurir =?";
            ps=con.prepareStatement(query);
            ps.setInt(1, idK);
            int resB=ps.executeUpdate();
            System.out.println("Izbrisan je red u tabeli voznja koji odgovara ovom kuriru." + resB);
            ps.close();
            
            System.out.println("Ne postoji vise paketa u okviru ove voznje i voznja je finalizirana.");

            return -1;}
            query="delete from PREVOZI where idPaket=?";
            ps=con.prepareStatement(query);
            ps.setInt(1, idSlanje);
            int resB=ps.executeUpdate();
            ps.close();
            query="update PAKET set status=3 where idPaket=?";
            ps=con.prepareStatement(query);
            ps.executeUpdate();
            ps.close();
            query="update VOZNJA set broj_isporuka=? where idVoznja=?";
            ps=con.prepareStatement(query);
            //UPDATOVANJE VOZNJE I ONDA POVRCAJ VREDNOSTI.
            
       //1.
       
            
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(MyPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
