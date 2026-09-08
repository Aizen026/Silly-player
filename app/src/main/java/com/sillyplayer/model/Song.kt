package com.sillyplayer.model

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String = "",
    val durationMs: Long = 0,
    val mediaUri: String = "",
    val artworkUrl: String = "",
    val lyrics: String = ""
)
