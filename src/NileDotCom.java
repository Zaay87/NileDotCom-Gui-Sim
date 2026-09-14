/*
Name: Zachary Ayres
Course CNT4714 - Fall 2026
Assignment Title: Project 1 - Event Driven Enterprise Simulation
Date: Sunday, Sept 13, 2026
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NileDotCom extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new NileDotCom();
    }

    //Input Labels
    private JLabel itemIdLabel;
    private JLabel quantityLabel;
    private JLabel detailsLabel;
    private JLabel subtotalLabel;

    //Input and Output fields
    private JTextField itemIdField;
    private JTextField quantityField;
    private JTextField detailsField;
    private JTextField subtotalField;

    //Shopping Cart
    private JLabel cartTitleLabel;
    private JTextArea cartArea;

    //Gui Buttons
    private JButton searchButton;
    private JButton addButton;
    private JButton deleteButton;
    private JButton checkoutButton;
    private JButton emptyButton;
    private JButton exitButton;

    //Tracking for which item number is being worked on
    private int itemNumber = 1;

    public NileDotCom() {

        super("Nile.com - Fall 2026");

        createComponents();
        createLayout();
        registerListeners();
        setInitialState();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //Layout
    private void createLayout() {
        setLayout(new BorderLayout(10, 10));

        //Top Inputs
        JPanel inputPanel = new JPanel(new GridLayout(4,2,5,5));

        inputPanel.add(itemIdLabel);
        inputPanel.add(itemIdField);

        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);

        inputPanel.add(detailsLabel);
        inputPanel.add(detailsField);

        inputPanel.add(subtotalLabel);
        inputPanel.add(subtotalField);

        add(inputPanel, BorderLayout.NORTH);

        //Shopping Cart
        JPanel cartPanel = new JPanel(new BorderLayout());

        cartPanel.add(cartTitleLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(cartArea);
        cartPanel.add(scrollPane,BorderLayout.CENTER);

        add(cartPanel, BorderLayout.CENTER);

        //Button panel
        JPanel buttonPanel = new JPanel(new  GridLayout(3,2,5,5));

        //Add butons
        buttonPanel.add(searchButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(emptyButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    //Components
    private void createComponents() {

        //Lables
        itemIdLabel = new JLabel("Enter item ID for item #" + itemNumber + ":");
        quantityLabel = new JLabel("Enter quantity for item #" + itemNumber + ":");
        detailsLabel = new JLabel("Details for item#" + itemNumber + ":");
        subtotalLabel = new JLabel("Subtotal for items:");
        cartTitleLabel = new JLabel("Your Cart is Empty", SwingConstants.CENTER);

        //Fields
        itemIdField = new JTextField(20);
        quantityField = new JTextField(20);
        detailsField = new JTextField(35);
        detailsField.setEditable(false);
        subtotalField = new JTextField(20);
        subtotalField.setEditable(false);
        cartArea = new JTextArea(8,60);
        cartArea.setEditable(false);

        //Buttons
        searchButton = new JButton("Search For Item #" + itemNumber);
        addButton = new JButton("Add Item #" + itemNumber + " To Cart");
        deleteButton = new JButton("Delete Last Item From Cart");
        checkoutButton = new JButton("Check Out");
        emptyButton = new JButton("Empty Cart - Start A New Order");
        exitButton = new JButton("Exit (Close App)");

    }

    //Listeners
    private void registerListeners() {

        searchButton.addActionListener(this);
        addButton.addActionListener(this);
        deleteButton.addActionListener(this);
        checkoutButton.addActionListener(this);
        emptyButton.addActionListener(this);
        exitButton.addActionListener(this);

    }

    private void setInitialState() {

        searchButton.setEnabled(true);
        addButton.setEnabled(false);
        deleteButton.setEnabled(false);
        checkoutButton.setEnabled(false);
        emptyButton.setEnabled(true);
        exitButton.setEnabled(true);

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == exitButton) {
            System.exit(0);
        }
        else if(event.getSource() == searchButton) {
            System.out.println("Search button clicked");
        }
        else if(event.getSource() == addButton) {
            System.out.println("Add Button clicked");
        }
        else if(event.getSource() == deleteButton) {
            System.out.println("Delete Button clicked");
        }
        else if(event.getSource() == checkoutButton) {
            System.out.println("Checkout button clicked");
        }
        else if(event.getSource() == emptyButton) {
            System.out.println("Empty cart button pressed");
        }
    }
}






































