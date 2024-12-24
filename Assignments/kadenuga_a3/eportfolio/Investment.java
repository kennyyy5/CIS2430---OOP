package eportfolio;

 /**
 * abstract class Investment for Stock and MutualFund
 */
public abstract class Investment{

    /**
     * abstract values
     */
    protected String symbol;
    protected String name;
    protected double price;
    protected int quantity;
    protected double bookValue;


    /**
     * Instantiation method with exception handling
     *
     * @param symbol  The symbol of the stock
     * @param name    The name of the stock
     * @param price   The price of the stock
     * @param quantity The quantity of the stock
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Investment(String symbol, String name, double price, int quantity) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be null or empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
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
     *   getter method for gain
     * @return gain of object
     */
    public double getGain() {
        return (this.quantity * this.price) - this.bookValue;
    }

    
    /**
     *   setter method for price
     * @param newPrice price to be set to
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public void setPrice(double newPrice) {
        if (newPrice <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        this.price = newPrice;
    }

    /**
     *   setter method for Symbol 
     * @param newSymbol price to be set to
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public void setSymbol(String newSymbol) {
        if (newSymbol == null || newSymbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be null or empty.");
        }
        this.symbol = newSymbol;
    }
    /**
     *   setter method for name 
     * @param newName price to be set to
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public void setName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = newName;
    }
   /**
     * override equal method
     * @return true or false  if investment objects are equal
     * @param otherObject investment object
      */
    public boolean equals(Object otherObject) {
       if (otherObject == null){
       return false;}
        else if (this.getClass( ) != otherObject.getClass()){
        return false;}
        else
        {
        Investment otherInv = (Investment)otherObject;
        return this.symbol.equals(otherInv.symbol) && this.name.equals(otherInv.name);

        }   
    }
 
    /**
     * abstract methods
     */
    public abstract void buyToUpdateQuantity(int newQuantity, double newPrice);
    public abstract double sell(int sellQuantity, double sellPrice);
    public abstract String toString() ;
}