import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.PriorityQueue;

/**
 * Represents a stock in the SafeTrade project
 *
 * @author Niranjan Mathirajan
 * @author Leo Xu
 *
 * @version March 22, 2021
 */
public class Stock
{

    private static DecimalFormat money = new DecimalFormat("0.00");

    private String stockSymbol;
    private String companyName;
    private double loPrice;
    private double hiPrice;
    private double lastPrice;
    private int                       volume;
    private PriorityQueue<TradeOrder> buyOrders;
    private PriorityQueue<TradeOrder> sellOrders;


    /**
     * Constructs a new stock with
     * a given symbol, company name, and starting price.
     * Sets low price, high price,
     * and last price to the same opening price. Sets
     * "day" volume to zero.
     * Initializes a priority qieue for sell orders to an
     * empty PriorityQueue with
     * a PriceComparator configured for comparing orders in
     * ascending order; initializes a
     * priority qieue for buy orders to an empty
     * PriorityQueue with a
     * PriceComparator configured for comparing orders in
     * descending order.
     *
     * @param symbol - the stock symbol.
     * @param name   - full company name.
     * @param price  - opening price for this stock.
     */
    public Stock(String symbol, String name, double price)
    {
        volume = 0;
        stockSymbol = symbol;
        companyName = name;
        loPrice = price;
        hiPrice = price;
        lastPrice = price;
        PriceComparator asc = new PriceComparator();
        PriceComparator desc = new PriceComparator(false);
        buyOrders = new PriorityQueue<>(2, desc);
        sellOrders = new PriorityQueue<>(2, asc);

    }


    /**
     * Returns a quote string for this stock.
     *
     * @return the quote for this stock.
     */
    public String getQuote()
    {
        String quote =
            companyName + " (" + stockSymbol + ")" +
                "\nPrice: " + getLastPrice() + "\thi: " + getHiPrice() +
                "\tlo: " + getLoPrice() + "\tvol: " + getVolume() + "\n";

        TradeOrder ask = sellOrders.peek();
        TradeOrder bid = buyOrders.peek();

        String askString = ask == null ?
            "Ask: none\t" :
            "Ask: " + ask.getPrice() + " size: " + ask.getShares() + "\t";

        String bidString = bid == null ?
            "Bid: none" :
            "Bid: " + bid.getPrice() + " size: " + bid.getShares();

        return quote + askString + bidString;

    }


    /**
     * Places a trading order for this stock.
     *
     * @param order - a trading order to be placed.
     */
    public void placeOrder(TradeOrder order)
    {
        String msg = "";
        if ( order != null && order.isLimit() )
        {
            if ( order.isSell() )
            {
                msg = "New Order:\t" + "Sell " + order
                    .getSymbol() + "(" + companyName + ")\n" + order
                    .getShares() + " shares at $" +
                    money.format(order.getPrice());
                sellOrders.add(order);
            }
            else if ( order.isBuy() )
            {
                msg = "New Order:\t" + "Buy " + order
                    .getSymbol() + "(" + companyName + ")\n" + order
                    .getShares() + " shares at $" +
                    money.format(order.getPrice());
                buyOrders.add(order);
            }
        }
        else if ( order != null && order.isMarket() )
        {
            if ( order.isSell() )
            {
                msg = "New Order:\t" + "Sell " + order
                    .getSymbol() + "(" + companyName + ")\n" + order
                    .getShares() + " shares at market";
                sellOrders.add(order);
            }
            else if ( order.isBuy() )
            {
                msg = "New Order:\t" + "Buy " + order
                    .getSymbol() + "(" + companyName + ")\n" + order
                    .getShares() + " shares at market";
                buyOrders.add(order);
            }
        }
        if ( order != null )
        {
            order.getTrader().receiveMessage(msg);
        }
        executeOrders();
    }


