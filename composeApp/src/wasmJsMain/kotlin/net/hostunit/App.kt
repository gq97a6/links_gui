package net.hostunit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import net.hostunit.pages.CodePage
import net.hostunit.pages.EditPage
import net.hostunit.pages.LoginPage
import net.hostunit.theme.Theme

@Composable
fun String.match(pattern: String, content: @Composable (List<String>) -> Unit): Boolean {
    //Return if pattern size does not match
    if (pattern.count { it == '/' } != this.count { it == '/' } - 3) return false

    //Store values from path that are marked with question mark in the pattern
    val extracted = mutableListOf<String>()

    //Split into parts
    val patternPaths = pattern.split("/")
    val pathParts = this.split("/").drop(3)

    pathParts.forEachIndexed { i, part ->
        if (patternPaths[i] == "?") extracted.add(pathParts[i])
        else if (patternPaths[i] != part) return false
    }

    content(extracted)

    return true
}

@Composable
fun App() {
    Theme {
        Box(
            modifier = Modifier.fillMaxSize().background(color = colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            document.URL.apply {
                when {
                    match("") { CodePage() } -> {}
                    match("login") { LoginPage() } -> {}
                    match("edit") { EditPage() } -> {}
                    match("login/?") { LoginPage(it.first()) } -> {}
                    match("edit/?") { EditPage(it.first()) } -> {}
                    match("?") { CodePage(it.first()) } -> {}
                }
            }
        }
    }
}