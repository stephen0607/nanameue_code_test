import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackbarController(
    private val snackbarHostState: SnackbarHostState,
    private val scope: CoroutineScope
) {
    suspend fun showMessage(message: String) {
        snackbarHostState.showSnackbar(message)
    }
}
