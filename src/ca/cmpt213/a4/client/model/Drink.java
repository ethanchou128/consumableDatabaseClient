package ca.cmpt213.a4.client.model;

public class Drink extends Consumable {
    private double volume;

    /**
     * method to retrieve the Drink subclass's volume field
     * @return the subclass volume field
     */
    public double getVolume() {
        return volume;
    }

    /**
     * method to set the volume field's value
     * @param volume the value to be set to the subclass's volume field
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    /**
     * empty constructor so no one else can instantiate it
     */
    public Drink() {

    }
}
