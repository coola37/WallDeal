package com.zeroone.wallpaperdeal.ui.screens.home

import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.data.model.Category
import com.zeroone.wallpaperdeal.ui.screens.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.screens.ui.TopAppbarText
import com.zeroone.wallpaperdeal.ui.theme.ThemeGray
import com.zeroone.wallpaperdeal.ui.theme.TopAppBarColor
import com.zeroone.wallpaperdeal.utils.BlurHashDecoder
import com.zeroone.wallpaperdeal.utils.ListCategory

@Composable
fun HomeCategoryScreen(navController: NavController){
    val scrollState = rememberLazyListState()
    var isTopAppBarVisible by remember { mutableStateOf(true) }
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
            TopAppbarText(navController)
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 200.dp)) {
                val start = Offset(0f, size.height / 2)
                val end = Offset(290f, size.height / 2)
                drawLine(
                    start = start,
                    end = end,
                    color = ThemeGray,
                    strokeWidth = 5f,
                    cap = StrokeCap.Round
                )
            }

            StaggeredLazyGrid(categoryList = ListCategory.list)

        }
    }
}

@Composable
fun CategoryItem(category: Category, modifier: Modifier = Modifier) {

    val painter = rememberImagePainter(
        data = category.imageUrl,
        builder = {
            placeholder(R.drawable.wallpaper_item) // Hata durumunda varsayılan resim
            error(R.drawable.logoandtext) // Hata durumunda gösterilecek resim
            crossfade(true) // Yükleme tamamlandığında geçiş efekti
        }
    )

    Box(modifier = modifier) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(.6f) // Kare görüntüleri göstermek için en-boy oranını 1:1 olarak ayarlar
                .padding(1.dp) // Fotoğraflar arasında boşluk bırakır // Hücrenin tüm alanını kaplar
        )
    }
}

@Composable
fun StaggeredLazyGrid(categoryList :List<Category>){
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(categoryList.size) { index ->
                CategoryItem(category = categoryList.get(index))
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}


/*
@Preview
@Composable
fun PreviewHomeCategoryScreen(){
    HomeCategoryScreen()
}*/
