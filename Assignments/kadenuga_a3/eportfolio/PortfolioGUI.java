package eportfolio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Portfolio GUI class - defines methods for update, buy, sell, getgain, search, and main class and uses swing and awt method for GUI.
 */
public class PortfolioGUI extends JFrame{
    /**
     * variables - GUI, arrays, etc.
     */
    private JTextArea messageArea;
    private JPanel initialPanel,buyPanel, sellPanel,updatePanel, searchPanel, getGainPanel;
    private String selectedInvestment;
    private int currentIndex =0;
    private double currentGain =0.0;

    private ArrayList<Investment> investments = new ArrayList<>();
    private HashMap<String, ArrayList<Integer>> keywordIndex = new HashMap<>();

    /**
     * Find Stock in stocks.
     *  @param symbol The symbol of the stock
     */
     private Stock findStockInStocks(String symbol) {
        for (Investment investment : investments) {
            if (investment instanceof Stock && investment.getSymbol().equalsIgnoreCase(symbol)) {
                return (Stock) investment;
            }
        }
        return null;
    }

    /**
     * Find MutualFund in mutualFunds.
     *  @param symbol The symbol of the mutualfund
     */
    private MutualFund findMutualFundInMutualFunds(String symbol) {
        for (Investment investment : investments) {
            if (investment instanceof MutualFund && investment.getSymbol().equalsIgnoreCase(symbol)) {
                return (MutualFund) investment;
            }
        }
        return null;
    }

    /**
     * Check if price is within range.
     *  @param lower The lowerbound of the price range
     *  @param price The price of the item
     *  @param upper The upperbound of the price range
     */
    private static boolean checkPriceRange(double lower, double price, double upper) {
        return (lower <= 0 || price >= lower) && (upper <= 0 || price <= upper);
    }

    /**
     * Check if value matches criteria.
     * @param criteria word being checked for in the string
     * @param value string being checked
     */
    private static boolean matchStringCriteria(String criteria, String value) {
        for (String keyword : criteria.toLowerCase().split("\\s+")) {
          if (!value.toLowerCase().contains(keyword)) return false;
        }
        return true;

    }

    /**
     * Buy an investment.
     * @param quantity quantity of the item being bought
     * @param price price of the item being bought
     * @param name name of the item being bought
     * @param type type of the item being bought
     * @param symbol symbol of the item being bought
     */
    public void buy(int quantity, double price, String name, String type, String symbol) {
    try {
        if (type.equalsIgnoreCase("stock")) {
            Stock stock = findStockInStocks(symbol);
            if (stock == null) {
                investments.add(new Stock(symbol, name, price, quantity));
            } else {
                stock.buyToUpdateQuantity(quantity, price);
            }
        } else if (type.equalsIgnoreCase("mutualfund")) {
            MutualFund mutualFund = findMutualFundInMutualFunds(symbol);
            if (mutualFund == null) {
                investments.add(new MutualFund(symbol, name, price, quantity));
            } else {
                mutualFund.buyToUpdateQuantity(quantity, price);
            }
        }
        displayMessage("Successfully bought the investment.");
    } catch (IllegalArgumentException e) {
        displayMessage("Input error: " + e.getMessage());
    }    
    catch (Exception e) {
        displayMessage("Error in buying investment: " + e.getMessage());
    }
}

    /**
     * Sell an investment.
     * @param quantity quantity of the item being bought
     * @param price price of the item being bought
     * @param symbol symbol of the item being bought
     */
    public void sell(int quantity, double price, String symbol) {
    try {
        if (quantity <= 0 || price <= 0) {
            throw new IllegalArgumentException("Quantity and price must be greater than zero.");
        }
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be null or empty.");
        }
        
        Investment investment = findStockInStocks(symbol);
        if (investment == null) {
            investment = findMutualFundInMutualFunds(symbol);
        }
        if (investment != null && investment.getQuantity() >= quantity) {
            investment.sell(quantity, price);
            if (investment.getQuantity() == 0) {
                investments.remove(investment);
            }
            displayMessage("Successfully sold the investment.");
        } else {
            throw new IllegalArgumentException("Invalid quantity or investment not found.");
        } }
    catch (IllegalArgumentException e) {
        displayMessage("Input error: " + e.getMessage());
    }    

