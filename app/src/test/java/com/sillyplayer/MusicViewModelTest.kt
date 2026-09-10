package com.sillyplayer

import com.sillyplayer.data.MusicRepository
import com.sillyplayer.model.AppTheme
import com.sillyplayer.model.AudioQuality
import com.sillyplayer.model.RepeatMode
import com.sillyplayer.ui.MusicViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MusicViewModelTest {

    private lateinit var repository: MusicRepository
    private lateinit var viewModel: MusicViewModel

    @Before
    fun setUp() {
        repository = MusicRepository()
        viewModel = MusicViewModel(repository)
    }

    @Test
    fun testInitialState() {
        val state = viewModel.playbackState.value
        assertNotNull(state.currentSong)
        assertEquals("Midnight City Echoes", state.currentSong?.title)
        assertFalse(state.isPlaying)
    }

    @Test
    fun testTogglePlayPause() {
        assertFalse(viewModel.playbackState.value.isPlaying)
        viewModel.togglePlayPause()
        assertTrue(viewModel.playbackState.value.isPlaying)
        viewModel.togglePlayPause()
        assertFalse(viewModel.playbackState.value.isPlaying)
    }

    @Test
    fun testSkipNextAndPrevious() {
        val initialSong = viewModel.playbackState.value.currentSong
        viewModel.skipToNext()
        val nextSong = viewModel.playbackState.value.currentSong
        assertNotEquals(initialSong?.id, nextSong?.id)

        viewModel.skipToPrevious()
        val prevSong = viewModel.playbackState.value.currentSong
        assertEquals(initialSong?.id, prevSong?.id)
    }

    @Test
    fun testToggleRepeatAndShuffle() {
        assertFalse(viewModel.playbackState.value.isShuffleOn)
        viewModel.toggleShuffle()
        assertTrue(viewModel.playbackState.value.isShuffleOn)

        assertEquals(RepeatMode.OFF, viewModel.playbackState.value.repeatMode)
        viewModel.toggleRepeatMode()
        assertEquals(RepeatMode.ALL, viewModel.playbackState.value.repeatMode)
        viewModel.toggleRepeatMode()
        assertEquals(RepeatMode.ONE, viewModel.playbackState.value.repeatMode)
        viewModel.toggleRepeatMode()
        assertEquals(RepeatMode.OFF, viewModel.playbackState.value.repeatMode)
    }

    @Test
    fun testToggleFavorite() {
        val songId = "1"
        assertFalse(viewModel.favorites.value.contains(songId))
        viewModel.toggleFavorite(songId)
        assertTrue(viewModel.favorites.value.contains(songId))
        viewModel.toggleFavorite(songId)
        assertFalse(viewModel.favorites.value.contains(songId))
    }

    @Test
    fun testInitialUserSettings() {
        val settings = viewModel.userSettings.value
        assertEquals(AudioQuality.HIGH, settings.audioQuality)
        assertEquals(0, settings.crossfadeSeconds)
        assertTrue(settings.isGaplessEnabled)
        assertTrue(settings.isAutoplayEnabled)
        assertEquals(AppTheme.SYSTEM, settings.theme)
        assertTrue(settings.downloadWifiOnly)
        assertEquals(128, settings.cacheSizeMb)
        assertEquals(0, settings.sleepTimerMinutes)
    }

    @Test
    fun testUserSettingsUpdates() {
        viewModel.updateAudioQuality(AudioQuality.LOSSLESS)
        assertEquals(AudioQuality.LOSSLESS, viewModel.userSettings.value.audioQuality)

        viewModel.updateCrossfade(5)
        assertEquals(5, viewModel.userSettings.value.crossfadeSeconds)

        viewModel.toggleGaplessPlayback()
        assertFalse(viewModel.userSettings.value.isGaplessEnabled)

        viewModel.toggleAutoplay()
        assertFalse(viewModel.userSettings.value.isAutoplayEnabled)

        viewModel.updateTheme(AppTheme.DARK)
        assertEquals(AppTheme.DARK, viewModel.userSettings.value.theme)

        viewModel.toggleDownloadWifiOnly()
        assertFalse(viewModel.userSettings.value.downloadWifiOnly)

        viewModel.setSleepTimer(30)
        assertEquals(30, viewModel.userSettings.value.sleepTimerMinutes)

        viewModel.clearCache()
        assertEquals(0, viewModel.userSettings.value.cacheSizeMb)

        viewModel.resetSettings()
        val resetSettings = viewModel.userSettings.value
        assertEquals(AudioQuality.HIGH, resetSettings.audioQuality)
        assertEquals(0, resetSettings.crossfadeSeconds)
        assertTrue(resetSettings.isGaplessEnabled)
        assertTrue(resetSettings.isAutoplayEnabled)
        assertEquals(AppTheme.SYSTEM, resetSettings.theme)
        assertTrue(resetSettings.downloadWifiOnly)
        assertEquals(128, resetSettings.cacheSizeMb)
        assertEquals(0, resetSettings.sleepTimerMinutes)
    }
}
