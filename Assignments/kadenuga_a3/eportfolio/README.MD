# Java Assignment 1

**ePortfolio**  
Provide simulation of investment and investment options - stock and mutualfund

## Table of Contents

- [Description](#description)

- [Features](#features)

- [Contact](#contact)

## Description

This project simulates investment. It uses Java and OOP concepts. It also uses Java swing and AWT for GUI

## Features

key features of the project:

- Classes i.e Stock, MutualFund, Portfolio
- Inheritance and Abstraction
- GUI
- JavaDoc

## Build instructions

compilation: javac eportfolio/PortfolioGUI.java
running: java eportfolio/PortfolioGUI

## Contact

- **Name**: Kenny Adenuga
- **Email**: kadenuga@uoguelph.ca

## Test cases

### **Buy Function Test Cases**

- **Test Case 1:** Buy 100 stocks with price 50.  
  **Input:** `buy(100, 50, "Apple", "stock", "AAPL")`  
  **Expected Outcome:** Apple stock is added with 100 units at $50.

- **Test Case 2:** Buy additional 50 stocks with price 60.  
  **Input:** `buy(50, 60, "Apple", "stock", "AAPL")`  
  **Expected Outcome:** Apple stock now has 150 units with average price of $53.33.

- **Test Case 3:** Attempt to buy 0 stocks.  
  **Input:** `buy(0, 50, "Apple", "stock", "AAPL")`  
  **Expected Outcome:** No new investment is added.

### **Sell Function Test Cases**

- **Test Case 1:** Sell 50 stocks of Apple.  
  **Input:** `sell(50, 60, "AAPL")`  
  **Expected Outcome:** Apple stock quantity reduces to 100, and gains are calculated.

- **Test Case 2:** Sell more stocks than owned.  
  **Input:** `sell(200, 60, "AAPL")`  
  **Expected Outcome:** Error message or no transaction.

- **Test Case 3:** Sell stock until quantity reaches zero, then remove it.  
  **Input:** `sell(100, 60, "AAPL")`  
  **Expected Outcome:** Apple stock is removed from the portfolio.

### **Update Prices Test Cases**

- **Test Case 1:** Update price to 55 for Apple stock.  
  **Input:** `updatePrices()` and enter 55 for AAPL.  
  **Expected Outcome:** Apple's price is updated to $55.

- **Test Case 2:** Attempt to update with a negative price.  
  **Input:** `updatePrices()` and enter -10 for AAPL.  
  **Expected Outcome:** Error message for invalid price.

### **Get Total Gain Test Cases**

- **Test Case 1:** Calculate gain for a stock with a positive gain.  
  **Input:** `getGain()` (Apple stock bought at $50 and sold at $60)  
  **Expected Outcome:** Total gain should be $1000.

- **Test Case 2:** Calculate gain when no investment has been made.  
  **Input:** `getGain()`  
  **Expected Outcome:** Total gain should be $0.

### **Search by Criteria Test Cases**

- **Test Case 1:** Search by symbol with no price range.  
  **Input:** `searchStringCriteria("AAPL", "", 0, 0)`  
  **Expected Outcome:** Return Apple stock matching the symbol.

- **Test Case 2:** Search by name with price range filter.  
  **Input:** `searchStringCriteria("", "Apple", 50, 60)`  
  **Expected Outcome:** Return Apple stock if price is between $50 and $60.

- **Test Case 3:** Search by partial symbol and full name.  
  **Input:** `searchStringCriteria("APL", "Apple", 0, 0)`  
  **Expected Outcome:** Return Apple stock matching partial symbol and name.

These test cases ensure that the core functionality of the portfolio system works correctly in a variety of scenarios.
