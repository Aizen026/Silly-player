package com.sillyplayer.data

import com.sillyplayer.model.Playlist
import com.sillyplayer.model.Song

class MusicRepository {

    private val sampleSongs = listOf(
        Song(
            id = "1",
            title = "Midnight City Echoes",
            artist = "Neon Dreamer",
            album = "Cyber Horizon",
            durationMs = 215000L,
            mediaUri = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
            artworkUrl = "https://picsum.photos/id/10/400/400",
            lyrics = "[00:15.00]Walking through the neon lights\n[00:30.00]Shadows dance into the night\n[01:00.00]City echoes soft and low\n[01:30.00]Where the quiet streamlets flow"
        ),
        Song(
            id = "2",
            title = "Starlight Serenade",
            artist = "Acoustic Waves",
            album = "Peaceful Mind",
            durationMs = 180000L,
            mediaUri = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            artworkUrl = "https://picsum.photos/id/20/400/400",
            lyrics = "[00:10.00]Underneath the starlight sky\n[00:40.00]Watching gentle clouds pass by\n[01:10.00]Peaceful rhythm in my soul\n[01:40.00]Making all the broken whole"
        ),
        Song(
            id = "3",
            title = "Retro Groove",
            artist = "Funky Beat",
            album = "Disco Revival",
            durationMs = 240000L,
            mediaUri = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
            artworkUrl = "https://picsum.photos/id/30/400/400",
            lyrics = "[00:20.00]Feel the bass and take a chance\n[00:50.00]Everybody start to dance\n[01:20.00]Retro vibe will keep us warm\n[01:50.00]Dancing through the vibrant storm"
        ),
        Song(
            id = "4",
            title = "Ocean Breeze",
            artist = "Serene Sound",
            album = "Chill Out Sessions",
            durationMs = 205000L,
            mediaUri = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
            artworkUrl = "https://picsum.photos/id/40/400/400",
            lyrics = "[00:12.00]Waves are crashing on the shore\n[00:45.00]Calmness here forevermore"
        ),
        Song(
            id = "5",
            title = "Mountain High",
            artist = "Peak Explorers",
            album = "Nature Calls",
            durationMs = 195000L,
            mediaUri = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3",
            artworkUrl = "https://picsum.photos/id/50/400/400",
            lyrics = "[00:18.00]Climbing up above the cloud\n[00:52.00]Singing simple tunes aloud"
        )
    )

    fun getSampleSongs(): List<Song> = sampleSongs

    fun getSamplePlaylists(): List<Playlist> = listOf(
        Playlist(
            id = "p1",
            title = "Chill Beats & Vibes",
            description = "Relaxing tunes inspired by SimpMusic interface",
            artworkUrl = "https://picsum.photos/id/60/400/400",
            songs = sampleSongs
        ),
        Playlist(
            id = "p2",
            title = "Top Picks",
            description = "Trending tracks right now",
            artworkUrl = "https://picsum.photos/id/70/400/400",
            songs = sampleSongs.take(3)
        )
    )

    fun searchSongs(query: String): List<Song> {
        if (query.isBlank()) return sampleSongs
        val q = query.lowercase()
        return sampleSongs.filter {
            it.title.lowercase().contains(q) ||
            it.artist.lowercase().contains(q) ||
            it.album.lowercase().contains(q)
        }
    }
}