     catch (Exception e) {
        displayMessage("Error in selling investment: " + e.getMessage());
    }
    }



    /**
     * Get total gain.
     * @return total gain of all investments
     */
    public double getGain() {
        double totalGain = 0.0;
        for (Investment investment : investments) {
            totalGain += investment.getGain();
        }
        return Math.round(totalGain * 100.0) / 100.0;

    }



    /**
     * Load keywordIndex hashmap.
     */
    public void loadKeywordIndex() {
        keywordIndex.clear(); // Clear existing data to avoid duplicates

        for (int i = 0; i < investments.size(); i++) {
            String[] keywords = investments.get(i).getName().toLowerCase().split("\\s+");

            for (String keyword : keywords) {
                keywordIndex.computeIfAbsent(keyword, k -> new ArrayList<>()).add(i);
            }
        }
    }



    /**
     * Search by criteria.
     * @param symbol symbol of the item being checked
     * @param name name of the item being checked
     * @param lower bound price of the item being checked
     * @param upper bound price of the item being checked
     */
    public void searchStringCriteria(String symbol, String name, double lower, double upper) {
        if (lower <= 0 || upper <= 0) {
            throw new IllegalArgumentException("Quantity and price must be greater than zero.");
        }

        loadKeywordIndex(); // Load or refresh index
        ArrayList<Integer> matchingIndices = new ArrayList<>();

        // If name is provided, find matching indices
        if (name != null && !name.isEmpty()) {
            String[] keywords = name.toLowerCase().split("\\s+");
            matchingIndices.addAll(keywordIndex.getOrDefault(keywords[0], new ArrayList<>()));

            for (int i = 1; i < keywords.length && !matchingIndices.isEmpty(); i++) {
                matchingIndices.retainAll(keywordIndex.getOrDefault(keywords[i], new ArrayList<>()));
            }

        } else {
            // No name specified, search the entire list
            for (int i = 0; i < investments.size(); i++) {
                matchingIndices.add(i);
            }
        }

        // Filter by symbol and price range
        boolean found = false;
        for (int index : matchingIndices) {
            Investment investment = investments.get(index);
            if (matchStringCriteria(symbol, investment.getSymbol()) && checkPriceRange(lower, investment.getPrice(), upper)) {
                found = true;
                displayMessage(investment.toString());
            }
        }

        if (!found) {
            displayMessage("No matching investments found.\n");
        }
}
     /**
     * constructor method that initialises panels for the buy, sell, search, etc. operations
     */
    public PortfolioGUI(){
        super("ePortfolio");
        setSize(500,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2,1));

        messageArea = new JTextArea();
        messageArea.setEditable(false);

        JMenuBar menuBar = new JMenuBar();
        JMenu commandsMenu = new JMenu("Commands");
        JMenuItem buy = new JMenuItem("Buy");
        JMenuItem sell = new JMenuItem("Sell");
        JMenuItem update = new JMenuItem("Update");
        JMenuItem getGain = new JMenuItem("Get Gain");
        JMenuItem search = new JMenuItem("Search");
        JMenuItem quit = new JMenuItem("Quit");

        commandsMenu.add(buy);
        commandsMenu.add(sell);
        commandsMenu.add(update);
        commandsMenu.add(getGain);
        commandsMenu.add(search);
        commandsMenu.add(quit);
        menuBar.add(commandsMenu);
        setJMenuBar(menuBar);

        /** initialPanel */
        initialPanel = new JPanel(new GridLayout(2,1));
        initialPanel.add(new JLabel("Welcome to ePortfolio."));
        initialPanel.add(new JLabel("<html>Choose a command from the 'Commands' menu to buy or sell<br>" +
    "an investment, update prices for all investments, get gain for the<br>" +
    "portfolio, search for relevant investments, or quit the program.</html>"));

       /** buyPanel */
        buyPanel = new JPanel(new GridLayout(7, 3));
        buyPanel.add(new JLabel("Buying an investment"));
        buyPanel.add(new JLabel("      "));
        buyPanel.add(new JLabel("      "));

        buyPanel.add(new JLabel("Type"));
        String[] investmentStrings = { "Stock", "Mutual Fund"};
      //  JComboBox investmentList = new JComboBox(investmentStrings);
      JComboBox<String> investmentList = new JComboBox<>(investmentStrings);
        investmentList.setSelectedIndex(0);
        selectedInvestment = (String) investmentList.getSelectedItem();
        investmentList.addActionListener(e->{
            JComboBox cb = (JComboBox)e.getSource();
            selectedInvestment = (String)cb.getSelectedItem();
        });
        JButton buyResetButton = new JButton("Reset");
        
        JButton buyButton = new JButton("Buy");
        buyPanel.add(investmentList);
        buyPanel.add(new JLabel("      "));

        JTextField buySymbolField = new JTextField();
        JTextField buyNameField = new JTextField();
        JTextField buyQuantityField = new JTextField();
        JTextField buyPriceField = new JTextField();
        
        buyPanel.add(new JLabel("Symbol"));
        buyPanel.add(buySymbolField);
        
         buyPanel.add(buyResetButton);
       

        buyPanel.add(new JLabel("Name"));
        buyPanel.add(buyNameField);
        buyPanel.add(new JLabel("      "));

        buyPanel.add(new JLabel("Quantity"));
        buyPanel.add(buyQuantityField);
        buyPanel.add(buyButton);

        buyPanel.add(new JLabel("Price"));
        buyPanel.add(buyPriceField);
        buyPanel.add(new JLabel("      "));

        buyPanel.add(new JLabel("Messages"));
        buyPanel.add(new JLabel("      "));
        buyPanel.add(new JLabel("      "));
        
        buyResetButton.addActionListener(e -> {
            buyPriceField.setText(" ");
            buyQuantityField.setText(" ");
            buyNameField.setText(" ");
            buySymbolField.setText(" ");
            investmentList.setSelectedIndex(0);
    });

       buyButton.addActionListener(e -> {
    //     if (buyNameField.getText().trim().equals("") || buySymbolField.getText().trim().equals("") || buyQuantityField.getText().trim().equals("") || buyPriceField.getText().trim().equals("")) {
    //    displayMessage("Problem with input. Please try again");
    //     return;
    // }
    int buyQuantity=0;
    double buyPrice=0.0;
    try {
     buyQuantity = Integer.parseInt(buyQuantityField.getText().trim());
     buyPrice = Double.parseDouble(buyPriceField.getText().trim());
} catch (NumberFormatException ex) {
    displayMessage("Please enter valid numbers for quantity and price.");
    return;
}
    String buyName = buyNameField.getText();
    String buySymbol = buySymbolField.getText();

    if (selectedInvestment == null) {
        displayMessage("Please select an investment type.");
        return;
    }
    
    
    // Determine the investment type based on the selected item in the combo box
    String buyType = selectedInvestment.equals("Stock") ? "stock" : "mutualfund";

    // if (buyPrice <= 0 || buyQuantity <= 0) {
    //    displayMessage("Problem with input. Please try again");
    //     return;
    // }
    
    // Call the buy method with the correct parameters
    buy(buyQuantity, buyPrice, buyName, buyType, buySymbol);
    
});


        /** sellPanel */
        sellPanel = new JPanel(new GridLayout(5, 3));
        sellPanel.add(new JLabel("Selling an investment"));
        sellPanel.add(new JLabel("      "));
        sellPanel.add(new JLabel("      "));
  
        JButton sellResetButton = new JButton("Reset");
        JButton sellButton = new JButton("Sell");
        

        JTextField sellSymbolField = new JTextField();
        JTextField sellQuantityField = new JTextField();
        JTextField sellPriceField = new JTextField();
        
        sellPanel.add(new JLabel("Symbol"));
        sellPanel.add(sellSymbolField);
        sellPanel.add(sellResetButton);

        sellPanel.add(new JLabel("Quantity"));
        sellPanel.add(sellQuantityField);
        sellPanel.add(new JLabel("      "));

        sellPanel.add(new JLabel("Price"));
        sellPanel.add(sellPriceField);   
        sellPanel.add(sellButton);

        sellPanel.add(new JLabel("Messages"));
        sellPanel.add(new JLabel("      "));
        sellPanel.add(new JLabel("      "));

         sellResetButton.addActionListener(e -> {
            sellPriceField.setText(" ");
            sellQuantityField.setText(" ");
            sellSymbolField.setText(" ");
    });

       sellButton.addActionListener(e -> {
    //     if (sellSymbolField.getText().trim().equals("") || sellQuantityField.getText().trim().equals("") || sellPriceField.getText().trim().equals("")) {
    //    displayMessage("Problem with input. Please try again");
    //     return;
    // }
    int sellQuantity=0;
    double sellPrice=0.0;
    try {
     sellQuantity = Integer.parseInt(sellQuantityField.getText().trim());
     sellPrice = Double.parseDouble(sellPriceField.getText().trim());
    // Proceed with logic if parsing is successful
} catch (NumberFormatException ex) {
    displayMessage("Please enter valid numbers for quantity and price.");
    return;
}
   
    String sellSymbol = sellSymbolField.getText();
    
    
    // Determine the investment type based on the selected item in the combo box

    // if (sellPrice <= 0 || sellQuantity <= 0) {
    //    displayMessage("Problem with input. Please try again");
    //     return;
    // }
    
    // Call the buy method with the correct parameters
    sell(sellQuantity, sellPrice, sellSymbol); //edit this out
});

        /** updatePanel */
        updatePanel = new JPanel(new GridLayout(5, 3));
        updatePanel.add(new JLabel("Updating investments"));
        updatePanel.add(new JLabel("      "));
        updatePanel.add(new JLabel("      "));
  
        JButton prevButton = new JButton("Prev");
        JButton nextButton = new JButton("Next");
        JButton saveButton = new JButton("Save");
        

        JTextField updateSymbolField = new JTextField();
        JTextField updateNameField = new JTextField();
        JTextField updatePriceField = new JTextField();
        
        updatePanel.add(new JLabel("Symbol"));
        updatePanel.add(updateSymbolField);
        updatePanel.add(prevButton);

        updatePanel.add(new JLabel("Name"));
        updatePanel.add(updateNameField);
        updatePanel.add(nextButton);

        updatePanel.add(new JLabel("Price"));
        updatePanel.add(updatePriceField);   
        updatePanel.add(saveButton);

        updatePanel.add(new JLabel("Messages"));
        updatePanel.add(new JLabel("      "));
        updatePanel.add(new JLabel("      "));

        prevButton.addActionListener(e -> {
            if (!investments.isEmpty()) {
                currentIndex = (currentIndex - 1 + investments.size()) % investments.size();
                
         Investment investment = investments.get(currentIndex);
            updateSymbolField.setText(investment.getSymbol());
            updateNameField.setText(investment.getName());
            updatePriceField.setText(String.valueOf(investment.getPrice()));
            }
        });

