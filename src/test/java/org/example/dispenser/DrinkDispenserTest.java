package org.example.dispenser;

import org.example.dispenser.exception.NoChangeException;
import org.example.dispenser.exception.SoldOutException;
import org.example.dispenser.manager.CashManager;
import org.example.dispenser.manager.DrinkManager;
import org.example.dispenser.model.Coin;
import org.example.dispenser.model.Drink;
import org.example.dispenser.model.response.SelectDrinkResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DrinkDispenserTest {

    private CashManager cashManager;
    private DrinkManager drinkManager;
    private DrinkDispenser drinkDispenser;

    @BeforeEach
    void setUp() {
        cashManager = new CashManager();
        drinkManager = new DrinkManager();
        drinkDispenser = new DrinkDispenser(cashManager,drinkManager);
    }

    @Test
    void selectDrink_shouldReturnJustTheDrinkPriceIfBalanceIsNotEnough() {
        drinkDispenser.insertCoin(Coin.FIFTY_CENTS);
        var expected = new SelectDrinkResponse(null,null,Drink.COKE.getPrice());
        Assertions.assertEquals(expected,drinkDispenser.selectDrink(Drink.COKE));
    }

    @Test
    void selectDrink_shouldThrowAnExceptionIfDrinkIsSoldOut() {
        drinkManager.updateStock(Drink.COKE,0);
        assertThrows(SoldOutException.class,
                ()->drinkDispenser.selectDrink(Drink.COKE));
    }

    @Test
    void selectDrink_shouldReturnTheDrinkWithNoChangeIfBalanceIsSameAsPrice() {
        drinkDispenser.insertCoin(Coin.ONE_EURO);
        var expected = new SelectDrinkResponse(Drink.COKE, Collections.EMPTY_LIST,Drink.COKE.getPrice());
        Assertions.assertEquals(expected,drinkDispenser.selectDrink(Drink.COKE));
    }

    @Test
    void selectDrink_shouldReturnTheDrinkWithChangeIfBalanceIsMoreThanPrice1() {
        drinkDispenser.insertCoin(Coin.TWO_EUROS);
        var response = drinkDispenser.selectDrink(Drink.COKE);
        var changeValue = response.getChange().stream().mapToInt(Coin::getValue).sum();

        Assertions.assertEquals(Drink.COKE,response.getDrink());
        Assertions.assertEquals(Drink.COKE.getPrice(), response.getPrice());
        Assertions.assertEquals(100, changeValue);
    }

    @Test
    void selectDrink_shouldReturnTheDrinkWithChangeIfBalanceIsMoreThanPrice2() {
        drinkDispenser.insertCoin(Coin.TWO_EUROS);
        drinkDispenser.insertCoin(Coin.TWO_EUROS);
        var response = drinkDispenser.selectDrink(Drink.ORANGE_JUICE);
        var changeValue = response.getChange().stream().mapToInt(Coin::getValue).sum();

        Assertions.assertEquals(Drink.ORANGE_JUICE,response.getDrink());
        Assertions.assertEquals(Drink.ORANGE_JUICE.getPrice(), response.getPrice());
        Assertions.assertEquals(205, changeValue);
    }

    @Test
    void selectDrink_shouldReturnAnExceptionIfThereIsNoEnoughChange() {
        // Clear coin stock
        Stream.of(Coin.values()).forEach(coin -> cashManager.updateStock(coin, 0));
        // Add not enough coin stock
        cashManager.updateStock(Coin.FIFTY_CENTS,1);
        // Insert enough balance to need not present change
        drinkDispenser.insertCoin(Coin.TWO_EUROS);

        assertThrows(NoChangeException.class,
                ()->drinkDispenser.selectDrink(Drink.COKE));
    }

    @Test
    void refund_shouldNotReturnAnyChangeIfBalanceIsNotGreaterThanZero() {
        Assertions.assertEquals(Collections.emptyList(),drinkDispenser.refund());
    }

    @Test
    void refund_shouldReturnInsertedBalance() {
        drinkDispenser.insertCoin(Coin.FIVE_CENTS);
        drinkDispenser.insertCoin(Coin.FIFTY_CENTS);
        drinkDispenser.insertCoin(Coin.TWO_EUROS);
        var refundValue = drinkDispenser.refund().stream().mapToInt(Coin::getValue).sum();
        Assertions.assertEquals(255,refundValue);
    }

    @Test
    void insertCoin_shouldUpdateCoinStock() {
        drinkDispenser.insertCoin(Coin.TWO_EUROS);
        drinkDispenser.insertCoin(Coin.TWENTY_CENTS);

        Assertions.assertEquals(cashManager.getStock(Coin.TWO_EUROS),11);
        Assertions.assertEquals(cashManager.getStock(Coin.TWENTY_CENTS),11);
    }
}