package com.example.flavourfiles

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flavourfiles.ui.theme.FlavourFilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlavourFilesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {

        composable(route = "home") {
            HomeScreen(
                { navController.navigate("details") }
            )
        }

        composable(route = "details") {
            DetailsScreen(
                { navController.navigate("home") }
            )
        }
    }
}

@Composable
fun HomeScreen(onNextScreen: () -> Unit) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "This is the home screen",
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.recipe_pasta_title),
        )
        Image(
            painter =painterResource(id = R.drawable.virtual_background),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = onNextScreen) {
            Text("Go to details screen")
        }
    }
}

@Composable
fun DetailsScreen(onNextScreen: () -> Unit) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "This is the details screen",
        )
        Button(onClick = onNextScreen) {
            Text("Go to home screen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlavourFilesPreview() {
    FlavourFilesTheme {
        App()
    }
}