nextButton.addActionListener(e -> {
    if (!investments.isEmpty()) {
        currentIndex = (currentIndex + 1) % investments.size();
        
      Investment investment = investments.get(currentIndex);
    updateSymbolField.setText(investment.getSymbol());
    updateNameField.setText(investment.getName());
    updatePriceField.setText(String.valueOf(investment.getPrice()));   
    }
});

saveButton.addActionListener(e -> {
    if (investments.isEmpty()) {
        displayMessage("No investments to update.");
        return;
    }
    try {
        String newSymbol = updateSymbolField.getText().trim();
        String newName = updateNameField.getText().trim();
        double newPrice = Double.parseDouble(updatePriceField.getText().trim());

        investments.get(currentIndex).setPrice(newPrice);
        investments.get(currentIndex).setSymbol(newSymbol);
        investments.get(currentIndex).setName(newName);

        displayMessage("Investment updated successfully.");
    } catch (NumberFormatException ex) {
        displayMessage("Invalid number for price: " + ex.getMessage());
    } catch (IllegalArgumentException ex) {
        displayMessage(ex.getMessage());
    } catch (Exception ex) {
        displayMessage("Unexpected error while updating investment: " + ex.getMessage());
    }
});


        /** getGainPanel */
        getGainPanel = new JPanel(new GridLayout(3, 23));
        getGainPanel.add(new JLabel("Getting total gain"));
        getGainPanel.add(new JLabel("      "));
        getGainPanel.add(new JLabel("      "));
      
        JTextField getGainField = new JTextField();
        
        
        getGainPanel.add(new JLabel("Total gain"));
        getGainPanel.add(getGainField);
        getGainPanel.add(new JLabel("      "));

        getGainPanel.add(new JLabel("Individual gains"));
        getGainPanel.add(new JLabel("      "));
        getGainPanel.add(new JLabel("      "));

        /** searchPanel */
        searchPanel = new JPanel(new GridLayout(6, 3));
        searchPanel.add(new JLabel("Searching investments"));
        searchPanel.add(new JLabel("      "));
        searchPanel.add(new JLabel("      "));


        JTextField searchSymbolField = new JTextField();
        JTextField searchNameKeywordsField = new JTextField();
        JTextField searchLowPriceField = new JTextField();
        JTextField searchHighPriceField = new JTextField();

        JButton searchResetButton = new JButton("Reset");
        JButton searchButton = new JButton("Search");
        
        searchPanel.add(new JLabel("Symbol"));
        searchPanel.add(searchSymbolField);
        searchPanel.add(searchResetButton);
       

        searchPanel.add(new JLabel("Name Keywords"));
        searchPanel.add(searchNameKeywordsField);
        searchPanel.add(new JLabel("      "));

        searchPanel.add(new JLabel("Low price"));
        searchPanel.add(searchLowPriceField);
        searchPanel.add(new JLabel("      "));

        searchPanel.add(new JLabel("High price"));
        searchPanel.add(searchHighPriceField);
        searchPanel.add(searchButton);

        searchPanel.add(new JLabel("Search Results"));
        searchPanel.add(new JLabel("      "));
        searchPanel.add(new JLabel("      "));

        searchResetButton.addActionListener(e -> {
            searchLowPriceField.setText(" ");
            searchHighPriceField.setText(" ");
            searchNameKeywordsField.setText(" ");
            searchSymbolField.setText(" ");
    });

       searchButton.addActionListener(e -> {
    //     if (searchSymbolField.getText().trim().equals("") || searchNameKeywordsField.getText().trim().equals("") || searchLowPriceField.getText().trim().equals("") || searchHighPriceField.getText().trim().equals("")) {
    //    displayMessage("Problem with input. Please try again");
    //     return;
    // }
    double searchLowPrice=0.0;
    double searchHighPrice=0.0;
    try {
     searchLowPrice = Double.parseDouble(searchLowPriceField.getText().trim());
     searchHighPrice = Double.parseDouble(searchHighPriceField.getText().trim());
    // Proceed with logic if parsing is successful
} catch (NumberFormatException ex) {
    displayMessage("Please enter valid numbers for price.");
    return;
}
   
    String searchSymbol = searchSymbolField.getText();
    String searchNameKeywords = searchNameKeywordsField.getText();
    
    
    // Determine the investment type based on the selected item in the combo box

    // if (searchLowPrice <= 0 || searchHighPrice <= 0) {
    //    displayMessage("Problem with input. Please try again");
    //     return;
    // }
    
    // Call the buy method with the correct parameters
    try{
    searchStringCriteria(searchSymbol,searchNameKeywords, searchLowPrice,searchHighPrice); }
    catch (IllegalArgumentException er) {
        displayMessage("Input error: " + er.getMessage());
    }   
    
});
        
      

        // Set default panel
        add(initialPanel);
        // Menu actions
        buy.addActionListener(e -> switchPanel(buyPanel));
        sell.addActionListener(e -> switchPanel(sellPanel));
        update.addActionListener(e -> 
        {
            switchPanel(updatePanel);
            if (!investments.isEmpty()) {
            // Access investments only if it's not empty
            Investment investment = investments.get(currentIndex);
            updateSymbolField.setText(investment.getSymbol());
            updateNameField.setText(investment.getName());
            updatePriceField.setText(String.valueOf(investment.getPrice()));
            // Perform actions with investment
        } else {
            displayMessage("No investments available.");
        }});
        getGain.addActionListener(e -> {
            currentGain=getGain();
            getGainField.setText(String.valueOf(currentGain));switchPanel(getGainPanel);
        });
        search.addActionListener(e -> switchPanel(searchPanel));
        quit.addActionListener(e->System.exit(0));

    }

    /**
     * switch panels for the menu.
     * @param panel GUI panel that needs to be switched
     */
    private void switchPanel(JPanel panel) {
        getContentPane().removeAll();
        add(panel);
        
        add(new JScrollPane(messageArea));
        revalidate();
        repaint();
    }

    /**
     * display message in the message box
     * @param message string to be put into message box
     */
    private void displayMessage(String message) {
        messageArea.append(message + "\n");
    }

    /**
     * Main method that initialises GUI
     */
    public static void main(String[] args) {
            PortfolioGUI gui =  new  PortfolioGUI();
           gui.setVisible(true);  
    }



}
