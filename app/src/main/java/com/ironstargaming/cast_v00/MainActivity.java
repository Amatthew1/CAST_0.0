package com.ironstargaming.cast_v00;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
                    startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  ListView centerCellListView = (ListView) findViewById(R.id.list);
        ListView childCellListView = (ListView) findViewById(R.id.child_cell_view);

        mEmptyStateView = (TextView) findViewById(R.id.empty_view);

 //       centerCellListView.setEmptyView(mEmptyStateView);
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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String numScopes = sharedPreferences.getString(
                        getString(R.string.settings_num_displayed_scopes_key),
                        getString(R.string.settings_num_displayed_scopes_default));
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri baseUri = Uri.parse(CAST_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("num_scopes", numScopes);
        //uriBuilder.appendQueryParameter("asST", asST);
        uriBuilder.appendQueryParameter("orderby", orderBy);






        return new CellLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<CellContainer> loader, CellContainer cellContainer) {

        View loadingIndicator = (View) findViewById(R.id.load_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateView.setText("No cells found?");

        mAdapter.clear();
///////////////////////////////////////////////////////////////////////////////////////
        Cell centerCell = cellContainer.getCenterCell();
        TextView centerCellNameTextView = (TextView) findViewById(R.id.center_cell_name);
        String centerCellName = centerCell.getName();
        centerCellNameTextView.setText(centerCellName);

        TextView centerCellFactionTextView = (TextView) findViewById(R.id.center_cell_faction);
        String centerCellFaction = centerCell.getFaction();
        centerCellFactionTextView.setText(centerCellFaction);

        TextView centerCellLockedTextView = (TextView) findViewById(R.id.center_cell_lock);
        String centerCellLock="Locked";
        if(!centerCell.isLocked()){centerCellLock="Unlocked";}
        centerCellLockedTextView.setText(centerCellLock);





        /////////////////////////////////////////////////////////////////////////////
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
