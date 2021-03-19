import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Represents a brokerage.
 */
public class Brokerage
    implements Login
{
    private Map<String, Trader> traders;
    private Set<Trader>         loggedTraders;
    private StockExchange       exchange;


    /**
     * @param exchange a stock exchange
     */
    public Brokerage(StockExchange exchange)
    {
        this.exchange = exchange;
        traders = new TreeMap<>();
        loggedTraders = new TreeSet<>();
    }


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


    public void logout(Trader trader)
    {
        loggedTraders.remove(trader);
    }

    public void placeOrder(TradeOrder order)
    {
        exchange.placeOrder(order);
    }

    public void getQuote(String symbol, Trader trader)
    {
        String quote = exchange.getQuote(symbol);
        trader.receiveMessage(quote);

    }


    //
    // The following are for test purposes only
    //
    protected Map<String, Trader> getTraders()
    {
        return traders;
    }


    protected Set<Trader> getLoggedTraders()
    {
        return loggedTraders;
    }


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


    /**
     * Tries to register a new user with a given screen name
     * and password;
     *
     * @param name     the screen name of the user.
     * @param password the password for the user.
     * @return 0 if successful, or an error code (a negative integer) if failed:<br>
     * -1 -- invalid screen name (must be 4-10 chars)<br>
     * -2 -- invalid password (must be 2-10 chars)<br>
     * -3 -- the screen name is already taken.
     */


    /**
     * Tries to login a user with a given screen name and password;
     *
     * @param name     the screen name of the user.
     * @param password the password for the user.
     * @return 0 if successful, or an error code (a negative integer) if failed:<br>
     * -1 -- screen name not found<br>
     * -2 -- invalid password<br>
     * -3 -- user is already logged in.
     */
