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
        Reiziger r1 = rdao.findById(2);
        System.out.println("Producten van reiziger " + r1.getId() + " (" + r1.getNaam() + "):");

        boolean gevonden = false;
        for (OVChipkaart ov : r1.getOvchipkaarten()) {
            for (Product p : ov.getProducten()) {
                System.out.println("  kaart " + ov.getKaartNummer() + ": " + p);
                gevonden = true;
            }
        }
        if (!gevonden) {
            System.out.println("  geen producten");
        }

        Product p = new Product(901, "Dalurenkorting", "Korting buiten de spits", 12.50);
        pdao.save(p);

        Reiziger r = new Reiziger(82, "P", null, "Producttester", LocalDate.of(2000, 1, 1));
        rdao.save(r);

        OVChipkaart ov = new OVChipkaart(94001, LocalDate.of(2030, 1, 1), 2, 30.0, r);
        r.addOVChipkaart(ov);
        odao.save(ov);

        ov.addProduct(p);
        odao.update(ov);

        System.out.println(odao.findByReiziger(r));
        System.out.println(pdao.findByOVChipkaart(ov));

        rdao.delete(r);
        pdao.delete(p);
    }
}