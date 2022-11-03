package com.example.mynasatask.screens.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynasatask.model.MarsListEntry
import com.example.mynasatask.repo.MarsRepo
import com.example.mynasatask.ui.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: MarsRepo
) : ViewModel() {

    var marsList = mutableStateOf<List<MarsListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cacheMarsLists = listOf<MarsListEntry>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        loadMarsList()
    }

    fun searchMarsPhotoLis(query: String) {
        val listToSearch = if(isSearchStarting) {
            marsList.value
        } else {
            cacheMarsLists
        }
        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()) {
                marsList.value = cacheMarsLists
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.full_name.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query.trim()
            }
            if(isSearchStarting) {
                cacheMarsLists = marsList.value
                isSearchStarting = false
            }
            marsList.value = results
            isSearching.value = true
        }
    }

    fun loadMarsList() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getMarsList()
            when (result) {
                is Resource.Success -> {
                    val marsListEntries = result.data?.photos?.mapIndexed { index, photo ->
                        MarsListEntry(
                            img_src = photo.img_src,
                            full_name = photo.camera.name,
                            number = photo.id,
                            rover_name = photo.rover.name,
                            camera_name = photo.camera.full_name,
                            launch_date =  photo.rover.launch_date,
                            landing_date = photo.rover.landing_date,
                            earth_date = photo.earth_date
                        )
                    }
                    Log.d("Srezzz", "marslistentries: $marsListEntries")
                    isLoading.value = false
                    if (marsListEntries != null) {
                        marsList.value = marsListEntries
                    }
                }
                is Resource.Error -> {
                    Log.d("Sreezz", "load Error")
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }
}