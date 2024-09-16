/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ca2;

import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author rafael
 */
public class CA2 {

    /**
     * @param args the command line arguments
     */
     public static void main(String[] args) {
       
        Scanner myScanner = new Scanner(System.in);
        
        
        // code to connect to the database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/houserenting", "root", "pass1234?");
            System.out.println("\n✔ We are successfully connected to the HouseRenting database \n");
          
            int chooseNumber = 0;
            
            do {
                try {
                    System.out.print("📝 Enter a corresponding number to the options bellow: "
                            + "\n 1 – Retrieve all clients along with their associated properties."
                            + "\n 2 – Calculate the total monthly rent for each client. "
                            + "\n 3 – Count the total number of properties owned by each owner. "
                            + "\n 4 – Identify Oweners who own multiple properties."
                            + "\n 5 – List all clients along with the total rent they pay annually, sorted in ascending order (i.e. lowest rent at the top)."
                            + "\n 6 – Find the client who pays the highest monthly rent."
                            + "\n 7 – Choose a letter to list all properties rented out by all clients whose name begins with it."
                            + "\n 8 – Quit the program. \n"); 
                    System.out.println("Type your option: ");
                    chooseNumber = myScanner.nextInt();
                    
                    switch (chooseNumber) {
                        case 1:
                            Statement statement_one = con.createStatement();
                            String query1 = "SELECT c.client_num, c.client_name, p.property_no, p.property_address " +
                                    "FROM Client c " +
                                    "JOIN Rentals r ON c.client_num = r.client_num " +
                                    "JOIN Properties p ON r.property_no = p.property_no";
                            ResultSet queryOutput1 = statement_one.executeQuery(query1);
                            System.out.println("\nList of all clients and their related properties:\n");
                            while (queryOutput1.next()) {
                                System.out.println("Client Number: " + queryOutput1.getString("client_num"));
                                System.out.println("Client Name: " + queryOutput1.getString("client_name"));
                                System.out.println("Property Number: " + queryOutput1.getString("property_no"));
                                System.out.println("Property Address: " + queryOutput1.getString("property_address"));
                                System.out.println("--------------------");
                            }
                            System.out.println("\n");
                            queryOutput1.close();
                            statement_one.close();
                            break;

                        case 2:
                            Statement statement_two = con.createStatement();
                            String query2 = "SELECT c.client_num, c.client_name, SUM(p.monthly_rent) AS total_monthly_rent " +
                                            "FROM Client c " +
                                            "JOIN Rentals r ON c.client_num = r.client_num " +
                                            "JOIN Properties p ON r.property_no = p.property_no " +
                                            "GROUP BY c.client_num, c.client_name";
                            ResultSet queryOutput2 = statement_two.executeQuery(query2);
                            System.out.println("\nTotal monthly rent for each client:\n");
                            while (queryOutput2.next()) {
                                System.out.println("Client Number: " + queryOutput2.getString("client_num"));
                                System.out.println("Client Name: " + queryOutput2.getString("client_name"));
                                System.out.println("Total Monthly Rent: " + queryOutput2.getDouble("total_monthly_rent"));
                                System.out.println("--------------------");
                            }
                            System.out.println("\n");
                            queryOutput2.close();
                            statement_two.close();
                            break;

                        case 3:
                            Statement statement_three = con.createStatement();
                            String query3 = "SELECT o.owner_no, o.owner_name, COUNT(*) AS total_properties_owned " +
                                            "FROM Owner o " +
                                            "JOIN Properties p ON o.owner_no = p.owner_no " +
                                            "GROUP BY o.owner_no, o.owner_name";
                            ResultSet queryOutput3 = statement_three.executeQuery(query3);
                            System.out.println("\nTotal number of properties owned by each owner:\n");
                            while (queryOutput3.next()) {
                                System.out.println("Owner Number: " + queryOutput3.getString("owner_no"));
                                System.out.println("Owner Name: " + queryOutput3.getString("owner_name"));
                                System.out.println("Total Properties Owned: " + queryOutput3.getInt("total_properties_owned"));
                                System.out.println("--------------------");
                            }
                            System.out.println("\n");
                            queryOutput3.close();
                            statement_three.close();
                            break;

                        case 4:
                            Statement statement_four = con.createStatement();
                            String query4 = "SELECT " +
                                               "o.owner_no, " +
                                               "o.owner_name, " +
                                               "COUNT(*) AS total_properties_owned " +
                                           "FROM " +
                                               "Owner o " +
                                           "JOIN " +
                                               "Properties p ON o.owner_no = p.owner_no " +
                                           "GROUP BY " +
                                               "o.owner_no, " +
                                               "o.owner_name " +
                                           "HAVING " +
                                               "COUNT(*) > 1";
                            ResultSet queryOutput4 = statement_four.executeQuery(query4);

                            System.out.println("\nOwners who own multiple properties:\n");
                            while (queryOutput4.next()) {
                                System.out.println("Owner Number: " + queryOutput4.getString("owner_no"));
                                System.out.println("Owner Name: " + queryOutput4.getString("owner_name"));
                                System.out.println("Total Properties Owned: " + queryOutput4.getInt("total_properties_owned"));
                                System.out.println("--------------------");
                            }
                            System.out.println("\n");

                            queryOutput4.close();
                            statement_four.close();
                            break;

                        case 5:
                            Statement statement_five = con.createStatement();
                            String query5 = "SELECT c.client_num, c.client_name, SUM(p.monthly_rent * 12) AS total_rent_annually " +
                                            "FROM Client c " +
                                            "JOIN Rentals r ON c.client_num = r.client_num " +
                                            "JOIN Properties p ON r.property_no = p.property_no " +
                                            "GROUP BY c.client_num, c.client_name " +
                                            "ORDER BY total_rent_annually ASC";
                            ResultSet queryOutput5 = statement_five.executeQuery(query5);
                            System.out.println("\nClients along with the total rent they pay annually (sorted in ascending order):\n");
                            while (queryOutput5.next()) {
                                System.out.println("Client Number: " + queryOutput5.getString("client_num"));
                                System.out.println("Client Name: " + queryOutput5.getString("client_name"));
                                System.out.println("Total Rent Annually: " + queryOutput5.getDouble("total_rent_annually"));
                                System.out.println("--------------------");
                            }
                            System.out.println("\n");
                            queryOutput5.close();
                            statement_five.close();
                            break;

                        case 6:
                            Statement statement_six = con.createStatement();
                            String query6 = "SELECT c.client_num, c.client_name, MAX(p.monthly_rent) AS highest_monthly_rent " +
                                            "FROM Client c " +
                                            "JOIN Rentals r ON c.client_num = r.client_num " +
                                            "JOIN Properties p ON r.property_no = p.property_no " +
                                            "GROUP BY c.client_num, c.client_name " +
                                            "ORDER BY highest_monthly_rent DESC " +
                                            "LIMIT 1";
                            ResultSet queryOutput6 = statement_six.executeQuery(query6);
                            System.out.println("\nClient who pays the highest monthly rent:\n");
                            if (queryOutput6.next()) {
                                System.out.println("Client Number: " + queryOutput6.getString("client_num"));
                                System.out.println("Client Name: " + queryOutput6.getString("client_name"));
                                System.out.println("Highest Monthly Rent: " + queryOutput6.getDouble("highest_monthly_rent"));
                            } else {
                                System.out.println("No clients found.");
                            }
                            System.out.println("\n");
                            queryOutput6.close();
                            statement_six.close();
                            break;

                        case 7:
                            Scanner myInput = new Scanner(System.in);
                            String initialLetter = "";
                            boolean validInput = false;
                            do {
                                try {
                                    System.out.print("Enter the initial letter of the client's name: ");
                                    initialLetter = myInput.nextLine().toUpperCase();
                                    if (initialLetter.length() == 1 && Character.isLetter(initialLetter.charAt(0))) {
                                        validInput = true;
                                    } else {
                                        System.out.println("Please enter a single letter.");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Invalid input. Please try again.");
                                    myInput.nextLine(); // consume invalid input
                                }
                            } while (!validInput);

                            Statement statement_seven = con.createStatement();
                            String query7 = "SELECT c.client_name, p.property_no, p.property_address " +
                                            "FROM Client c " +
                                            "JOIN Rentals r ON c.client_num = r.client_num " +
                                            "JOIN Properties p ON r.property_no = p.property_no " +
                                            "WHERE c.client_name LIKE '" + initialLetter + "%' " +
                                            "ORDER BY c.client_name, p.property_no";
                            ResultSet queryOutput7 = statement_seven.executeQuery(query7);

                            boolean foundClients = false;
                            System.out.println("\nProperties rented out by clients whose name begins with '" + initialLetter + "':\n");
                            while (queryOutput7.next()) {
                                foundClients = true;
                                System.out.println("Client Name: " + queryOutput7.getString("client_name"));
                                System.out.println("Property Number: " + queryOutput7.getString("property_no"));
                                System.out.println("Property Address: " + queryOutput7.getString("property_address"));
                                System.out.println("--------------------");
                            }
                            if (!foundClients) {
                                System.out.println("There are no clients with names beginning with '" + initialLetter + "'.");
                            }
                            System.out.println("\n");

                            queryOutput7.close();
                            statement_seven.close();
                            break;

                        case 8:
                            System.out.println("\n --End of the program.-- \n");
                            break;

                        default:
                            System.out.println("Not in the options");
                    }

                } catch (Exception e) {
                    System.out.println("It's not a valid number, try any number from 1 to 8");
                    myScanner.next();
                }
            } while (chooseNumber != 8);

        } catch (SQLException e) {
            System.out.println("SQL Error -->");
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
        } catch (ClassNotFoundException e) {
            System.out.println("Class Error --" + e.getMessage());
        }
    }
}

    

