package daopsql;

import dao.AdresDAO;
import dao.ReizigerDAO;
import domain.Adres;
import domain.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAO adresDAO;

    public ReizigerDAOPsql(Connection conn) throws SQLException {
        this.conn = conn;
    }

    public void setAdresDAO(AdresDAO adresDAO) throws SQLException {
        this.adresDAO = adresDAO;
    }

    @Override
    public Boolean save(Reiziger r) {
        String sql = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, r.getId());
            pst.setString(2, r.getVoorletters());
            pst.setString(3, r.getTussenvoegsel());
            pst.setString(4, r.getAchternaam());
            pst.setDate(5, java.sql.Date.valueOf(r.getGeboortedatum()));

            boolean reizigerSaved = pst.executeUpdate() > 0;

            if (reizigerSaved && r.getAdres() != null) {
                adresDAO.save(r.getAdres());
            }

            return reizigerSaved;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean update(Reiziger r) {
        String sql = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(5, r.getId());
            pst.setString(1, r.getVoorletters());
            pst.setString(2, r.getTussenvoegsel());
            pst.setString(3, r.getAchternaam());
            pst.setDate(4, java.sql.Date.valueOf(r.getGeboortedatum()));

            boolean updatedReiziger = pst.executeUpdate() > 0;

            Adres bestaandAdres = adresDAO.findByReiziger(r);

            if (r.getAdres() != null) {
                if (bestaandAdres != null) {
                    adresDAO.update(r.getAdres());
                } else {
                    adresDAO.save(r.getAdres());
                }
            } else if (bestaandAdres != null){
                    adresDAO.delete(bestaandAdres);
            }

            return updatedReiziger;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean delete(Reiziger r) {
        Adres adres = adresDAO.findByReiziger(r);
        if (adres != null) {
            adresDAO.delete(adres);
        }

        String sql = "DELETE FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, r.getId());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Reiziger findById(int id) {
        String sql = "SELECT reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Reiziger r = new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum").toLocalDate()
                );
                Adres adres = adresDAO.findByReiziger(r);
                r.setAdres(adres);
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Reiziger> findByGbdatum(LocalDate geboortedatum) {
        List<Reiziger> reizigers = new ArrayList<>();
        String sql = "SELECT reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum FROM reiziger WHERE geboortedatum = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setDate(1, java.sql.Date.valueOf(geboortedatum));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Reiziger r = new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum").toLocalDate()
                );
                Adres adres = adresDAO.findByReiziger(r);
                r.setAdres(adres);

                reizigers.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() {
        List<Reiziger> reizigers = new ArrayList<>();
        String sql = "SELECT reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum FROM reiziger";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Reiziger r = new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum").toLocalDate()
                );

                Adres adres = adresDAO.findByReiziger(r);
                r.setAdres(adres);

                reizigers.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reizigers;
    }
}
