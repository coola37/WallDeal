package com.zeroone.wallpaperdeal.ui.screens.home

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.ui.screens.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.screens.ui.TopAppbarText
import com.zeroone.wallpaperdeal.ui.theme.TopAppBarColor
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.ui.theme.Purple40
import com.zeroone.wallpaperdeal.ui.theme.ThemeGray


@Composable
fun HomeScreen(navController: NavController, auth: FirebaseAuth) {
    val scrollState = rememberLazyListState()
    var isTopAppBarVisible by remember { mutableStateOf(true) }
    var isTop10ListVisible by remember { mutableStateOf(true) }

    Scaffold(
        backgroundColor = Color.Black,
        topBar = {
            if (isTopAppBarVisible) {
                TopAppBar(
                    backgroundColor = TopAppBarColor,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.walldeallogo),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 110.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(0, navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TopAppbarText(navController = navController)
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 70.dp)) {
                val start = Offset(0f, size.height / 2)
                val end = Offset(250f, size.height / 2)
                drawLine(
                    start = start,
                    end = end,
                    color = ThemeGray,
                    strokeWidth = 5f,
                    cap = StrokeCap.Round
                )
            }
            if (isTop10ListVisible) {
                Text(text = "Populer Collection Top 10",fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(all = 8.dp))
            }else{ Log.d("istop10listVisible", isTop10ListVisible.toString())}

            val listItem = R.drawable.wallpaper_item
            var photos: List<Int> = arrayListOf(listItem, listItem, listItem, listItem, listItem, listItem, listItem, listItem, listItem, listItem, listItem, listItem, listItem)

            Top10WallpaperList(photos = photos, modifier = Modifier, isVisible = isTop10ListVisible)
            if (isTop10ListVisible) {
                Text( text = "Popular New Wallpapers",fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(all = 8.dp)
                )
            }else{ Log.d("istop10listVisible", isTop10ListVisible.toString())}
            PhotoGrid(photos = photos, modifier = Modifier, scrollState = scrollState) {
                isTopAppBarVisible = it
                isTop10ListVisible = it // Photogrid içerisinde görünürlüğü değiştir
            }
        }
    }
}

@Composable
fun Top10WallpaperList(photos: List<Int>, modifier: Modifier = Modifier, isVisible: Boolean = true) {
    if (isVisible) {
        LazyRow(modifier = modifier) {
            items(photos) { photo ->
                PhotoItem(photoRes = photo, modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(120.dp))
            }
        }
    }
}


@Composable
fun PhotoGrid(photos: List<Int>, modifier: Modifier = Modifier, scrollState: LazyListState, onTopAppBarVisibilityChanged: (Boolean) -> Unit) {
    val columns = 3 // Sütun sayısını ayarlayabilirsiniz
    val rows = (photos.size + columns - 1) / columns
    LazyColumn(
        modifier = modifier.padding(all = 4.dp),
        state = scrollState
    ) {
        items(rows) { rowIndex ->
            Row {
                for (columnIndex in 0 until columns) {
                    val photoIndex = rowIndex * columns + columnIndex
                    if (photoIndex < photos.size) {
                        // Öğelerin yüksekliğini ayarlayın
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                //.height(100.dp) // Yükseklik burada ayarlanıyor
                                .clickable {
                                    // Tıklama işlemi burada gerçekleşir
                                    // Örneğin, tıklanan öğenin indeksini yazdırabiliriz
                                    println("Clicked item at index: $photoIndex")
                                }
                        ) {
                            PhotoItem(photoRes = photos[photoIndex])
                        }
                    } else {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        scrollState.run {
            val firstVisibleItemIndex = firstVisibleItemIndex
            val firstItemScrollOffset = firstVisibleItemScrollOffset
            onTopAppBarVisibilityChanged(firstVisibleItemIndex == 0 && firstItemScrollOffset == 0)
        }
    }
}



@Composable
fun PhotoItem(photoRes: Int, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = photoRes),
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(.6f) // Kare görüntüleri göstermek için en-boy oranını 1:1 olarak ayarlar
                .padding(1.dp) // Fotoğraflar arasında boşluk bırakır
                .fillMaxSize() // Hücrenin tüm alanını kaplar
        )
    }
}

/*
@Composable
@Preview
fun previewScreen(@PreviewParameter navController: NavController){
    HomeScreen(navController)
}

*/
