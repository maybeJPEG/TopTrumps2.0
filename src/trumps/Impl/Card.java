package trumps.Impl;

import trumps.Exceptions.CategoryDoesNotExistException;
import trumps.Exceptions.NotExistentValueException;

import java.util.Random;

public class Card {

    private int id;
    private Player owner;
    private int Category1;
    private int Category2;
    private int Category3;
    private int Category4;

    public Card(int id, Player owner) throws NotExistentValueException {
        this.id = id;
        this.owner = owner;
        this.Category1 = randomNumber();
        this.Category2 = randomNumber();
        this.Category3 = randomNumber();
        this.Category4 = randomNumber();
    }

    private int randomNumber() throws NotExistentValueException {
        int random = 1;
        Random r = new Random();
        random = r.nextInt(10);
        if(random>10 && random <0){
            throw new NotExistentValueException("Category must be a value between 1 and 10");
        }
        return random;
    }

    public int[] getSecureList(){
        return new int[]{this.Category1, this.Category2, this.Category3, this.Category4};
    }

    public int getCategory(int category) throws CategoryDoesNotExistException {
        switch (category){
            case 1:
                return this.Category1;
            case 2:
                return this.Category2;
            case 3:
                return this.Category3;
            case 4:
                return this.Category4;
            default:
                throw new CategoryDoesNotExistException();
        }

    }

}
