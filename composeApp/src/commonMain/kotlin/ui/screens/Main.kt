package ui.screens

import Save
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import services.letUserSaveFile
import texteditexperiment.composeapp.generated.resources.Res
import texteditexperiment.composeapp.generated.resources.save

private data class SingleAppBarAction(val icon: ImageVector, val description: String, val action: () -> Unit)

@Composable
fun MainScreen() {
    var textContent by rememberSaveable { mutableStateOf("") }

    fun updateTextContent(newValue: String) {
        textContent = newValue
    }

    fun handleSaveSuccess() {

    }

    fun handleSaveError(error: Exception) {

    }

    Scaffold(topBar = {
        AppBar(onSaveRequest = {
            letUserSaveFile(
                contentToSave = textContent,
                onSuccess = ::handleSaveSuccess,
                onError = ::handleSaveError
            )
        })
    }) {
        Row(
            modifier = Modifier.padding(it), horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(modifier = Modifier.fillMaxSize(), value = textContent, onValueChange = ::updateTextContent)
        }
    }
}

@Composable
private fun AppBar(
    onSaveRequest: () -> Unit = {},
) {
    val actions = listOf(
        SingleAppBarAction(icon = Save, description = stringResource(Res.string.save), action = onSaveRequest)
    )

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        contentPadding = PaddingValues(4.dp),
        elevation = 5.dp,
    ) {
        Text("Simple text editor")
        Spacer(modifier = Modifier.weight(1f))
        for (singleAction in actions) {
            IconButton(
                content = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = singleAction.icon,
                        contentDescription = singleAction.description
                    )
                },
                onClick = singleAction.action
            )
        }
    }
}