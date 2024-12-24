package eportfolio;

abstract class Investment{

    /**
     * abstract values
     */
    protected String symbol;
    protected String name;
    protected double price;
    protected int quantity;
    protected double bookValue;


    /**
     *   instantiation method 
     * 
     * @param symbol The symbol of the stock
     * @param name The name of the stock
     * @param price The price of the stock
     * @param quantity The quantity of the stock
     *
     */
    public Investment(String symbol, String name, double price, int quantity) {
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
     */
    public void setPrice(double newPrice) {
        this.price = newPrice;
    }
 /**
     * override equal method
     * @return true or false  if investment objects are equal
     * @param investment object
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