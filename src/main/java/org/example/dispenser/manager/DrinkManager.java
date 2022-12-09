package org.example.dispenser.manager;

import org.example.dispenser.model.Drink;

import java.util.HashMap;
import java.util.Map;

public class DrinkManager {
    private Map<Drink, Integer> drinkStock = new HashMap();
    private Drink currentDrink;

    public Drink getCurrentDrink() {
        return currentDrink;
    }

    public void setCurrentDrink(Drink currentDrink) {
        this.currentDrink = currentDrink;
    }

    public void clearCurrentDrink() {
        this.currentDrink = null;
    }

    public void updateStock(Drink drink, int number){
        drinkStock.put(drink,number);
    }

    public void removeStock(Drink drink){
        drinkStock.put(drink,drinkStock.get(drink) - 1);
    }

    public int getStock(Drink drink){
        return drinkStock.get(drink);
    }

    public int getCurrentDrinkPrice() {
        return currentDrink.getPrice();
    }
}
