package com.ironstargaming.cast_v00;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Admin on 11/16/2016.
 */

public class CellLoader extends AsyncTaskLoader<CellContainer> {

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
    public CellContainer loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        CellContainer cellContainer = QueryUtils.fetchCellData(mUrl);
        return cellContainer;
    }

}
