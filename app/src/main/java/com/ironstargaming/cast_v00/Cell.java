package com.ironstargaming.cast_v00;
/**
 * Created by Admin on 11/16/2016.
 */

public class Cell {
    private String mName;
    private String mFaction;
    private boolean mLock;
    private boolean mPlayerAwareness;

    public Cell(String name, String faction, boolean lock, boolean playerAwareness){
        this.mName=name;
        this.mFaction=faction;
        this.mLock=lock;
        this.mPlayerAwareness=playerAwareness;
    }

    public String getName(){return mName;}
    public String getFaction(){return mFaction;}
    public boolean isLocked(){return mLock;}
    public boolean areAware(){return mPlayerAwareness;       }
}