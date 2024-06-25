package services

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.IOException

@Composable
actual fun ShowTextFileChooserButton(
    buttonIcon: @Composable () -> Unit,
    getContentToSave: () -> String,
    getSuggestedFilename: () -> String,
    onSuccess: (String) -> Unit,
    onError: (Exception) -> Unit
) {
    val context = LocalContext.current

    fun saveTextFile(uri: Uri) {
        val contentResolver = context.contentResolver
        try {
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(getContentToSave().toByteArray())
            }
            val newFilename =
                getFileName(uri, context.contentResolver) ?: throw IOException("File has not been created !")
            onSuccess(newFilename)
        } catch (e: IOException) {
            onError(e)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri ->
        uri?.let { saveTextFile(it) }
    }

    IconButton(onClick = {
        launcher.launch(getSuggestedFilename())
    }) {
        buttonIcon()
    }
}

private fun getFileName(uri: Uri, contentResolver: ContentResolver): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    result = cursor.getString(nameIndex)
                }
            }
        } finally {
            cursor?.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != -1) {
            result = result?.substring(cut!! + 1)
        }
    }
    return result
}