package com.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

//probably need to add logging and better error handling
//still need to connect databases via em_db_path
//will need to create a file to setup/reset the db rather than doing it from each app
public class Main {
    public static void main(String[] args){
        smallMain();
    }

    public static void smallMain(){
        Scanner scanner = new Scanner(System.in);
        try (Connection conn = testsql.connect()) {
            Statement stmt = conn.createStatement();
            
            int failed = 0;

            while(true){
                System.out.print("Please enter your username: ");
                String user = scanner.nextLine();
                System.out.print("Please enter your password: ");
                String pasw = scanner.nextLine();

                if(user.trim().isEmpty() || pasw.trim().isEmpty()){
                    System.out.println("Username or Password is empty. Please try again");
                    continue;
                }

                stmt.execute("Select * from users where username = '"
                    +user.trim()+"' and password = '"+pasw.trim()+"'");
                
                ResultSet row = stmt.getResultSet();
                if(!row.next()){
                    failed += 1;
                    System.out.println("Invalid username or password");
                }else if (!row.getString("role").equals("manager")){
                    System.out.println("This account does not have access to this app");
                    System.exit(1);
                }else{
                    failed = 0;
                    System.out.println("Welcome, "+row.getString("full_name"));
                    Menu.menu(stmt, scanner, row.getString("full_name"), row.getString("id"));
                }

                if(failed == 3){
                    System.out.println("Too many failed attempts");
                    System.exit(1);
                }

            }

        }catch(Exception e){
            System.err.println("Error executing SQL script: " + e.getMessage());
            e.printStackTrace();
        }
        scanner.close();
    }
}
