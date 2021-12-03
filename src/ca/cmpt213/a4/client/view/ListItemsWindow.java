package ca.cmpt213.a4.client.view;


import ca.cmpt213.a4.client.control.ConsumablesManager;
import ca.cmpt213.a4.client.model.Consumable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

public class ListItemsWindow extends JDialog implements ActionListener {
    JFrame mainFrame = new JFrame("No Mold = Gold Database");
    JTextArea itemListArea = new JTextArea();
    JScrollPane listOfItems;

    JDialog removeItemDialog;

    JPanel mainItemListingPanel = new JPanel();
    JPanel listButtonsPanel = new JPanel();
    JPanel addDeleteButtonsPanel = new JPanel();
    JPanel mainDisplayPanel = new JPanel();

    JPanel allItemButtonPanel = new JPanel();
    JPanel expiredItemButtonPanel = new JPanel();
    JPanel freshItemButtonPanel = new JPanel();
    JPanel sevenDaysButtonPanel = new JPanel();
    JPanel addButtonPanel = new JPanel();
    JPanel deleteButtonPanel = new JPanel();
    JPanel quitButtonPanel = new JPanel();
    JPanel currentDateTimePanel = new JPanel();

    JButton allButton = new JButton("All");
    JButton expiredButton = new JButton("Expired");
    JButton freshButton = new JButton("Non-Expired");
    JButton sevenDaysButton = new JButton("Expires within 7 Days");
    JButton addButton = new JButton("Add Item");
    JButton deleteButton = new JButton("Delete Item");
    JButton quitButton = new JButton("Quit");

    JLabel emptyLabel;
    JLabel currentDateLabel;

    //components of the remove item dialog
    JPanel mainRemoveItemPanel = new JPanel();
    JPanel removeLabelPanel = new JPanel();
    JPanel removeTextFieldPanel = new JPanel();
    JPanel removeDialogButtonPanel = new JPanel();

    JPanel confirmRemovePanel = new JPanel();
    JPanel exitDialogPanel = new JPanel();

    JButton cancelRemoveButton = new JButton("Cancel");
    JButton confirmRemoveButton = new JButton("Confirm");
    JLabel removeItemPrompt;
    JTextField removeItemInput = new JTextField(20);

    Dimension DEFAULT_DIALOG_SIZE = new Dimension(350, 150);

