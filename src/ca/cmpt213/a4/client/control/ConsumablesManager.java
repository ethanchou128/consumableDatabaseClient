package ca.cmpt213.a4.client.control;

import ca.cmpt213.a4.client.model.Consumable;
import ca.cmpt213.a4.client.model.Drink;
import ca.cmpt213.a4.client.model.Food;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * "manager" class that handles all modifications of the list of consumables
 * and the list's information.
 */
public class ConsumablesManager {
    private static List<Consumable> consumableList = new ArrayList<>();
    private static List<Consumable> unfilteredConsumableList = new ArrayList<>();
    private static ConsumablesManager instance;

    public static ConsumablesManager getInstance() {
        if(instance == null) {
            instance = new ConsumablesManager();
        }
        return instance;
    }

    /**
     * helper method to retrieve the mass value (weight for food, volume for drinks)
     * from corresponding consumable items
     * @param consumable the consumable in question to have its mass value extracted
     * @return the double of the mass volume
     */
    private static double getMass(Consumable consumable) {
        Class consumableType = consumable.getClass();
        if(consumableType.equals(Food.class)
                || consumable.getConsumableType().equals("Food")) {
            return ((Food) consumable).getWeight();
        } else if(consumableType.equals(Drink.class)
                || consumable.getConsumableType().equals("Drink")) {
            return ((Drink) consumable).getVolume();
        } else {
            return 0.0;
        }
    }

    /**
     * helper to "stringify" the consumable type for a more helpful program output.
     * @param consumable consumable in question that has its value determined
     * @return the user-friendly string
     */
    private String stringifyFoodType(Consumable consumable) {
        if(consumable.getConsumableType().equals("Food")) {
            return "This is a Food item.";
        } else {
            return "This is a Drink item.";
        }
    }

    /**
     * helper to stringify the mass units into another more helpful program output
     * @param consumable  consumable in question that has its type matched to a corresponding string
     * @return the user-friendly string
     */
    private String stringifyMassUnits(Consumable consumable) {
        if(consumable.getConsumableType().equals("Food")) {
            return "Weight: " + getMass(consumable) + "g";
        } else {
            return "Volume: " + getMass(consumable) + "mL";
        }
    }

    /**
     * getter for the arrayList
     * @return the ConsumableManager's arrayList of consumables
     */
    public List<Consumable> getConsumablesList() {
        return consumableList;
    }

    /**
     * helper method to execute commands from the server according to the specified filter requested by the
     * user on the client GUI with the buttons at the top of the ListItemsWindow
     * @param itemFilter the filter at the end of the curl command to be appended to the
     *                   end of the command and executed.
     */
    public void executeCommandToServer(String itemFilter) {
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).setPrettyPrinting().create();
        try {
            String serverJSONString = "";
            String curlCommand = "curl -H \"Content-Type: application/json\" -X GET localhost:8080/" + itemFilter;
            Process process = Runtime.getRuntime().exec(curlCommand);
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String readNull;
            while ((readNull = bufferedReader.readLine()) != null) {
                serverJSONString += readNull;
            }
            Type listType = new TypeToken<ArrayList<Consumable>>(){}.getType();
            unfilteredConsumableList = myGson.fromJson(serverJSONString, listType);
            repairConsumableList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * method to load all the items from the server on startup
     * @throws IOException
     */
    public void loadItemsFromServer() throws IOException {
        executeCommandToServer("load");
    }

    /**
     * method to get an arraylist of all items no matter the expiry date
     * (soonest expiry dates will appear first)
     * @throws IOException
     */
    public void getAllItemsFromServer() throws IOException {
        executeCommandToServer("listAll");
    }

    /**
     * method to get array list of all expired items
     * (soonest expiry dates will appear first)
     * @throws IOException
     */
    public void getExpiredConsumablesFromServer() throws IOException {
        executeCommandToServer("listExpired");
    }

    /**
     * method to get arraylist of all non-expired items
     * (soonest expiry dates will appear first)
     * @throws IOException
     */
    public void getNonExpiredConsumablesFromServer() throws IOException {
        executeCommandToServer("listNonExpired");
    }

    /**
     * method to get arraylist of all items expiring in <=7 days
     * (soonest expiry dates will appear first)
     * @throws IOException
     */
    public void getConsumablesExpiringIn7DaysFromServer() throws IOException {
        executeCommandToServer("listExpiringIn7Days");
    }

    /**
     * helper used with deleting food list to determine eligibility for indexes to be removed
     * @return size of foodList
     */
    public int getListSize() {
        return consumableList.size();
    }

    /**
     * method to determine whether an item is expired or not
     * @param numToBeChecked # used by the expiry date checking lists indicating # days
     * @return true if expired, false if not
     */
    public boolean isExpired(long numToBeChecked) {
        return numToBeChecked < 0;
    }

    /**
     * method to make a more user-friendly output when listing items
     * @param consumable item to be "stringified"
     * @return the resulting string
     */
    public String toString(Consumable consumable) {
        return stringifyFoodType(consumable) +
                "\nConsumable name: " + consumable.getName() +
                "\nNotes: " + consumable.getNotes() +
                "\nPrice: " + consumable.getPrice() +
                "\n" + stringifyMassUnits(consumable) +
                "\nExpiry Date: " + consumable.formatExpiryDate(consumable.getExpiryDate());
    }

    //helper method used to parse the content into a json file accordingly.
    /**
     * helper method used to help the readFile parse the information into a Json file
     * and split the items into the subclasses.
     */
    public static void repairConsumableList() {
        consumableList.clear();
        for(Consumable c : unfilteredConsumableList) {
            String consumableType = c.getConsumableType();
            String consumableName = c.getName();
            String consumableNotes = c.getNotes();
            double consumablePrice = c.getPrice();
            double consumableMass = c.getMass();
            LocalDateTime expiryDate = c.getExpiryDate();
            Consumable newConsumable;
            if(consumableType.equals("Food")) {
                newConsumable = new Food();
                ((Food)newConsumable).setWeight(consumableMass);
            } else {
                newConsumable = new Drink();
                ((Drink)newConsumable).setVolume(consumableMass);
            }
            newConsumable.setConsumableType(consumableType);
            newConsumable.setName(consumableName);
            newConsumable.setNotes(consumableNotes);
            newConsumable.setPrice(consumablePrice);
            newConsumable.setMass(consumableMass);
            newConsumable.setExpiryDate(expiryDate);
            consumableList.add(newConsumable);
        }
    }
}


