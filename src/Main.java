import dao.*;
import daopsql.AdresDAOPsql;
import daopsql.ReizigerDAOPsql;
import domain.Adres;
import domain.Reiziger;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try(Connection myConn = DatabaseConnection.getConnection()){
            //Test P3
            System.out.println("\n\nTest 3:");

            ReizigerDAOPsql rdao = new ReizigerDAOPsql(myConn);
            AdresDAOPsql adao = new AdresDAOPsql(myConn);

            rdao.setAdresDAO(adao);
            adao.setReizigerDAO(rdao);

            testP3(rdao, adao);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void testP3(ReizigerDAO rdao, AdresDAO adao) throws SQLException {
        //Haal alle reizigers + adressen op
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("ReizigerDAO.findAll() geeft:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }

        //Haal alle adressen op
        List<Adres> adressen = adao.findAll();
        System.out.println("\nAdresDAO.findAll() geeft:");
        for (Adres a : adressen) {
            System.out.println(a);
        }

        //Nieuwe reiziger + nieuw adres koppelen
        Reiziger valentijn = new Reiziger(77, "V", null, "Tol", LocalDate.of(2003,9,26));
        System.out.println("\nReiziger valentijn geeft: " +  valentijn);
        Adres valentijnAdres = new Adres(77, "3704AZ", "13", "Harmonielaan", "Zeist", null);
        System.out.println("Adres geeft zonder koppeling: " + valentijnAdres);
        valentijn.setAdres(valentijnAdres);
        valentijnAdres.setReiziger(valentijn);

        System.out.println("\nTest opslaan reiziger + adres:");
        rdao.save(valentijn);

        System.out.println("Reiziger na save: " + rdao.findById(77));
        System.out.println("Adres na save: " + adao.findByReiziger(valentijn));
        System.out.println();

        //update van reiziger en adres
        System.out.println("\nTest update reiziger + adres");
        valentijn.setAchternaam("Tollenaar");
        valentijn.getAdres().setStraat("Laan van Vollenhove");

        rdao.update(valentijn);

        System.out.println("Reiziger na update: " + rdao.findById(77));
        System.out.println("Adres na update: " + adao.findByReiziger(valentijn));

        //delete van reiziger en adres
        System.out.println("\nTest delete reiziger + adres");
        rdao.delete(valentijn);

        System.out.println("Reiziger na delete: " + rdao.findById(77));
        System.out.println("Adres na delete: " + adao.findByReiziger(valentijn));
        System.out.println();

        //findbyReiziger na delete
        System.out.println("ReizigerDAO.findAll() na delete geeft:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }

        //findByGbdatum
        String gbdatumFind = rdao.findByGbdatum(LocalDate.of(2002, 9, 17)).toString();
        System.out.println("\nfindByGbdatum geeft: " + gbdatumFind);


    }
}