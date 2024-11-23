package com.ccinfo208_dbapp.Barcelita;

import java.util.*;
import java.sql.*;
import com.ccinfo208_dbapp.Barcelita.model.Patient;
import com.ccinfo208_dbapp.Barcelita.utility.InputUtility;

public class CaseTransactions {
    private Connection connection;
    private Scanner sc;

    public CaseTransactions(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    public void run() {
        boolean exitSubMenu = false;

        while (!exitSubMenu) {
            System.out.println( "\nCase Registration and Closing\n");
            System.out.println("[1] Register A Patient Case."); 
            System.out.println("[2] Close an Existing Patient Case.");
            System.out.println("[3] Register A Close Contact Case.");
            System.out.println("[4] Elevate a Close Contact to a Patient Case.");
			System.out.println("[5] Add Guardian to Patient Record.");
            System.out.print("Enter choice: ");
            int choice = InputUtility.getIntInput("Enter choice: ");
        }
    }
}
