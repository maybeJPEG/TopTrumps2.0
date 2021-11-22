package trumps;
import org.junit.Assert;
import org.junit.Test;
import trumps.Exceptions.*;
import trumps.Impl.TopTrumpsImpl;

public class TrumpsTests {
    public static final int first_player = 1;
    public static final int second_player = 2;

    public TopTrumpsImpl getTopTrumps() throws NotExistentValueException, NoCardsException {
        return new TopTrumpsImpl();
    }


    @Test()
    public void goodStart() throws StatusException, GameExceptions, NoCardsException, WrongNameException, NotExistentPlayerException, NotExistentValueException, StartNotAllowedException {
        TopTrumps Game = getTopTrumps();
        int player = Game.start();
        Assert.assertTrue(if1or2(player)); ;
    }

    private boolean if1or2(int player) {
        return player==1||player==2;
    }

    @Test(expected = StartNotAllowedException.class)
    public void cantCallStartTwice() throws StatusException, GameExceptions, WrongNameException, NoCardsException, NotExistentValueException, NotExistentPlayerException, StartNotAllowedException {
        TopTrumps tt = this.getTopTrumps();
        tt.start();
        tt.start();
    }
    /**
     * test getFirstCard()
     * -statusException, falls die player noch keine cards Listen haben
     * -MatchException, falls keine Karten mehr in der cards Liste eines der player ist
     */
    @Test
    public void ifCardListHas4Categories() throws StatusException, GameExceptions, MatchException, NotYourTurnException, NotExistentPlayerException, NotExistentValueException, NoCardsException, WrongNameException, StartNotAllowedException {
        TopTrumps tt = this.getTopTrumps();
        tt.start();
        int[] actual_card_list = tt.getFirstCard(tt.getActive_Player());
        Assert.assertEquals(4, actual_card_list.length);
    }

    @Test
    public void ifCategoryHasCorrectValue() throws NotExistentValueException, StatusException, GameExceptions, NotExistentPlayerException, NotYourTurnException, MatchException, NoCardsException, WrongNameException, StartNotAllowedException {
        TopTrumps tt = this.getTopTrumps();
        tt.start();
        int[] actual_card_list = tt.getFirstCard(1);
        Assert.assertTrue(actual_card_list[0]<11);
    }

    @Test
    public void ifCategoryHasCorrectValue2() throws NotExistentValueException, StatusException, GameExceptions, NotExistentPlayerException, NotYourTurnException, MatchException, NoCardsException, WrongNameException, StartNotAllowedException {
        TopTrumps tt = this.getTopTrumps();
        tt.start();
        int[] actual_card_list = tt.getFirstCard(1);
        Assert.assertTrue(actual_card_list[0]>0);
    }


    /** test CompareCategory()
     * -kann man alle Kategorien wählen?
     * -Exception bei ausgewählter Category > 4.
     * -statusexception falls noch keine Decks gewählt.
     * -statusexception, wenn jemand versucht start() aufzurufen, nachdem decks vergeben wurden und bereits
     *  compareCategory gespielt wird.
     */

    @Test
    public void CompareCategoryReturnsValidPlayerNumber() throws StatusException, GameExceptions, CategoryDoesNotExistException, NotExistentValueException, NotExistentPlayerException, NotYourTurnException, DrawException, NoCardsException, WrongNameException, StartNotAllowedException {
        TopTrumps tt = this.getTopTrumps();
        tt.start();
        int actual = tt.compareCategory(1,tt.getActive_Player());
        Assert.assertTrue(actual < 4 || actual > 0);
    }

    @Test(expected = CategoryDoesNotExistException.class)
    public void failureInvalidCategory() throws StatusException, GameExceptions, CategoryDoesNotExistException, NotExistentValueException, NotExistentPlayerException, NotYourTurnException, DrawException, NoCardsException, WrongNameException, StartNotAllowedException {
        TopTrumps tt = new TopTrumpsImpl();
        tt.start();
        tt.compareCategory(5,tt.getActive_Player());
    }

    @Test(expected = StartNotAllowedException.class)
    public void failureWrongStatusTest() throws StatusException, GameExceptions, WrongNameException, NoCardsException, NotYourTurnException, MatchException, NotExistentValueException, NotExistentPlayerException, StartNotAllowedException {
        TopTrumps tt = new TopTrumpsImpl();
        tt.start();
        tt.getFirstCard(tt.getActive_Player());
        tt.start();
    }

    @Test(expected = StartNotAllowedException.class)
    public void failureWrongStatusTest2() throws StatusException, GameExceptions, WrongNameException, NoCardsException, NotYourTurnException, MatchException, CategoryDoesNotExistException, NotExistentValueException, NotExistentPlayerException, DrawException, StartNotAllowedException {
        TopTrumps tt = new TopTrumpsImpl();
        tt.start();
        tt.getFirstCard(1);
        tt.compareCategory(3,tt.getActive_Player());
        tt.start();
    }

    @Test(expected = StatusException.class)
    public void failureWrongStatusTest3() throws StatusException, GameExceptions, WrongNameException, NoCardsException, NotYourTurnException, MatchException, CategoryDoesNotExistException, NotExistentValueException, NotExistentPlayerException, DrawException {
        TopTrumps tt = new TopTrumpsImpl();
        tt.compareCategory(3,tt.getActive_Player());
        // start() hasn't been called yet, no players found or decks not distributed yet
    }

    @Test(expected = StatusException.class)
    public void failureWrongStatusTest4() throws StatusException, GameExceptions, WrongNameException, NoCardsException, NotYourTurnException, MatchException, CategoryDoesNotExistException, NotExistentValueException, NotExistentPlayerException, DrawException {
        TopTrumps tt = new TopTrumpsImpl();
        tt.getFirstCard(tt.getActive_Player());
        // start() hasn't been called yet, no players found or decks not distributed yet
    }


    /*@Test(expected=NotYourTurnException.class)
    void failureGetFirstCard1() throws StatusException, GameExceptions, MatchException, NotYourTurnException {
        TopTrumps tt = this.getTopTrumps();
        Player actual = tt.getFirstCard().getOwner();
        Assert.assertNotEquals(TopTrumpsImpl.active_player, actual);
    }*/

   /* @Test(expected = MatchException.class)
    void failureGetfirstCard2() throws StatusException, GameExceptions, MatchException, NotYourTurnException {
        TopTrumps tt = this.getTopTrumps();
        tt.getFirstCard();
        // No more cards in players Deck, other Player has won
    }*/

}
