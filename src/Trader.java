import java.lang.reflect.*;
import java.util.*;

/**
 * Represents a stock trader.
 */
public class Trader implements Comparable<Trader> {
    private Brokerage brokerage;
    private String screenName, password;
    private TraderWindow myWindow;
    private Queue<String> mailbox;

    /**
     * Constructs a new trader, affiliated with a given brockerage, with a given
     * screen name and password.
     * 
     * @param brokerage - the brokerage for this trader.
     * @param name      - user name.
     * @param pswd      - password.
     */
    public Trader(Brokerage brokerage, String name, String pswd) {
        this.brokerage = brokerage;
        this.screenName = name;
        this.password = pswd;
    }

    /**
     * Compares this trader to another by comparing their screen names case blind.
     * 
     * @param other - the reference to a trader with which to compare.
     * @return the result of the comparison of this trader and other.
     */
    public int compareTo(Trader other) {
        return other.screenName.compareToIgnoreCase(getName());
    }

    /**
     * Indicates whether some other trader is "equal to" this one, based on
     * comparing their screen names case blind. This method will throw a
     * ClassCastException if other is not an instance of Trader.
     * 
     * @param other - the reference to an object with which to compare.
     * @return true if this trader's screen name is the same as other's; false
     *         otherwise.
     * 
     */
    public boolean equals(Object other) {
        try {
            return compareTo((Trader) other) == 0;
        } catch (Exception e) {
            throw new ClassCastException();
        }
    }

    /**
     * Returns the screen name for this trader.
     * 
     * @return the screen name for this trader.
     */
    public String getName() {
        return screenName;
    }

    /**
     * Returns the password for this trader.
     * 
     * @return the password for this trader.
     */
    public String getPassword() {
        return password;
    }

    public void getQuote(String symbol) {
        brokerage.getQuote(symbol, this);
    }

    public boolean hasMessages() {
        return mailbox.isEmpty();
    }

    public void openWindow() {
        myWindow = new TraderWindow(this);
        for (String msg : mailbox) {
            myWindow.showMessage(msg);
        }
    }

    public void placeOrder(TradeOrder order) {
        brokerage.placeOrder(order);
    }

    public void quit() {
        brokerage.logout(this);
        myWindow = null;
    }

    public void receiveMessage(String msg) {
        if (myWindow != null) {
            mailbox.add(msg);
            for (String m : mailbox) {
                myWindow.showMessage(m);
            }
        }
    }

    //
    // The following are for test purposes only
    //
    protected Queue<String> mailbox() {
        return mailbox;
    }

    /**
     * <p>
     * A generic toString implementation that uses reflection to print names and
     * values of all fields <em>declared in this class</em>. Note that superclass
     * fields are left out of this implementation.
     * </p>
     * 
     * @return a string representation of this Trader.
     */
    public String toString() {
        String str = this.getClass().getName() + "[";
        String separator = "";

        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                if (field.getType().getName().equals("Brokerage"))
                    str += separator + field.getType().getName() + " " + field.getName();
                else
                    str += separator + field.getType().getName() + " " + field.getName() + ":" + field.get(this);
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }

            separator = ", ";
        }

        return str + "]";
    }
}
