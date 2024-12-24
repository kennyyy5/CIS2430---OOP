package eportfolio;

import java.io.*;
import java.util.*;

/**
 * Portfolio class - defines methods for update, buy, sell, getgain, search, and main class.
 */
public class Portfolio {
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
    }

    /**
     * Sell an investment.
     * @param quantity quantity of the item being bought
     * @param price price of the item being bought
     * @param symbol symbol of the item being bought
     */
    public void sell(int quantity, double price, String symbol) {
        Investment investment = findStockInStocks(symbol);
        if (investment == null) {
            investment = findMutualFundInMutualFunds(symbol);
        }
        if (investment != null && investment.getQuantity() >= quantity) {
            investment.sell(quantity, price);
            if (investment.getQuantity() == 0) {
                investments.remove(investment);
            }
        }
    }

    /**
     * Update prices.
     */
    public void updatePrices() {
        Scanner scanner = new Scanner(System.in);
        for (Investment investment : investments) {
            System.out.println("Enter a new price for " + investment.getSymbol() + ":");
            double newPrice = scanner.nextDouble();
            if(newPrice <= 0){
                System.out.println("Problem with input. Pls try again");
                break;
            }
            investment.setPrice(newPrice);
        }
    }

    /**
     * Get total gain.
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
                System.out.println(investment);
            }
        }

        if (!found) {
            System.out.println("No matching investments found.");
        }
}

    /**
     * save investments to file.
     * @param filename name of the file being saved to
     */

    public void saveToFile(String filename) {
        File file = new File(filename);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Investment investment : investments) {
                if (investment instanceof Stock) {
                    writer.println("STOCK;" + investment.getSymbol() + ";" + investment.getName() + ";" + investment.getPrice() + ";" + investment.getQuantity());
                } else if (investment instanceof MutualFund) {
                    writer.println("MUTUALFUND;" + investment.getSymbol() + ";" + investment.getName() + ";" + investment.getPrice() + ";" + investment.getQuantity());
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }


     /**
     * load investments from file.
     * @param filename name of the file being loaded from
     */
    public void loadFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";", 5);
                String type = parts[0];
                if (type.equals("STOCK")) {
                    investments.add(new Stock(parts[1], parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[4])));
                } else if (type.equals("MUTUALFUND")) {
                    investments.add(new MutualFund(parts[1], parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[4])));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    /**
     * Main method with options for update, buy, sell, getgain, search, and quit.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Provide a filename.");
            return;
        }
        String filename = args[0];
        Portfolio eportfolio = new Portfolio();
        eportfolio.loadFromFile(filename);
        Scanner scanner = new Scanner(System.in);
        String option;

        do {
            System.out.println("Enter one (buy, sell, update, getGain, search, quit): ");
            option = scanner.next();

            switch (option.toLowerCase()) {
                case "buy":
                    System.out.println("Enter type (stock/mutualfund), symbol, name, quantity, price:");
                    String type = scanner.next();
                    String symbol = scanner.next();
                    String name = scanner.next();
                    int quantity = scanner.nextInt();
                    double price = scanner.nextDouble();
                    if(price <= 0 || quantity <= 0){
                        System.out.println("Problem with input. Pls try again");
                        break;
                    }
                    eportfolio.buy(quantity, price, name, type, symbol);
                    break;
                case "sell":
                    System.out.println("Enter symbol, quantity, price:");
                    symbol = scanner.next();
                    quantity = scanner.nextInt();
                    price = scanner.nextDouble();
                    if(price <= 0 || quantity <= 0){
                        System.out.println("Problem with input. Pls try again");
                        break;
                    }
                    eportfolio.sell(quantity, price, symbol);
                    break;
                case "update":
                    eportfolio.updatePrices();
                    break;
                case "getgain":
                    System.out.println("Total gain: " + eportfolio.getGain());
                    break;
                case "search":
                    System.out.println("Enter symbol, name, lower price, upper price:");
                    symbol = scanner.next();
                    name = scanner.next();
                    double lower = scanner.nextDouble();
                    double upper = scanner.nextDouble();
                    if(lower <= 0 || upper <= 0){
                        System.out.println("Problem with input. Pls try again");
                        break;
                    }
                    eportfolio.searchStringCriteria(symbol, name, lower, upper);
                    break;
                case "quit":
                   eportfolio.saveToFile(filename);
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        } while (!option.equalsIgnoreCase("quit") && !option.equalsIgnoreCase("q"));
        
        eportfolio.saveToFile(filename);
    }
}
