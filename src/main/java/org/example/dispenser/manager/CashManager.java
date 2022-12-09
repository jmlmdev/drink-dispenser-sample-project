package org.example.dispenser.manager;

import org.example.dispenser.exception.NoChangeException;
import org.example.dispenser.model.Coin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashManager {
    private final Map<Coin, Integer> coinStock = new HashMap();

    private Integer insertedBalance = 0;
    private Integer changeBalance = 0;
    private List<Coin> changeCoins = new ArrayList<>();

    public void setChangeBalance(Integer changeBalance) {
        this.changeBalance = changeBalance;
    }

    public Integer getInsertedBalance() {
        return insertedBalance;
    }
    public List<Coin> getChangeCoins() {
        return changeCoins;
    }


    public void updateStock(Coin coin, int number) {
        coinStock.put(coin, number);
    }

    public int getStock(Coin coin) {
        return coinStock.get(coin);
    }

    public void addChangeCoin(Coin coin) {
        changeCoins.add(coin);
    }

    public void addCoinToChangeAndUpdateStock(Coin coin) {
        addChangeCoin(coin);
        setChangeBalance(changeBalance - coin.getValue());
        updateStock(coin, getStock(coin) - 1);
    }

    public boolean isEnoughBalance(int drinkPrice) {
        return insertedBalance >= drinkPrice;
    }

    public void insertCoin(Coin coin) {
        insertedBalance = insertedBalance + coin.getValue();
        coinStock.put(coin, coinStock.get(coin) + 1);
    }

    public void updateChangeAfterApplyPrice(int drinkPrice) {
        changeBalance = insertedBalance - drinkPrice;
        updateChangeCoins();
    }

    public void updateChangeCoins() {
        if (changeBalance == 0) {
            return;
        }

        while (changeBalance > 0) {
            if (changeBalance >= Coin.TWO_EUROS.getValue() && isCoinInStock(Coin.TWO_EUROS)) {
                addCoinToChangeAndUpdateStock(Coin.TWO_EUROS);
            } else if (changeBalance >= Coin.ONE_EURO.getValue() && isCoinInStock(Coin.ONE_EURO)) {
                addCoinToChangeAndUpdateStock(Coin.ONE_EURO);
            } else if (changeBalance >= Coin.FIFTY_CENTS.getValue() && isCoinInStock(Coin.FIFTY_CENTS)) {
                addCoinToChangeAndUpdateStock(Coin.FIFTY_CENTS);
            } else if (changeBalance >= Coin.TWENTY_CENTS.getValue() && isCoinInStock(Coin.TWENTY_CENTS)) {
                addCoinToChangeAndUpdateStock(Coin.TWENTY_CENTS);
            } else if (changeBalance >= Coin.TEN_CENTS.getValue() && isCoinInStock(Coin.TEN_CENTS)) {
                addCoinToChangeAndUpdateStock(Coin.TEN_CENTS);
            } else if (changeBalance >= Coin.FIVE_CENTS.getValue() && isCoinInStock(Coin.FIVE_CENTS)) {
                addCoinToChangeAndUpdateStock(Coin.FIVE_CENTS);
            } else {
                throw new NoChangeException("No change available for this product");
            }
        }
    }

    public void applyRefund() {
        setChangeBalance(insertedBalance);
        clearInsertedBalance();
        updateChangeCoins();
        setChangeBalance(null);
    }

    private boolean isCoinInStock(Coin coin) {
        return getStock(coin) > 0;
    }

    public void clearInsertedBalance() {
        insertedBalance = 0;
    }
}
