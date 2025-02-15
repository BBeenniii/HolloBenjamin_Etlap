package com.example.etlap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtelDB {
    private static final String URL = "jdbc:mysql://localhost:3306/etlapdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static List<Etel> getEtlap() {
        List<Etel> etlap = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM etlap")) {

            while (rs.next()) {
                Etel etel = new Etel(
                        rs.getInt("id"),
                        rs.getString("nev"),
                        rs.getString("kategoria"),
                        rs.getInt("ar")
                );
                etlap.add(etel);
            }
        } catch (SQLException e) {
            System.out.println("Hiba az adatbázis lekérdezésnél: " + e.getMessage());
        }
        return etlap;
    }
}
