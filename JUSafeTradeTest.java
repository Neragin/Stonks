import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * SafeTrade tests:
 * TradeOrder
 * PriceComparator
 * Trader
 * Brokerage
 * StockExchange
 * Stock
 *
 * @author Leo Xu
 * @author Niranjan Mathirajan
 * @author Assignment: JM Chapter 19 - SafeTrade
 * @author Sources: None
 * @version March 18 2021
 */
public class JUSafeTradeTest
{
    // --Test TradeOrder
    /**
     * TradeOrder tests:
     * TradeOrderConstructor - constructs TradeOrder and then compare toString
     * TradeOrderGetTrader - compares value returned to constructed value
     * TradeOrderGetSymbol - compares value returned to constructed value
     * TradeOrderIsBuy - compares value returned to constructed value
     * TradeOrderIsSell - compares value returned to constructed value
     * TradeOrderIsMarket - compares value returned to constructed value
     * TradeOrderIsLimit - compares value returned to constructed value
     * TradeOrderGetShares - compares value returned to constructed value
     * TradeOrderGetPrice - compares value returned to constructed value
     * TradeOrderSubtractShares - subtracts known value & compares result
     * returned by getShares to expected value
     */
    private String  symbol        = "GGGL";
    private boolean buyOrder      = true;
    private boolean marketOrder   = true;
    private int     numShares     = 123;
    private int     numToSubtract = 24;
    private double  price         = 123.45;


    @Test public void tradeOrderConstructor()
    {
        TradeOrder to = new TradeOrder(null,
            symbol,
            buyOrder,
            marketOrder,
            numShares,
            price);
        String toStr = to.toString();

        assertTrue("<< Invalid TradeOrder Constructor >>",
            toStr.contains("TradeOrder[Trader trader:null") && toStr
                .contains("java.lang.String symbol:" + symbol) && toStr
                .contains("boolean buyOrder:" + buyOrder) && toStr
                .contains("boolean marketOrder:" + marketOrder) && toStr
                .contains("int numShares:" + numShares) && toStr
                .contains("double price:" + price));
    }


    @Test public void TradeOrderToString()
    {
        TradeOrder to = new TradeOrder(null,
            symbol,
            buyOrder,
            marketOrder,
            numShares,
            price);
        assertNotNull(to.toString());
    }


    @Test public void tradeOrderGetTrader()
    {
        TradeOrder to = new TradeOrder(null,
            symbol,
            buyOrder,
            marketOrder,
            numShares,
            price);
        assertNull("<< TradeOrder: " + to.getTrader() + " should be null >>",
            to.getTrader());
    }


    @Test public void tradeOrderGetSymbol()
    {
        TradeOrder to = new TradeOrder(null,
            symbol,
            buyOrder,
            marketOrder,
            numShares,
            price);
        assertEquals(
            "<< TradeOrder: " + to.getTrader() + " should be " + symbol + " >>",
            symbol,
            to.getSymbol());
    }


    @Test public void tradeOrderIsBuy()
    {
        TradeOrder to = new TradeOrder(null,
            symbol,
            buyOrder,
            marketOrder,
            numShares,
            price);

        assertTrue("<< TradeOrder: " + to
            .isBuy() + " should be " + buyOrder + " >>", to.isBuy());
    }


    @Test public void tradeOrderIsSell()
    {
        TradeOrder to = new TradeOrder(null,
            symbol,
            buyOrder,
            marketOrder,
            numShares,
            price);
        assertFalse("<< TradeOrder: " + to
            .isSell() + " should be " + !buyOrder + " >>", to.isSell());
    }


    @Test public void tradeOrderIsMarket()
    {
        TradeOrder to = new TradeOrder(null,
            symbol,
            buyOrder,
            marketOrder,
            numShares,
            price);
        assertTrue("<< TradeOrder: " + to
            .isMarket() + " should be " + marketOrder + " >>", to.isMarket());
    }


    @Test public void tradeOrderIsLimit()
    {
        TradeOrder to = new TradeOrder(null,
            symbol,
            buyOrder,
            marketOrder,
            numShares,
            price);

        assertFalse("<< TradeOrder: " + to
            .isLimit() + " should be " + !marketOrder + ">>", to.isLimit());
    }


    @Test public void tradeOrderGetShares()
    {
        TradeOrder to = new TradeOrder(null,
            symbol,
            buyOrder,
            marketOrder,
            numShares,
            price);
        assertTrue("<< TradeOrder: " + to
                .getShares() + " should be " + numShares + ">>",
            numShares == to.getShares() || (numShares - numToSubtract) == to
                .getShares());
    }


    @Test public void tradeOrderGetPrice()
    {
        TradeOrder to = new TradeOrder(null,
            symbol,
            buyOrder,
            marketOrder,
            numShares,
            price);
        assertEquals(
            "<< TradeOrder: " + to.getPrice() + " should be " + price + ">>",
            price,
            to.getPrice(),
            0.0);
    }


