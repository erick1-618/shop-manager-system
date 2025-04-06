package br.com.erick.sms.controller;

import java.sql.*;

public class ConnectionController {

    private String DBUserName;
    
    private String DBPassword;
    
    private String DBUrl;

    private Connection conn = null;

    public ConnectionController(String usr, String pwk, String DBUrl){
        this.DBUserName = usr;
        this.DBPassword = pwk;
        this.DBUrl = DBUrl;    
    }

    public Connection open(){

        try{
            if(this.conn != null){
                 if(!this.conn.isClosed()) System.err.println("A connection is still open");
            }
        }catch(SQLException e){
            System.err.println("Erro checking the connection");        
        }

        Connection conn;        
        
        try{

            conn = DriverManager.getConnection(DBUrl, DBUserName, DBPassword);

            this.conn = conn; 

            return this.conn;

        }catch(SQLException e){
            System.err.println("Error: Could not connect to database");
            e.printStackTrace();        
        } return null; 
    }

    public void close(){
        try{
            if(this.conn.isClosed()) return;
            this.conn.close();
        }catch(SQLException e){
            System.err.println("Error closing the connection");        
        }        
    }
}
