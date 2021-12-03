package ca.cmpt213.a4.client.control;

import ca.cmpt213.a4.client.model.Consumable;
import ca.cmpt213.a4.client.model.Drink;
import ca.cmpt213.a4.client.model.Food;

/**
 * class used to determine subclass of consumables
 */
public class ConsumableFactory {
    /**
     * "factory method" used to determine the subclass of a consumable that
     * the user creates in the "Add Game" use case of the game.
     * @param userInputType the user input, which helps determine which subclass
     *                      the new consumable will belong to.
     * @return the new Consumable of the correct subclass, or null if the userInput is invalid.
     */
    public Consumable getConsumableType(int userInputType) {
        if(userInputType == 1) {
            return new Food();
        } else if(userInputType == 2) {
            return new Drink();
        } else {
            return null;
        }
    }
}
