package ca.cmpt213.a4.client.view;

import ca.cmpt213.a4.client.control.ConsumableFactory;
import ca.cmpt213.a4.client.control.ConsumablesManager;
import ca.cmpt213.a4.client.model.Consumable;
import ca.cmpt213.a4.client.model.Drink;
import ca.cmpt213.a4.client.model.Food;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.optionalusertools.DateTimeChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateTimeChangeEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class AddItemWindow extends JDialog implements ActionListener, DateTimeChangeListener {
    JDialog dialog;
    JDialog errorDialog;
    JDialog successDialog;

    JPanel labelsPanel = new JPanel();

    JPanel mainTypePanel = new JPanel();
    JPanel mainNamePanel = new JPanel();
    JPanel mainNotesPanel = new JPanel();
    JPanel mainPricePanel = new JPanel();
    JPanel mainMassPanel = new JPanel();
    JPanel mainExpiryDatePanel = new JPanel();

    JPanel typeLabelPanel = new JPanel();
    JPanel nameLabelPanel = new JPanel();
    JPanel notesLabelPanel = new JPanel();
    JPanel priceLabelPanel = new JPanel();
    JPanel massLabelPanel = new JPanel();
    JPanel expiryLabelPanel = new JPanel();

    JPanel userInputsPanel = new JPanel();

    JPanel typeInputPanel = new JPanel();
    JPanel nameInputPanel = new JPanel();
    JPanel notesInputPanel = new JPanel();
    JPanel priceInputPanel = new JPanel();
    JPanel massInputPanel = new JPanel();
    JPanel expiryDateInputPanel = new JPanel();

    JPanel buttonPanel = new JPanel();
    JPanel addButtonPanel = new JPanel();
    JPanel exitButtonPanel = new JPanel();

    JComboBox<String> typeChoices;
    JTextField newNameInput;
    JTextField newNotesInput;
    JTextField newPriceInput;
    JTextField newMassInput;

    JLabel typeLabel = new JLabel("Food Type: ");
    JLabel nameLabel = new JLabel("Name: ");
    JLabel notesLabel = new JLabel("Notes: ");
    JLabel priceLabel = new JLabel("Price: ");
    JLabel massLabel = new JLabel("Mass: "); //debug later to change label according to type
    JLabel expiryDateLabel = new JLabel("Expiry Date: ");
    JLabel errorMessage = new JLabel("Error: One or more fields are invalid.");
    JLabel successMessage = new JLabel("Item added. Click 'X' to return to main menu.");

    JButton confirmAddButton = new JButton("Add Item");
    JButton exitButton = new JButton("Exit");

    Dimension DEFAULT_PANEL_SIZE = new Dimension(75, 25);
    Dimension MAIN_DIALOG_SIZE = new Dimension(600, 600);
    Dimension DEFAULT_DIALOG_SIZE = new Dimension(350, 150);

    Consumable newConsumable = null;
    ConsumablesManager consumablesManager = ConsumablesManager.getInstance();

    DateTimePicker dateTimePicker = new DateTimePicker();

    private final ConsumableFactory consumableFactory = new ConsumableFactory();
    private FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
    private LocalDateTime expiryDate;

    public AddItemWindow() {
        dialog = new JDialog(AddItemWindow.this, true);
        dialog.setTitle("Add Item");
        dialog.setMinimumSize(MAIN_DIALOG_SIZE);
        dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));
        labelsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dialog.add(labelsPanel, BorderLayout.WEST);

        userInputsPanel.setLayout(new BoxLayout(userInputsPanel, BoxLayout.Y_AXIS));
        userInputsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dialog.add(userInputsPanel, BorderLayout.CENTER);

        //code to add the type label and combobox to the "main" type panel
        mainTypePanel.setLayout(flowLayout);
        typeLabelPanel.setPreferredSize(DEFAULT_PANEL_SIZE);
        typeLabelPanel.add(typeLabel);
        mainTypePanel.add(typeLabelPanel);

        String[] FOOD_OR_DRINK = {"Food", "Drink"};
        typeChoices = new JComboBox<String>(FOOD_OR_DRINK);
        typeChoices.setSelectedItem(null);
        typeChoices.addActionListener(this);

        typeInputPanel.add(typeChoices);
        mainTypePanel.add(typeChoices);

        userInputsPanel.add(mainTypePanel);

        //add name label and textfield to the "main" name panel
        mainNamePanel.setLayout(flowLayout);
        nameLabelPanel.setPreferredSize(DEFAULT_PANEL_SIZE);
        nameLabelPanel.add(nameLabel);
        mainNamePanel.add(nameLabelPanel);

        int maxTextfieldWidth = 30;
        newNameInput = new JTextField(maxTextfieldWidth);
        nameInputPanel.add(newNameInput);
        mainNamePanel.add(nameInputPanel);
        userInputsPanel.add(mainNamePanel);

        //add notes label and textfield to "main" note panel
        mainNotesPanel.setLayout(flowLayout);
        notesLabelPanel.setPreferredSize(DEFAULT_PANEL_SIZE);
        notesLabelPanel.add(notesLabel);
        mainNotesPanel.add(notesLabelPanel);

        newNotesInput = new JTextField(maxTextfieldWidth);
        notesInputPanel.add(newNotesInput);
        mainNotesPanel.add(notesInputPanel);
        userInputsPanel.add(mainNotesPanel);

        //add price label and textfield to "main" price panel
        mainPricePanel.setLayout(flowLayout);
        priceLabelPanel.setPreferredSize(DEFAULT_PANEL_SIZE);
        priceLabelPanel.add(priceLabel);
        mainPricePanel.add(priceLabelPanel);

        newPriceInput = new JTextField(maxTextfieldWidth);
        priceInputPanel.add(newPriceInput);
        mainPricePanel.add(priceInputPanel);
        userInputsPanel.add(mainPricePanel);

        //add mass label and textfield to "main" mass panel
        //the changes between "weight" and "volume" label according to type happens in the ActionListener below
        mainMassPanel.setLayout(flowLayout);
        massLabelPanel.setPreferredSize(DEFAULT_PANEL_SIZE);
        massLabelPanel.add(massLabel);
        mainMassPanel.add(massLabelPanel);

        newMassInput = new JTextField(maxTextfieldWidth);
        massInputPanel.add(newMassInput);
        mainMassPanel.add(massInputPanel);
        userInputsPanel.add(mainMassPanel);

        //add expiry date label and the according combo boxes to set the dates.
        mainExpiryDatePanel.setLayout(flowLayout);
        expiryLabelPanel.setPreferredSize(DEFAULT_PANEL_SIZE);
        expiryLabelPanel.add(expiryDateLabel);
        mainExpiryDatePanel.add(expiryLabelPanel);
        expiryDateInputPanel.setLayout(flowLayout);

        expiryDateInputPanel.add(dateTimePicker);
        dateTimePicker.addDateTimeChangeListener(this);

        mainExpiryDatePanel.add(expiryDateInputPanel);
        userInputsPanel.add(mainExpiryDatePanel);

        confirmAddButton.addActionListener(this);
        addButtonPanel.add(confirmAddButton);
        buttonPanel.add(addButtonPanel);

        exitButton.addActionListener(this);
        exitButtonPanel.add(exitButton);
        buttonPanel.add(exitButtonPanel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    /**
     * function to check if all the inputs in the add window are valid and can
     * be processed/parsed by the system
     */
    private void checkValidityOfInputs() {
        try {
            //check for an inputted name
            if (newNameInput.getText().isEmpty()) {
                throw new IllegalArgumentException();
            } else {
                newConsumable.setName(newNameInput.getText());
            }
            //set notes for each item; if null just add "N/A" to the notes section of each item
            if (newNotesInput.getText().isEmpty()) {
                newConsumable.setNotes("N/A");
            } else {
                newConsumable.setNotes(newNotesInput.getText());
            }

            //check for valid price input
            if (newPriceInput.getText().isEmpty()) {
                throw new IllegalArgumentException();
            }
            double parsedUserPrice;
            try {
                parsedUserPrice = Double.parseDouble(newPriceInput.getText());
                if (parsedUserPrice < 0) {
                    throw new NumberFormatException();
                }
                newConsumable.setPrice(parsedUserPrice);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException();
            }

            //check for valid mass input
            if (newMassInput.getText().isEmpty()) {
                throw new IllegalArgumentException();
            }
            double parsedMass;
            try {
                parsedMass = Double.parseDouble(newMassInput.getText());
                if (parsedMass < 0) {
                    throw new NumberFormatException();
                }
                Class consumableType = newConsumable.getClass();
                if(consumableType.equals(Food.class)) {
                    ((Food) newConsumable).setWeight(parsedMass);
                } else {
                    ((Drink) newConsumable).setVolume(parsedMass);
                }
                newConsumable.setMass(parsedMass);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException();
            }

            newConsumable.setExpiryDate(expiryDate);

            successDialog = new JDialog(AddItemWindow.this, true);
            successDialog.setMinimumSize(DEFAULT_DIALOG_SIZE);
            successDialog.add(successMessage, SwingConstants.CENTER);
            successDialog.setVisible(true);
            String command = createAddItemCurlCommand(newConsumable);
            Process process = Runtime.getRuntime().exec(command);
            process.getInputStream();
            dispose();
        } catch (Exception e) {
            errorDialog = new JDialog(AddItemWindow.this, true);
            errorDialog.setMinimumSize(DEFAULT_DIALOG_SIZE);
            errorDialog.add(errorMessage, SwingConstants.CENTER);
            errorDialog.setVisible(true);
        }
    }

    /**
     * actionlisteners for the buttons on the screen.
     * @param e the action event that is triggered by a button press or dropdown selection switch
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(typeChoices.getSelectedItem() != null) {
            if (typeChoices.getSelectedItem().equals("Food")) {
                massLabel.setText("Weight (g): ");
                newConsumable = consumableFactory.getConsumableType(1);
                newConsumable.setConsumableType("Food");
            } else {
                massLabel.setText("Volume (ml): ");
                newConsumable = consumableFactory.getConsumableType(2);
                newConsumable.setConsumableType("Drink");
            }
        }
        if(e.getActionCommand().equals("Add Item")) {
            checkValidityOfInputs();
        }
        if(e.getActionCommand().equals("Exit")) {
            dispose();
        }
    }

    /**
     * helper method to generate a curl command for a new item
     * which is executed to add an item to the array list in the server
     * @param consumable the consumable the user has inputted to add through
     *                   the AddItemsWindow which the data is extracted and placed
     *                   in the curl command accordingly
     * @return the curl command executed above; in a String object.
     */
    private String createAddItemCurlCommand(Consumable consumable) {
       return "curl -i -H \"Content-Type: application/json\" -X POST -d " +
               "\"{\\\"consumableType\\\": \\\"" + consumable.getConsumableType() + "\\\", " +
               "\\\"name\\\": \\\"" + consumable.getName() + "\\\", " +
               "\\\"notes\\\": \\\"" + consumable.getNotes() + "\\\", " +
               "\\\"price\\\": " + consumable.getPrice() + ", " +
               "\\\"mass\\\": " + consumable.getMass() + ", " +
               "\\\"expiryDate\\\": \\\"" + consumable.getExpiryDate() + "\\\"}\" localhost:8080/addItem";
    }

    /**
     * override function for DateTimeChangeListener
     * @param dateTimeChangeEvent event that triggers function
     */
    @Override
    public void dateOrTimeChanged(DateTimeChangeEvent dateTimeChangeEvent) {
        expiryDate = dateTimeChangeEvent.getNewDateTimeStrict();
    }
}
