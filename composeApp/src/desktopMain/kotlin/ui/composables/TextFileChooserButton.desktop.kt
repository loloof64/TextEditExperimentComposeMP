package services

import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import org.jetbrains.compose.resources.stringResource
import texteditexperiment.composeapp.generated.resources.Res
import texteditexperiment.composeapp.generated.resources.save_dialog_title
import java.io.File
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
actual fun ShowTextFileChooserButton(
    buttonIcon: @Composable () -> Unit,
    getContentToSave: () -> String,
    getSuggestedFilename: () -> String,
    onSuccess: (String) -> Unit,
    onError: (Exception) -> Unit
) {
    val dialogTitle = stringResource(Res.string.save_dialog_title)
    var currentPath by rememberSaveable { mutableStateOf(File(System.getProperty("user.home"))) }
    IconButton(onClick = {
        openSaveTextFileChooser(
            suggestedName = getSuggestedFilename(),
            title = dialogTitle,
            currentPath = currentPath,
            content = getContentToSave(),
            onSuccess = { newFileName, newPath ->
                currentPath = newPath
                onSuccess(newFileName)
            },
            onError = onError
        )
    }) {
        buttonIcon()
    }
}

private fun openSaveTextFileChooser(
    title: String,
    suggestedName: String,
    content: String,
    currentPath: File,
    onSuccess: (String, File) -> Unit,
    onError: (Exception) -> Unit
) {
    SwingUtilities.invokeLater {
        try {
            val extFilter = FileNameExtensionFilter(
                "Text files", "txt"
            )
            val fileChooser = JFileChooser().apply {
                fileSelectionMode = JFileChooser.FILES_ONLY
                dialogTitle = title
                fileFilter = extFilter
                selectedFile = File(currentPath, suggestedName)
            }

            val result = fileChooser.showSaveDialog(null)
            if (result == JFileChooser.APPROVE_OPTION) {
                val selectedFile = fileChooser.selectedFile
                selectedFile.writeText(content)
                onSuccess(selectedFile.name, selectedFile.parentFile)
            }
        } catch (e: Exception) {
            onError(e)
        }
    }
}