package com.manager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    private static Path schema = Path.of("sql", "schema.sql");
    private static Path seed = Path.of("sql", "seed.sql");
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        try (Connection conn = testsql.connect()) {
            Statement stmt = conn.createStatement();
            // Read the entire file content into a String
            String content = Files.readString(schema) +" "+ Files.readString(seed);
            // Split the script by semicolons to get individual SQL statements
            String[] statements = content.split(";");
            //creating and populating tables
            for (String sql : statements) {
                String trimmedSql = sql.trim();
                // Skip empty lines or comments
                if (!trimmedSql.isEmpty() && !trimmedSql.startsWith("--")) {
                    stmt.execute(trimmedSql);
                }
            }
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

                stmt.execute(
    "Select username, password, role, full_name from users where username = '"+user.trim()+"' and password = '"+pasw.trim()+"'");
                
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
    }
}
