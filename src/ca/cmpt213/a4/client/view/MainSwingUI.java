package ca.cmpt213.a4.client.view;


import ca.cmpt213.a4.client.control.ConsumablesManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainSwingUI extends JDialog implements ActionListener {
    JPanel mainPanel;
    JFrame applicationFrame;

    JScrollPane listOfItems;

    JButton listItemsButton;
    JButton addItemButton;
    JButton removeItemsButton;
    JButton listExpiredItemsButton;
    JButton listNonExpiredItemsButton;
    JButton listItemsExpiringIn7DaysButton;
    JButton quitButton;

    private ConsumablesManager consumablesManager;
    private boolean fileExists;
    private String fileName;

    private final int WINDOW_WIDTH = 600;
    private final int WINDOW_HEIGHT = 600;
    private String[] prompts = {
            "List all consumables.",
            "Add consumable",
            "Remove consumable",
            "List expired consumables",
            "List non-expired consumables",
            "List consumables expiring within 7 days",
            "Exit Program"
    };

    public MainSwingUI() {

        consumablesManager = new ConsumablesManager();

        fileName = "text.json";
        fileExists = ConsumablesManager.loadFile(fileName);

        mainPanel = new JPanel();
        mainPanel.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        applicationFrame = new JFrame("My Consumables Tracker");
        applicationFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        applicationFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //applicationFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        listItemsButton = new JButton(prompts[0]);
        listItemsButton.addActionListener(this);
        mainPanel.add(listItemsButton);

        addItemButton = new JButton(prompts[1]);
        addItemButton.addActionListener(this);
        mainPanel.add(addItemButton);

        removeItemsButton = new JButton(prompts[2]);
        removeItemsButton.addActionListener(this);
        mainPanel.add(removeItemsButton);

        listExpiredItemsButton = new JButton(prompts[3]);
        listExpiredItemsButton.addActionListener(this);
        mainPanel.add(listExpiredItemsButton);

        listNonExpiredItemsButton = new JButton(prompts[4]);
        listNonExpiredItemsButton.addActionListener(this);
        mainPanel.add(listNonExpiredItemsButton);

        listItemsExpiringIn7DaysButton = new JButton(prompts[5]);
        listItemsExpiringIn7DaysButton.addActionListener(this);
        mainPanel.add(listItemsExpiringIn7DaysButton);

        quitButton = new JButton(prompts[6]);
        quitButton.addActionListener(this);
        mainPanel.add(quitButton);

        applicationFrame.add(mainPanel);
        applicationFrame.setVisible(true);
    }

    //replace with swing functions accordingly
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == listItemsButton) {
            ListItemsWindow listItemsWindow = new ListItemsWindow();
        } else if (e.getActionCommand().equals(prompts[1])) {
            AddItemWindow addItemWindow = new AddItemWindow();
        } else if (e.getActionCommand().equals(prompts[2])) {
            System.out.println("sugondese");
        } else if (e.getActionCommand().equals(prompts[3])) {
            System.out.println("gargalon");
        } else if (e.getActionCommand().equals(prompts[4])) {
            System.out.println("yukon");
        } else if (e.getActionCommand().equals(prompts[5])){
            System.out.println("tulips");
        } else {
            try {
                //if the file does not exist, as determined by the boolean above
                //write a new file. if it does exist, overwrite the passed in file.
                if (!fileExists) {
                    ConsumablesManager.writeFile("newFile.json");
                } else {
                    ConsumablesManager.writeFile(fileName);
                }
            } catch (IOException file) {
                System.out.println("I don't know I got here.");
            }
            System.exit(0);
        }
    }
}


