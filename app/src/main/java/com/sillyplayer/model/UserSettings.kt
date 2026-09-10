package com.sillyplayer.model

enum class AudioQuality(val displayName: String) {
    LOW("Low (96 kbps)"),
    MEDIUM("Normal (160 kbps)"),
    HIGH("High (320 kbps)"),
    LOSSLESS("Lossless (FLAC)")
}

enum class AppTheme(val displayName: String) {
    SYSTEM("System Default"),
    LIGHT("Light"),
    DARK("Dark")
}

data class UserSettings(
    val audioQuality: AudioQuality = AudioQuality.HIGH,
    val crossfadeSeconds: Int = 0,
    val isGaplessEnabled: Boolean = true,
    val isAutoplayEnabled: Boolean = true,
    val theme: AppTheme = AppTheme.SYSTEM,
    val downloadWifiOnly: Boolean = true,
    val cacheSizeMb: Int = 128,
    val sleepTimerMinutes: Int = 0
)
