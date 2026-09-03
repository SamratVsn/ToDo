package com.example.todovsn.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todovsn.data.preference.PreferenceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class FocusUiState(
    val totalTimeSeconds: Int = 1500, //25 minutes
    val timeLeftSeconds: Int = 1500,
    val isRunning: Boolean = false,
    val selectedDuration: Int = 1500,
    val isSessionCompleted: Boolean = false,
    val sessionsCompletedToday: Int = 0
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

class FocusViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {
    private val _timerState = MutableStateFlow(
        TimerState(
            totalTimeSeconds = 1500,
            timeLeftSeconds = 1500,
            isRunning = false,
            selectedDuration = 1500,
            isSessionCompleted = false
        )
    )

    val uiState: StateFlow<FocusUiState> = combine(
        _timerState,
        preferenceRepository.preferences
    ) { timer, prefs ->
        FocusUiState(
            totalTimeSeconds = timer.totalTimeSeconds,
            timeLeftSeconds = timer.timeLeftSeconds,
            isRunning = timer.isRunning,
            selectedDuration = timer.selectedDuration,
            isSessionCompleted = timer.isSessionCompleted,
            sessionsCompletedToday = prefs.focusSessionsToday
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FocusUiState()
    )

    private var timerJob: Job? = null

    fun startTimer() {
        if (_timerState.value.isRunning || _timerState.value.timeLeftSeconds <= 0) return

        _timerState.update { it.copy(isRunning = true, isSessionCompleted = false) }

        timerJob = viewModelScope.launch {
            while (isActive && _timerState.value.timeLeftSeconds > 0) {
                delay(1000L.milliseconds)
                _timerState.update { state ->
                    state.copy(timeLeftSeconds = state.timeLeftSeconds - 1)
                }
            }

            if (_timerState.value.timeLeftSeconds == 0) {
                _timerState.update { it.copy(isRunning = false, isSessionCompleted = true) }
                preferenceRepository.incrementFocusSessions()
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _timerState.update { it.copy(isRunning = false) }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _timerState.update {
            it.copy(
                timeLeftSeconds = it.selectedDuration,
                isRunning = false,
                isSessionCompleted = false
            )
        }
    }

    fun selectDuration(seconds: Int) {
        pauseTimer()
        _timerState.update {
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
        _timerState.update {
            it.copy(
                isSessionCompleted = false,
                timeLeftSeconds = it.selectedDuration
            )
        }
    }
}

private data class TimerState(
    val totalTimeSeconds: Int,
    val timeLeftSeconds: Int,
    val isRunning: Boolean,
    val selectedDuration: Int,
    val isSessionCompleted: Boolean
)