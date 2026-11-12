package com.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class test_unit {
//click the left side checkmarks to run tests

    protected Path testDbPath;

    // Helper methods
    private String runMainWithDatabase(String simulatedInput) throws Exception {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();

        try {
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            System.setOut(new PrintStream(outContent));
            System.setErr(new PrintStream(errContent));

            Main.smallMain();

        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        return outContent.toString();
    }

    private String runMenuWithDatabase(String simulatedInput) throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try (Connection connection = testsql.connect();
             Statement stmt = connection.createStatement();
             Scanner scanner = new Scanner(
                 new ByteArrayInputStream(simulatedInput.getBytes()))) {

            System.setOut(new PrintStream(outContent));

            Menu.menu(stmt, scanner, "Jane Manager", "1");

        } finally {
            System.setOut(originalOut);
        }

        return outContent.toString();
    }

    private String getApprovalStatus(int expenseId) throws Exception {
        try (Connection connection = testsql.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT status FROM approvals WHERE expense_id = " + expenseId)) {

            if (rs.next()) {
                return rs.getString("status");
            }
        }

        return null;
    }

    private String getApprovalComment(int expenseId) throws Exception {
        try (Connection connection = testsql.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT comment FROM approvals WHERE expense_id = " + expenseId)) {

            if (rs.next()) {
                return rs.getString("comment");
            }
        }

        return null;
    }

    private String getReviewerId(int expenseId) throws Exception {
        try (Connection connection = testsql.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT reviewer_id FROM approvals WHERE expense_id = " + expenseId)) {

            if (rs.next()) {
                return rs.getString("reviewer_id");
            }
        }

        return null;
    }

    private String getReviewDate(int expenseId) throws Exception {
        try (Connection connection = testsql.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT review_date FROM approvals WHERE expense_id = " + expenseId)) {

            if (rs.next()) {
                return rs.getString("review_date");
            }
        }

        return null;
    }


    @BeforeEach
    void setUpTestDatabase() throws IOException {
        testDbPath = Paths.get("test_em.db").toAbsolutePath();
        System.setProperty("EM_DB_PATH", testDbPath.toString());
        testsql.reset();
        
    }
    @AfterEach
    void tearDown(){
        System.clearProperty("EM_DB_PATH");
    }


    @Test
    public void testnewInvalidUsernameAndPassword() throws Exception {

        // Simulate input
        String simulatedInput = "wrongUser\nwrongPass\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //the method being tested
        Main.smallMain();

        // Verify that the invalid login message was printed
        String output = outContent.toString();
        assertTrue(output.contains("Invalid username or password"), 
            "Expected 'Invalid username or password' message to be printed.");
    }


    @Test
    public void testInvalidUsernameAndPassword() throws Exception {
        // Mock the static database connection call: testsql.connect()
        try (MockedStatic<testsql> mockedTestSql = Mockito.mockStatic(testsql.class)) {
            Connection connectionMock = mock(Connection.class);
            Statement statementMock = mock(Statement.class);
            ResultSet resultSetMock = mock(ResultSet.class);

            // Set up the mock chain
            mockedTestSql.when(testsql::connect).thenReturn(connectionMock);
            when(connectionMock.createStatement()).thenReturn(statementMock);
            when(statementMock.getResultSet()).thenReturn(resultSetMock);
            
            // Make row.next() return false to simulate an invalid login
            when(resultSetMock.next()).thenReturn(false);

            // Simulate input
            String simulatedInput = "wrongUser\nwrongPass\n";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

            // Capture console output
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            //the method being tested
            Main.smallMain();

            // Verify that the invalid login message was printed
            String output = outContent.toString();
            assertTrue(output.contains("Invalid username or password"), 
                "Expected 'Invalid username or password' message to be printed.");
        }
    }

    // TC-JMA-001
    @Test
    public void testValidUsernameAndPassword() throws Exception {
        // Mock the static database connection call: testsql.connect()
        try (MockedStatic<testsql> mockedTestSql = Mockito.mockStatic(testsql.class)) {
            Connection connectionMock = mock(Connection.class);
            Statement statementMock = mock(Statement.class);
            ResultSet resultSetMock = mock(ResultSet.class);

            // Set up the mock chain
            mockedTestSql.when(testsql::connect).thenReturn(connectionMock);
            when(connectionMock.createStatement()).thenReturn(statementMock);
            when(statementMock.getResultSet()).thenReturn(resultSetMock);
            
            // Make row.next() return true to simulate finding a user
            when(resultSetMock.next()).thenReturn(true);
            
            // Configure mock row data for a valid manager
            when(resultSetMock.getString("role")).thenReturn("manager");
            when(resultSetMock.getString("full_name")).thenReturn("Jane Manager");
            when(resultSetMock.getString("id")).thenReturn("1");

            // Simulate input
            String simulatedInput = "managerUser\nmanagerPass\n";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

            // Capture console output
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            //the method being tested
            Main.smallMain();

            // Verify that the valid login message was printed
            String output = outContent.toString();
            assertTrue(output.contains("Welcome, Jane Manager"), 
                "Expected 'Welcome, Jane Manager' message to be printed.");
        }
    }

    @Test
    public void testMenuExit() throws Exception {
        Statement statementMock = mock(Statement.class);
        
        // Simulate input
        String simulatedInput = "0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //the method being tested
        Menu.menu(statementMock, scanner, "Jane Manager", "1");

        // Verify that the exit message was printed
        String output = outContent.toString();
        assertTrue(output.contains("Goodbye, Jane Manager"), 
            "Expected 'Goodbye, Jane Manager' message to be printed.");
    }

    @Test
    public void testInvalidMenuSelection() throws Exception {

        String output = runMenuWithDatabase("9\n0\n");

        assertTrue(output.contains("Invalid input"),
            "Expected an invalid menu selection message.");
    }

    @Test
    public void testValidListPending() throws Exception {
        Statement stmt = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);

        //mock that the result set exists
        when(resultSet.isBeforeFirst()).thenReturn(true);
        // Simulate a result set for printing
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getMetaData()).thenReturn(mock(java.sql.ResultSetMetaData.class));
        when(resultSet.getMetaData().getColumnCount()).thenReturn(2);
        when(resultSet.getMetaData().getColumnLabel(1)).thenReturn("ID");
        when(resultSet.getMetaData().getColumnLabel(2)).thenReturn("Amount");
        when(resultSet.getString(1)).thenReturn("101");
        when(resultSet.getString(2)).thenReturn("150.00");

        // when stmt asks for result set, return the mock
        //we do this instead of mocking the execute statement
        when(stmt.getResultSet()).thenReturn(resultSet);

        // Simulate input, must always exit after menu test to avoid errors
        String simulatedInput = "1\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //the method being tested
        Menu.menu(stmt, scanner, "Jane Manager", "1");

        // Verify that the results from the mocked result set were printed
        String output = outContent.toString();
        assertTrue(output.contains("ID") && output.contains("101")
        && output.contains("Amount") && output.contains("150.00"), 
            "Expected the values from mocked result set");
    }


    // TC-JMA-002
    @Test
    public void testValidUsernameInvalidPassword() throws Exception {

        String output = runMainWithDatabase(
            "manager1\npassword1\n"
        );

        assertTrue(output.contains("Invalid username or password"),
            "Expected invalid password to be rejected.");
    }

    // TC-JMA-003
    @Test
    public void testInvalidUsernameValidPassword() throws Exception {

        String output = runMainWithDatabase(
            "manager3\nm1password123\n"
        );

        assertTrue(output.contains("Invalid username or password"),
            "Expected invalid username to be rejected.");
    }


    // TC-JMA-008 - TC-JMA-012
    @ParameterizedTest
    @CsvSource({
        "-1",
        "1.5",
        "abc",
        "a2"
    })
    public void testInvalidExpenseId(String id) throws Exception {

        String output = runMenuWithDatabase("2\n" + id + "\n0\n");

        assertTrue(output.contains("Invalid input, returning to Menu"),
            "Expected invalid expense ID to be rejected: " + id);
    }


    @Test
    public void testExpenseIdZero() throws Exception {

        String output = runMenuWithDatabase(
            "2\n0\n0\n"
        );

        assertTrue(output.contains("Invalid expense id, returning to Menu"),
            "Expected expense ID 0 to be rejected.");
    }

    @Test
    public void testEmptyExpenseId() throws Exception {

        String output = runMenuWithDatabase(
            "2\n\n0\n"
        );

        assertTrue(output.contains("Invalid input, returning to Menu"),
            "Expected an empty expense ID to be rejected.");
    }


    // TC-JMA-013
    @Test
    public void testNonexistentExpenseId() throws Exception {

        String output = runMenuWithDatabase("2\n999999\n0\n");

        assertTrue(output.contains("Invalid expense id, returning to Menu"),
            "Expected nonexistent expense ID to be rejected.");
    }

    // TC-JMA-014
    @Test
    public void testApproveExpenseWith10CharacterComment() throws Exception {

        String comment = "Looks good"; // Exactly 10 characters

        runMenuWithDatabase(
            "2\n1\na\n" + comment + "\n0\n"
        );

        assertEquals("approved", getApprovalStatus(1));
        assertEquals(comment, getApprovalComment(1));
        assertEquals("1", getReviewerId(1));
        assertTrue(getReviewDate(1) != null);
    }

    // TC-JMA-016 (Defect)
    @Test
    public void testApproveExpenseWith200CharacterComment() throws Exception {

        String comment = "A".repeat(200);

        runMenuWithDatabase(
            "2\n1\na\n" + comment + "\n0\n"
        );

        assertEquals("approved", getApprovalStatus(1));
        assertEquals(200, getApprovalComment(1).length());
        assertEquals(comment, getApprovalComment(1));
    }

    // TC-JMA-017 (Defect)
    @Test
    public void testApproveExpenseWith201CharacterComment() throws Exception {

        String comment = "A".repeat(201);

        runMenuWithDatabase(
            "2\n1\na\n" + comment + "\n0\n"
        );

        // Expected behavior: reject the comment and keep expense pending.
        assertEquals("pending", getApprovalStatus(1),
            "A 201-character comment should not be accepted.");
    }

    // TC-JMA-018
    @Test
    public void testAlreadyApprovedExpense() throws Exception {

        String output = runMenuWithDatabase("2\n8\n0\n");

        assertTrue(output.contains("Invalid expense id"),
            "Expected an already approved expense to be rejected.");

        assertEquals("approved", getApprovalStatus(8));
    }

    // TC-JMA-019
    @Test
    public void testAlreadyDeniedExpense() throws Exception {

        String output = runMenuWithDatabase("2\n18\n0\n");

        assertTrue(output.contains("Invalid expense id"),
            "Expected an already denied expense to be rejected.");

        assertEquals("denied", getApprovalStatus(18));
    }

    // TC-JMA-020
    @Test
    public void testDenyExpenseWithEmptyCommentThenValidComment() throws Exception {

        String comment = "Valid";

        runMenuWithDatabase(
            "2\n1\nd\n\n" + comment + "\n0\n"
        );

        // Empty comment should trigger another prompt.
        // The subsequent valid comment should allow the denial.
        assertEquals("denied", getApprovalStatus(1));
        assertEquals(comment, getApprovalComment(1));
    }

    // TC-JMA-021 (Defect)
    @Test
    public void testDenyExpenseWith4CharacterComment() throws Exception {

        String comment = "Nope"; // Exactly 4 characters

        runMenuWithDatabase(
            "2\n1\nd\n" + comment + "\n0\n"
        );

        // Expected behavior: reject a comment shorter than 5 characters.
        assertEquals("pending", getApprovalStatus(1),
            "A 4-character denial comment should be rejected.");
    }

    // TC-JMA-022 (Defect)
    @Test
    public void testDenyExpenseWith201CharacterComment() throws Exception {

        String comment = "A".repeat(201);

        runMenuWithDatabase(
            "2\n1\nd\n" + comment + "\n0\n"
        );

        // Expected behavior: reject the comment and keep expense pending.
        assertEquals("pending", getApprovalStatus(1),
            "A 201-character denial comment should be rejected.");
    }

    // TC-JMA-023
    @Test
    public void testDenyExpenseWith5CharacterComment() throws Exception {

        String comment = "Valid"; // Exactly 5 characters

        runMenuWithDatabase(
            "2\n1\nd\n" + comment + "\n0\n"
        );

        assertEquals("denied", getApprovalStatus(1));
        assertEquals(comment, getApprovalComment(1));
        assertEquals("1", getReviewerId(1));
        assertTrue(getReviewDate(1) != null);
    }

    // TC-JMA-024
    @Test
    public void testDenyExpenseWith200CharacterComment() throws Exception {

        String comment = "A".repeat(200);

        runMenuWithDatabase(
            "2\n1\nd\n" + comment + "\n0\n"
        );

        assertEquals("denied", getApprovalStatus(1));
        assertEquals(comment, getApprovalComment(1));
        assertEquals(200, getApprovalComment(1).length());
    }

    
    // TC-JMA-025
    @Test
    public void testDenyExpenseWith15CharacterComment() throws Exception {

        String comment = "AAAAAAAAAAAAAAA"; // Exactly 15 characters

        runMenuWithDatabase(
            "2\n1\nd\n" + comment + "\n0\n"
        );

        assertEquals("denied", getApprovalStatus(1));
        assertEquals(comment, getApprovalComment(1));
    }



    // ==========================================
    // Expense review - invalid decision
    // ==========================================

    @Test
    public void testInvalidReviewDecision() throws Exception {

        String output = runMenuWithDatabase("2\n4\nx\n0\n");

        assertTrue(output.contains("Invalid input"),
            "Expected an invalid review decision message.");

        assertEquals("pending", getApprovalStatus(4),
            "Expense should remain pending after an invalid decision.");
    }
}