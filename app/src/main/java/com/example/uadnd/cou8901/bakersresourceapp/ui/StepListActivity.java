package com.example.uadnd.cou8901.bakersresourceapp.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.example.uadnd.cou8901.bakersresourceapp.R;

import com.example.uadnd.cou8901.bakersresourceapp.cp.BakersResourceContract;
import com.example.uadnd.cou8901.bakersresourceapp.db.Step;

import timber.log.Timber;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor>{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private static final int RECIPE_STEP_LOADER_ID = 1;
    private boolean mTwoPane;
    private int recipeId = 1;
    Context mContext;
    private StepCursorAdapterMD mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        mContext = this;
        Timber.plant(new Timber.DebugTree());


        Intent intent = getIntent();
        if(intent.hasExtra("RECIPE_ID")) {
            recipeId = intent.getIntExtra("RECIPE_ID", 1);
        }


        mAdapter = new StepCursorAdapterMD(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);


        }

        View recyclerView = findViewById(R.id.step_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        getSupportLoaderManager().initLoader(RECIPE_STEP_LOADER_ID, null, StepListActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the ingredients menu
        getMenuInflater().inflate(R.menu.menu_ingredients, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_ingredients) {
            //Navigate to Ingredients Activity
            Intent recipeIngredientIntent = new Intent(mContext, RecipeIngredientActivity.class );
            recipeIngredientIntent.putExtra("RECIPE_ID", recipeId);
            Bundle bundle = new Bundle();
            bundle.putString("RECIPE_ID", String.valueOf(recipeId) );
            startActivity(recipeIngredientIntent);


        } else  if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mStepData = null;

            @Override
            protected void onStartLoading() {
                Timber.d("onStartLoading");
                if(mStepData != null) {
                    deliverResult(mStepData);
                } else {
                    //Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                Timber.d("loadInBackground");

                try {
                    return getContentResolver().query(BakersResourceContract.Steps.STEPS_URI,
                            null,
                            " recipe_id = " + recipeId + " ", //null,
                            null,
                            BakersResourceContract.Steps.COLUMN_STEP_ID);  // order steps by step_id


                }catch (Exception e) {
                    Timber.d(e);
                    return null;
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                Timber.d("deliverResult=" + data.getCount());
                mStepData = data;
                super.deliverResult(data);

                // Test Code  just loop through curson and print result
                int recipeIdIndex = data.getColumnIndex(BakersResourceContract.Steps.COLUMN_RECIPE_ID);
                int stepIdIndex = data.getColumnIndex(BakersResourceContract.Steps.COLUMN_STEP_ID);
                int shortDescriptionIndex = data.getColumnIndex(BakersResourceContract.Steps.COLUMN_SHORT_DESCRIPTION);
                int descriptionIndex = data.getColumnIndex(BakersResourceContract.Steps.COLUMN_DESCRIPTION);
                data.moveToFirst();

//                while(!data.isAfterLast()) {
//                    Timber.d(String.valueOf(data.getInt(recipeIdIndex)) + ":" + data.getString(stepIdIndex) + ":" +
//                            data.getString(shortDescriptionIndex) + ":" + data.getString(descriptionIndex));
//                    data.moveToNext();
//                }



            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Timber.d("onLoadFinished");
        if(data == null) {
            Timber.d("onLoadFinished data is null");
        }
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Timber.d("onLoaderReset");
    }




    public class StepCursorAdapterMD  extends RecyclerView.Adapter<StepCursorAdapterMD.StepViewHolder>{
        private Cursor mCursor;
        private Context mContext;

//        @Override
//        public StepCursorAdapterMD.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.step_list_content, parent, false);
//            return new SimpleItemRecyclerViewAdapter.ViewHolder(view);
//        }

        public StepCursorAdapterMD(Context mContext) {
            Timber.d("StepCursorAdapter");
            this.mContext = mContext;
        }

        @Override
        public StepCursorAdapterMD.StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Timber.d("onCreateViewHolder");
//            View view = LayoutInflater.from(mContext)
//                    .inflate(R.layout.step_layout, parent, false);
//            return new StepCursorAdapterMD.StepViewHolder(view);
                        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_content, parent, false);
            return new StepCursorAdapterMD.StepViewHolder(view);

        }

        @Override
        public void onBindViewHolder(final StepCursorAdapterMD.StepViewHolder holder, int position) {
            Timber.d("onBindViewHolder");
            int recipeIdIndex = mCursor.getColumnIndex(BakersResourceContract.Steps.COLUMN_RECIPE_ID);
            final int stepIdIndex = mCursor.getColumnIndex(BakersResourceContract.Steps.COLUMN_STEP_ID);
            int shortDescriptionIndex = mCursor.getColumnIndex(BakersResourceContract.Steps.COLUMN_SHORT_DESCRIPTION);

            int descriptionIndex = mCursor.getColumnIndex(BakersResourceContract.Steps.COLUMN_DESCRIPTION);
            int videoURLIndex = mCursor.getColumnIndex(BakersResourceContract.Steps.COLUMN_VIDEO_URL);
            int thumbnailURLIndex = mCursor.getColumnIndex(BakersResourceContract.Steps.COLUMN_THUMB_NAIL_URL);

            int idIndex = mCursor.getColumnIndex(BakersResourceContract.Steps._ID);

            mCursor.moveToPosition(position);
            int recipeId = mCursor.getInt(recipeIdIndex);
            final String stepId = mCursor.getString(stepIdIndex);
            final String shortDescription = mCursor.getString(shortDescriptionIndex);
            final String description = mCursor.getString(descriptionIndex);
            final String videoURL = mCursor.getString(videoURLIndex);
            final String thumbnailURL = mCursor.getString(thumbnailURLIndex);
            final String id = mCursor.getString(idIndex); // URL
            Step step = new Step();
            step.setRecipe_id(recipeId);
            step.setStep_id(Integer.parseInt(stepId));
            step.setId(Integer.parseInt(stepId));
            step.setShortDescription(shortDescription);
            step.setDescription(description);
            step.setVideoURL(videoURL);
            step.setThumbnailURL(thumbnailURL);
            holder.mItem = step;
            Timber.d("holder.mItem.getStep_id() : " + holder.mItem.getStep_id());
            //holder.mStepView.setText(String.valueOf(stepId + ". " + shortDescription));
            holder.mIdView.setText(String.valueOf(stepId));
            holder.mContentView.setText(String.valueOf(shortDescription));
            Timber.d("before:setOnClickListener");
            //holder.mStepView.setOnClickListener(new View.OnClickListener() {
            holder.mView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Timber.d("onClick");
                    //Toast.makeText(mContext, "On Click Listener", Toast.LENGTH_LONG).show();
                    if(mTwoPane) {
                        Bundle arguments = new Bundle();
                        Timber.d("onBindViewHolder: TwoPane:" + String.valueOf(holder.mItem.getId()));
                        Timber.d(holder.mItem.getShortDescription());
                        Timber.d(holder.mItem.getDescription());

                        //arguments.putString(StepDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.getId()));
                        arguments.putString(StepDetailFragment.STEP_ID, String.valueOf(holder.mItem.getStep_id()));
                        arguments.putString(StepDetailFragment.STEP_SHORT_DESCRIPTION, holder.mItem.getShortDescription());
                        arguments.putString(StepDetailFragment.STEP_DESCRIPTION, holder.mItem.getDescription());
                        arguments.putString(StepDetailFragment.STEP_VEDEO_URL, holder.mItem.getVideoURL());
                        arguments.putString(StepDetailFragment.STEP_THUMB_NAIL_URL, holder.mItem.getThumbnailURL());
                        StepDetailFragment fragment = new StepDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.step_detail_container, fragment)
                                .commit();

                    }else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, StepDetailActivity.class);
                        //intent.putExtra(StepDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        intent.putExtra(StepDetailFragment.STEP_ID, String.valueOf(holder.mItem.getStep_id()));
                        //arguments.putString(StepDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.getId()));
                        //intent.putExtra(StepDetailFragment.STEP_ID, String.valueOf(holder.mItem.getStep_id()));
                        intent.putExtra(StepDetailFragment.STEP_SHORT_DESCRIPTION, holder.mItem.getShortDescription());
                        intent.putExtra(StepDetailFragment.STEP_DESCRIPTION, holder.mItem.getDescription());
                        intent.putExtra(StepDetailFragment.STEP_VEDEO_URL, holder.mItem.getVideoURL());
                        intent.putExtra(StepDetailFragment.STEP_THUMB_NAIL_URL, holder.mItem.getThumbnailURL());

                        context.startActivity(intent);

                    }
//                    Intent recipeStepDetailIntent = new Intent(mContext, RecipeStepDetailActivity.class );
//                    recipeStepDetailIntent.putExtra("_ID", id);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("_ID", id);
//                    bundle.putString("STEP_ID", stepId );
//                    bundle.putString("DESCRIPTION", description);
//                    bundle.putString("SHORT_DESCRIPTION", shortDescription);
//                    bundle.putString("VIDEO_URL", videoURL);
//                    bundle.putString("THUMB_NAIL_URL", thumbnailURL);
//                    recipeStepDetailIntent.putExtra("STEP_BUNDLE", bundle);

                    //startActivity(mContext, recipeStepDetailIntent, bundle);  // on click start detail activity
                }
            });


        }

        @Override
        public int getItemCount() {
            Timber.d("getItemCount");
            if (mCursor == null) {
                Timber.d("getItemCount : mCursor == null");
                return 0;
            }
            Timber.d("getItemCount=" + mCursor.getCount());
            return mCursor.getCount();
        }

        public Cursor swapCursor(Cursor cursor) {
            Timber.d("swapCursor");
            if(mCursor == cursor) {
                return null;  // No change in cursor
            }
            Timber.d("swapped");
            Cursor tempCursor = mCursor;
            this.mCursor = cursor;

            if(cursor != null) {
                this.notifyDataSetChanged();
            }
            return tempCursor;
        }

        public class StepViewHolder extends RecyclerView.ViewHolder {
            //@BindView(R.id.tv_step) TextView mStepView;
            public final View mView;
            public final TextView mIdView;  // Step id
            public final TextView mContentView; //Step name
            public Step mItem;

            public StepViewHolder(View view) {
                super(view);
                Timber.d("StepViewHolder");
                //ButterKnife.bind(this, view);
                //super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);

            }
        }
    }





/*
    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(StepDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        StepDetailFragment fragment = new StepDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.step_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, StepDetailActivity.class);
                        intent.putExtra(StepDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
    */

//    public class StepContent {
//        public ArrayList<Step> getSteps(int recipeId){
//
//
//        }
//
//    }

}
