package br.com.erick.sms.model;

import java.util.UUID;

public class Produto {
    
    private String name;

    private double unitValue;

    private int salesQuantity;

    private String id;

    public Produto(String name, double unitValue, int salesQuantity, String id){
        this.id = id;
        this.name = name;
        this.unitValue = unitValue;
        this.salesQuantity = salesQuantity;
    }

    public Produto(String name, double unitValue){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.unitValue = unitValue;
    }

    public String getName(){
        return this.name;
    }

    public double getUnitValue(){
        return this.unitValue;    
    }

    public int getSalesQuantity(){
        return this.salesQuantity;
    }

    public void setSalesQuantity(int qt){
        this.salesQuantity = qt;    
    }
    
    public String getId(){
        return this.id.toString();
    }
}
