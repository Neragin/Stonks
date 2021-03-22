import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Represents a brokerage.
 * @author Niranjan Mathirajan
 * @author Leo Xu
 *
 * @version March 22, 2021
 */
public class Brokerage
    implements Login
{
    private Map<String, Trader> traders;
    private Set<Trader>         loggedTraders;
    private StockExchange       exchange;


    /**
     * Constructs new brokerage affiliated with a given stock exchange.
     * Initializes the map of traders to an empty map (a TreeMap),
     * keyed by trader's name; initializes the set of active (logged-in)
     * traders to an empty set (a TreeSet).
     * @param exchange - a stock exchange
     */
    public Brokerage(StockExchange exchange)
    {
        this.exchange = exchange;
        traders = new TreeMap<>();
        loggedTraders = new TreeSet<>();
    }


    /**
     * Tries to register a new trader with a
     * given screen name and password.
     *  If successful, creates a Trader object for
     *  this trader and adds this trader to the map of
     *  all traders (using the screen name as the key).
     * @param name - the screen name of the trader.
     * @param password - the password for the trader.
     *
     * @return 0 if successful, or an error code (a negative integer) if failed:
     * -1 -- invalid screen name (must be 4-10 chars)
     * -2 -- invalid password (must be 2-10 chars)
     * -3 -- the screen name is already taken.

     */
    public int addUser(String name, String password)
    {
        if ( traders.get(name) != null )
        {
            return -3;
        }
        else if ( !(name.length() > 4 && name.length() < 10) )
        {
            return -1;
        }
        else if ( !(password.length() < 10 && password.length() > 2) )
        {
            return -2;
        }
        else
        {
            traders.put(name, new Trader(this, name, password));
            return 0;
        }
    }


    /**
     * Tries to login a trader with a given screen name and password.
     * @param name - the screen name of the trader.
     * @param password - the password for the trader.
     *
     * @return 0 if successful, or an error code (a negative integer) if failed:
     * -1 -- invalid screen name (must be 4-10 chars)
     * -2 -- invalid password (must be 2-10 chars)
     * -3 -- the screen name is already taken.
     */
    public int login(String name, String password)
    {
        Trader trader = traders.get(name);
        if ( trader == null )
        {
            return -1;
        }

        else if ( loggedTraders.contains(trader) )
        {
            return -3;
        }
        else if ( !trader.getPassword().equals(password) )
        {
            return -2;
        }
        else
        {
            loggedTraders.add(trader);
            trader.receiveMessage("Welcome to SafeTrade!");
            trader.openWindow();
            return 0;
        }
    }


    /**
     * Removes a specified trader from the set of logged-in traders.
     * The trader may be assumed to logged in already.
     * @param trader - the trader that logs out
     */
    public void logout(Trader trader)
    {
        loggedTraders.remove(trader);
    }


    /**
     *
     * Places an order at the stock exchange.
     * @param order - an order to be placed at the stock exchange.
     */
    public void placeOrder(TradeOrder order)
    {
        exchange.placeOrder(order);
    }


    /**
     * Requests a quote for a given stock from the stock
     * exchange and passes it along to the trader by
     * calling trader's receiveMessage method.
     * @param symbol - the stock symbol.
     * @param trader - the trader who requested a quote.
     */
    public void getQuote(String symbol, Trader trader)
    {
        String quote = exchange.getQuote(symbol);
        trader.receiveMessage(quote);

    }


    //
    // The following are for test purposes only
    //

    /**
     *
     * Gets traders for testing
     * @return Map of traders
     */
    protected Map<String, Trader> getTraders()
    {
        return traders;
    }

    /**
     *
     * Gets logged in traders for testing
     * @return Map of logged in traders
     */
    protected Set<Trader> getLoggedTraders()
    {
        return loggedTraders;
    }

    /**
     *
     * Gets the broker's stock exchange for testing
     * @return the broker's stock exchange
     */
    protected StockExchange getExchange()
    {
        return exchange;
    }


    /**
     * <p>
     * A generic toString implementation that uses reflection to print names and
     * values of all fields <em>declared in this class</em>. Note that
     * superclass fields are left out of this implementation.
     * </p>
     *
     * @return a string representation of this Brokerage.
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

