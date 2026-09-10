package com.sillyplayer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sillyplayer.model.AppTheme
import com.sillyplayer.model.AudioQuality
import com.sillyplayer.model.UserSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userSettings: UserSettings,
    onBackClick: () -> Unit,
    onAudioQualitySelect: (AudioQuality) -> Unit,
    onCrossfadeChange: (Int) -> Unit,
    onToggleGapless: () -> Unit,
    onToggleAutoplay: () -> Unit,
    onThemeSelect: (AppTheme) -> Unit,
    onToggleDownloadWifiOnly: () -> Unit,
    onSetSleepTimer: (Int) -> Unit,
    onClearCache: () -> Unit,
    onResetSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAudioQualityDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showSleepTimerDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Playback & Audio
            SettingsSectionHeader(title = "Playback & Audio")

            SettingsClickableItem(
                icon = Icons.Default.GraphicEq,
                title = "Audio Quality",
                subtitle = userSettings.audioQuality.displayName,
                onClick = { showAudioQualityDialog = true }
            )

            SettingsSliderItem(
                icon = Icons.Default.Tune,
                title = "Crossfade",
                valueText = if (userSettings.crossfadeSeconds == 0) "Off" else "${userSettings.crossfadeSeconds} seconds",
                value = userSettings.crossfadeSeconds.toFloat(),
                range = 0f..12f,
                steps = 11,
                onValueChange = { onCrossfadeChange(it.toInt()) }
            )

            SettingsSwitchItem(
                icon = Icons.Default.GraphicEq,
                title = "Gapless Playback",
                subtitle = "Seamless transitions between consecutive tracks",
                checked = userSettings.isGaplessEnabled,
                onCheckedChange = { onToggleGapless() }
            )

            SettingsSwitchItem(
                icon = Icons.Default.Autorenew,
                title = "Autoplay Similar Music",
                subtitle = "Keep playing similar tracks when your queue ends",
                checked = userSettings.isAutoplayEnabled,
                onCheckedChange = { onToggleAutoplay() }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Section 2: Appearance
            SettingsSectionHeader(title = "Appearance")

            SettingsClickableItem(
                icon = Icons.Default.Palette,
                title = "App Theme",
                subtitle = userSettings.theme.displayName,
                onClick = { showThemeDialog = true }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Section 3: Downloads & Storage
            SettingsSectionHeader(title = "Storage & Downloads")

            SettingsSwitchItem(
                icon = Icons.Default.Wifi,
                title = "Download over Wi-Fi only",
                subtitle = "Save cellular data when downloading tracks",
                checked = userSettings.downloadWifiOnly,
                onCheckedChange = { onToggleDownloadWifiOnly() }
            )

            SettingsClickableItem(
                icon = Icons.Default.CleaningServices,
                title = "Clear Cache",
                subtitle = if (userSettings.cacheSizeMb > 0) "${userSettings.cacheSizeMb} MB cached" else "Cache cleared",
                onClick = onClearCache
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Section 4: Timer
            SettingsSectionHeader(title = "Sleep Timer")

            SettingsClickableItem(
                icon = Icons.Default.Timer,
                title = "Sleep Timer",
                subtitle = if (userSettings.sleepTimerMinutes == 0) "Off" else "${userSettings.sleepTimerMinutes} minutes",
                onClick = { showSleepTimerDialog = true }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Section 5: Reset
            OutlinedButton(
                onClick = onResetSettings,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Restore,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reset All Settings")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Audio Quality Dialog
    if (showAudioQualityDialog) {
        AlertDialog(
            onDismissRequest = { showAudioQualityDialog = false },
            title = { Text("Audio Quality") },
            text = {
                Column {
                    AudioQuality.entries.forEach { quality ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAudioQualitySelect(quality)
                                    showAudioQualityDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (quality == userSettings.audioQuality),
                                onClick = {
                                    onAudioQualitySelect(quality)
                                    showAudioQualityDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(quality.displayName)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAudioQualityDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Theme Dialog
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("App Theme") },
            text = {
                Column {
                    AppTheme.entries.forEach { theme ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onThemeSelect(theme)
                                    showThemeDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (theme == userSettings.theme),
                                onClick = {
                                    onThemeSelect(theme)
                                    showThemeDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(theme.displayName)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Sleep Timer Dialog
    if (showSleepTimerDialog) {
        val timerOptions = listOf(0, 15, 30, 45, 60, 90)
        AlertDialog(
            onDismissRequest = { showSleepTimerDialog = false },
            title = { Text("Sleep Timer") },
            text = {
                Column {
                    timerOptions.forEach { minutes ->
                        val label = if (minutes == 0) "Off" else "$minutes minutes"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSetSleepTimer(minutes)
                                    showSleepTimerDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (minutes == userSettings.sleepTimerMinutes),
                                onClick = {
                                    onSetSleepTimer(minutes)
                                    showSleepTimerDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSleepTimerDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun SettingsClickableItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SettingsSliderItem(
    icon: ImageVector,
    title: String,
    valueText: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = valueText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            steps = steps,
            modifier = Modifier.padding(start = 40.dp)
        )
    }
}
