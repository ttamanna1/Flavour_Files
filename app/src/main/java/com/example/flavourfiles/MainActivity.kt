package com.example.flavourfiles

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flavourfiles.ui.theme.FlavourFilesTheme

data class Recipe (
    val id: Int,
    val title: Int,
    val image: Int,
    val ingredients: Int,
    val method: Int
)

val recipes = listOf(Recipe(1,R.string.recipe_pasta_title, R.drawable.pasta_flavour_files, R.array.recipe_pasta_ingredients, R.array.recipe_pasta_steps),
    Recipe(2,R.string.recipe_pancakes_title, R.drawable.pancakes_flavour_files, R.array.recipe_pancakes_ingredients, R.array.recipe_pancakes_steps))

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
                { id -> navController.navigate("details/$id") }
            )
        }

        composable(route = "details/{id}") { backStackEntry ->
            val idString = backStackEntry.arguments?.getString("id")
            val id = idString?.toIntOrNull()

            if (id != null) {
                DetailsScreen(id = id) {
                    navController.navigate("home")
                }
            } else {
                // Handle the error case: maybe go back or show an error message
                navController.navigate("home") // or a fallback screen
            }
        }
    }
}

@Composable
fun HomeScreen(onRecipeClick: (Int) -> Unit) {
    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(36.dp),
            text = "Flavour Files",
            fontSize = 80.sp
        )
        recipes.forEach { recipe ->
            Card(
                modifier = Modifier
                    .clickable { onRecipeClick(recipe.id) }
            ) {
                Text(
                    text = stringResource(id = recipe.title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Image(
                    painter = painterResource(id = recipe.image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}



@Composable
fun DetailsScreen(id: Int, onNextScreen: () -> Unit) {
    val recipe = recipes.find { it.id == id }

    if (recipe != null) {
        Column {
            Text(text="Ingredients")
            val ingredients = stringArrayResource(id = recipe.ingredients)

            ingredients.forEach { ingredient ->
                Text(text = "• $ingredient")
            }

            Text(text="Method")

            val method = stringArrayResource(id = recipe.method)

            method.forEach { step ->
                Text(text = "• $step")
            }

            Button(onClick = onNextScreen) {
                Text("Go to home screen")
            }
        }
    } else {
        Column {
            Text("Recipe not found (id=$id)")
            Button(onClick = onNextScreen) {
                Text("Go back")
            }
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

