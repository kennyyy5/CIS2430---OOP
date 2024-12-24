package eportfolio;

import java.util.*;
import java.lang.*;

   /**
     * Portfolio class - defines methods for update, buy, sell, getgain, search and main class
     */
public class Portfolio {
    /**arraylist variables */
    private ArrayList<Stock> stocks = new ArrayList<>();
    private ArrayList<MutualFund> mutualFunds = new ArrayList<>();

     
    /**
     *  Find Stock in stocks 
     * 
     * @param symbol The symbol we are looking for in stock arraylist.
     * @return The stock found.
     */
    private Stock findStockInStocks(String symbol) {
        for (int i = 0; i < stocks.size(); i++) {
            if (stocks.get(i).getSymbol().equalsIgnoreCase(symbol)) {
                return stocks.get(i);
            }
        }
        return null;
    }

      
    /**
     *  Find MutualFund in mutualFunds 
     * 
     * @param symbol The symbol we are looking for in mutualfund arraylist.
     * @return The MutualFund found.
     */
    private MutualFund findMutualFundInMutualFunds(String symbol) {
        for (int i = 0; i < mutualFunds.size(); i++) {
            if (mutualFunds.get(i).getSymbol().equalsIgnoreCase(symbol)) {
                return mutualFunds.get(i);
            }
        }
        return null;
    }

    /**
     *  Check price range
     * 
     * @param lower The lowerbound for price range
     * @param price price we are checking to see fits in the price range.
     * @param upper The upperbound for price range
     * @return true or false if price fits into price range
     */
    private static boolean checkPriceRange(double lower, double price, double upper) {
        return price > lower && price < upper;
    }

  
    /**
     * find string according to criteria
     * 
     * @param value string being checked
     * @param criteria being checked for
     * @return true or false if any string fits criteria
     */
    private static boolean matchStringCriteria(String value, String criteria) {
        String delimiters = " ";
        StringTokenizer stringTokeniser = new StringTokenizer(criteria, delimiters);
        while (stringTokeniser.hasMoreTokens()) {
            if (stringTokeniser.nextToken().equals(value)) {
                return true;
            }
        }
        return false;
    }

    
    /**
     * Buy method - simulates buy investment opion
     * 
     * @param quantity of investment option
     * @param price of investment option
     * @param name of investment option
     * @param type of investment option
     * @param symbol of investment option
     */
    public void buy(int quantity, double price, String name, String type, String symbol) {
        if (type.equalsIgnoreCase("stock")) {
            Stock stock = findStockInStocks(symbol);
            if (stock == null) {
                stocks.add(new Stock(symbol, name, price, quantity));
            } else {
                stock.buyToUpdateQuantity(quantity, price);
            }
        }
        if (type.equalsIgnoreCase("mutualfund")) {
            MutualFund mutualFund = findMutualFundInMutualFunds(symbol);
            if (mutualFund == null) {
                mutualFunds.add(new MutualFund(symbol, name, price, quantity));
            } else {
                mutualFund.buyToUpdateQuantity(quantity, price);
            }
        }
    }

    /**
     * Sell method - simulates sell investment opion
     * 
     * @param quantity of investment option
     * @param price of investment option
     * @param symbol of investment option
     */
    public void sell(int quantity, double price, String symbol) {
        Stock stock = findStockInStocks(symbol);
        if (stock != null && stock.getQuantity() >= quantity) {
            stock.sell(quantity, price);
            if (stock.getQuantity() == 0) {
                stocks.remove(stock);
            }
        } else {
            MutualFund mutualFund = findMutualFundInMutualFunds(symbol);
            if (mutualFund != null && mutualFund.getQuantity() >= quantity) {
                mutualFund.sell(quantity, price);
                if (mutualFund.getQuantity() == 0) {
                    mutualFunds.remove(mutualFund);
                }
            }
        }
    }

   
    /**
     * updates prices according to user input
     * 
     */
    public void updatePrices() {
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < stocks.size(); i++) {
            System.out.println("Enter a new price for stock " + stocks.get(i).getSymbol() + " :");
            double newPrice = scanner.nextDouble();
            stocks.get(i).setPrice(newPrice);
        }
        for (int i = 0; i < mutualFunds.size(); i++) {
            System.out.println("Enter a new price for mutualFund " + mutualFunds.get(i).getSymbol() + " :");
            double newPrice = scanner.nextDouble();
            mutualFunds.get(i).setPrice(newPrice);
        }
    }

    
    /**
     * Calculates total gain
     * @return total gain
     */
    public double getGain() {
        double totalGain = 0.0;

        for (int i = 0; i < stocks.size(); i++) {
            totalGain += stocks.get(i).getGain();
        }
        for (int i = 0; i < mutualFunds.size(); i++) {
            totalGain += mutualFunds.get(i).getGain();
        }
        return Math.round(totalGain);
    }

    /**
     * Search method according to string cateria and price range
     * 
     * @param symbol being checked
     * @param name being checked
     * @param lower being checked
     * @param upper being checked
     */
    public void searchStringCriteria(String symbol, String name, double lower, double upper) {
        boolean found= false;
        for (int i = 0; i < stocks.size(); i++) {
            if (matchStringCriteria(symbol, stocks.get(i).getSymbol()) && 
                matchStringCriteria(name, stocks.get(i).getName()) && 
                checkPriceRange(lower, stocks.get(i).getPrice(), upper)) {
                    found=true;
                System.out.println(stocks.get(i));
            }
        }
        for (int i = 0; i < mutualFunds.size(); i++) {
            if (matchStringCriteria(symbol, mutualFunds.get(i).getSymbol()) && 
                matchStringCriteria(name, mutualFunds.get(i).getName()) && 
                checkPriceRange(lower, mutualFunds.get(i).getPrice(), upper)) {
                    found=true;
                System.out.println(mutualFunds.get(i));
            }
        }
        if (found == false){
            for (int i = 0; i < mutualFunds.size(); i++) {
                System.out.println(mutualFunds.get(i));          
           }
            for (int i = 0; i < stocks.size(); i++) {
                System.out.println(stocks.get(i));          
           }
        }
    }

    
    /**
     * main method - uses cases for update, buy, sell, getgain, search, quit methods
     */
    public static void main(String[] args) {
        Portfolio eportfolio = new Portfolio();
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
                    eportfolio.buy(quantity, price, name, type, symbol);
                    break;
                case "sell":
                    System.out.println("Enter symbol, quantity, price:");
                    symbol = scanner.next();
                    quantity = scanner.nextInt();
                    price = scanner.nextDouble();
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
                    eportfolio.searchStringCriteria(symbol, name, lower, upper);
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        } while (!option.equalsIgnoreCase("quit") && !option.equalsIgnoreCase("q"));
    }
}
