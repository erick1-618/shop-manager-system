package br.com.erick.sms.model;

public class Produto {
    
    private String name;

    private double unitValue;

    private int salesQuantity;

    public Produto(String name, double unitValue, int salesQuantity){
        this.name = name;
        this.unitValue = unitValue;
        this.salesQuantity = salesQuantity;
    }

    public Produto(String name, double unitValue){
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
}
