/*
 * Copyright 2016 Juliane Lehmann <jl@lambdasoup.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.lambdasoup.quickfit;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lambdasoup.quickfit.databinding.ActivitySchedulesBinding;
import com.lambdasoup.quickfit.model.DayOfWeek;
import com.lambdasoup.quickfit.persist.QuickFitContract;
import com.lambdasoup.quickfit.viewmodel.WorkoutItem;

public class SchedulesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_WORKOUT_ID = "com.lambdasoup.quickfit_workoutId";
    private static final int LOADER_WORKOUT = 0;
    private static final int LOADER_SCHEDULES = 1;

    private ActivitySchedulesBinding workoutBinding;
    private SchedulesRecyclerViewAdapter schedulesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_schedules);

        setSupportActionBar(workoutBinding.toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        schedulesAdapter = new SchedulesRecyclerViewAdapter();
        workoutBinding.scheduleList.setAdapter(schedulesAdapter);
        workoutBinding.scheduleList.setEmptyView(workoutBinding.scheduleListEmpty);

        getLoaderManager().initLoader(LOADER_WORKOUT, null, this);
        getLoaderManager().initLoader(LOADER_SCHEDULES, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        long workoutId = getIntent().getLongExtra(EXTRA_WORKOUT_ID, -1); // TODO: use args bundle?
        switch (id) {
            case LOADER_WORKOUT:
                return new WorkoutLoader(this, workoutId);
            case LOADER_SCHEDULES:
                return new SchedulesLoader(this, workoutId);
        }
        throw new IllegalArgumentException("Not a loader id: " + id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_WORKOUT:
                bindHeader(data);
                return;
            case LOADER_SCHEDULES:
                schedulesAdapter.swapCursor(data);
                return;
        }
        throw new IllegalArgumentException("Not a loader id: " + loader.getId());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_WORKOUT:
                // nothing to do
                return;
            case LOADER_SCHEDULES:
                schedulesAdapter.swapCursor(null);
                return;
        }
        throw new IllegalArgumentException("Not a loader id: " + loader.getId());
    }

    private void bindHeader(Cursor cursor) {
        cursor.moveToFirst();

        long workoutId = cursor.getLong(cursor.getColumnIndex(QuickFitContract.WorkoutEntry.WORKOUT_ID));
        WorkoutItem workoutItem = new WorkoutItem.Builder(this, null)
                .withWorkoutId(workoutId)
                .withActivityTypeKey(cursor.getString(cursor.getColumnIndex(QuickFitContract.WorkoutEntry.ACTIVITY_TYPE)))
                .withDurationInMinutes(cursor.getInt(cursor.getColumnIndex(QuickFitContract.WorkoutEntry.DURATION_MINUTES)))
                .withCalories(cursor.getInt(cursor.getColumnIndex(QuickFitContract.WorkoutEntry.CALORIES)))
                .withLabel(cursor.getString(cursor.getColumnIndex(QuickFitContract.WorkoutEntry.LABEL)))
                .build(null);
        workoutBinding.setWorkout(workoutItem);

        workoutBinding.toolbarLayout.setTitle(workoutItem.activityTypeDisplayName);
    }
}
