package com.sillyplayer.model

data class Playlist(
    val id: String,
    val title: String,
    val description: String = "",
    val artworkUrl: String = "",
    val songs: List<Song> = emptyList()
)
