package com.example.flavourfiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme

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
            AppTheme (dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
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
//            fontSize = 80.sp
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
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                text = stringResource(recipe.title),
//                fontSize = 40.sp,
                lineHeight = 40.sp,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = "Ingredients",
//                fontSize = 40.sp
            )
            val ingredients = stringArrayResource(id = recipe.ingredients)

            ingredients.forEach { ingredient ->
                Text(text = "• $ingredient")
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = "Method",
//                fontSize = 40.sp
            )

            val method = stringArrayResource(id = recipe.method)

            method.forEach { step ->
                Text(text = "• $step")
            }

            ElevatedButton(onClick = onNextScreen,
//                shape = RoundedCornerShape(30.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xff2175c4),
//                    contentColor = Color.DarkGray
//                ),
                ) {
                Text("Back to all recipes")
            }
        }
    } else {
        Column {
            Text("Recipe not found (id=$id)")
            ElevatedButton(onClick = onNextScreen) {
                Text("Go back")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlavourFilesPreview() {
    AppTheme(dynamicColor = false) {
        App()
    }
}

