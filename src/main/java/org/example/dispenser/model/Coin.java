package org.example.dispenser.model;

public enum Coin{
    FIVE_CENTS(5), TEN_CENTS(10), TWENTY_CENTS(20), FIFTY_CENTS(50), ONE_EURO(100), TWO_EUROS(200);

    private int value;

    private Coin(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
