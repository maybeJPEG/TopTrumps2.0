package trumps.Impl;

import trumps.*;
import trumps.Exceptions.*;
import trumps.tcp.TCPStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public abstract class TopTrumpsImpl implements TopTrumps {

    public Player first_player;
    public Player second_player;
    public Player active_player = null;
    private ArrayList<Card> activeCards = new ArrayList<Card>();
    private Card actual_card;

    public TopTrumpsImpl() throws NotExistentValueException, NoCardsException {
        this.first_player = new Player("alice");
        this.second_player = new Player("bob");
    }

    @Override
    public int start() throws NotExistentPlayerException, StartNotAllowedException {
        if(active_player == null) {
            determineActivePlayer();
            if (determineActivePlayer() == 1) {
                active_player = first_player;
                return 1;
            } else {
                active_player = second_player;
                return 2;
            }
        }
        else throw new StartNotAllowedException();
    }

    private int determineActivePlayer() throws NotExistentPlayerException {
        int random = 1;
        Random r = new Random();
        random = r.nextInt(2);
        if(random>2 || random <0){
            throw new NotExistentPlayerException("Must return number 1 for first_player or number 2 for second_player");
        }
        return random;
    }

    @Override
    public int[] getFirstCard(int player) throws StatusException, NotExistentPlayerException, IOException {
        if(active_player == null) {
            throw new StatusException("First call method start(), no Players determined yet!");
        }
        else {
            Player playerOBJ = getPlayerFromInteger(player);
            addActiveCards();
            Card actual_card = playerOBJ.getActual_card();
            return actual_card.getSecureList();
        }
    }

    private Player getPlayerFromInteger(int player) throws NotExistentPlayerException {
            if (player == 1) {
                return first_player;
            }
            if (player == 2) {
                return second_player;
            } else {
                throw new NotExistentPlayerException("This Player does not exist");
            }
    }

    @Override
    public int compareCategory(int category, int player) throws StatusException, NotExistentPlayerException, NotYourTurnException, CategoryDoesNotExistException, DrawException, IOException {
        if(active_player == null) {
            throw new StatusException("First call method start(), no Players determined yet!");
        }
        else {
            validateActualPlayer(player);
            validateCategory(category);
            return compareCards(category);
        }
    }

    private void validateActualPlayer(int player) throws NotExistentPlayerException, NotYourTurnException {
        Player playerOBJ = getPlayerFromInteger(player);
        if(playerOBJ != this.active_player){
            throw new NotYourTurnException();
        }
    }

    private void validateCategory(int category) throws CategoryDoesNotExistException {
        int categoryOBJ = category;
        if(categoryOBJ > 5 || categoryOBJ < 1){
            throw new CategoryDoesNotExistException();
        }
    }

    private int compareCards(int category) throws CategoryDoesNotExistException, DrawException {

        if(this.first_player.getActual_card().getCategory(category) > this.second_player.getActual_card().getCategory(category)){
            for(int i = 0; i < activeCards.size(); i++ ){
                this.first_player.getCards().add(activeCards.get(i));
            }
            activeCards.clear();
            active_player = first_player;
            return 1;
        }
        if(this.second_player.getActual_card().getCategory(category) > this.first_player.getActual_card().getCategory(category)){
            for(int j = 0; j < activeCards.size(); j++){
                this.second_player.getCards().add(activeCards.get(j));
            }
            activeCards.clear();
            active_player = second_player;
            return 2;
        }
        //TODO
        //if equal. return -> 3
        if(this.first_player.getActual_card().getCategory(category) == this.second_player.getActual_card().getCategory(category)){
            addActiveCards();
            throw new DrawException();
        }
        return 0;
    }

    private void addActiveCards(){
        updateActualCard();
        placeActualCardInActiveCards();
        deleteActualCardFromPlayersCards();
    }

    private void updateActualCard(){
        this.first_player.setActualCard(this.first_player.getCards().get(0));
        this.second_player.setActualCard(this.second_player.getCards().get(0));
    }

    private void placeActualCardInActiveCards() {
        activeCards.add(this.first_player.getActual_card());
        activeCards.add(this.second_player.getActual_card());
    }

    private void deleteActualCardFromPlayersCards() {
        this.first_player.getCards().remove(0);
        this.second_player.getCards().remove(0);
    }


    @Override
    public void giveUp(int player) {
    }

    @Override
    public int getActive_Player() {
        {
            if(this.active_player == this.first_player){
                return 1;
            }
            else return 2;
        }
    }

}

