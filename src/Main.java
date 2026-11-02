import dao.*;
import daopsql.AdresDAOPsql;
import daopsql.OVChipkaartDAOPsql;
import daopsql.ProductDAOPsql;
import daopsql.ReizigerDAOPsql;
import domain.Adres;
import domain.OVChipkaart;
import domain.Product;
import domain.Reiziger;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try(Connection myConn = DatabaseConnection.getConnection()){
            //Test P5
            System.out.println("\n\nTest 5:");

            ReizigerDAOPsql rdao = new ReizigerDAOPsql(myConn);
            AdresDAOPsql adao = new AdresDAOPsql(myConn);
            OVChipkaartDAOPsql odao = new OVChipkaartDAOPsql(myConn);
            ProductDAOPsql pdao = new ProductDAOPsql(myConn);

            rdao.setAdresDAO(adao);
            rdao.setOvChipkaartDAO(odao);
            adao.setReizigerDAO(rdao);
            odao.setReizigerDAO(rdao);
            odao.setProductDAO(pdao);
            pdao.setOVChipkaartDAO(odao);

            testP5(rdao, adao, odao, pdao);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void testP5(ReizigerDAO rdao, AdresDAO adao, OVChipkaartDAO odao, ProductDAO pdao) throws SQLException {
        System.out.println("\n---------- Test ProductDAO.findAll() ----------");

        List<Product> producten = pdao.findAll();

        for (Product p : producten) {
            System.out.println("Product " + p.getNaam() + " wordt " + p.getOvchipkaarten().size() + " keer gebruikt");
            System.out.println("\n" + p);

            for (OVChipkaart ov : p.getOvchipkaarten()) {
                System.out.println("  OV-chipkaart: " + ov);
                System.out.println("  Reiziger: " + ov.getReiziger());
                System.out.println("  Adres: " + ov.getReiziger().getAdres());
                System.out.println("\n");
            }
        }
    }
}