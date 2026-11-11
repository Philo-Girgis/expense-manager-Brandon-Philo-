package com.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;

public class test_unit {
//click the left side checkmarks to run tests

    protected Path testDbPath;

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
}