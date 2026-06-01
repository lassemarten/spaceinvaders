package main.java.de.game.entity.items;

import main.java.de.game.entity.Entity;

public abstract class Item extends Entity {

    protected int speedboost;
    protected int shootboost;
    protected boolean verwundbarkeit;
    protected int dauer;

    public Item(int x, int y){
        super(x, y, 10 ,10);
    }
}
