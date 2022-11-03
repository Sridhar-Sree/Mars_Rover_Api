package com.example.mynasatask.screens.details

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        loadmarsimage(photid = 1)
    }

    private fun loadmarsimage(photid: Int) {
        viewModelScope.launch {
            isLoading.value = true

            var result = repository.getMarsPhotoInfo(photid)
            Log.d("Srezzz", "loaddetails: ${result.data}")

            when (result) {
                is Resource.Success -> {

                    val phots = result.data?.id.toString().mapIndexed { index, photo ->
//                        Photo(
//                            camera = photscamera,
//                            earth_date = phots.earth_date,
//                            id = photo.id,
//                            img_src = phots.img_src,
//                            rover = phots.rover,
//                            sol = phots.sol
//                        )
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
