package com.example.flavourfiles

import androidx.compose.ui.text.font.FontFamily
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.example.ui.theme.libertinusFamily
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import java.text.BreakIterator
import java.text.StringCharacterIterator

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
fun App(
    viewModel: FlavourFilesViewModel = viewModel()
) {

    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = "home") {

        composable(route = "home") {
            HomeScreen(
                favoriteRecipes = uiState.favoriteRecipes,
                onToggleFavorite = {recipe -> viewModel.toggleFavorite(recipe) },
                onRecipeClick = { id -> navController.navigate("details/$id") },
                search = uiState.search,
                onSearchChange = { search -> viewModel.updateSearch(search) },

                )
        }

        composable(route = "details/{id}") { backStackEntry ->
            LaunchedEffect(Unit) {
                viewModel.resetExpandState()
            }
            val idString = backStackEntry.arguments?.getString("id")
            val id = idString?.toIntOrNull()

            if (id != null) {
                DetailsScreen(
                    id = id,
                    isFavorite = uiState.favoriteRecipes.contains(id),
                    ingredientsClicked = uiState.ingredientsClicked,
                    methodClicked = uiState.methodClicked,
                    onToggleFavorite = { viewModel.toggleFavorite(id) },
                    toggleIngredients = { viewModel.toggleIngredients()},
                    toggleMethods = { viewModel.toggleMethods()},
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
    onToggleFavorite: (Int) -> Unit,
    search: String,
    onSearchChange: (String) -> Unit,
    ) {

    val filteredRecipes = recipes.filter { recipe ->
        stringResource(id = recipe.title).contains(search, ignoreCase = true)
    }

    LazyColumn (
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding( 20.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.flavour_files_logo_no_words),
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp)
                )
                AnimatedText()
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ElevatedButton(
                    onClick = {
                        val randomRecipe = recipes.random()
                        onRecipeClick(randomRecipe.id)
                    },
                ) {
                    Text("Surprise Me!")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item {
            TextField(
                value = search,
                onValueChange = { search -> onSearchChange(search) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search recipes...") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(filteredRecipes) { recipe ->
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
                        fontSize = 40.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        fontFamily = libertinusFamily,
                        fontWeight = FontWeight.Normal
                    )
                    Box {
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
                                .align(Alignment.BottomEnd)
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
}



@Composable
fun DetailsScreen(
    id: Int,
    onGoBack: () -> Unit,
    isFavorite: Boolean,
    ingredientsClicked: Boolean,
    methodClicked: Boolean,
    onToggleFavorite: () -> Unit,
    toggleIngredients: () -> Unit,
    toggleMethods: () -> Unit
) {
    val recipe = recipes.find { it.id == id }

    if (recipe != null) {
        LazyColumn {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top=25.dp, bottom=0.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.flavour_files_logo_no_words),
                        contentDescription = null,
                        modifier = Modifier
                            .height(50.dp)
                            .width(50.dp)
                    )
                    AnimatedText(
                        text = "Flavour Files",
                        fontSize = 25.sp,
                        lineHeight = 30.sp,
                        fontWeight = FontWeight.Light,
                        loop = false,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                }
            }
            item {
                Divider(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 25.dp) // spacing above and below the line
                )
            }
            item{
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                        .padding(bottom=10.dp)
                ) {
                    Text(
                        text = stringResource(recipe.title),
                        fontSize = 40.sp,
                        lineHeight = 40.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = libertinusFamily,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
            item {
                Box {
                    Image(
                        painter = painterResource(id = recipe.image),
                        contentDescription = stringResource(id = recipe.title),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.White
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))

                Column {
                    val rotationAngle by animateFloatAsState(targetValue = if (ingredientsClicked) 180f else 0f, label = "ingredientsRotation")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = toggleIngredients)
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = "Ingredients",
                            fontSize = 35.sp,
                        fontFamily = libertinusFamily,
                        fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand or collapse section",
                            modifier = Modifier.rotate(rotationAngle)
                        )
                    }
                    AnimatedVisibility(visible = ingredientsClicked) {
                        Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
                            val ingredients = stringArrayResource(id = recipe.ingredients)
                            ingredients.forEach { ingredient ->
                                Text(text = "• $ingredient", modifier = Modifier.padding(bottom = 4.dp))
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Column {
                    val rotationAngle by animateFloatAsState(targetValue = if (methodClicked) 180f else 0f, label = "methodRotation")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = toggleMethods)
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Method",
                            fontSize = 35.sp,
                            fontFamily = libertinusFamily,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand or collapse section",
                            modifier = Modifier.rotate(rotationAngle)
                        )
                    }
                    AnimatedVisibility(visible = methodClicked) {
                        Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
                            val method = stringArrayResource(id = recipe.method)
                            method.forEachIndexed { index, step ->
                                Text(text = "${index + 1}.  $step", modifier = Modifier.padding(bottom = 4.dp))
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ElevatedButton(
                        onClick = onGoBack
                    ) {
                        Text("Back to all recipes")
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
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

@Composable
fun AnimatedText(
    text: String = "Flavour Files",
    fontSize: TextUnit = 30.sp,
    fontWeight: FontWeight = FontWeight.Light,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    fontFamily: FontFamily = libertinusFamily,
    lineHeight: TextUnit = 45.sp,
    loop: Boolean = true // <- NEW parameter
) {
    val breakIterator = remember(text) { BreakIterator.getCharacterInstance() }
    val typingDelayInMs = 100L
    val pauseBetweenLoops = 1000L

    var substringText by remember { mutableStateOf("") }

    LaunchedEffect(text, loop) {
        do {
            breakIterator.text = StringCharacterIterator(text)

            var nextIndex = breakIterator.next()
            while (nextIndex != BreakIterator.DONE) {
                substringText = text.subSequence(0, nextIndex).toString()
                nextIndex = breakIterator.next()
                delay(typingDelayInMs)
            }

            if (loop) {
                delay(pauseBetweenLoops)
                substringText = ""
            }

        } while (loop)
    }

    Text(
        text = substringText,
        fontSize = fontSize,
        fontWeight = fontWeight,
        lineHeight = lineHeight,
        textAlign = textAlign,
        fontFamily = fontFamily,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun FlavourFilesPreview() {
    AppTheme(dynamicColor = false) {
        App()
    }
}