    @Test public void tradeOrderSubtractShares()
    {
        TradeOrder to = new TradeOrder(null,
            symbol,
            buyOrder,
            marketOrder,
            numShares,
            price);
        to.subtractShares(numToSubtract);
        assertEquals(
            "<< TradeOrder: subtractShares(" + numToSubtract + ") should be " + (numShares - numToSubtract) + ">>",
            numShares - numToSubtract,
            to.getShares());
    }


    // --Test TraderWindow Stub
    @Test public void traderWindowConstructor()
    {
        TraderWindow tw = new TraderWindow(null);
        assertNotNull(tw);
    }


    @Test public void traderWindowShowMessage()
    {
        TraderWindow tw = new TraderWindow(null);
        assertNotNull(tw);
        tw.showMessage(null);
    }

    //  --Test PriceComparator


    @Test public void priceComparatorCompare()
    {
        PriceComparator pcNoParam = new PriceComparator();
        PriceComparator pcParamFalse = new PriceComparator(false);

        Brokerage broke = new Brokerage(new StockExchange());

        TradeOrder o1 = new TradeOrder(new Trader(broke, "bob", "bruh"),
            "GG",
            true,
            false,
            69,
            420.0);

        TradeOrder o2 = new TradeOrder(new Trader(broke, "adf", "asdf"),
            "BRUH",
            false,
            true,
            70,
            500.25);

        TradeOrder o3 = new TradeOrder(new Trader(broke, "asda", "bragsduh"),
            "LMAO",
            true,
            false,
            12,
            229.47);

        TradeOrder o4 = new TradeOrder(new Trader(broke, "bsob", "brudfah"),
            "UWU",
            false,
            true,
            1,
            10);

        assertEquals(pcNoParam.compare(o2, o4), 0);
        assertEquals(pcParamFalse.compare(o3, o4), 1);
        assertEquals(pcParamFalse.compare(o2, o1), -1);
        assertEquals(pcNoParam.compare(o3, o3), 0);
        assertEquals(pcNoParam.compare(o1, o3), 19053, 0.00001);
        assertEquals(pcParamFalse.compare(o1, o3), -19053, 0.00001);
    }


    // --Test Trader
    @Test public void traderToStringTest()
    {
        Brokerage broke = new Brokerage(new StockExchange());
        Trader t = new Trader(broke, "joe", "183nco91hpdb");
        assertNotNull(t.toString());
    }




    @Test public void traderCompareTo()
    {
        Brokerage broke = new Brokerage(new StockExchange());
        Trader t = new Trader(broke, "Neragin", "183nco91hpdb");
        Trader j = new Trader(broke, "Dhanish", "183nco91hpdb");
        Trader bruh = new Trader(broke, "Neragin", "a9odihufpiadubfai");
        assertNotEquals(0, t.compareTo(j));
        assertEquals(0, t.compareTo(bruh));
    }


    @Test public void traderEquals()
    {
        Brokerage broke = new Brokerage(new StockExchange());
        Trader t = new Trader(broke, "Neragin", "183nco91hpdb");
        Trader j = new Trader(broke, "Dhanish", "183nco91hpdb");
        Trader bruh = new Trader(broke, "Neragin", "a9odihufpiadubfai");
        LinkedList<Integer> random = new LinkedList<>();
        assertNotEquals(t, j);
        assertEquals(t, bruh);
        try
        {
            t.equals(random);
        }
        catch ( ClassCastException e )
        {
            assertTrue(true);
        }
    }


    @Test public void traderGetName()
    {
        Brokerage broke = new Brokerage(new StockExchange());
        Trader t = new Trader(broke, "Neragin", "183nco91hpdb");
        assertEquals("Neragin", t.getName());
    }


    @Test public void traderGetPassword()
    {
        Brokerage broke = new Brokerage(new StockExchange());
        Trader t = new Trader(broke, "Neragin", "183nco91hpdb");
        assertEquals("183nco91hpdb", t.getPassword());
    }


    @Test public void traderGetQuote()
    {
        Brokerage broke = new Brokerage(new StockExchange());
        Trader t = new Trader(broke, "Neragin", "183nco91hpdb");
        assertEquals("183nco91hpdb", t.getPassword());
        t.getQuote("DS");
        assertTrue(t.mailbox().peek().contains("not found"));

    }

    @Test public void traderHasMessages()
    {
        Brokerage broke = new Brokerage(new StockExchange());
        Trader t = new Trader(broke, "Neragin", "183nco91hpdb");
        t.getQuote("DS");
        assertTrue(t.hasMessages());
    }

    @Test public void traderOpenWindow()
    {
        Brokerage broke = new Brokerage(new StockExchange());
        Trader t = new Trader(broke, "Neragin", "183nco91hpdb");
        t.openWindow();
        t.getQuote("DS");
        assertFalse(t.hasMessages());
    }

