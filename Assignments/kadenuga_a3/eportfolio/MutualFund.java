package eportfolio;

/**
 * 
 *  MutualFund class - defines iinstantiation and other methods 
 */
public class MutualFund extends Investment {

    /**
     *   instantiation method 
     * 
     * @param symbol The symbol of the mutualfund
     * @param name The name of the mutualfund
     * @param price The price of the mutualfund
     * @param quantity The quantity of the mutualfund
     *
     */
    public MutualFund(String symbol, String name, double price, int quantity){
        super(symbol, name, price, quantity);
        this.bookValue = (this.price * this.quantity);
    }

    /**
     *   buy method for stock to update quantity
     * @param newQuantity quantity you want to buy
     * @param newPrice price you want to buy
     */
    public void buyToUpdateQuantity(int newQuantity, double newPrice) {
        this.bookValue += (newQuantity * newPrice);
        this.quantity += newQuantity;
    }
   /**
     *   sell method for stock to update quantity and price
     * @param sellQuantity quantity you want to sell
     * @param sellPrice price you want to sell
     * @return gain gotten by transfer
     */
    public double sell(int sellQuantity, double sellPrice) {
        double payment = (sellQuantity * sellPrice);
        double bookValueLoss = bookValue * (sellQuantity / (double) this.quantity);
        bookValue -= bookValueLoss;
        quantity -= sellQuantity;
        price = sellPrice;
        return payment - bookValueLoss; // gain
    }

    

    /**
     * override toString method
     * @return  string explaining the mutual fund object
      */
    public String toString() {
        return "Mutual Fund: " + this.name + ", " + this.symbol + "\n Quantity: " + this.quantity + "\n Price: " + this.price + "\n";
    }



}