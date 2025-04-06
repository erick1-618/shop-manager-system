package br.com.erick.sms.controller;

import br.com.erick.sms.model.Produto;
import br.com.erick.sms.model.Compra;
import br.com.erick.sms.model.Item;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

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
                 if(!this.conn.isClosed()) System.out.println("Redundant open");
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

    public void initializeDatabase(){
    
        open();

        String produtoInitializer = "CREATE TABLE IF NOT EXISTS produto ( id varchar(100) primary key, name varchar(100) not null, unit_value real not null, sales_quantity integer default 0);";

        String compraInitializer = "CREATE TABLE IF NOT EXISTS compra (id varchar(100) primary key, date varchar(100) not null, total real);";            

        String itemInitializer = "CREATE TABLE IF NOT EXISTS item ( compra_id varchar(100), produto_id varchar(100), quantity integer not null, primary key(compra_id, produto_id), foreign key(compra_id) references compra(id), foreign key(produto_id) references produto(id));";

        try{
            Statement stmt = conn.createStatement();
            stmt.execute(produtoInitializer);
            stmt.execute(compraInitializer);
            stmt.execute(itemInitializer);

            System.out.println("Schema initializer UP");
        }catch(SQLException e){
            System.err.println("Could not initialize the database");        
        }finally{
            close();        
        }        
    }

    public void restartDatabase(){
        open();

        String sql = "DROP TABLE IF EXISTS item, compra, produto";
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(sql);        
        }catch(SQLException e){
            System.err.println("Error: Could not restart the database");        
        }

        close();

        initializeDatabase();

        System.out.println("Database restarted");
    }

    public void addNewProduct(Produto p){
        String sql = "INSERT INTO produto (id, name, unit_value) values (?, ?, ?);";
        
        open();

        try{
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setDouble(3, p.getUnitValue());    
            stmt.setString(2, p.getName());
            stmt.setString(1, p.getId());
            stmt.execute();
        }catch(Exception e){
            System.err.println("Could not add the new product");        
            e.printStackTrace();        
        }finally{
            close();        
        }    
    }

    public void addCompra(Compra c){
        String sql = "INSERT INTO compra (id, date, total) values (?, ?, ?)";

        open();

        try{
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(2, c.getTimestamp().toString());    
            stmt.setDouble(3, c.getTotal());
            stmt.setString(1, c.getId());
            stmt.execute();

            if(c.getItens() != null){
                for(Item i: c.getItens()){
                    addItem(c, i);                
                }            
            }                            

        }catch(Exception e){
            System.err.println("Could not add the new sale");        
            e.printStackTrace();        
        }finally{
            close();        
        } 
    }

    public void addItem(Compra c, Item i){
        String sql = "INSERT INTO item (compra_id, produto_id, quantity) values (?, ?, ?)";

        open();

        try{
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, c.getId());
            stmt.setString(2, i.getProduto().getId());
            stmt.setInt(3, i.getQuantity());
            stmt.execute();

            updateQuantity(i.getProduto());
        }catch(Exception e){
            System.err.println("Could not add the new item");        
            e.printStackTrace();        
        }finally{
            close();        
        } 
    }

    public void updateQuantity(Produto p){
        String sql1 = "SELECT SUM(item.quantity) AS soma FROM item WHERE item.produto_id = ?;";    
        String sql2 = "UPDATE produto SET sales_quantity = ? WHERE produto.id = ?";       
        
        open();

         try{
            PreparedStatement stmt = this.conn.prepareStatement(sql1);
            stmt.setString(1, p.getId());
            ResultSet sum = stmt.executeQuery();

            sum.next();
            
            PreparedStatement stmt2 = this.conn.prepareStatement(sql2);
            stmt2.setInt(1, sum.getInt("soma"));
            stmt2.setString(2, p.getId());
            stmt2.execute();
        }catch(Exception e){
            System.err.println("Could not update the item qt");        
            e.printStackTrace();        
        }finally{
            close();        
        }     
    }

    public List<Produto> getProducts(){
        String sql = "SELECT * FROM produto;";
        
        List<Produto> list = new ArrayList<>();
        
        ResultSet prods = null;    

        open();

        try{
            Statement stmt = this.conn.createStatement();
            prods = stmt.executeQuery(sql);
            while(prods.next()){
                 list.add(new Produto(prods.getString("name"), prods.getDouble("unit_value"), prods.getInt("sales_quantity"), prods.getString("id")));           
            }              
        }catch(SQLException e){
            System.err.println("Could not get the products");        
        }finally{
            close();
            return list;        
        }    
    }
}
