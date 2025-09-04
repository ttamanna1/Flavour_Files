package com.example.flavourfiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

data class Recipe (
    val id: Int,
    val title: Int,
    val image: Int,
    val ingredients: Int,
    val method: Int
)

val recipes = listOf(
    Recipe(1, R.string.recipe_pasta_title, R.drawable.pasta_flavour_files, R.array.recipe_pasta_ingredients, R.array.recipe_pasta_steps),
    Recipe(2, R.string.recipe_pancakes_title, R.drawable.pancakes_flavour_files, R.array.recipe_pancakes_ingredients, R.array.recipe_pancakes_steps),
    Recipe(3, R.string.recipe_tacos_title, R.drawable.tacos, R.array.recipe_tacos_ingredients, R.array.recipe_tacos_steps),
    Recipe(4, R.string.recipe_cookies_title, R.drawable.cookies, R.array.recipe_cookies_ingredients, R.array.recipe_cookies_steps),
    Recipe(5, R.string.recipe_soup_title, R.drawable.soup, R.array.recipe_soup_ingredients, R.array.recipe_soup_steps),
    Recipe(6, R.string.recipe_guacamole_title, R.drawable.guacamole, R.array.recipe_guacamole_ingredients, R.array.recipe_guacamole_steps),
    Recipe(7, R.string.recipe_chicken_title, R.drawable.chicken, R.array.recipe_chicken_ingredients, R.array.recipe_chicken_steps),
    Recipe(8, R.string.recipe_pizza_title, R.drawable.pizza, R.array.recipe_pizza_ingredients, R.array.recipe_pizza_steps),
    Recipe(9, R.string.recipe_garlic_bread_title, R.drawable.garlic_bread, R.array.recipe_garlic_bread_ingredients, R.array.recipe_garlic_bread_steps),
    Recipe(10, R.string.recipe_salad_title, R.drawable.salad, R.array.recipe_salad_ingredients, R.array.recipe_salad_steps)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme (dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primaryContainer
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
    var instructionsClicked by remember { mutableStateOf(false) }
    var methodClicked by remember { mutableStateOf(false) }

    var favoriteRecipes by remember { mutableStateOf(setOf<Int>()) }

    val onToggleFavorite = { recipe: Int ->
        favoriteRecipes = if (favoriteRecipes.contains(recipe)) {
            favoriteRecipes - recipe
        } else {
            favoriteRecipes + recipe
        }
    }

    NavHost(navController = navController, startDestination = "home") {

        composable(route = "home") {
            HomeScreen(
                favoriteRecipes = favoriteRecipes,
                onToggleFavorite = onToggleFavorite,
                onRecipeClick = { id -> navController.navigate("details/$id") }
            )
        }

        composable(route = "details/{id}") { backStackEntry ->
            val idString = backStackEntry.arguments?.getString("id")
            val id = idString?.toIntOrNull()

            if (id != null) {
                DetailsScreen(
                    instructionsClicked,
                    onInstructionsClickedChange = {instructionsClicked = !it},
                    methodClicked,
                    onMethodClickedChange = {methodClicked = !it},
                    id = id,
                    isFavorite = favoriteRecipes.contains(id),
                    onToggleFavorite = { onToggleFavorite(id) },
                    onGoBack = { navController.popBackStack() }
                    )
            } else {
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun HomeScreen(
    onRecipeClick: (Int) -> Unit,
    favoriteRecipes: Set<Int>,
    onToggleFavorite: (Int) -> Unit
    ) {
    LazyColumn (
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding( 34.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Flavour\n Files",
                    fontSize = 60.sp,
                    lineHeight = 70.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        items(recipes) { recipe ->
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRecipeClick(recipe.id) },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Column {
                    Text(
                        text = stringResource(id = recipe.title),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                    Image(
                        painter = painterResource(id = recipe.image),
                        contentDescription = stringResource(id = recipe.title),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentScale = ContentScale.Crop

                    )
                    IconButton(
                        onClick = { onToggleFavorite(recipe.id) },
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = if (favoriteRecipes.contains(recipe.id)) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (favoriteRecipes.contains(recipe.id)) Color.Red else Color.White
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun DetailsScreen(    instructionsClicked: Boolean,
                      onInstructionsClickedChange: (Boolean) -> Unit,
                      methodClicked: Boolean,
                      onMethodClickedChange: (Boolean) -> Unit,
                      id: Int,
                      onGoBack: () -> Unit,
                      isFavorite: Boolean,
                      onToggleFavorite: () -> Unit
) {
    val recipe = recipes.find { it.id == id }

    if (recipe != null) {
        LazyColumn {
            item{
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    text = stringResource(recipe.title),
//                fontSize = 40.sp,
                    lineHeight = 40.sp,
                    textAlign = TextAlign.Center
                )
            }
            item {
                Image(
                    painter = painterResource(id = recipe.image),
                    contentDescription = stringResource(id = recipe.title),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable(onClick = {onInstructionsClickedChange(instructionsClicked)}),
                    text = "Ingredients",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                val ingredients = stringArrayResource(id = recipe.ingredients)

                if (instructionsClicked) {ingredients.forEach { ingredient ->
                    Text(text = "• $ingredient")
                }
            }}

            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable(onClick = {onMethodClickedChange(methodClicked)}),
                    text = "Method",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                val method = stringArrayResource(id = recipe.method)

                if (methodClicked) {method.forEach { step ->
                    Text(text = "• $step")
                }
            }}

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ElevatedButton(onClick = onGoBack) {
                        Text("Back to all recipes")
                    }
                }
            }
        }
    } else {
        LazyColumn {
            item {
                Text("Recipe not found (id=$id)")
            }
            item {
                ElevatedButton(onClick = onGoBack) {
                    Text("Go back")
                }
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

