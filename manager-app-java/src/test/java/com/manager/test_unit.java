package com.manager;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Statement;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;

public class test_unit {
//click the left side checkmarks to run tests
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
}