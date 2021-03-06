import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a stock exchange. A <code>StockExchange</code> keeps a
 * <code>HashMap</code> of stocks, keyed by a stock symbol. It has methods to
 * list a new stock, request a quote for a given stock symbol, and to place a
 * specified trade order.
 *
 * @author Niranjan Mathirajan
 * @author Leo Xu
 *
 * @version March 22, 2021
 */
public class StockExchange
{
    private Map<String, Stock> listedStocks;

    /**
     *
     * Create a new StockExchange object.
     */
    public StockExchange()
    {
        listedStocks = new HashMap<>();
    }


    /**
     * Adds a new stock with given parameters to the listen stocks.
     *
     * @param symbol stock symbol
     * @param name   full company name
     * @param price  opening stock price
     */
    public void listStock(String symbol, String name, double price)
    {
        listedStocks.put(symbol, new Stock(symbol, name, price));
    }


    /**
     * Returns a quote for a given stock.
     * If the symbol (ex. XYZ) is not found
     * in the exchange's list of stocks,
     * the string that is returned should be "XYZ not found".
     *
     * @param symbol Stock symbol.
     * @return a text message that contains the quote.
     */
    public String getQuote(String symbol)
    {
        if ( !listedStocks.containsKey(symbol) )
        {
            return symbol + " not found";
        }
        return listedStocks.get(symbol).getQuote();
    }

    /**
     *
     *  Places a trade order by calling
     *  stock.placeOrder
     *  for the stock specified by the stock
     *  symbol in the trade order.
     * @param order - a trading order to be placed with this stock exchange.
     */
    public void placeOrder(TradeOrder order)
    {
        if ( !listedStocks.containsKey(order.getSymbol()) )
        {
            order.getTrader().receiveMessage(order.getSymbol() + " not found");
        }
        else
        {
            listedStocks.get(order.getSymbol()).placeOrder(order);
        }
    }


    //
    // The following are for test purposes only
    //

    /**
     * Gets the listed stocks for testing
     * @return A map of all the listed Stocks
     */
    protected Map<String, Stock> getListedStocks()
    {
        return listedStocks;
    }


    /**
     * <p>
     * A generic toString implementation that uses reflection to print names and
     * values of all fields <em>declared in this class</em>. Note that
     * superclass fields are left out of this implementation.
     * </p>
     *
     * @return a string representation of this StockExchange.
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
