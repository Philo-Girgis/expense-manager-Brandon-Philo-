package com.manager;

import java.sql.Statement;
import java.util.Scanner;
import java.sql.ResultSet;

public class Menu {
    public static void menu(Statement stmt, Scanner scanner, ResultSet manager){
        while(true){
            System.out.println("Menu Options");
            System.out.println("1: List pending expenses");
            System.out.println("2: Review an expense");
            System.out.println("3: Reports");
            System.out.println("0: Exit");
            System.out.print("Please enter the number of your selection: ");
            String input = scanner.nextLine().trim();
            //stmt.execute("Select * from users");
            //ResultSet row = stmt.getResultSet();
            //row.next();
            //row.getString("role");
            try{
                if(input.equals("1")){
                    System.out.println("list pending expenses to be implemented");
                }else if(input.equals("2")){
                    System.out.println("review an expense to be implemented");
                }else if(input.equals("3")){
                    System.out.println("reports to be implemented");
                }else if(input.equals("0")){
                    System.out.println("Goodbye, "+manager.getString("full_name"));
                    break;
                }else{
                    System.out.println("Invalid input, please try again");
                }
            }catch(Exception e){
                System.err.println("Error executing SQL script: " + e.getMessage());
                e.printStackTrace();
            }         
        }
    }
}
