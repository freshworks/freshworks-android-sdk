package com.freshworks.southwest.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freshworks.sdk.FreshworksSDK
import com.freshworks.southwest.R
import com.freshworks.southwest.components.RenderImage
import com.freshworks.southwest.ui.theme.SouthWestTheme

const val TOPIC_NAME = "Durai topic"
const val ORDER_ONE_ID = "10001"
const val ORDER_TWO_ID = "10002"
const val DIVIDER_ALPHA_3F = 0.3f
const val WEIGHT_2F = 0.2f

class MyOrdersActivity : ComponentActivity() {
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = stringResource(id = R.string.my_orders),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(48.dp)
            )
            RenderImage(
                imageId = R.drawable.product_1,
                contentDescription = stringResource(id = R.string.bomber_jackets_white)
            ) {
                FreshworksSDK.showConversation(
                    context = this@MyOrdersActivity,
                    conversationReferenceId = ORDER_ONE_ID,
                    topicName = TOPIC_NAME
                )
            }
            Divider(
                modifier = Modifier
                    .width(336.dp)
                    .alpha(DIVIDER_ALPHA_3F)
            )
            RenderImage(
                imageId = R.drawable.product_2,
                contentDescription = stringResource(id = R.string.bomber_jackets_black)
            ) {
                FreshworksSDK.showConversation(
                    context = this@MyOrdersActivity,
                    conversationReferenceId = ORDER_TWO_ID,
                    topicName = TOPIC_NAME
                )
            }
        }
    }

    @Preview
    @Composable
    fun ShowMyOrders() {
        SetContent()
    }
}
