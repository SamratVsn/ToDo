package com.example.todovsn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class FocusUiState(
    val totalTimeSeconds: Int = 1500, //25 minutes
    val timeLeftSeconds: Int = 1500,
    val isRunning: Boolean = false,
    val selectedDuration: Int = 1500,
    val isSessionCompleted: Boolean = false //to track down session completion details
) {
    val formattedTime: String
        get() {
            val minutes = timeLeftSeconds / 60
            val seconds = timeLeftSeconds % 60
            return "%02d:%02d".format(minutes, seconds)
        }

    val progress: Float
        get() = if (totalTimeSeconds > 0) {
            timeLeftSeconds.toFloat() / totalTimeSeconds.toFloat()
        } else 0f
}

class FocusViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FocusUiState())
    val uiState: StateFlow<FocusUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun startTimer() {
        if (_uiState.value.isRunning || _uiState.value.timeLeftSeconds <= 0) return

        _uiState.update { it.copy(isRunning = true) }

        timerJob = viewModelScope.launch {
            while (isActive && _uiState.value.timeLeftSeconds > 0) {
                delay(1000L.milliseconds)
                _uiState.update { state ->
                    state.copy(timeLeftSeconds = state.timeLeftSeconds - 1)
                }
            }

            if (_uiState.value.timeLeftSeconds == 0) {
                _uiState.update { it.copy(isRunning = false) }
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isRunning = false) }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _uiState.update {
            it.copy(
                timeLeftSeconds = it.selectedDuration,
                isRunning = false
            )
        }
    }

    fun selectDuration(seconds: Int) {
        pauseTimer()
        _uiState.update {
            it.copy(
                selectedDuration = seconds,
                totalTimeSeconds = seconds,
                timeLeftSeconds = seconds
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    fun dismissCompletionDialog() {
        _uiState.update { it.copy(isSessionCompleted = false) }
    }
}