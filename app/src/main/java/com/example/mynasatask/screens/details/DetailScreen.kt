package com.example.mynasatask.screens.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mynasatask.model.Photo
import com.example.mynasatask.ui.Resource
import java.util.*

@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun MarsDetailScreen(
    marsid: Int,
    navController: NavController,
    topPadding: Dp = 20.dp,
    marsImageSize: Dp = 200.dp,
    viewModel: marsDetailViewModel = hiltViewModel()
) {

    val marsInfo = produceState<Resource<Photo>>(initialValue = Resource.Loading()) {

        Log.d("DetailScreen", "MarsDetailScreen: $value")

    }.value
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(bottom = 16.dp)
    ) {
        DetailScreenTop(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )
        DetailScreenStateWrapper(
            marsInfo = marsInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + marsImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = topPadding + marsImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        )
        Box(contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()) {
            if(marsInfo is Resource.Success) {
                Log.d("MarsDetailScreen", "MarsDetailScreen: ${marsInfo.data}")

                AsyncImage(model = marsInfo.data?.img_src, contentDescription ="Image" )
            }
        }
    }
}
//}

@Composable
fun DetailScreenTop(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun DetailScreenStateWrapper(
    marsInfo: Resource<Photo>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when(marsInfo) {
        is Resource.Success -> {
            MarsDetailSection(
                marsInfo = marsInfo.data!!,
                modifier = modifier
                    .offset(y = (-20).dp)
            )
        }
        is Resource.Error -> {
            Text(
                text = marsInfo.message!!,
                color = Color.Red,
                modifier = modifier
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )
        }
    }
}

@Composable
fun MarsDetailSection(
    marsInfo: Photo,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${marsInfo.id} ${marsInfo.rover.name.capitalize(Locale.ROOT)}",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface
        )
    }
}
