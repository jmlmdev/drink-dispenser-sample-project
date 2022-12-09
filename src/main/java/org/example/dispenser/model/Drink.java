package org.example.dispenser.model;

public enum Drink{
    COKE(100), REDBULL(125), WATER(50),ORANGE_JUICE(195);

    private int price;

    Drink(int price){
        this.price = price;
    }
    public Integer getPrice(){
        return price;
    }
}
