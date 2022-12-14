package com.example.mynasatask.screens.details

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mynasatask.model.MarsListEntry
import com.example.mynasatask.model.Photo
import com.example.mynasatask.repo.MarsRepo
import com.example.mynasatask.ui.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class marsDetailViewModel @Inject constructor(
    private val repository: MarsRepo
) : ViewModel() {


    var marsphoto = mutableStateOf<List<Photo>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    fun loadmarsimage(photid: Int) {
        viewModelScope.launch {
            isLoading.value = true

            var result = repository.getMarsList()

            Log.d("MarsTaskLogd", "loaddetails: ${result.data}")

            when (result) {
                is Resource.Success -> {
                    val details = result.data?.photos?.filter { photo -> photo.id == photid }
                    Log.d("MarsTaskLogdfilter", "filter: ${details}")

                    if (details != null) {
                        details.mapIndexed { index, photo ->
                            Photo(
                                camera = photo.camera,
                                img_src = photo.img_src,
                                rover = photo.rover,
                                sol = photo.sol,
                                id = photo.id,
                                earth_date = photo.earth_date
                            )
                        }
                    }

                    if (details != null) {
                        marsphoto.value = details
                    }
                    Log.d("Sreezznn viewmodel last", "${marsphoto.value}")

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
