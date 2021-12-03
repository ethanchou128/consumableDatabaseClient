package ca.cmpt213.a4.client;

import ca.cmpt213.a4.client.view.ListItemsWindow;

import javax.swing.*;

public class ConsumablesTracker {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ListItemsWindow();
            }
        });
    }
}
