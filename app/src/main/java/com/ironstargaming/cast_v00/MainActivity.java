package com.ironstargaming.cast_v00;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<CellContainer> {


    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int CELL_LOADER_ID = 1;
    private static final String CAST_REQUEST_URL = "http://flaskson.herokuapp.com/populate";

    private CellAdapter mAdapter;

    private TextView mEmptyStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        ListView centerCellListView = (ListView) findViewById(R.id.list);
        ListView childCellListView = (ListView) findViewById(R.id.list);

        mEmptyStateView = (TextView) findViewById(R.id.empty_view);

        centerCellListView.setEmptyView(mEmptyStateView);
        childCellListView.setEmptyView(mEmptyStateView);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();


            loaderManager.initLoader(CELL_LOADER_ID, null, this);
        } else {
            // Set progress bar to gone
            View loadingIndicator = (View) findViewById(R.id.load_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateView.setText("NO INTERNET");
        }

        mAdapter = new CellAdapter(this, new ArrayList<Cell>());
        childCellListView.setAdapter(mAdapter);
    }

    @Override
    public Loader<CellContainer> onCreateLoader(int id, Bundle bundle) {
        return new CellLoader(this, CAST_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<CellContainer> loader, CellContainer cellContainer) {

        View loadingIndicator = (View) findViewById(R.id.load_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateView.setText("No cells found?");

        mAdapter.clear();

        List<Cell> childCellList = cellContainer.getChildCellList();

        if (childCellList != null && !childCellList.isEmpty()) {
            mAdapter.addAll(childCellList);
        }


    }

    @Override
    public void onLoaderReset(Loader<CellContainer> loader) {
        mAdapter.clear();
    }
}
