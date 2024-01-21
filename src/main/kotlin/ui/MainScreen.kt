package ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import data.NewsApiClient
import data.model.Article
import data.model.News
import io.ktor.client.plugins.*
import kotlinx.coroutines.launch


@Composable
fun MainScreen() {
    var articles by remember { mutableStateOf(emptyList<Article>()) }
    var headerTitle by remember { mutableStateOf("Headlines") }
    var searchedText by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(searchedText) {
        scope.launch {
            try {
                val newData = if (searchedText.isNotEmpty()) {
                    NewsApiClient.getSearchedNews(searchedText)
                } else {
                    NewsApiClient.getTopHeadlines()
                }
                articles = newData.articles
            } catch (e: ClientRequestException) {
                println("Error Fetching data : ${e.message}")
            }
        }
    }

    Row {
        //SidePanel
        SidePanel(onMenuSelected = {
            headerTitle = it
            searchedText = ""
            articles = emptyList()
        }, onNewsSearched = { _searchedText, _headerTitle ->
            searchedText = _searchedText
            headerTitle = _headerTitle
        })
        //MainContent
        MainContent(headerTitle, articles)
    }
}