package com.manager;

import java.sql.Statement;
import java.util.Scanner;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import de.vandermeer.asciitable.AsciiTable;
import com.opencsv.CSVWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Menu {
    public static void menu(Statement stmt, Scanner scanner, String manager_name, String manager_id){
        while(true){
            System.out.println("Menu Options");
            System.out.println("1: List pending expenses");
            System.out.println("2: Review an expense");
            System.out.println("3: Reports");
            System.out.println("0: Exit");
            System.out.print("Please enter the number of your selection: ");
            String input = scanner.nextLine().trim();
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
                    
                    System.out.print("Please enter a pending expense id: ");
                    String id = scanner.nextLine().trim();
                    if (!id.matches("\\d+")){
                        System.out.println("Invalid input, returning to Menu");
                        continue;
                    }
                    String check = """
                        Select * from approvals
                        where status like "pending" and expense_id =
                    """ + id;
                    stmt.execute(check);
                    ResultSet rows = stmt.getResultSet();
                    if (!rows.isBeforeFirst()){
                        System.out.println("Invalid expense id, returning to Menu");
                        continue;
                    }

                    String update = "Update approvals set ";
                    update += "reviewer_id = '" +manager_id+"', ";
                    update += "review_date = '"+LocalDate.now()+"', ";
                    
                    System.out.println("Would you like to approve or deny this expense?");
                    System.out.println("a: Approve");
                    System.out.println("d: Deny");
                    System.out.print("Please enter your selection: ");
                    String decision = scanner.nextLine().trim();

                    if(decision.equalsIgnoreCase("a")){
                        update += "status = 'approved'";
                        System.out.println("Please submit a comment (or press enter to continue)");
                        String comment = scanner.nextLine().trim();
                        if(comment.length() > 200){
                            System.out.println("Comment cannot be greater than 200 characters, returning to Menu");
                            continue;
                        }else if(!comment.equals("")){
                            update += ", comment = '"+comment+"'";
                        }
                        
                        System.out.println("This expense has successfully been approved");
                    }else if(decision.equalsIgnoreCase("d")){
                        update += "status = 'denied', ";
                        String comment = "";
                        while(true){
                            System.out.println("Please submit a comment");
                             comment = scanner.nextLine().trim();
                            if(comment.equals("")){
                                System.out.println("Denied requests must have a comment");
                            }else{
                                update += "comment = '"+comment+"'";
                                break;
                            }
                        }
                        if(comment.length() < 5 || comment.length() > 200){
                            System.out.println("Comment must be between 5 and 200 characters, returning to Menu");
                            continue;
                        }
                        System.out.println("This expense has successfully been denied");
                        
                    }else{
                        System.out.println("Invalid input, returning to Menu");
                        continue;
                    }
                    update += " where expense_id = " +id;
                    //do the thing
                    stmt.execute(update);
                }else if(input.equals("3")){
                    //just missing datarange cause i dont know what that means
                    System.out.println("Would you like reports by employee or category?");
                    System.out.println("e: Employee");
                    System.out.println("c: Category");
                    System.out.println("d: Date Range");
                    System.out.print("Please enter your selection: ");
                    String decision = scanner.nextLine().trim();
                    String fileName = "";

                    String report = """
                        count(*) as count, round(sum(e.amount),2) as total_amount,
                        SUM(CASE WHEN a.status = 'approved' THEN 1 ELSE 0 END) AS total_approved
                        from expenses as e inner join approvals as a on a.expense_id = e.id
                    """;

                    if(decision.equalsIgnoreCase("e")){
                        report = "Select u.full_name, " + report +
                        " inner join users as u on u.id = e.user_id group by u.full_name";
                        fileName = "employee_";
                    }else if(decision.equalsIgnoreCase("c")){
                        report = "Select c.name, " + report +
                        " inner join categories as c on c.id = e.category_id group by c.name";
                        fileName = "category_";
                    }else if(decision.equalsIgnoreCase("d")){
                        System.out.print("Please enter a starting date (YYYY-MM-DD): ");
                        String start = scanner.nextLine().trim();
                        System.out.print("Please enter an ending date (YYYY-MM-DD): ");
                        String end = scanner.nextLine().trim();
                        if(!start.matches("^\\d{4}-\\d{2}-\\d{2}$") || 
                        !end.matches("^\\d{4}-\\d{2}-\\d{2}$") ||
                        LocalDate.parse(end).isBefore(LocalDate.parse(start))){
                            System.out.println("Invalid date range, returning to Menu");
                            continue;
                        }
                        report = "Select e.expense_date, "+report+
                        " where e.expense_date between '"+start+"' and '"+end+
                        "' group by e.expense_date";
                        fileName = "dateRange_";
                    }else{
                        System.out.println("Invalid input, returning to Menu");
                        continue;
                    }
                    
                    stmt.execute(report);
                    ResultSet rows = stmt.getResultSet();
                    if (!rows.isBeforeFirst()){
                        System.out.println("No data for the selected criteria");
                        continue;
                    }else{
                        printRows(rows);
                    }
                    
                    System.out.println("Would you like to print out this report?");
                    System.out.println("y: Yes");
                    System.out.println("n: No");
                    System.out.print("Please enter your selection: ");
                    decision = scanner.nextLine().trim();

                    if(decision.equalsIgnoreCase("y")){
                        System.out.println("Exporting the report as a CSV");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
                        fileName += LocalDateTime.now().format(formatter);
                        stmt.execute(report);
                        rows = stmt.getResultSet();
                        exportCSV(rows, fileName);
                    }else if(decision.equalsIgnoreCase("n")){
                        System.out.println("Returning to Menu");
                    }else{
                        System.out.println("Invalid input, returning to Menu");
                    }


                }else if(input.equals("0")){
                    System.out.println("Goodbye, "+manager_name);
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

    //created with help of AI, just asked it how to write to csv
    public static void exportCSV(ResultSet rs, String fileName) {
        try (CSVWriter writer = new CSVWriter(new FileWriter("reports/"+fileName+".csv"))) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Write headers
            String[] headers = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                headers[i - 1] = metaData.getColumnName(i);
            }
            writer.writeNext(headers);

            // Write rows
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getString(i);
                }
                writer.writeNext(row);
            }
            System.out.println("The report was successfully exported");
        } catch (Exception e) {
            System.out.println("Sorry, looks like the report couldn't be exported");
        }
    }

    //created with help of Ai. just asked it to how to print rows nicely
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
