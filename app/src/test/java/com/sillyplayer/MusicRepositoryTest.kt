package com.sillyplayer

import com.sillyplayer.data.MusicRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MusicRepositoryTest {

    private lateinit var repository: MusicRepository

    @Before
    fun setUp() {
        repository = MusicRepository()
    }

    @Test
    fun testGetSampleSongs() {
        val songs = repository.getSampleSongs()
        assertTrue(songs.isNotEmpty())
        assertEquals("Midnight City Echoes", songs[0].title)
    }

    @Test
    fun testGetSamplePlaylists() {
        val playlists = repository.getSamplePlaylists()
        assertTrue(playlists.isNotEmpty())
        assertEquals("Chill Beats & Vibes", playlists[0].title)
    }

    @Test
    fun testSearchSongs() {
        val results = repository.searchSongs("Acoustic")
        assertEquals(1, results.size)
        assertEquals("Starlight Serenade", results[0].title)

        val emptyResults = repository.searchSongs("NonExistentSongQuery")
        assertTrue(emptyResults.isEmpty())
    }
}
