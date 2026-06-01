package main.java.de.game.entity.items;

public class Blitz extends Item{

    public Blitz(int x, int y){
        super(x,y);
        this.speedboost = 10;
        this.shootboost = 10;
        this.verwundbarkeit = false;
        this.dauer = 30;
    }

}
