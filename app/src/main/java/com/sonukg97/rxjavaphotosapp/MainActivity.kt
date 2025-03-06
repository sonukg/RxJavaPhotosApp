package com.sonukg97.rxjavaphotosapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.sonukg97.rxjavaphotosapp.data.remote.Photo
import com.sonukg97.rxjavaphotosapp.ui.theme.RxJavaPhotosAppTheme
import com.sonukg97.rxjavaphotosapp.view.PhotoViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RxJavaPhotosAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val viewModel: PhotoViewModel = koinViewModel()
                    PhotoListScreen(viewModel)
                }
            }
        }
    }
}

/*@Composable
fun PhotoListScreen(viewModel: PhotoViewModel) {
    val photos = viewModel.photos.collectAsLazyPagingItems()

    LazyColumn {
        items(photos.itemCount) { index ->
            photos[index]?.let { photo ->
                PhotoItem(photo)
            }
        }
    }
}*/


/*@Composable
fun PhotoListScreen(viewModel: PhotoViewModel) {
    val photos = viewModel.photos.collectAsLazyPagingItems()
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    val isRefreshing = rememberSwipeRefreshState(isRefreshing = isLoading)

    Box(modifier = Modifier.fillMaxSize()) {
        SwipeRefresh(
            state = isRefreshing,
            onRefresh = { viewModel.fetchPhotos(1, 20) }
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn {
                    items(photos.itemCount) { index ->
                        photos[index]?.let { photo ->
                            PhotoItem(photo)
                        }
                    }
                }
            }
        }

        // Show error message
        if (!errorMessage.isNullOrEmpty()) {
            LaunchedEffect(errorMessage) {
                delay(3000) // Show error for 3 seconds
                viewModel.errorMessage.value = null
            }
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(text = errorMessage)
            }
        }
    }
}*/


@Composable
fun PhotoListScreen(viewModel: PhotoViewModel) {
    val photos = viewModel.photos.collectAsLazyPagingItems()
    val isRefreshing = rememberSwipeRefreshState(isRefreshing = photos.loadState.refresh is LoadState.Loading)

    SwipeRefresh(
        state = isRefreshing,
        onRefresh = { photos.refresh() }
    ) {
        when (val refreshState = photos.loadState.refresh) {
            is LoadState.Loading -> {
                // Show loading indicator for the first load
                CircularProgressIndicator(modifier = Modifier.fillMaxSize().padding(16.dp))
            }
            is LoadState.Error -> {
                // Show error message
                Text(
                    text = "Error: ${refreshState.error.localizedMessage}",
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    color = Color.Red
                )
            }
            else -> {
                LazyColumn {
                    items(photos.itemCount) { index ->
                        photos[index]?.let { photo ->
                            PhotoItem(photo)
                        }
                    }


                    if (photos.loadState.append is LoadState.Loading) {
                        item {
                            CircularProgressIndicator(modifier = Modifier.fillMaxWidth().padding(16.dp))
                        }
                    }

                    // Show error message at the bottom if loading more items fails
                    if (photos.loadState.append is LoadState.Error) {
                        item {
                            Text(
                                text = "Error loading more items",
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                color = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoItem(photo: Photo) {
    Surface(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(model = photo.thumbnailUrl),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Text(text = photo.title, style = MaterialTheme.typography.titleMedium)
        }
    }
}