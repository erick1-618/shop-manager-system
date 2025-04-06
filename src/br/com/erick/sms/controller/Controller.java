package br.com.erick.sms.controller;

import br.com.erick.sms.model.Produto;
import br.com.erick.sms.model.Compra;
import br.com.erick.sms.model.Item;

import java.sql.*;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;
import java.util.ArrayList;
import java.util.List;

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

        this.cc.initializeDatabase();

        System.out.println("First connection UP");
    }

    public void restart(){
        this.cc.restartDatabase();
    }        

    public void addNewProduct(String name, double value){
        Produto p = new Produto(name, value);
        this.cc.addNewProduct(p);        
    }
    
    public void addCompra(Compra c){
        this.cc.addCompra(c);    
    }
        
    public void addItem(Compra c, Item i){
        this.cc.addItem(c, i);
    }

    public void updateQuantity(Produto p){
        this.cc.updateQuantity(p);    
    }

    public List<String> getAllProducts(){
        List<String> products = new ArrayList<>();

        List<Produto> prodQ = this.cc.getProducts();

        String line;

        if(prodQ == null) return products;
        
        for(Produto p : prodQ){
             line = "";
             line += p.getName() + "|";
             line += p.getUnitValue() + "|"; 
             line += p.getId();
             products.add(line);
        }

        return products;
    }

    // General tests;
    public static void main(String [] args){

        Controller c = Controller.getInstance();

        c.restart();

        Produto p = new Produto("ABC", 2.0);
    
        Item i = new Item(p, 12);

        ArrayList<Item> list = new ArrayList<>();

        list.add(i);

        Compra comp = new Compra(list);

        c.addNewProduct("Produto A", 15);

        c.addCompra(comp); 
    }
}
