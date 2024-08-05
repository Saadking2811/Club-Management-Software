package com.club.Model;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EntrantSortant {

    private LocalDateTime time;
    private int membreID;
    private int categoriees;
    private boolean supprime; // Ajout du champ supprime


    public EntrantSortant() {

    }
    public EntrantSortant(LocalDateTime time, int membreID, boolean supprime) {
        this.time = time;
        this.membreID = membreID;
        this.supprime = supprime;
    }


    public LocalDateTime getTime() {return time;}

    public void setTime(LocalDateTime time) {this.time = time;}

    public int getMembreID() {return membreID;}

    public void setMembreID(int membreID) {this.membreID = membreID;}

    public int getCategoriees() {
        return categoriees;
    }
    public void setCategoriees(int categoriees) {
        this.categoriees = categoriees;
    }

    public void ajouterEntrantSortant(Connection connection, int categoriees) throws SQLException{
        String sql;
        if (categoriees == 0) {
            sql = "INSERT INTO entrants (Temps, MembreID) VALUES (?,?)";
        }
        else {
            sql = "INSERT INTO sortants (Temps, MembreID) VALUES (?,?)";
        }
        LocalDateTime currentTime = LocalDateTime.now();
        // Define a formatter for the output
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Format the current date and time as a string
        String formattedTime = currentTime.format(formatter);
        try {
            // Prepare the SQL statement
            PreparedStatement statement2 = connection.prepareStatement(sql);
            // Set the value of the time_column parameter
            statement2.setString(1, formattedTime);
            statement2.setInt(2, membreID);
            // Execute the SQL statement
            statement2.executeUpdate();
            if (categoriees == 0) {
                System.out.println("Membre Entered");
            } else {
                System.out.println("Membre Exited");
            }
            ;
            // Close the statement
            statement2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static List<EntrantSortant> getListEntrantSortantbyDay(Connection connection, String selectedDate, int categoriees) {
        List<EntrantSortant> listentrantssortants = new ArrayList<>();
        String table;
        if (categoriees==0) {table="entrants";}
        else {table="sortants";}
        String query = "SELECT " + table + ".*, utilisateurs.Nom, utilisateurs.Prenom " +
                "FROM " + table + " " +
                "JOIN utilisateurs ON " + table + ".MembreID = utilisateurs.MembreID " +
                "WHERE " + table + ".Temps >= '" + selectedDate + " 00:00:00' " +
                "AND " + table + ".Temps < DATE_ADD('" + selectedDate + "', INTERVAL 1 DAY) ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int membreID = resultSet.getInt("MembreID");
                    Timestamp timestamp = resultSet.getTimestamp("Temps");
                    LocalDateTime localDateTime = timestamp.toLocalDateTime();
                    boolean supprime = resultSet.getBoolean("Supprime");
                    EntrantSortant entrantSortant = new EntrantSortant(localDateTime , membreID, supprime);
                    listentrantssortants.add(entrantSortant);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listentrantssortants;
    }

    public boolean isSupprime() {
        return supprime;
    }

    public void setSupprime(boolean supprime) {
        this.supprime = supprime;
    }
}
