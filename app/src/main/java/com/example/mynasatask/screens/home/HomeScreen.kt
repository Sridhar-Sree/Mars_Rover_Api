package com.example.mynasatask.screens.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.mynasatask.model.MarsListEntry

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Mars Rover API",
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            SearchBar(
                hint = "Search Here...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                viewModel.searchMarsPhotoLis(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            MarsList(navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.hasFocus && text.isEmpty()
                }
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun MarsList(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val marsList by remember { viewModel.marsList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val isSearching by remember { viewModel.isSearching }


    Log.d("Loading Checking", "MarsList: $isLoading")
    Log.d("MarsListSize", "MarsListSize: ${marsList.size}")
    Log.d("MarsList", "MarsListTotal: ${marsList}")

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = marsList.size
        items(itemCount) {
            if (it >= itemCount - 1 && !endReached && !isLoading && !isSearching) {
                LaunchedEffect(key1 = true) {
                    viewModel.loadMarsList()
                }
            }

            marsCard(rowIndex = it, entries = marsList, navController = navController)

        }
    }
    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            Column() {
                CircularProgressIndicator(color = MaterialTheme.colors.primary)
                Text(text = "Loading")
            }
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadMarsList()
            }
        }
    }

}

@Composable
fun marsCard(
    rowIndex: Int,
    entries: List<MarsListEntry>,
    navController: NavController
) {
    Column {
        MarsCard(
            entry = entries[rowIndex],
            navController = navController,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun MarsCard(
    entry: MarsListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    var expanded by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate(
                    "mars_detail_screen/${entry.number}"
                )
            },
        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        elevation = 5.0.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Surface(
                modifier = Modifier
                    .padding(12.dp)
                    .size(90.dp),
                shape = RectangleShape,
                elevation = 5.dp
            ) {
                val painter = rememberAsyncImagePainter(
                    model = entry.img_src,
                    placeholder = rememberVectorPainter(Icons.Default.Refresh)
                )


                Image(painter = painter, contentDescription = "Thumbnail")
            }
            Column() {
                Text(text = entry.camera_name, style = MaterialTheme.typography.h6)
                Text(
                    text = " Rover Name: ${entry.rover_name}",
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = " Earth Date : ${entry.earth_date}",
                    style = MaterialTheme.typography.caption
                )

                AnimatedVisibility(visible = expanded) {
                    Column {
                        Text(buildAnnotatedString {

                            withStyle(style = SpanStyle(color = Color.DarkGray, fontSize = 13.sp)) {
                                //append("Rover Details: ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color.DarkGray,
                                    fontSize = 13.sp
                                )
                            ) {
                                //append(entry.rover_name)
                            }
                        }, modifier = Modifier.padding(0.dp))

                        Divider(modifier = Modifier.padding(6.0.dp))
                        Text(
                            text = "Landing Date: ${entry.landing_date}",
                            style = MaterialTheme.typography.caption
                        )
                        Text(
                            text = "Launch Date: ${entry.launch_date}",
                            style = MaterialTheme.typography.caption
                        )
                        Text(
                            text = "Camera Name: ${entry.full_name}",
                            style = MaterialTheme.typography.caption
                        )

                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Arrow Down",
                    modifier = Modifier
                        .clickable {
                            expanded = !expanded
                        }
                        .padding(start = 50.0.dp)

                )

            }

        }
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}