    WindowListener windowListener = new WindowListener() {
        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            try {
                quitProgram();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    };

    private final ConsumablesManager consumablesManager = new ConsumablesManager();

    private boolean isListingAll;
    private boolean isListingExpired;
    private boolean isListingNonExpired;
    private boolean isListingExpiringIn7Days;

//    private final String fileName;
//    private final boolean fileExists;

    //TODO: add title to window
    public ListItemsWindow() {

//        fileName = "text.json";
//        fileExists = ConsumablesManager.loadFile(fileName);
        try {
            consumablesManager.getAllItemsFromServer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        mainFrame.setMinimumSize(new Dimension(600, 400));
        mainFrame.setPreferredSize(new Dimension(800, 800));
        mainFrame.addWindowListener(windowListener);
        mainDisplayPanel.setLayout(new BoxLayout(mainDisplayPanel, BoxLayout.Y_AXIS));
        mainItemListingPanel.setLayout(new BoxLayout(mainItemListingPanel, BoxLayout.Y_AXIS));

        listButtonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        addDeleteButtonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        allButton.addActionListener(this);
        expiredButton.addActionListener(this);
        freshButton.addActionListener(this);
        sevenDaysButton.addActionListener(this);
        addButton.addActionListener(this);
        deleteButton.addActionListener(this);
        quitButton.addActionListener(this);

        allItemButtonPanel.add(allButton);
        expiredItemButtonPanel.add(expiredButton);
        freshItemButtonPanel.add(freshButton);
        sevenDaysButtonPanel.add(sevenDaysButton);
        addButtonPanel.add(addButton);
        deleteButtonPanel.add(deleteButton);
        quitButtonPanel.add(quitButton);

        listButtonsPanel.add(allItemButtonPanel);
        listButtonsPanel.add(expiredItemButtonPanel);
        listButtonsPanel.add(freshItemButtonPanel);
        listButtonsPanel.add(sevenDaysButtonPanel);

        addDeleteButtonsPanel.add(addButton);
        addDeleteButtonsPanel.add(deleteButton);
        addDeleteButtonsPanel.add(quitButton);

        currentDateLabel = new JLabel("Today's Date is: "+ getFormattedExpiryDate());
        currentDateTimePanel.add(currentDateLabel);
        addDeleteButtonsPanel.add(currentDateTimePanel, RIGHT_ALIGNMENT);

        mainDisplayPanel.add(listButtonsPanel);
        listOfItems = new JScrollPane(mainItemListingPanel);
        mainDisplayPanel.add(listOfItems);
        itemListArea.setEditable(false);

        mainFrame.add(addDeleteButtonsPanel, BorderLayout.SOUTH);

        mainFrame.add(mainDisplayPanel);
        mainFrame.setVisible(true);

        //this is the default setting if it is the first time the user loads the program
        listItems();
    }

    /**
     * action event listener
     * @param e event that is passed in and function acts accordingly.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("All")) {
            clearItemListings();
            retrieveAllItemsFromServer();
        } else if (e.getActionCommand().equals("Expired")) {
            clearItemListings();
            retrieveExpiredItemsFromServer();
        } else if (e.getActionCommand().equals("Non-Expired")) {
            clearItemListings();
            retrieveNonExpiredItemsFromServer();
        } else if (e.getActionCommand().equals("Expires within 7 Days")){
            clearItemListings();
            retrieveItemsExpiringIn7DaysFromServer();
        } else if (e.getActionCommand().equals("Add Item")) {
            new AddItemWindow();
            updateListViewAfterRemoving();
        } else if (e.getActionCommand().equals("Delete Item")) {
            removeItem();
            updateListViewAfterRemoving();
        } else if (e.getActionCommand().equals("Confirm")) {
            checkValidIndex();
        } else if (e.getActionCommand().equals("Cancel")) {
            dispose();
        } else if(e.getActionCommand().equals("Quit")) {
            try {
                quitProgram();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void retrieveAllItemsFromServer() {
        isListingAll = true;
        isListingExpired = false;
        isListingNonExpired = false;
        isListingExpiringIn7Days = false;
        try {
            consumablesManager.getAllItemsFromServer();
            listItems();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void retrieveExpiredItemsFromServer() {
        isListingAll = false;
        isListingExpired = true;
        isListingNonExpired = false;
        isListingExpiringIn7Days = false;
        try {
            consumablesManager.getExpiredConsumablesFromServer();
            listItems();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void retrieveNonExpiredItemsFromServer() {
        isListingAll = false;
        isListingExpired = false;
        isListingNonExpired = true;
        isListingExpiringIn7Days = false;
        try {
            consumablesManager.getNonExpiredConsumablesFromServer();
            listItems();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void retrieveItemsExpiringIn7DaysFromServer() {
        isListingAll = false;
        isListingExpired = false;
        isListingNonExpired = false;
        isListingExpiringIn7Days = true;
        try {
            consumablesManager.getConsumablesExpiringIn7DaysFromServer();
            listItems();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * method to list all items currently in the filtered lists from server
     */
    private void listItems() {
        clearItemListings();
        int counter = 1;
        Collections.sort(consumablesManager.getConsumablesList());
        for(Consumable c : consumablesManager.getConsumablesList()) {
            String output = "";
            output += "Item " + counter + "\n";
            output += consumablesManager.toString(c);
            long daysBetween = ChronoUnit.DAYS.between(LocalDateTime.now(), c.getExpiryDate());
            boolean isExpired = consumablesManager.isExpired(daysBetween);
            if(isExpired)  {
                daysBetween = daysBetween * (-1);
                output += "\nThis item has been expired for " + daysBetween + " days.";
            } else if (daysBetween == 0) {
                output += "\nThis item expires today.";
            } else {
                output += "\nThere is " + daysBetween + " days until this item expires.";
            }
            createNewPanelForList(output);
            counter++;
        }
        if(consumablesManager.getListSize() == 0) {
            emptyLabel = new JLabel("There are no items in the database.");
            mainItemListingPanel.add(emptyLabel);
        }
        returnToTop();
    }

    /**
     * method to create a new panel for each new stringified consumable according
     * to the filter that is chosen by the user.
     * @param output the string of information for the consumable which will be
     *               put into a panel via separate text areas.
     */
    private void createNewPanelForList(String output) {
        JTextArea newItemText = new JTextArea();
        JPanel newItemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        newItemPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        newItemPanel.add(newItemText);
        newItemText.setText(output);
        newItemText.setEditable(false);
        newItemText.setOpaque(false);
        mainItemListingPanel.add(newItemPanel);
    }

    /**
     * method that provides a new JDialog with which the user will input the item number
     * of the item they want to remove from the database.
     */
    private void removeItem() {
        mainRemoveItemPanel.setLayout(new BoxLayout(mainRemoveItemPanel,
                BoxLayout.Y_AXIS));
        removeItemDialog = new JDialog(ListItemsWindow.this, true);
        removeItemDialog.setTitle("Remove Item");
        removeItemDialog.setMinimumSize(DEFAULT_DIALOG_SIZE);
        removeItemDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        removeItemDialog.add(mainRemoveItemPanel, BorderLayout.CENTER);

        removeItemPrompt = new JLabel("What's the item # of the product you want to remove?",
                SwingConstants.CENTER);
        removeLabelPanel.add(removeItemPrompt);
        removeTextFieldPanel.add(removeItemInput);

        mainRemoveItemPanel.add(removeLabelPanel);
        mainRemoveItemPanel.add(removeTextFieldPanel);

        removeItemDialog.add(mainRemoveItemPanel);

        confirmRemoveButton.addActionListener(this);
        cancelRemoveButton.addActionListener(this);

        confirmRemovePanel.add(confirmRemoveButton);
        exitDialogPanel.add(cancelRemoveButton);

        removeDialogButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        removeDialogButtonPanel.add(confirmRemovePanel);
        removeDialogButtonPanel.add(exitDialogPanel);

        removeItemDialog.add(removeDialogButtonPanel, BorderLayout.SOUTH);

        removeItemDialog.setVisible(true);
    }

    /**
     * helper method used in tandem with removeItem() to check whether the item number input by the user
     * actually corresponds to an existing item in the list.
     */
    private void checkValidIndex() {
        JDialog removeVerificationDialog = new JDialog(removeItemDialog, true);
        removeVerificationDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        boolean isValidIndex = false;
        int index = 0;
        int listSize = consumablesManager.getListSize();
        try {
            index = Integer.parseInt(removeItemInput.getText());
            if(index < 1 || index > listSize) {
                throw new IllegalArgumentException();
            } else {
                isValidIndex = true;
                Process process = Runtime.getRuntime()
                        .exec(createRemoveItemCurlCommand(index-1));
                process.getInputStream();
                consumablesManager.deleteConsumable(index-1);
            }
        } catch (Exception e) {
            removeVerificationDialog.setMinimumSize(DEFAULT_DIALOG_SIZE);
            JLabel invalidIndex = new JLabel("Please enter a valid index between 1 and " + listSize);
            removeVerificationDialog.add(invalidIndex, SwingConstants.CENTER);
            removeVerificationDialog.setVisible(true);
        }

        if (isValidIndex) {
            JDialog validRemovalDialog = new JDialog(removeItemDialog, true);
            validRemovalDialog.setMinimumSize(DEFAULT_DIALOG_SIZE);
            validRemovalDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            JLabel removeSuccess = new JLabel("Item " + index + " has been removed.",
                    SwingConstants.CENTER);
            validRemovalDialog.add(removeSuccess);
            validRemovalDialog.setVisible(true);
            removeItemDialog.dispose();
        }
    }

    /**
     * method to create curl command for removing an item.
     */
    private String createRemoveItemCurlCommand(int index) {
        return "curl -i -H \"Content-Type: application/json\" -X POST localhost:8080/removeItem/" + index;
    }

    /**
     * method to refresh the items after an item is added according to what the user was viewing last
     */
    private void updateListViewAfterRemoving() {
        if(isListingAll) {
            retrieveAllItemsFromServer();
        } else if (isListingExpired) {
            retrieveExpiredItemsFromServer();
        } else if(isListingNonExpired) {
            retrieveNonExpiredItemsFromServer();
        } else {
            retrieveItemsExpiringIn7DaysFromServer();
        }
    }

    /**
     * method to clear the text area; used with intentions to refresh screen when user
     * wants to change their item view according to the corresponding filter.
     */
    private void clearItemListings() {
        mainItemListingPanel.removeAll();
        mainItemListingPanel.updateUI();
    }

    /**
     * method to return list of items back to top of list when a new
     * item filter is chosen.
     */
    private void returnToTop() {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                listOfItems.getViewport().setViewPosition( new Point(0, 0) );
            }
        });
    }

    /**
     * method to format expiry date to more user-friendly output
     */
    private String getFormattedExpiryDate() {
        DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDateTime.now().format(newFormat);
    }

    /**
     * method to quit program
     */
    private void quitProgram() throws IOException {
        try {
            Process process = Runtime.getRuntime()
                    .exec("curl -i -H \"Content-Type: application/json\" -X GET localhost:8080/exit");
            process.getInputStream();
        } catch (IOException file) {
            System.out.println("I don't know I got here.");
        }
        System.exit(0);
    }
}