package services

import androidx.compose.runtime.Composable
import java.io.OutputStream

@Composable
expect fun ShowTextFileChooserButton(
    buttonIcon: @Composable () -> Unit,
    getContentToSave: () -> String,
    getSuggestedFilename: () -> String,
    onSuccess: (String) -> Unit,
    onError: (Exception) -> Unit
)