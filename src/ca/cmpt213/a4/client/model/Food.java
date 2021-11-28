package ca.cmpt213.a4.client.model;

public class Food extends Consumable {
    private double weight;

    /**
     * method to retrieve the weight from the Food subclass
     * @return the subclass's weight field
     */
    public double getWeight() {
        return weight;
    }

    /**
     * method to set the Food's weight field
     * @param weight the value that is set to weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * empty constructor so no one else can instantiate it
     */
    public Food() {

    }
}
