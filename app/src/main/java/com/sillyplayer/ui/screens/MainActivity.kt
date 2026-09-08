package com.sillyplayer.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.sillyplayer.ui.MusicViewModel
import com.sillyplayer.ui.components.MiniPlayerBar

class MainActivity : ComponentActivity() {

    private val viewModel: MusicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val playbackState by viewModel.playbackState.collectAsState()
                val songs by viewModel.songs.collectAsState()
                val playlists by viewModel.playlists.collectAsState()
                val searchQuery by viewModel.searchQuery.collectAsState()
                val favorites by viewModel.favorites.collectAsState()

                var isPlayerExpanded by remember { mutableStateOf(false) }

                Scaffold(
                    bottomBar = {
                        if (!isPlayerExpanded && playbackState.currentSong != null) {
                            val duration = if (playbackState.totalDurationMs > 0) playbackState.totalDurationMs else 1L
                            val progress = playbackState.currentPositionMs.toFloat() / duration.toFloat()
                            MiniPlayerBar(
                                song = playbackState.currentSong,
                                isPlaying = playbackState.isPlaying,
                                progressFraction = progress,
                                onPlayPauseClick = { viewModel.togglePlayPause() },
                                onNextClick = { viewModel.skipToNext() },
                                onClick = { isPlayerExpanded = true }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        if (isPlayerExpanded) {
                            val currentSongId = playbackState.currentSong?.id ?: ""
                            PlayerScreen(
                                playbackState = playbackState,
                                isFavorite = favorites.contains(currentSongId),
                                onPlayPauseClick = { viewModel.togglePlayPause() },
                                onPreviousClick = { viewModel.skipToPrevious() },
                                onNextClick = { viewModel.skipToNext() },
                                onShuffleClick = { viewModel.toggleShuffle() },
                                onRepeatClick = { viewModel.toggleRepeatMode() },
                                onFavoriteClick = { viewModel.toggleFavorite(currentSongId) },
                                onSeek = { viewModel.seekTo(it) },
                                onBackClick = { isPlayerExpanded = false }
                            )
                        } else {
                            HomeScreen(
                                songs = songs,
                                playlists = playlists,
                                searchQuery = searchQuery,
                                currentPlayingSongId = playbackState.currentSong?.id,
                                onSearchQueryChange = { viewModel.searchSongs(it) },
                                onSongClick = { viewModel.playSong(it) },
                                onPlaylistClick = { viewModel.playPlaylist(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}
