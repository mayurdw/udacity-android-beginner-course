/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.*
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {
        private var sleepTrackerJob = Job()
        override fun onCleared() {
                super.onCleared()
                this.sleepTrackerJob.cancel()
        }

        private val scope = CoroutineScope( Dispatchers.Main + sleepTrackerJob )
        private var tonight = MutableLiveData<SleepNight?>()
        private val allNights = database.getAllNights()
        val nightString = Transformations.map(allNights) { nights ->
                formatNights(nights, application.resources)
        }
        private var _navigateToSleepQuality = MutableLiveData<SleepNight>()
        val navigateToSleepQuality: LiveData<SleepNight>
                get() = _navigateToSleepQuality

        val startButtonVisible = Transformations.map(tonight) {
                null == it
        }
        val stopButtonVisible = Transformations.map(tonight) {
                null != it
        }
        val clearButtonVisible = Transformations.map(allNights) {
                it?.isNotEmpty()
        }

        private var _showSnackbarEvent = MutableLiveData<Boolean>()

        val showSnackBarEvent: LiveData<Boolean>
                get() = _showSnackbarEvent

        fun doneShowingSnackbar() {
                _showSnackbarEvent.value = false
        }


        fun doneNavigating(){
                _navigateToSleepQuality.value = null
        }

        init {
            initializeTonight()
        }

        private fun initializeTonight() {
                scope.launch {
                        tonight.value = getTonightFromDatabase()
                }
        }

        private suspend fun getTonightFromDatabase(): SleepNight? {
                return withContext(Dispatchers.IO){
                        var night = database.getTonight()
                        if( night?.startTimeMilliSec != night?.endTimeMilliSec ){
                                night = null
                        }
                        night
                }
        }

        fun onStartTracking() {
                scope.launch {
                        val newNight = SleepNight()
                        insert( newNight )
                        tonight.value = newNight
                }
        }

        private suspend fun insert(newNight: SleepNight) {
                return withContext(Dispatchers.IO){
                        database.insert( newNight )
                }
        }

        fun onStopTracking(){
                scope.launch {
                        val oldNight = tonight.value ?: return@launch
                        oldNight.endTimeMilliSec = System.currentTimeMillis()
                        update( oldNight )
                        _navigateToSleepQuality.value = oldNight
                }
        }

        private suspend fun update(oldNight: SleepNight) {
                withContext(Dispatchers.IO){
                        database.update(oldNight)
                }
        }

        fun onClear(){
                scope.launch {
                        clearAll()
                        tonight.value = null
                        _showSnackbarEvent.value = true
                }
        }

        private suspend fun clearAll() {
                withContext(Dispatchers.IO){
                        database.clear()
                }
        }
}

