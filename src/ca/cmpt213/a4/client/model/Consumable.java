package ca.cmpt213.a4.client.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Consumable implements Comparable<Consumable> {
    private String consumableType;
    private String name;
    private String notes;
    private double price;
    private double mass;
    private LocalDateTime expiryDate;

    /**
     * retrieves the consumable type stored in the field
     * @return the consumable's consumableType field
     */
    public String getConsumableType() {
        return consumableType;
    }

    /**
     * sets the consumable type field of the consumable
     * @param consumableType the type of the field passed in
     */
    public void setConsumableType(String consumableType) {
        this.consumableType = consumableType;
    }

    /**
     * retrieves food's name
     * @return private name field stored by food object
     */
    public String getName() {
        return name;
    }

    /**
     * sets the food name
     * @param name the name that is assigned to the private name field
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * retrieves the notes stored in food's field
     * @return food's private field to be used elsewhere
     */
    public String getNotes() {
        return notes;
    }

    /**
     * sets the notes of corresponding food
     * @param notes notes that are assigned to private field
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * retrieves food price
     * @return food's private price field
     */
    public double getPrice() {
        return price;
    }

    /**
     * sets price of food
     * @param price the price the item is set to
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * retrieves the mass of a certain consumable
     * @return the mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * sets mass of certain consumable
     * @param mass the variable that is assigned to the consumable field
     */
    public void setMass(double mass) {
        this.mass = mass;
    }


    /**
     * retrieves expiry date from food object
     * @return Food's private field of expiry date
     */
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    /**
     * sets expiry date of food object
     */
    public void setExpiryDate(LocalDateTime date) {
        this.expiryDate = date;
    }

    /**
     * method to set the date and time; converted using the LocalDateTime object
     * numbers that are input are ensured valid by the textMenu code blocks.
     * @param year expiry year passed in
     * @param month expiry month passed in
     * @param day expiry day passed in
     */
    public void convertDateTime(int year, int month, int day) {
        this.expiryDate = LocalDateTime.of(year, month, day, 23, 59);
    }

    /**
     * format expiry date to cleaner, userFriendly interpretation
     * @param expiryDate expiry date stored by food object
     * @return formatted date
     */
    public String formatExpiryDate(LocalDateTime expiryDate) {
        DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return expiryDate.format(newFormat);
    }

    /**
     * method that is used in the Comparable interface; sorts the consumables by expiry dates
     * @param c the consumable that has its expiry compared to the calling object's expiry date
     * @return 0 if theyre equal, 1 if the calling object is after expiry date, -1 if
     * calling object expiry is before the parameter's expiry
     */
    @Override
    public int compareTo(Consumable c) {
        if(expiryDate.equals(c.getExpiryDate())) {
            return 0;
        } else if(expiryDate.isAfter(c.getExpiryDate())) {
            return 1;
        } else {
            return -1;
        }
    }
}
