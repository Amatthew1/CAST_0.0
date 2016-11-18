package com.ironstargaming.cast_v00;

import java.util.List;

/**
 * Created by Admin on 11/17/2016.
 */

public class CellContainer {

    private Cell mCenterCell;
    private List<Cell> mChildCells;

    public CellContainer(Cell centerCell, List<Cell> childCells) {

        this.mCenterCell = centerCell;
        this.mChildCells = childCells;

    }
    public Cell getCenterCell(){
        return mCenterCell;
    }

    public void addChild(Cell child) {
        this.mChildCells.add(child);
    }

    public Cell getChild(int i) {
        return mChildCells.get(i);
    }

    public List<Cell> getChildCellList(){
        return  mChildCells;
    }
}





