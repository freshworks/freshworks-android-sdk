package com.freshworks.southwest.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freshworks.southwest.R
import com.freshworks.southwest.components.DividerWithOr
import com.freshworks.southwest.components.FormField
import com.freshworks.southwest.components.RenderImage
import com.freshworks.southwest.data.DataStore
import com.freshworks.southwest.ui.theme.SouthWestTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetContent()
        }
    }

    @Composable
    fun SetContent() {
        SouthWestTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                RenderImage(
                    imageId = R.drawable.login_page_bg,
                    contentDescription = stringResource(id = R.string.login_bg),
                    contentScale = ContentScale.FillBounds
                )
                LoginContent()
            }
        }
    }

    @Composable
    fun LoginContent() {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            LoginPageHeader()
            LoginPageContent()
            DividerWithOr()
            Image(
                painter = painterResource(id = R.drawable.ic_social),
                contentDescription = stringResource(id = R.string.social),
                modifier = Modifier
                    .padding(top = 44.dp)
                    .align(CenterHorizontally)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = stringResource(id = R.string.no_account),
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(top = 50.dp)
                )
                Text(
                    text = " " + stringResource(id = R.string.register_now),
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 50.dp)
                )
            }
        }
    }

    @Composable
    fun LoginPageHeader() {
        Image(
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = stringResource(id = R.string.back),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .padding(16.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() } // This is mandatory
                ) {
                    onBackPressed()
                }
        )
        Text(
            text = stringResource(id = R.string.lets_sign_in),
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(top = 100.dp, start = 20.dp)
        )
        Text(
            text = stringResource(id = R.string.login_desc),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(top = 16.dp, start = 20.dp)
        )
    }

    @Composable
    fun LoginPageContent() {
        val userName = rememberSaveable { mutableStateOf("") }
        val passWord = rememberSaveable { mutableStateOf("") }
        FormField(
            userName = userName.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 30.dp),
            onValueChange = { userName.value = it.trim() }
        )
        FormField(
            userName = passWord.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp),
            onValueChange = { passWord.value = it }
        )
        Button(
            onClick = {
                DataStore.apply {
                    setUserName(userName.value)
                    startActivity(HomeActivity.getIntent(applicationContext, false)).also {
                        finishAffinity()
                    }
                }
            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 30.dp)
        ) {
            Text(text = stringResource(id = R.string.sign_in))
        }
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(id = R.string.forgot_password),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .padding(top = 20.dp, end = 20.dp)
            )
        }
    }

    @Preview
    @Composable
    fun ShowLoginPage() {
        SetContent()
    }
}
