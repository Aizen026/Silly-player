package com.sillyplayer

import com.sillyplayer.data.MusicRepository
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
}
