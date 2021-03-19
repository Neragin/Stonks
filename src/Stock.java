import java.util.*;
import java.lang.reflect.*;
import java.text.DecimalFormat;

/**
 * Represents a stock in the SafeTrade project
 */
public class Stock {
    public static DecimalFormat money = new DecimalFormat("0.00");

    private String stockSymbol;
    private String companyName;
    private double loPrice, hiPrice, lastPrice;
    private int volume;
    private PriorityQueue<TradeOrder> buyOrders, sellOrders;

    /**
     * Constructs a new stock with a given symbol, company name, and starting price.
     * Sets low price, high price, and last price to the same opening price. Sets
     * "day" volume to zero. Initializes a priority qieue for sell orders to an
     * empty PriorityQueue with a PriceComparator configured for comparing orders in
     * ascending order; initializes a priority qieue for buy orders to an empty
     * PriorityQueue with a PriceComparator configured for comparing orders in
     * descending order.
     *
     * @param symbol - the stock symbol.
     * @param name   - full company name.
     * @param price  - opening price for this stock.
     */
    public Stock(String symbol, String name, double price) {
        volume = 0;
        stockSymbol = symbol;
        companyName = name;
        loPrice = hiPrice = lastPrice = price;
        PriceComparator asc = new PriceComparator();
        PriceComparator desc = new PriceComparator(false);
        buyOrders = new PriorityQueue<TradeOrder>(2, desc);
        sellOrders = new PriorityQueue<TradeOrder>(2, asc);

    }

    /**
     * Returns a quote string for this stock.
     *
     * @return the quote for this stock.
     */
    public String getQuote() {
        String quote = companyName + " (" + stockSymbol + ")" + "\n Price: " + getLastPrice() + "\thi: " + getHiPrice()
                + "\tlo: " + getLoPrice() + "\tvol: " + getVolume() + "\n ";

        TradeOrder Ask = sellOrders.peek();
        TradeOrder Bid = sellOrders.peek();

        String askString = Ask == null ? "Ask: none\t" : "Ask: " + Ask.getPrice() + " size: " + Ask.getShares() + "\t";

        String bidString = Bid == null ? "Bid: none" : "Bid: " + Bid.getPrice() + " size: " + Bid.getShares();

        return quote + askString + bidString;

    }

    /**
     * Places a trading order for this stock.
     *
     * @param order - a trading order to be placed.
     */
    public void placeOrder(TradeOrder order) {
        String msg = "";
        if (order != null && order.isLimit()) {
            if (order.isSell()) {
                msg = "New Order:\t" + "Sell " + order.getSymbol() + "(" + companyName + ")\n" + order.getShares()
                        + " shares at " + order.getPrice();
            } else if (order.isBuy()) {
                msg = "New Order:\t" + "Buy " + order.getSymbol() + "(" + companyName + ")\n" + order.getShares()
                        + " shares at " + order.getPrice();
            }
        } else if (order != null && order.isMarket()) {
            if (order.isSell()) {
                msg = "New Order:\t" + "Sell " + order.getSymbol() + "(" + companyName + ")\n" + order.getShares()
                        + " shares at market";
            } else if (order.isBuy()) {
                msg = "New Order:\t" + "Buy " + order.getSymbol() + "(" + companyName + ")\n" + order.getShares()
                        + " shares at market";
            }
        }
        if ( order != null )
        {
            order.getTrader().receiveMessage(msg);
        }
    }

    /**
     * Executes as many pending orders as possible.
     */
    protected void executeOrders() {
        TradeOrder topSell = sellOrders.peek();
        TradeOrder topBuy = buyOrders.peek();

        while (topSell.isMarket() || topBuy.isMarket() || topSell.getPrice() <= topBuy.getPrice()) {

            if (topSell.isLimit() && topBuy.isLimit() && topBuy.getPrice() >= topSell.getPrice()) {
                execution(topSell, topBuy, topSell.getPrice());
            } else if (topSell.isMarket() && topBuy.isMarket()) {
                execution(topSell, topBuy, lastPrice);
            } else if (topSell.isLimit() && topBuy.isMarket()) {
                execution(topSell, topBuy, topSell.getPrice());
            } else if (topBuy.isLimit() && topSell.isMarket()) {
                execution(topSell, topBuy, topBuy.getPrice());
            }
        }
    }

    /**
     * Helper function to carry out an order. Sends message, updates day prices,
     * completes and updates pending orders.
     *
     * @param topSell - Sell order with the lowest price
     * @param topBuy  - Buy order with the highest price
     * @param price   - Actual price set for the transaction
     */
    protected void execution(TradeOrder topSell, TradeOrder topBuy, double price) {
        int numShares = topSell.getShares() > topBuy.getShares() ? topBuy.getShares() : topSell.getShares();
        money.applyPattern(Double.toString(price));
        String sellMsg =
            "You sold:\t" + numShares + " " + topSell.getSymbol() + "at " + price;
        String buyMsg =
            "You bought:\t" + numShares + " " + topSell.getSymbol() + "at " + price;

        topBuy.getTrader().receiveMessage(buyMsg);
        topSell.getTrader().receiveMessage(sellMsg);

        volume += numShares;
        hiPrice = price > getHiPrice() ? price : getHiPrice();
        loPrice = price < getLoPrice() ? price : getLoPrice();
        lastPrice = price;

        topSell.subtractShares(numShares);
        topBuy.subtractShares(numShares);

        if (topSell.getShares() == 0 && topBuy.getShares() == 0) {
            buyOrders.poll();
            sellOrders.poll();
        } else if (topSell.getShares() == 0) {
            sellOrders.poll();
        } else if (topBuy.getShares() == 0) {
            buyOrders.poll();
        }
    }

    //
    // The following are for test purposes only
    //

    protected String getStockSymbol() {
        return stockSymbol;
    }

    protected String getCompanyName() {
        return companyName;
    }

    protected double getLoPrice() {
        return loPrice;
    }

    protected double getHiPrice() {
        return hiPrice;
    }

    protected double getLastPrice() {
        return lastPrice;
    }

    protected int getVolume() {
        return volume;
    }

//    public String getQuote()
//    {
//        return companyName = " (" + stockSymbol + ")\nPrice: "+lastPrice +
//            "\thi: " + hiPrice + "\tlo: " + loPrice + "\tvol: " + volume;
//    }

    protected PriorityQueue<TradeOrder> getBuyOrders() {
        return buyOrders;
    }

    protected PriorityQueue<TradeOrder> getSellOrders() {
        return sellOrders;
    }

    /**
     * <p>
     * A generic toString implementation that uses reflection to print names and
     * values of all fields <em>declared in this class</em>. Note that superclass
     * fields are left out of this implementation.
     * </p>
     * 
     * @return a string representation of this Stock.
     */
    public String toString() {
        String str = this.getClass().getName() + "[";
        String separator = "";

        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                str += separator + field.getType().getName() + " " + field.getName() + ":" + field.get(this);
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }

            separator = ", ";
        }

        return str + "]";
    }
}
