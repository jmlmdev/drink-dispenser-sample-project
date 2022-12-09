package org.example.dispenser.model.response;

import org.example.dispenser.model.Coin;
import org.example.dispenser.model.Drink;

import java.util.List;
import java.util.Objects;

public class SelectDrinkResponse {
    private Drink drink;
    private Integer price;
    private List<Coin> change;

    public SelectDrinkResponse(Drink drink, List<Coin> change, Integer price) {
        this.drink = drink;
        this.change = change;
        this.price = price;
    }

    public List<Coin> getChange() {
        return change;
    }
    public Integer getPrice() {
        return price;
    }
    public Drink getDrink() {
        return drink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectDrinkResponse that = (SelectDrinkResponse) o;
        return drink == that.drink && Objects.equals(price, that.price) && Objects.equals(change, that.change);
    }

    @Override
    public int hashCode() {
        return Objects.hash(drink, price, change);
    }
}
