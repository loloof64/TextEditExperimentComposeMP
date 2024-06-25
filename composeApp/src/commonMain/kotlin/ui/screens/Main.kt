package ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen() {
    var textContent by rememberSaveable { mutableStateOf("") }

    fun updateTextContent(newValue: String) {
        textContent = newValue
    }

    Scaffold(topBar = { AppBar() }) {
        TextField(modifier = Modifier.fillMaxSize(), value = textContent, onValueChange = ::updateTextContent)
    }
}

@Composable
fun AppBar() {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        contentPadding = PaddingValues(4.dp),
        elevation = 5.dp,
    ) {
        Text("Simple text editor")
    }
}