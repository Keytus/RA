package com.russianairports;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class ExceptionLogger {
    public static Boolean LogException(Exception exception, Statement stmtObj){
        String exceptionName = exception.getClass().getCanonicalName();
        String strTimestamp = "TO_TIMESTAMP('" + new Timestamp(System.currentTimeMillis()) + "', 'YYYY-MM-DD HH24:MI:SS.FF')";

        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter( writer );
        exception.printStackTrace( printWriter );
        printWriter.flush();
        String stackTraceF = writer.toString();

        System.out.println(stackTraceF);

        try {
            stmtObj.executeUpdate("INSERT INTO ExceptionsLogs VALUES ("+ strTimestamp +
                     ", '" + exceptionName + "')");
        } catch (SQLException e) {

            return false;
        }
        System.out.println("Ням");

        String stackTrace = String.valueOf(Thread.getAllStackTraces());

        try {
            String insertXML = "INSERT INTO ExceptionsLogsFull VALUES ("+ strTimestamp +", '"+ exceptionName + "', ?) ";
            PreparedStatement statement = stmtObj.getConnection().prepareStatement(insertXML);
            statement.setString(1,stackTrace);
            statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("INSERT INTO ExceptionsLogsFull VALUES ("+ strTimestamp
                    + ", '" + exception.getClass().getCanonicalName() + "', '" + stackTrace + "')");
            return false;
        }
        System.out.println("Ням");
        return true;
    }
}
