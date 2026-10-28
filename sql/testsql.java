package sql;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import org.sqlite.SQLiteConfig;


public class testsql{
    private static Path schema = Path.of("sql", "schema.sql");
    private static Path seed = Path.of("sql", "seed.sql");
    private static Path queries = Path.of("sql", "validation", "test.sql");

    public static void main(String[] args){
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

            String temp = Files.readString(queries);
            String[] tests = temp.split(";");

            for (String sql : tests) {
                String trimmedSql = sql.trim();

                // Skip empty lines and comments
                if (trimmedSql.isEmpty() || trimmedSql.startsWith("--")) {
                    continue;
                }

                System.out.println("==================================================");
                System.out.println("Running: " + trimmedSql);
                System.out.println("==================================================");

                // 3. Execute statement. stmt.execute() returns true if it's a SELECT (has a ResultSet)
                boolean hasResultSet = stmt.execute(trimmedSql);

                if (hasResultSet) {
                    // It's a SELECT query -> Print results nicely
                    try (ResultSet rs = stmt.getResultSet()) {
                        printNicely(rs);
                    }
                } else {
                    // It's an INSERT, UPDATE, CREATE, etc. -> Print affected rows
                    int updateCount = stmt.getUpdateCount();
                    System.out.println("✔ Executed successfully. Rows affected / status code: " + updateCount + "\n");
                }
            }


        }catch(Exception e){
            System.err.println("Error executing SQL script: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printNicely(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Print column headers
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(metaData.getColumnName(i) + "\t| ");
        }
        System.out.println("\n--------------------------------------------------");

        // Print row values dynamically
        int rowCount = 0;
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                Object value = rs.getObject(i);
                System.out.print((value != null ? value : "NULL") + "\t| ");
            }
            System.out.println();
            rowCount++;
        }
        System.out.println("\n(" + rowCount + " row(s) returned)\n");
    }

    public static Connection connect() throws SQLException {
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        config.setBusyTimeout(5000);
        return DriverManager.getConnection("jdbc:sqlite::memory:", config.toProperties());
    }

}