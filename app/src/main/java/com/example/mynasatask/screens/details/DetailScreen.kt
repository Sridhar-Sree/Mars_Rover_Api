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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mynasatask.model.Photo
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

    viewModel.loadmarsimage(photid = marsid)

    val marsdetails by remember { viewModel.marsphoto }


    Log.d("Srezzz", "in detail screen: ${marsdetails}")
    Log.d("Srezzz", "in detail screen: ${marsid}")

    Box(
        modifier = Modifier
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
            marslist = marsdetails,
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
    marslist: List<Photo>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {

    Column {
        if (marslist.isEmpty()) {
            CircularProgressIndicator()
        } else {
            Log.d("Detailed Function", "DetailScreenStateWrapper: $marslist")
            MarsDetailSection(marsInfo = marslist[0])

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
    ) {
        Text(
            text = "#${marsInfo.id} ${marsInfo.rover.name.capitalize(Locale.ROOT)}",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface
        )
        AsyncImage(model = marsInfo.img_src, contentDescription = "Detail Image")
        Text(
            text = "Lanched Date: ${marsInfo.rover.launch_date}",
            style = MaterialTheme.typography.caption
        )
        Text(
            text = "Rover Name: ${marsInfo.rover.name}",
            fontSize = 20.0.sp
        )
        Text(
            text = "Camera Name: ${marsInfo.camera.name}",
            fontSize = 20.0.sp,
        )

    }
}
