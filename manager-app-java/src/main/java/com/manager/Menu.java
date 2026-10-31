package com.manager;

import java.sql.Statement;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import de.vandermeer.asciitable.AsciiTable;
import java.time.LocalDate;

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
                    String pending = """
                        Select e.id, u.full_name, e.expense_date, c.name,
                        e.amount, e.description from expenses as e
                        inner join users as u on u.id = e.user_id
                        inner join categories as c on c.id = e.category_id
                        inner join approvals as a on a.expense_id = e.id
                        where a.status like "pending"
                        order by e.expense_date
                    """;
                    stmt.execute(pending);
                    ResultSet rows = stmt.getResultSet();
                    if (!rows.isBeforeFirst()){
                        System.out.println("No pending expenses");
                    }else{
                        printRows(rows);
                    }
                    
                }else if(input.equals("2")){
                    System.out.println("review an expense to be implemented");
                    //ask for expense id and whether you wanna approve or deny
                    //then handle accordingly
                    LocalDate today = LocalDate.now();
                    System.out.println("Today's date: "+today);
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

    public static void printRows(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        AsciiTable at = new AsciiTable();

        // Add headers
        String[] headers = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            headers[i - 1] = metaData.getColumnLabel(i);
        }
        at.addRule();
        at.addRow((Object[]) headers);
        at.addRule();

        // Add rows
        while (rs.next()) {
            String[] row = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getString(i);
            }
            at.addRow((Object[]) row);
            at.addRule();
        }

        System.out.println(at.render());
    }

}
