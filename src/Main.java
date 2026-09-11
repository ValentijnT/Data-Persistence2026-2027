import util.DatabaseConnection;

import java.sql.*;
public class Main {
    public static void main(String[] args) {

        try(Connection myConn = DatabaseConnection.getConnection()){

            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("SELECT * FROM reiziger");

            System.out.println("Alle reizigers:");
            int nummer = 0;

            while(myRs.next()){
                nummer ++;

                String voorletters = myRs.getString("voorletters");
                String achternaam = myRs.getString("achternaam");
                String geboortedatum = myRs.getString("geboortedatum");
                String tussenvoegsel = (myRs.getString("tussenvoegsel") != null) ? myRs.getString("tussenvoegsel") + " " : "";

                System.out.println("#" + nummer + ": " + voorletters + ". " + tussenvoegsel + achternaam + " (" + geboortedatum + ")");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
}