    @Test public void traderPlaceOrder()
    {
        StockExchange s = new StockExchange();
        Brokerage broke = new Brokerage(s);
        Trader t = new Trader(broke, "Neragin", "183nco91hpdb");
        TradeOrder o = new TradeOrder(t, "GGGL", true, false, 100, 10.0);
        t.placeOrder(o);
        assertEquals(t.mailbox().peek(), "GGGL not found");
        s.listStock("GGGL", "Giggle.com", 15.00);
        t.placeOrder(o);
        t.mailbox().remove();
        assertEquals(t.mailbox().peek(), "New Order:\tBuy GGGL(Giggle.com)\n100 shares at $10.00");
    }

    @Test public void traderQuit()
    {
        Brokerage broke = new Brokerage(new StockExchange());
        Trader t = new Trader(broke, "Neragin", "183nco91hpdb");
        t.openWindow();
        t.quit();
        t.getQuote("DS");
        assertTrue(t.hasMessages());
    }

    @Test public void traderRecieveMessage()
    {
        Brokerage broke = new Brokerage(new StockExchange());
        Trader t = new Trader(broke, "Neragin", "183nco91hpdb");
        t.receiveMessage("bruh");
        assertEquals(t.mailbox().peek(), "bruh");
    }
    @Test public void traderMailbox()
    {
        Brokerage broke = new Brokerage(new StockExchange());
        Trader t = new Trader(broke, "Neragin", "183nco91hpdb");
        assertEquals(t.mailbox(), new LinkedList<String>());
    }

//    @Test public void StockExecuteOrders()
//    {
//        StockExchange s = new StockExchange();
//        s.listStock("GGGL", "Giggle.com", 15.00);
//        Brokerage broke = new Brokerage(s);
//        Trader buyer = new Trader(broke, "Neragin", "183nco91hpdb");
//        Trader seller = new Trader(broke, "Leo", "oadhfoid")
//        TradeOrder buy = new TradeOrder(buyer, "GGGL", true, false, 100, 10.0);
//        TradeOrder sell = new TradeOrder(seller, "GGGL", true, false, 100, 9.0);
//        buyer.placeOrder(buy);
//        seller.placeOrder(sell);
//        assertEquals(t.mailbox().peek(), "GGGL not found");
//    }

    // --Test Brokerage


    // TODO your tests here
    @Test public void brokerageAddUser()
    {
        Brokerage brokerer = new Brokerage(new StockExchange());
        assertEquals(-1, brokerer.addUser("a", "aaaaa"));
        assertEquals(-1, brokerer.addUser("aaaaaaaaaaaaaaaa", "aaaaaa"));
        assertEquals(-2, brokerer.addUser("aaaaa", "a"));
        assertEquals(-2, brokerer.addUser("aaaaa", "aaaaaaaaaaaaaaaaaaa"));
        assertEquals(0, brokerer.addUser("aaaaa", "aaaaa"));
        assertEquals(-3, brokerer.addUser("aaaaa", "aaaaa"));
    }


    @Test public void brokerageLogout()
    {
        Brokerage brokerman = new Brokerage(new StockExchange());
        brokerman.addUser("aaaaaa", "aaaaa");
        brokerman.login("aaaaaa", "aaaaa");
        brokerman.logout(brokerman.getTraders().get("aaaaaa"));
        assertFalse(brokerman.getLoggedTraders()
            .contains(new Trader(brokerman, "aaaaaa", "aaaaaa")));

    }


    @Test public void brokerageLogin()
    {
        Brokerage broking = new Brokerage(new StockExchange());
        assertEquals(-1, broking.login("abcde", "abcde"));
        broking.addUser("abcde", "abcde");
        assertEquals(-2, broking.login("abcde", "abcd"));
        broking.login("abcde", "abcde");
        assertEquals(-3, broking.login("abcde", "abcde"));
        broking.addUser("edfge", "edfge");
        assertEquals(0, broking.login("edfge", "edfge"));
    }


    @Test public void brokeragePlaceOrder()
    {
        Brokerage broke = new Brokerage(new StockExchange());
        Trader trader = new Trader(broke, "aaaaaa", "aaaaaa");
        TradeOrder order = new TradeOrder(trader, symbol, true, true, 15, 10);
        broke.placeOrder(order);
        assertTrue(trader.hasMessages());

    }
    // --Test StockExchange

    // TODO your tests here

    // --Test Stock

    // TODO your tests here

    // Remove block comment below to run JUnit test in console
/*
    public static junit.framework.Test suite()
    {
        return new JUnit4TestAdapter( JUSafeTradeTest.class );
    }

    public static void main( String args[] )
    {
        org.junit.runner.JUnitCore.main( "JUSafeTradeTest" );
    }
*/
}

