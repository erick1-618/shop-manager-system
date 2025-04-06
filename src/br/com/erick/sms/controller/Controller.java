package br.com.erick.sms.controller;

import br.com.erick.sms.model.Produto;

import java.sql.*;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

public class Controller{
    
    private static Controller instance;

    private ConnectionController cc;

    public static Controller getInstance(){
        if(instance == null){
            return new Controller();        
        }

        return instance;
    }

    private Controller(){
            
        Properties props = new Properties();            
                
        try(FileInputStream fis = new FileInputStream("resources/config.properties")){
            props.load(fis);
            System.out.println("Enviroment files UP");
        }catch(IOException e){
            System.err.println("Error: could not load enviroment files");
            e.printStackTrace();        
        }    

        String url = props.getProperty("db_url") + props.getProperty("db");
        
        this.cc = new ConnectionController(props.getProperty("user"), props.getProperty("pwk"), url);

        initializeDataBase(); 
    }

    private void initializeDataBase(){

        Connection conn = this.cc.open();

        System.out.println("First connection UP");

        String produtoInitializer = "CREATE TABLE IF NOT EXISTS produto ( id serial primary key, name varchar(100) not null, unit_value real not null, sales_quantity integer default 0);";

        String compraInitializer = "CREATE TABLE IF NOT EXISTS compra (id serial primary key, date timestamp not null, total real);";            

        String itemInitializer = "CREATE TABLE IF NOT EXISTS item ( compra_id integer, produto_id integer, quantity integer not null, primary key(compra_id, produto_id), foreign key(compra_id) references compra(id), foreign key(produto_id) references produto(id));";

        try{
            Statement stmt = conn.createStatement();
            stmt.execute(produtoInitializer);
            stmt.execute(compraInitializer);
            stmt.execute(itemInitializer);

            System.out.println("Schema initializer UP");
        }catch(SQLException e){
            System.err.println("Could not initialize the database");        
        }finally{
            this.cc.close();        
        }        
    }

    public void addNewProduct(Produto p){
        String sql = "INSERT INTO produto (name, unit_value) values (?, ?);";
        
        Connection c = this.cc.open();

        try{
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setDouble(2, p.getUnitValue());    
            stmt.setString(1, p.getName());
            stmt.execute();
        }catch(Exception e){
            System.err.println("Could not add the new product");        
            e.printStackTrace();        
        }finally{
            this.cc.close();        
        }
}        

    public static void main(String [] args){

        Controller c = Controller.getInstance();

        Produto p = new Produto("ABC", 1.0);

        c.addNewProduct(p);    
    }
}
