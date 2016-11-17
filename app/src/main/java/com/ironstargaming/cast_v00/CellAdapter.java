package com.ironstargaming.cast_v00;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Admin on 11/16/2016.
 */

public class CellAdapter extends ArrayAdapter<Cell> {

    public CellAdapter(Activity context, ArrayList<Cell> cell) {
        super(context, 0, cell);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, parent, false);
        }


        final Cell currentCell = getItem(position);

        TextView cellNameTextView = (TextView) listItemView.findViewById(R.id.cell_name);
        String currentCellName = currentCell.getName();
        cellNameTextView.setText(currentCellName);

        TextView cellFactionTextView = (TextView) listItemView.findViewById(R.id.cell_faction);
        String currentCellFaction = currentCell.getFaction();
        cellFactionTextView.setText(currentCellFaction);

        TextView cellLockTextView = (TextView) listItemView.findViewById(R.id.cell_lock);
        String currentCellLock = String.valueOf(currentCell.isLocked());
        cellLockTextView.setText(currentCellLock);

        TextView cellPATextView = (TextView) listItemView.findViewById(R.id.cell_PA);
        String currentCellPA = String.valueOf(currentCell.areAware());
        cellPATextView.setText(currentCellPA);


        return listItemView;
    }
}
