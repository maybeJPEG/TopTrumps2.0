package trumps.Impl;

import trumps.*;
import trumps.Exceptions.*;

import java.util.ArrayList;
import java.util.Random;

public class TopTrumpsImpl implements TopTrumps {

    private Player first_player;
    private Player second_player;
    public Player active_player;
    private ArrayList<Card> activeCards = new ArrayList<Card>();
    private Card actual_card;

    public TopTrumpsImpl() throws NotExistentValueException, NoCardsException {
        this.first_player = new Player("alice");
        this.second_player = new Player("bob");
    }

    @Override
    public int start() throws GameExceptions, StatusException, WrongNameException, NotExistentPlayerException {
        determine_active_player();
        if(determine_active_player()==1){
            active_player = first_player;
            return 1;
        }
        else{
            active_player = second_player;
            return 2;
        }
    }

    private int determine_active_player() throws NotExistentPlayerException {
        int random = 1;
        Random r = new Random();
        random = r.nextInt(2);
        if(random>2 || random <0){
            throw new NotExistentPlayerException("Must return number 1 for first_player or number 2 for second_player");
        }
        return random;
    }

    @Override
    public int[] getFirstCard(int player) throws GameExceptions, StatusException, MatchException, NotYourTurnException, NotExistentPlayerException {
        Player playerOBJ = get_player_from_integer(player);
        add_active_cards();
        Card actual_card = playerOBJ.getActual_card();
        return actual_card.get_secure_list();
    }

    private Player get_player_from_integer(int player) throws NotExistentPlayerException{
        if(player == 1){
            return first_player;
        }
        if(player == 2){
            return second_player;
        }
        else {
            throw new NotExistentPlayerException("This Player does not exist");
        }
    }

    @Override
    public int compareCategory(int category, int player) throws GameExceptions, StatusException, NotExistentPlayerException, NotYourTurnException, CategoryDoesNotExistException, DrawException {
       validate_actual_player(player);
       validate_category(category);
       compare_cards(category);
        return 0;
    }

    private void validate_actual_player(int player) throws NotExistentPlayerException, NotYourTurnException {
        Player playerOBJ = get_player_from_integer(player);
        if(playerOBJ != this.active_player){
            throw new NotYourTurnException();
        }
    }

    private void validate_category(int category) throws CategoryDoesNotExistException {
        int categoryOBJ = category;
        if(categoryOBJ > 5 || categoryOBJ < 1){
            throw new CategoryDoesNotExistException();
        }
    }

    private int compare_cards(int category) throws CategoryDoesNotExistException, DrawException {

        if(this.first_player.getActual_card().getCategory(category) > this.second_player.getActual_card().getCategory(category)){
            for(int i = 0; i < activeCards.size(); i++ ){
                this.first_player.getCards().add(activeCards.get(i));
                activeCards.remove(i);
            }
            first_player = active_player;
            return 1;
        }
        if(this.second_player.getActual_card().getCategory(category) > this.first_player.getActual_card().getCategory(category)){
            for(int j = 0; j < activeCards.size(); j++){
                this.second_player.getCards().add(activeCards.get(j));
                activeCards.remove(j);
            }
            second_player = active_player;
            return 2;
        }
        //TODO
        //if equal. return -> 3
        if(this.first_player.getActual_card().getCategory(category) == this.second_player.getActual_card().getCategory(category)){
            add_active_cards();
            throw new DrawException();
        }
        return 0;
    }
    private void add_active_cards(){
        delete_actual_card_from_players_cards();
        update_actual_card();
        place_actual_card_in_activeCards();
    }

    private void delete_actual_card_from_players_cards() {
        this.first_player.getCards().remove(0);
        this.second_player.getCards().remove(0);
    }

    private void update_actual_card(){
        this.actual_card = this.first_player.getCards().get(0);
        this.actual_card = this.second_player.getCards().get(0);
    }

    private void place_actual_card_in_activeCards() {
        activeCards.add(this.first_player.getActual_card());
        activeCards.add(this.second_player.getActual_card());
    }

    @Override
    public void giveUp(int player) {
    }
}

