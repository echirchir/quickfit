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

import android.provider.BaseColumns;

/**
 * Created by jl on 06.01.16.
 */
public class QuickFitContract {
    abstract static class WorkoutEntry implements BaseColumns {
        static final String TABLE_NAME = "workout";

        static final String ACTIVITY_TYPE = "activity_type";
        static final String DURATION_MINUTES = "duration_minutes";

        static final String[] COLUMNS = {_ID, ACTIVITY_TYPE, DURATION_MINUTES};

        static final String[] CREATE_STATEMENTS = {
                "CREATE TABLE " + TABLE_NAME + " ( " +
                        _ID + " INTEGER PRIMARY KEY, " +
                        ACTIVITY_TYPE + " TEXT NOT NULL, " +
                        DURATION_MINUTES + " INTEGER NOT NULL " +
                        ")"
        };
    }

    abstract static class SessionEntry implements BaseColumns {
        static final String TABLE_NAME = "session";

        static final String ACTIVITY_TYPE = "activity_type";
        static final String START_TIME = "start_time";
        static final String END_TIME = "end_time";
        static final String STATUS = "status";

        static final String[] COLUMNS = {_ID, ACTIVITY_TYPE, START_TIME, END_TIME, STATUS};

        static final String[] CREATE_STATEMENTS = {
            "CREATE TABLE " + TABLE_NAME + " ( " +
                    _ID + " INTEGER PRIMARY KEY, " +
                    ACTIVITY_TYPE + " TEXT NOT NULL, " +
                    START_TIME + " INTEGER NOT NULL, " +
                    END_TIME + " INTEGER NOT NULL, " +
                    STATUS + " TEXT NOT NULL " +
                    ")"
        };

        enum SessionStatus {
            NEW, SYNCED
        }
    }
}
