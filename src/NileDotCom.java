/*
Name: Zachary Ayres
Course CNT4714 - Fall 2026
Assignment Title: Project 1 - Event Driven Enterprise Simulation
Date: Sunday, Sept 13, 2026
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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

    //Variables for items being worked on
    private int itemNumber = 1;
    private String currentItemId;
    private String currentItemDescription;
    private int currentItemQuantity;
    private double currentItemPrice;
    private double currentItemDiscount;
    private double currentItemTotal;

    //Shopping cart data
    private ArrayList<CartItem> cart = new ArrayList<>();
    private double orderSubtotal = 0.0;


    //Class used to store each item in the shopping cart
    private static class CartItem {

        String id;
        String description;
        int quantity;
        double price;
        double discount;
        double total;

        //CartItem constructor
        CartItem(String id, String description, int quantity,
                 double price, double discount, double total) {

            this.id = id;
            this.description = description;
            this.quantity = quantity;
            this.price = price;
            this.discount = discount;
            this.total = total;
        }
    }


    //NileDotCom constructor
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

    //Discounts based on quantity
    private double getDiscount(int quantity) {
        if(quantity >=15) {
            return 0.20;
        }
        else if(quantity >=10) {
            return 0.15;
        }
        else if(quantity >=5) {
            return 0.10;
        }
        else {
            return 0.00;
        }
    }

    //parse through inventory.csv for item searched
    private void searchForItem() {
        String enteredId = itemIdField.getText().trim();
        String quantityText = quantityField.getText().trim();
        int requestedQuantity;

        try {
            requestedQuantity = Integer.parseInt(quantityText);
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please Enter A Valid Quantity.", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //check for negative
        if( requestedQuantity <=0) {
            JOptionPane.showMessageDialog(this, "Please Enter A Valid Quantity.", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
            quantityField.setText("");
            return;

        }

        boolean itemFound = false;

        //read text
        try (BufferedReader reader = new BufferedReader(new FileReader("inventory.csv")))
        {
            String line;
            while ((line = reader.readLine()) !=null) {
                String[] parts = line.split(",");
                String id = parts[0].trim();

                if(id.equals(enteredId)) {
                    itemFound = true;
                    String description = parts[1].trim().replace("\"","");
                    boolean inStock = Boolean.parseBoolean(parts[2].trim());
                    int quantityOnHand = Integer.parseInt(parts[3].trim());
                    double price = Double.parseDouble(parts[4].trim());

                    //Out of Stock
                    if(!inStock) {
                        JOptionPane.showMessageDialog(this,"Sorry, That item is out of stock", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
                        itemIdField.setText("");
                        quantityField.setText("");
                        return;

                    }

                    //Low Inventory
                    if (requestedQuantity > quantityOnHand) {
                        JOptionPane.showMessageDialog(this, "Insufficient Stock. Only " + quantityOnHand + "available.", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
                        quantityField.setText("");
                        return;
                    }

                    //Item good
                    currentItemId = id;
                    currentItemDescription = description;
                    currentItemQuantity = requestedQuantity;
                    currentItemPrice = price;
                    currentItemDiscount = getDiscount(requestedQuantity);
                    currentItemTotal = (price * requestedQuantity) * (1-currentItemDiscount);

                    detailsField.setText(currentItemId + " " +currentItemDescription + " $" +String.format("%.2f", currentItemPrice) + " " +currentItemQuantity +" " + String.format("%.0f%%", currentItemDiscount * 100) + " $" + String.format("%.2f", currentItemTotal));

                    searchButton.setEnabled(false);
                    addButton.setEnabled(true);

                    return;
                }

            }

            // Item not Found
            if(!itemFound) {
                JOptionPane.showMessageDialog(this, "Item ID "+ enteredId + " not found", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);

                itemIdField.setText("");
                quantityField.setText("");
            }

        }

        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading inventory.csv", "File Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void addItemToCart() {

        //create cart items from items searched for
        CartItem item = new CartItem(
                currentItemId,
                currentItemDescription,
                currentItemQuantity,
                currentItemPrice,
                currentItemDiscount,
                currentItemTotal
        );

        //Add to shopping cart
        cart.add(item);

        //update subtotal
        orderSubtotal += currentItemTotal;

        //update shopping cart displayed
        updateCartDisplay();

        //Move on to the next item
        itemNumber++;

        //Clear input field for the next item
        itemIdField.setText("");
        quantityField.setText("");

        //Update labels
        itemIdLabel.setText("Enter item ID for item #" + itemNumber);
        quantityLabel.setText("Enter quantity for item #" + itemNumber);
        detailsLabel.setText("Details for item #" + itemNumber);
        subtotalLabel.setText("Current Subtotal for" + cart.size() + " item(s)");

        //Update subtotal field
        subtotalField.setText("$" + String.format("%.2f", orderSubtotal));

        //Update button Names
        searchButton.setText("Search For Item # " + itemNumber);
        addButton.setText("Add item # " + itemNumber + "to cart");

        //Button states
        addButton.setEnabled(false);
        deleteButton.setEnabled(true);
        checkoutButton.setEnabled(true);

        //Cart max 5 different items
        if(cart.size() >=5) {
            searchButton.setEnabled(false);
        }
        else {
            searchButton.setEnabled(true);
        }

        }
    //redraw shopping cart
    private void updateCartDisplay() {
        StringBuilder cartText = new StringBuilder();

        for (int i = 0; i < cart.size(); i++) {
            CartItem item = cart.get(i);

            cartText.append("Item ")
                    .append(i + 1)
                    .append("- SKU: ")
                    .append(item.id)
                    .append(", Desc: \"")
                    .append(item.description)
                    .append("\", Price Ea. $")
                    .append(String.format("%.2f", item.price))
                    .append(", Qty: ")
                    .append(item.quantity)
                    .append(", Total: $")
                    .append(String.format("%.2f", item.total))
                    .append("\n");
        }
        cartArea.setText(cartText.toString());

        if(cart.isEmpty()) {
            cartTitleLabel.setText("Your Shopping Cart Is Currently Empty");
        }

        else {
            cartTitleLabel.setText("Your Shopping Cart Currently Contains " + cart.size() + " Item(s)");
        }

    }
    private void deleteLastItem() {

        //Make sure there is something to delete
        if (cart.isEmpty()) {
            return;
        }

        //Remove the last item from the cart
        CartItem removedItem = cart.remove(cart.size() - 1);

        //Subtract item's total from order subtotal
        orderSubtotal -= removedItem.total;

        //rounding for errors
        if (orderSubtotal < 0.01) {
            orderSubtotal = 0.0;
        }

        //The next item number is cart size + 1
        itemNumber = cart.size() + 1;

        //Clear the input fields
        itemIdField.setText("");
        quantityField.setText("");
        detailsField.setText("");

        //Update labels
        itemIdLabel.setText("Enter item ID for item #" + itemNumber);
        quantityLabel.setText("Enter quantity for item #" + itemNumber);
        detailsLabel.setText("Details for item #" + itemNumber);
        subtotalLabel.setText("Subtotal for " + cart.size() + " item(s)");

        //Update subtotal display
        subtotalField.setText("$" + String.format("%.2f", orderSubtotal));

        //Update button text
        searchButton.setText("Search For Item #" + itemNumber);
        addButton.setText("Add Item #" + itemNumber + " To Cart");

        //Refresh shopping cart display
        updateCartDisplay();

        //Search ready for the next item
        searchButton.setEnabled(true);
        addButton.setEnabled(false);

        //If cart is empty disable Delete and Checkout
        if (cart.isEmpty()) {
            deleteButton.setEnabled(false);
            checkoutButton.setEnabled(false);
        }
        else {
            deleteButton.setEnabled(true);
            checkoutButton.setEnabled(true);
        }
    }


    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == exitButton) {
            System.exit(0);
        }
        else if(event.getSource() == searchButton) {
            searchForItem();
        }
        else if(event.getSource() == addButton) {
            addItemToCart();
        }
        else if(event.getSource() == deleteButton) {
            deleteLastItem();
        }
        else if(event.getSource() == checkoutButton) {
            System.out.println("Checkout button clicked");
        }
        else if(event.getSource() == emptyButton) {
            System.out.println("Empty cart button pressed");
        }
    }

}






































