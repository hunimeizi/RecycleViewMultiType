package com.recycleview.multitype.item;

/**
 * Created by Duo Nuo on 2017/4/12.
 */
public class GuessGrid {
    public GuessGrid(int position) {
        this.position = position;
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}