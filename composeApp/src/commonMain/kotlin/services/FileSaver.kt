package services

expect fun letUserSaveFile(contentToSave: String, onSuccess: () -> Unit, onError: (Exception) -> Unit)