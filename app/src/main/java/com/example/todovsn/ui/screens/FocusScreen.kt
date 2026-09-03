package com.example.todovsn.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todovsn.R
import com.example.todovsn.ui.AppViewModelProvider
import com.example.todovsn.ui.components.SessionCompletedDialog
import com.example.todovsn.ui.navigation.NavDestination

object FocusDestination : NavDestination {
    override val route = "focus"
    override val titleRes = R.string.focus
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen(
    viewModel: FocusViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current
    var showCustomTimeSheet by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = uiState.progress,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "TimerProgress"
    )

    // Keep screen on while timer is running
    DisposableEffect(uiState.isRunning) {
        view.keepScreenOn = uiState.isRunning
        onDispose {
            view.keepScreenOn = false
        }
    }

    // Trigger haptic feedback on completion
    LaunchedEffect(uiState.isSessionCompleted) {
        if (uiState.isSessionCompleted) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    if (uiState.isSessionCompleted) {
        SessionCompletedDialog(
            durationSeconds = uiState.totalTimeSeconds,
            sessionsCompletedToday = uiState.sessionsCompletedToday,
            onStartNewSession = {
                viewModel.dismissCompletionDialog()
                viewModel.startTimer()
            },
            onDismiss = { viewModel.dismissCompletionDialog() }
        )
    }

    if (showCustomTimeSheet) {
        CustomTimePickerSheet(
            currentSeconds = uiState.selectedDuration,
            onDurationSelect = {
                viewModel.selectDuration(it)
                showCustomTimeSheet = false
            },
            onDismiss = { showCustomTimeSheet = false }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FocusHeader()

            Spacer(modifier = Modifier.height(24.dp))

            FocusTimer(
                progress = animatedProgress,
                timeRemaining = uiState.formattedTime
            )

            Spacer(modifier = Modifier.height(32.dp))

            DurationSelector(
                selectedDuration = uiState.selectedDuration,
                onDurationSelect = { viewModel.selectDuration(it) },
                enabled = !uiState.isRunning
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "\"Focus on being productive instead of busy.\"",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            FocusControls(
                isRunning = uiState.isRunning,
                isAtStart = uiState.timeLeftSeconds == uiState.totalTimeSeconds,
                onToggleTimer = {
                    if (uiState.isRunning) viewModel.pauseTimer() else viewModel.startTimer()
                },
                onStopTimer = { viewModel.resetTimer() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = { showCustomTimeSheet = true },
                enabled = !uiState.isRunning
            ) {
                Text(
                    text = stringResource(R.string.select_custom_time).uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (uiState.isRunning) {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun FocusHeader() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Focus Session",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun FocusTimer(
    progress: Float,
    timeRemaining: String,
    modifier: Modifier = Modifier
) {
    val strokeWidth = 20.dp
    val primaryColor = MaterialTheme.colorScheme.primary
    val trackColor = primaryColor.copy(alpha = 0.2f)

    Box(
        modifier = modifier.size(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(strokeWidth / 2) // Prevent clipping
        ) {
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                color = primaryColor,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = timeRemaining,
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "TIME REMAINING",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
private fun DurationSelector(
    selectedDuration: Int,
    onDurationSelect: (Int) -> Unit,
    enabled: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "SELECT DURATION",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(15, 25, 45, 60).forEach { mins ->
                val seconds = mins * 60
                DurationChip(
                    label = "${mins}m",
                    isSelected = selectedDuration == seconds,
                    onClick = { onDurationSelect(seconds) },
                    enabled = enabled
                )
            }
        }
    }
}

@Composable
private fun FocusControls(
    isRunning: Boolean,
    isAtStart: Boolean,
    onToggleTimer: () -> Unit,
    onStopTimer: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- PRIMARY TOGGLE BUTTON ---
        Button(
            onClick = onToggleTimer,
            modifier = Modifier
                .height(72.dp)
                .weight(1f)
                .padding(end = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(
                        if (isRunning) R.drawable.pause
                        else R.drawable.play_arrow
                    ),
                    contentDescription = stringResource(
                        if (isRunning) R.string.pause_timer else R.string.start_timer
                    ),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = when {
                        isRunning -> "Pause"
                        isAtStart -> "Start"
                        else -> "Resume"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        val stopDescription = stringResource(R.string.stop_timer)
        Surface(
            onClick = onStopTimer,
            modifier = Modifier
                .size(72.dp)
                .semantics { contentDescription = stopDescription },
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun DurationChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean
) {
    val containerColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        !enabled -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }

    val contentColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimary
        !enabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        onClick = if (enabled) onClick else ({}),
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        modifier = Modifier
            .width(80.dp)
            .height(56.dp)
            .then(
                if (isSelected) Modifier.shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = MaterialTheme.colorScheme.primary,
                    spotColor = MaterialTheme.colorScheme.primary
                ) else Modifier
            ),
        shadowElevation = if (isSelected) 8.dp else 0.dp,
        enabled = enabled
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = contentColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomTimePickerSheet(
    currentSeconds: Int,
    onDurationSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var sliderValue by remember { mutableStateOf((currentSeconds / 60).toFloat()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.select_custom_time),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = stringResource(R.string.minutes_format, sliderValue.toInt()),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                valueRange = 1f..120f,
                steps = 118,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Button(
                onClick = { onDurationSelect(sliderValue.toInt() * 60) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.set_duration),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
