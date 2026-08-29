import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RestaurantMenuSystem {
    private JFrame frame;
    private JPanel orderSummaryPanel;
    private Map<String, Integer> selectedItems;
    private int totalAmount = 0;
    private String paymentMethod = "Cash"; // Default payment method
    private JPanel mainPanel;
    private JPanel paymentPanel;
    private JButton payButton;
    private String customerName;
    private String tableNumber;
    
    public RestaurantMenuSystem() {
        selectedItems = new HashMap<>();
        welcomePage(); // Show the welcome page first
    }
    private void welcomePage() {
        frame = new JFrame("Welcome to NBCC Restaurant");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel welcomePanel = new JPanel(new GridLayout(3, 2, 10, 10));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Enter Customer Name:");
        JTextField nameField = new JTextField();

        JLabel tableLabel = new JLabel("Enter Table Number:");
        JTextField tableField = new JTextField();

        JButton proceedButton = new JButton("Proceed to Menu");
        proceedButton.addActionListener(_ -> {
            customerName = nameField.getText().trim();
            tableNumber = tableField.getText().trim();

            if (customerName.isEmpty() || tableNumber.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all details.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                frame.dispose();
                initialize(); // Proceed to the menu page
            }
        });

        welcomePanel.add(nameLabel);
        welcomePanel.add(nameField);
        welcomePanel.add(tableLabel);
        welcomePanel.add(tableField);
        welcomePanel.add(new JLabel()); // Empty cell for alignment
        welcomePanel.add(proceedButton);

        frame.setContentPane(welcomePanel);
        frame.setVisible(true);
    }
    private void initialize() {
        frame = new JFrame("Restaurant Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(250, 245, 240));

        // Header Panel
        JLabel headerLabel = new JLabel("Welcome to NBCC Restaurant!", JLabel.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(51, 102, 153));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Menu Panel
        JPanel menuPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 102, 153)), 
                                                              "Menu", 0, 0, new Font("Serif", Font.BOLD, 20), 
                                                              new Color(51, 102, 153)));
        menuPanel.setBackground(new Color(240, 240, 255));

        addCategory(menuPanel, "Starters", new String[]{"Spring Rolls - ₹500", "Soup - ₹400", "Salad - ₹300"});
        addCategory(menuPanel, "Main Course", new String[]{"Pizza - ₹1000", "Pasta - ₹800", "Burger - ₹700"});
        addCategory(menuPanel, "Drinks", new String[]{"Coke - ₹200", "Juice - ₹300", "Water - ₹100"});
        addCategory(menuPanel, "Desserts", new String[]{"Ice Cream - ₹400", "Cake - ₹500", "Pie - ₹600"});

        // Order Summary Panel
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(102, 51, 0)), 
                                                              "Order Summary", 0, 0, new Font("Serif", Font.BOLD, 20), 
                                                              new Color(102, 51, 0)));
        orderPanel.setBackground(new Color(255, 250, 240));

        orderSummaryPanel = new JPanel();
        orderSummaryPanel.setLayout(new BoxLayout(orderSummaryPanel, BoxLayout.Y_AXIS));
        orderSummaryPanel.setBackground(new Color(255, 250, 240));

        JScrollPane scrollPane = new JScrollPane(orderSummaryPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        orderPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton finalizeButton = new JButton("Finalize Order");
        finalizeButton.setFont(new Font("Serif", Font.BOLD, 16));
        finalizeButton.setBackground(new Color(102, 204, 102));
        finalizeButton.setForeground(Color.WHITE);
        finalizeButton.addActionListener(_ -> finalizeOrder());
        buttonPanel.add(finalizeButton);
        orderPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Payment Panel
        paymentPanel = new JPanel(new FlowLayout());
        paymentPanel.setBackground(new Color(250, 245, 240));
        JLabel paymentLabel = new JLabel("Payment Method: ");
        paymentLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        JComboBox<String> paymentCombo = new JComboBox<>(new String[]{"Cash", "UPI", "Card", "QR Code"});
        paymentCombo.setSelectedItem(paymentMethod);
        paymentCombo.addActionListener(_ -> paymentMethod = (String) paymentCombo.getSelectedItem());
        paymentPanel.add(paymentLabel);
        paymentPanel.add(paymentCombo);

        // Adding Panels
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(orderPanel, BorderLayout.CENTER);
        mainPanel.add(paymentPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private void addCategory(JPanel menuPanel, String category, String[] items) {
        JPanel categoryPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        categoryPanel.setBorder(BorderFactory.createTitledBorder(category));
        categoryPanel.setBackground(new Color(240, 240, 255));
        for (String item : items) {
            JButton button = new JButton(item);
            button.setFont(new Font("SansSerif", Font.PLAIN, 14));
            button.setBackground(new Color(204, 229, 255));
            button.setForeground(Color.BLACK);
            button.setFocusPainted(false);
            button.addActionListener(_ -> addItem(item));
            categoryPanel.add(button);
        }
        menuPanel.add(categoryPanel);
    }

    private void addItem(String item) {
        String itemName = item.split(" - ")[0];
        int itemPrice = Integer.parseInt(item.split(" - ₹")[1]);

        selectedItems.put(itemName, selectedItems.getOrDefault(itemName, 0) + 1);
        totalAmount += itemPrice;
        updateOrderSummary();
    }

    private void updateOrderSummary() {
        orderSummaryPanel.removeAll();
        for (Map.Entry<String, Integer> entry : selectedItems.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            int itemPrice = getItemPrice(itemName);

            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            itemPanel.setBackground(new Color(255, 250, 240));
            itemPanel.add(new JLabel(itemName + " - ₹" + itemPrice + " x " + quantity));

            JButton increaseButton = new JButton("+");
            JButton decreaseButton = new JButton("-");
            JButton removeButton = new JButton("Remove");

            increaseButton.addActionListener(_ -> changeItemQuantity(itemName, quantity + 1));
            decreaseButton.addActionListener(_ -> changeItemQuantity(itemName, quantity - 1));
            removeButton.addActionListener(_ -> removeItem(itemName));

            itemPanel.add(increaseButton);
            itemPanel.add(decreaseButton);
            itemPanel.add(removeButton);

            orderSummaryPanel.add(itemPanel);
        }

        JLabel totalLabel = new JLabel("Total: ₹" + totalAmount);
        totalLabel.setFont(new Font("Serif", Font.BOLD, 16));
        orderSummaryPanel.add(totalLabel);

        orderSummaryPanel.revalidate();
        orderSummaryPanel.repaint();
    }

    private int getItemPrice(String itemName) {
        Map<String, Integer> itemPrices = new HashMap<>();
        itemPrices.put("Spring Rolls", 500);
        itemPrices.put("Soup", 400);
        itemPrices.put("Salad", 300);
        itemPrices.put("Pizza", 1000);
        itemPrices.put("Pasta", 800);
        itemPrices.put("Burger", 700);
        itemPrices.put("Coke", 200);
        itemPrices.put("Juice", 300);
        itemPrices.put("Water", 100);
        itemPrices.put("Ice Cream", 400);
        itemPrices.put("Cake", 500);
        itemPrices.put("Pie", 600);

        return itemPrices.getOrDefault(itemName, 0);
    }

    private void changeItemQuantity(String itemName, int newQuantity) {
        if (newQuantity <= 0) {
            removeItem(itemName);
        } else {
            int itemPrice = getItemPrice(itemName);
            int oldQuantity = selectedItems.get(itemName);
            selectedItems.put(itemName, newQuantity);
            totalAmount += (newQuantity - oldQuantity) * itemPrice;
            updateOrderSummary();
        }
    }

    private void removeItem(String itemName) {
        if (selectedItems.containsKey(itemName)) {
            int itemPrice = getItemPrice(itemName);
            totalAmount -= itemPrice * selectedItems.get(itemName);
            selectedItems.remove(itemName);
            updateOrderSummary();
        }
    }

    private void finalizeOrder() {
        if (selectedItems.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No items in the order.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder orderSummaryText = new StringBuilder("Order Summary:\n");
        for (Map.Entry<String, Integer> entry : selectedItems.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            int itemPrice = getItemPrice(itemName);
            orderSummaryText.append(itemName).append(" - ₹").append(itemPrice).append(" x ").append(quantity).append("\n");
        }

        orderSummaryText.append("\nTotal: ₹").append(totalAmount);

        JFrame summaryFrame = new JFrame("Bill Summary");
        summaryFrame.setSize(400, 300);
        summaryFrame.setLocationRelativeTo(frame);

        JTextArea billArea = new JTextArea(orderSummaryText.toString());
        billArea.setEditable(false);
        summaryFrame.add(new JScrollPane(billArea));
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(_ -> {
            summaryFrame.dispose();
            showPayButton(); // Show the "Pay" button after closing the bill
        });
        buttonPanel.add(closeButton);
        summaryFrame.add(buttonPanel, BorderLayout.SOUTH);
        summaryFrame.setVisible(true);
        
        JOptionPane.showMessageDialog(frame, "Payment method: " + paymentMethod + "\nThank you for your order!");
    }

    private void showPayButton() {
        // Add a "Pay" button after closing the bill summary
        if (payButton == null) {
            payButton = new JButton("Pay");
            payButton.addActionListener(_ -> processPayment());
        }
        paymentPanel.add(payButton);
        paymentPanel.revalidate();
        paymentPanel.repaint();
    }

    private void processPayment() {
        // Process payment based on selected method
        switch (paymentMethod) {
            case "UPI":
                processUPI();
                break;
            case "Card":
                processCard();
                break;
            case "QR Code":
                processQRCode();
                break;
            default:
                JOptionPane.showMessageDialog(frame, "Payment successful via Cash. Thank you!", "Payment", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                break;
        }
        requestFeedback();
    }

    private void processUPI() {
        String upiId = JOptionPane.showInputDialog(frame, "Enter your UPI ID:");
        if (upiId != null && !upiId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Payment successful via UPI. Thank you!", "Payment", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        }
    }

    private void processCard() {
        String cardDetails = JOptionPane.showInputDialog(frame, "Enter card details (Card Number, Expiry, CVV):");
        if (cardDetails != null && !cardDetails.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Payment successful via Card. Thank you!", "Payment", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Please fill in all card details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processQRCode() {
        String qrCode = "https://pay.example.com/qr/" + UUID.randomUUID().toString();
        JOptionPane.showMessageDialog(frame, "Scan this QR Code to make the payment: " + qrCode, "QR Code Payment", JOptionPane.INFORMATION_MESSAGE);
    }
    private void requestFeedback() {
        String feedback = JOptionPane.showInputDialog(frame, "Thank you for your order! Please provide your feedback:");
        if (feedback != null && !feedback.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Your feedback has been recorded. Thank you!", "Feedback", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "No feedback provided. Thank you!", "Feedback", JOptionPane.INFORMATION_MESSAGE);
        }
        System.exit(0); // Exit the application
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RestaurantMenuSystem::new);
    }
}  

