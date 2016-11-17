package com.ironstargaming.cast_v00;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Admin on 11/16/2016.
 */

public class CellLoader extends AsyncTaskLoader<List<Cell>> {

    private static final String LOG_TAG = CellLoader.class.getName();

    private String mUrl;

    public CellLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Cell> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Cell> cells = QueryUtils.fetchCellData(mUrl);
        return cells;
    }

}
