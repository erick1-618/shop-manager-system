package br.com.erick.sms.model;

public class Item {
    private Produto produto;
    private int quantity;

    public Item(Produto produto, int quantity){
        this.produto = produto;
        this.quantity = quantity;    
    }

    public Produto getProduto(){
        return this.produto;
    }

    public int getQuantity(){
        return this.quantity;    
    }
}
