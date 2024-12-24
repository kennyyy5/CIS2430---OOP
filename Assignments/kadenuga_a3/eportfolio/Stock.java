package eportfolio;

/**
 * 
 *  Stock class - defines iinstantiation and other methods 
 */
class Stock extends Investment {


    
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
        super(symbol, name, price, quantity);
        this.bookValue = (price * quantity) + 9.99;
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
        price = sellPrice;
        return payment - bookValueLoss; 
    }

     /**
     * override toString method
     * @return  string explaining the stock object
      */
    public String toString() {
        return "Stock: " + this.name + ", " + this.symbol + "\n Quantity: " + this.quantity + "\n Price: " + this.price + "\n";
    }

}