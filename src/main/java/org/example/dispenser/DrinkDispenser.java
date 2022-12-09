package org.example.dispenser;

import org.example.dispenser.exception.SoldOutException;
import org.example.dispenser.manager.CashManager;
import org.example.dispenser.manager.DrinkManager;
import org.example.dispenser.model.Coin;
import org.example.dispenser.model.Drink;
import org.example.dispenser.model.response.SelectDrinkResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class DrinkDispenser {
    private final CashManager cashManager;
    private final DrinkManager drinkManager;

    public DrinkDispenser(CashManager cashManager, DrinkManager drinkManager) {
        this.cashManager = cashManager;
        this.drinkManager = drinkManager;
        initializeStocks();
    }

    private void initializeStocks() {
        Stream.of(Coin.values()).forEach(coin -> cashManager.updateStock(coin, 10));
        Stream.of(Drink.values()).forEach(drink -> drinkManager.updateStock(drink, 10));
    }

    public SelectDrinkResponse selectDrink(Drink drink) {
        drinkManager.setCurrentDrink(drink);

        if (drinkManager.getStock(drink) == 0) {
            throw new SoldOutException("Sold Out, Please buy another Drink");
        }
        if (cashManager.isEnoughBalance(drink.getPrice())) {
            return collectDrinkAndChange();
        } else {
            return new SelectDrinkResponse(null, null, drink.getPrice());
        }
    }

    public List<Coin> refund() {
        if (cashManager.getInsertedBalance() == 0) {
            return Collections.EMPTY_LIST;
        }

        cashManager.applyRefund();
        drinkManager.clearCurrentDrink();
        return cashManager.getChangeCoins();
    }

    public void insertCoin(Coin coin) {
        cashManager.insertCoin(coin);
    }

    private SelectDrinkResponse collectDrinkAndChange() {
        SelectDrinkResponse selectDrinkResponse;
        updateChange();
        drinkManager.removeStock(drinkManager.getCurrentDrink());
        selectDrinkResponse = new SelectDrinkResponse(drinkManager.getCurrentDrink(), cashManager.getChangeCoins(), drinkManager.getCurrentDrinkPrice());
        reset();

        return selectDrinkResponse;
    }

    private void updateChange() {
        int drinkPrice = drinkManager.getCurrentDrinkPrice();
            cashManager.updateChangeAfterApplyPrice(drinkPrice);
    }

    private void reset() {
        drinkManager.clearCurrentDrink();
        cashManager.clearInsertedBalance();
    }

}

