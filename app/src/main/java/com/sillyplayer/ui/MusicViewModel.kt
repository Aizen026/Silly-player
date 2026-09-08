package com.sillyplayer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sillyplayer.data.MusicRepository
import com.sillyplayer.model.PlaybackState
import com.sillyplayer.model.Playlist
import com.sillyplayer.model.RepeatMode
import com.sillyplayer.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MusicViewModel(
    private val repository: MusicRepository = MusicRepository()
) : ViewModel() {

    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()

    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val initialSongs = repository.getSampleSongs()
        _songs.value = initialSongs
        _playlists.value = repository.getSamplePlaylists()
        if (initialSongs.isNotEmpty()) {
            _playbackState.update {
                it.copy(
                    queue = initialSongs,
                    queueIndex = 0,
                    currentSong = initialSongs[0],
                    totalDurationMs = initialSongs[0].durationMs
                )
            }
        }
    }

    fun searchSongs(query: String) {
        _searchQuery.value = query
        _songs.value = repository.searchSongs(query)
    }

    fun playSong(song: Song) {
        val currentQueue = _playbackState.value.queue
        val index = currentQueue.indexOfFirst { it.id == song.id }
        if (index != -1) {
            _playbackState.update {
                it.copy(
                    currentSong = song,
                    queueIndex = index,
                    isPlaying = true,
                    currentPositionMs = 0L,
                    totalDurationMs = song.durationMs
                )
            }
        } else {
            val newQueue = currentQueue + song
            _playbackState.update {
                it.copy(
                    queue = newQueue,
                    queueIndex = newQueue.lastIndex,
                    currentSong = song,
                    isPlaying = true,
                    currentPositionMs = 0L,
                    totalDurationMs = song.durationMs
                )
            }
        }
    }

    fun playPlaylist(playlist: Playlist) {
        if (playlist.songs.isNotEmpty()) {
            _playbackState.update {
                it.copy(
                    queue = playlist.songs,
                    queueIndex = 0,
                    currentSong = playlist.songs[0],
                    isPlaying = true,
                    currentPositionMs = 0L,
                    totalDurationMs = playlist.songs[0].durationMs
                )
            }
        }
    }

    fun togglePlayPause() {
        _playbackState.update {
            if (it.currentSong == null && it.queue.isNotEmpty()) {
                it.copy(currentSong = it.queue[0], queueIndex = 0, isPlaying = true)
            } else {
                it.copy(isPlaying = !it.isPlaying)
            }
        }
    }

    fun skipToNext() {
        val state = _playbackState.value
        if (state.queue.isEmpty()) return

        var nextIndex = state.queueIndex + 1
        if (state.isShuffleOn) {
            nextIndex = (state.queue.indices).random()
        } else if (nextIndex >= state.queue.size) {
            nextIndex = if (state.repeatMode == RepeatMode.ALL) 0 else state.queue.size - 1
        }

        val nextSong = state.queue.getOrNull(nextIndex) ?: return
        _playbackState.update {
            it.copy(
                queueIndex = nextIndex,
                currentSong = nextSong,
                isPlaying = true,
                currentPositionMs = 0L,
                totalDurationMs = nextSong.durationMs
            )
        }
    }

    fun skipToPrevious() {
        val state = _playbackState.value
        if (state.queue.isEmpty()) return

        var prevIndex = state.queueIndex - 1
        if (prevIndex < 0) {
            prevIndex = if (state.repeatMode == RepeatMode.ALL) state.queue.lastIndex else 0
        }

        val prevSong = state.queue.getOrNull(prevIndex) ?: return
        _playbackState.update {
            it.copy(
                queueIndex = prevIndex,
                currentSong = prevSong,
                isPlaying = true,
                currentPositionMs = 0L,
                totalDurationMs = prevSong.durationMs
            )
        }
    }

    fun seekTo(positionMs: Long) {
        _playbackState.update {
            it.copy(currentPositionMs = positionMs)
        }
    }

    fun toggleShuffle() {
        _playbackState.update {
            it.copy(isShuffleOn = !it.isShuffleOn)
        }
    }

    fun toggleRepeatMode() {
        _playbackState.update {
            val nextMode = when (it.repeatMode) {
                RepeatMode.OFF -> RepeatMode.ALL
                RepeatMode.ALL -> RepeatMode.ONE
                RepeatMode.ONE -> RepeatMode.OFF
            }
            it.copy(repeatMode = nextMode)
        }
    }

    fun toggleFavorite(songId: String) {
        _favorites.update { current ->
            if (current.contains(songId)) {
                current - songId
            } else {
                current + songId
            }
        }
    }
}
