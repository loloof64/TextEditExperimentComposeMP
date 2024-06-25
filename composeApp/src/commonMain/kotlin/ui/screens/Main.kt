package ui.screens

import Save
import Open
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import services.ShowOpenTextFileChooserButton
import services.ShowSaveTextFileChooserButton
import texteditexperiment.composeapp.generated.resources.*
import texteditexperiment.composeapp.generated.resources.Res
import texteditexperiment.composeapp.generated.resources.save
import texteditexperiment.composeapp.generated.resources.save_error
import texteditexperiment.composeapp.generated.resources.save_success

@Composable
fun MainScreen() {
    var textContent by rememberSaveable { mutableStateOf("") }
    var saveFilename by rememberSaveable { mutableStateOf("example.txt") }

    val saveErrorString = stringResource(Res.string.save_error)
    val saveSuccessString = stringResource(Res.string.save_success)
    val openErrorString = stringResource(Res.string.open_error)
    val openSuccessString = stringResource(Res.string.open_success)

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    fun updateTextContent(newValue: String) {
        textContent = newValue
    }

    fun handleSaveSuccess(newFilename: String) {
        saveFilename = newFilename
        scope.launch {
            snackbarHostState.showSnackbar(saveSuccessString)
        }
    }

    fun handleSaveError(error: Exception) {
        error.printStackTrace()
        scope.launch {
            snackbarHostState.showSnackbar(saveErrorString)
        }
    }

    fun handleOpenSuccess(content: String) {
        textContent = content
        scope.launch {
            snackbarHostState.showSnackbar(openSuccessString)
        }
    }

    fun handleOpenError(error: Exception) {
        error.printStackTrace()
        scope.launch {
            snackbarHostState.showSnackbar(openErrorString)
        }
    }

    fun getSuggestedSaveFilename(): String = saveFilename

    fun getContentToSave(): String = textContent

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }, topBar = {
        AppBar(
            getSuggestedSaveFileName = ::getSuggestedSaveFilename,
            getContentToSave = ::getContentToSave,
            onSaveSuccess = ::handleSaveSuccess,
            onSaveError = ::handleSaveError,
            onOpenSuccess = ::handleOpenSuccess,
            onOpenError = ::handleOpenError,
        )
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
    getSuggestedSaveFileName: () -> String,
    getContentToSave: () -> String,
    onSaveSuccess: (String) -> Unit,
    onSaveError: (Exception) -> Unit,
    onOpenSuccess: (String) -> Unit,
    onOpenError: (Exception) -> Unit,
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        contentPadding = PaddingValues(4.dp),
        elevation = 5.dp,
    ) {
        Text("Simple text editor")
        Spacer(modifier = Modifier.weight(1f))
        ShowSaveTextFileChooserButton(
            buttonIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Save,
                    contentDescription = stringResource(Res.string.save)
                )
            },
            getSuggestedFilename = { getSuggestedSaveFileName() },
            getContentToSave = { getContentToSave() },
            onSuccess = { onSaveSuccess(it) },
            onError = { onSaveError(it) },
        )
        ShowOpenTextFileChooserButton(
            buttonIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Open,
                    contentDescription = stringResource(Res.string.open)
                )
            },
            onSuccess = { onOpenSuccess(it) },
            onError = { onOpenError(it) },
        )
    }
}