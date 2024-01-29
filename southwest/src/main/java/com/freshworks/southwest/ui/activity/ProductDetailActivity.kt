package com.freshworks.southwest.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.freshworks.sdk.FreshworksSDK
import com.freshworks.southwest.R
import com.freshworks.southwest.components.RenderImage
import com.freshworks.southwest.data.DataStore
import com.freshworks.southwest.ui.activity.ProductDetailActivity.ScreenState.ProductDetail
import com.freshworks.southwest.ui.theme.SouthWestTheme

class ProductDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SouthWestTheme {
                SetContent()
            }
        }
    }

    @Composable
    fun SetContent() {
        val screenState = remember {
            mutableStateOf<ScreenState>(ProductDetail)
        }
        when (screenState.value) {
            is ProductDetail -> {
                ProductDetailContent {
                    screenState.value = it
                }
            }

            is ScreenState.Cart -> {
                Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    RenderImage(
                        imageId = R.drawable.cart,
                        contentDescription = stringResource(id = R.string.cart)
                    ) {
                        screenState.value = ScreenState.CheckOut
                    }
                }
            }

            is ScreenState.CheckOut -> {
                Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    RenderImage(
                        imageId = if (DataStore.getUserName().isNotEmpty()) {
                            R.drawable.checkout_login
                        } else {
                            R.drawable.checkout
                        },
                        contentDescription = stringResource(id = R.string.check_out)
                    ) {
                        screenState.value = ScreenState.OrderPlaced
                    }
                }
            }

            else -> {
                Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    RenderImage(
                        imageId = R.drawable.order_placed,
                        contentDescription = stringResource(
                            id = R.string.order_placed
                        )
                    ) {
                        finish()
                    }
                }
            }
        }
    }

    @Composable
    fun ProductDetailContent(onClick: (ScreenState) -> Unit) {
        Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
            RenderImage(
                imageId = R.drawable.product_detail,
                contentDescription = stringResource(id = R.string.product_detail)
            ) {
                onClick.invoke(ScreenState.Cart)
            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_circle_help),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            FreshworksSDK.showFAQs(this@ProductDetailActivity)
                        },
                    contentDescription = stringResource(id = R.string.show_faqs)
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_bag),
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(end = 16.dp),
                    contentDescription = stringResource(id = R.string.shopping_bag)
                )
            }
        }
    }

    sealed class ScreenState {
        object ProductDetail : ScreenState()
        object Cart : ScreenState()
        object CheckOut : ScreenState()
        object OrderPlaced : ScreenState()
    }
}
