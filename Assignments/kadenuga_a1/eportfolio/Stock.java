package eportfolio;

/**
 * 
 *  Stock class - defines iinstantiation and other methods 
 */
public class Stock {
    private String symbol;
    private String name;
    private double price;
    private int quantity;
    private double bookValue;

    
    /**
     *   instantiation method 
     * 
     * @param symbol The symbol of the stock
     * @param name The name of the stock
     * @param price The price of the stock
     * @param quantity The quantity of the stock
     *
     */
    public Stock(String symbol, String name, double price, int quantity) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.bookValue = (price * quantity) + 9.99;
    }

   
    /**
     *   getter method for symbol 
     * @return symbol of object
     */
    public String getSymbol() {
        return this.symbol;
    }
    /**
     *   getter method for name 
     * @return name of object
     */
    public String getName() {
        return this.name;
    }

    /**
     *   getter method for price
     * @return price of object
     */
    public double getPrice() {
        return this.price;
    }

    /**
     *   getter method for quantity 
     * @return quantity of object
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     *   getter method for bookvalue 
     * @return bookvalue of object
     */
    public double getBookValue() {
        return this.bookValue;
    }

    
    /**
     *   setter method for price
     * @param newPrice price to be set to
     */
    public void setPrice(double newPrice) {
        this.price = newPrice;
    }

    
    /**
     *   buy method for stock to update quantity
     * @param newQuantity quantity you want to buy
     * @param newPrice price you want to buy
     */
    public void buyToUpdateQuantity(int newQuantity, double newPrice) {
        this.bookValue += (newQuantity * newPrice) + 9.99;
        this.quantity += newQuantity;
    }

    /**
     *   sell method for stock to update quantity and price
     * @param sellQuantity quantity you want to sell
     * @param sellPrice price you want to sell
     * @return gain gotten by transfer
     */
    public double sell(int sellQuantity, double sellPrice) {
        double payment = (sellQuantity * sellPrice) - 9.99;
        double bookValueLoss = bookValue * (sellQuantity / (double) this.quantity);
        bookValue -= bookValueLoss;
        quantity -= sellQuantity;
        price -= sellPrice;
        return payment - bookValueLoss; 
    }

    
    /**
     *   getter method for gain
     * @return gain of object
     */
    public double getGain() {
        return (this.quantity * this.price) - this.bookValue;
    }

    /**
     * override toString method
     * @return  string explaining the stock object
      */
    public String toString() {
        return "Stock: " + this.name + ", " + this.symbol + "\n Quantity: " + this.quantity + "\n Price: " + this.price +"\n";
    }

    /**
     * override equal method
     * @return true or false  if stock objects are equal
     * @param stock object
      */
    public boolean equal(Stock stock) {
        return this.symbol.equals(stock.symbol) && this.name.equals(stock.name);
    }

}