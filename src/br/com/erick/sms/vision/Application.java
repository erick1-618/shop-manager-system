package br.com.erick.sms.vision;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import br.com.erick.sms.model.*;
import br.com.erick.sms.controller.Controller;

public class Application{

        private static Controller control;

        public static void main(String[] args){
            control = Controller.getInstance();
            Scanner scan = new Scanner(System.in);
            String opt = "";
            List<Item> cart = new ArrayList<>();

            fmsg();
            
            while(!opt.equals("6")){
                menu();
                opt = scan.nextLine();
                redirect(opt);            
            }            
            
            System.out.println("Bye!");
        }
        
        private static void redirect(String option){
            if(option == "6") return;
            switch(option){
                case "1": addNewProduct(); break;
                case "2": addToCart(); break;
                default: return;            
            }        
        }
    
        private static void addNewProduct(){
            Scanner s = new Scanner(System.in);
            System.out.println("Enter the product name:");        
            String name = s.nextLine();
            System.out.println("Enter the product value:");
            double value = s.nextDouble();
            control.addNewProduct(name, value);     
        }

        private static void addToCart(){
            List<String> prods = control.getAllProducts();
            System.out.println("---------------------------- PRODUCTS LIST -----------------------------");
            prods.forEach(p -> System.out.println(p + "\n"));        
        }

        private static void fmsg(){
            System.out.println("---------------SHOP MANAGER SYSTEM-----------------\n"+
                   "v0.1 by Erick Andrade                   psql for db\n");        
        }

        private static void menu(){
            System.out.println("[1] - Add new product   [2] - Add product to the cart   [3] - Remove product of the cart"
                +  "\n[4] - Close the cart    [5] - Clear the cart            [6] - Exit\n");
        }
}
