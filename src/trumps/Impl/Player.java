package trumps.Impl;

import trumps.Exceptions.NoCardsException;
import trumps.Exceptions.NotExistentValueException;

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Card> cards = new ArrayList<Card>();
    private Card actual_card;

    public Player(String name) throws NotExistentValueException, NoCardsException {
        this.name = name;
        this.distributeCards();
        this.updateActualCard();
    }

    private void distributeCards() throws NotExistentValueException {
        for(int i = 0; i < 20; i++){
            Card card = new Card(i, this);
            this.cards.add(card);
        }
    }

    public ArrayList<Card> getCards() {
        return cards;
    }


    private void updateActualCard() throws NoCardsException {
        if (this.cards.get(0) == null)
            throw new NoCardsException("No cards");
        this.actual_card = this.cards.get(0);
    }

    public Card getActual_card() {
        return this.actual_card;
    }

}