    /**
     * Executes as many pending orders as possible.
     */
    protected void executeOrders()
    {
        TradeOrder topSell = sellOrders.peek();
        TradeOrder topBuy = buyOrders.peek();
        while ( topSell != null && topBuy != null && !(topBuy
            .isLimit() && topSell.isLimit() && topSell.getPrice() > topBuy
            .getPrice()) )
        {

            if ( topSell.isLimit() && topBuy.isLimit() && topBuy
                .getPrice() >= topSell.getPrice() )
            {
                execution(topSell, topBuy, topSell.getPrice());
            }
            else if ( topSell.isMarket() && topBuy.isMarket() )
            {
                execution(topSell, topBuy, lastPrice);
            }
            else if ( topSell.isLimit() && topBuy.isMarket() )
            {
                execution(topSell, topBuy, topSell.getPrice());
            }
            else if ( topBuy.isLimit() && topSell.isMarket() )
            {
                execution(topSell, topBuy, topBuy.getPrice());
            }
            topSell = sellOrders.peek();
            topBuy = buyOrders.peek();
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
    public void execution(TradeOrder topSell, TradeOrder topBuy, double price)
    {
        int numShares = Math.min(topSell.getShares(), topBuy.getShares());

        String sellMsg = "You sold:\t" + numShares + " " + topSell
            .getSymbol() + " at " + money.format(price) + " amt " + money
            .format(price * numShares);

        String buyMsg = "You bought:\t" + numShares + " " + topSell
            .getSymbol() + " at " + money.format(price) + " amt " + money
            .format(price * numShares);

        topBuy.getTrader().receiveMessage(buyMsg);
        topSell.getTrader().receiveMessage(sellMsg);

        volume += numShares;
        hiPrice = Math.max(price, getHiPrice());
        loPrice = Math.min(price, getLoPrice());
        lastPrice = price;

        topSell.subtractShares(numShares);
        topBuy.subtractShares(numShares);

        System.out.println(buyOrders);
        if ( topSell.getShares() == 0 && topBuy.getShares() == 0 )
        {
            buyOrders.remove(topBuy);
            sellOrders.remove(topSell);
        }
        else if ( topSell.getShares() == 0 )
        {
            sellOrders.remove(topSell);
        }
        else if ( topBuy.getShares() == 0 )
        {
            buyOrders.remove(topBuy);
        }
    }

    //
    // The following are for test purposes only
    //


    /**
     *
     * Returns a stock's symbol
     * @return A stock's symbol
     */
    protected String getStockSymbol()
    {
        return stockSymbol;
    }

    /**
     *
     * Returns the company name
     * @return the company name
     */
    protected String getCompanyName()
    {
        return companyName;
    }

    /**
     * Returns a stock's lowest price
     * @return lowest price
     */
    protected double getLoPrice()
    {
        return loPrice;
    }

    /**
     * Returns a stock's highest price
     * @return highest price
     */
    protected double getHiPrice()
    {
        return hiPrice;
    }

    /**
     * Returns a stock's last price
     * @return last price
     */
    protected double getLastPrice()
    {
        return lastPrice;
    }

    /**
     * Returns a stock's daily volume
     * @return daily volume
     */
    protected int getVolume()
    {
        return volume;
    }

    /**
     *
     * Returns priority queue of buy orders
     * @return buy orders priority queue
     */
    protected PriorityQueue<TradeOrder> getBuyOrders()
    {
        return buyOrders;
    }


    /**
     *
     * Returns priority queue of sell orders
     * @return sell orders priority queue
     */
    protected PriorityQueue<TradeOrder> getSellOrders()
    {
        return sellOrders;
    }


    /**
     * <p>
     * A generic toString implementation that uses reflection to print names and
     * values of all fields <em>
     * declared in this class</em>. Note that superclass
     * fields are left out of this implementation.
     * </p>
     *
     * @return a string representation of this Stock.
     */
    public String toString()
    {
        String str = this.getClass().getName() + "[";
        String separator = "";

        Field[] fields = this.getClass().getDeclaredFields();

        for ( Field field : fields )
        {
            try
            {
                str += separator + field.getType().getName() + " " + field
                    .getName() + ":" + field.get(this);
            }
            catch ( IllegalAccessException ex )
            {
                System.out.println(ex);
            }

            separator = ", ";
        }

        return str + "]";
    }
}
