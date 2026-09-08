package com.sillyplayer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sillyplayer.model.RepeatMode

@Composable
fun PlayerControls(
    isPlaying: Boolean,
    isShuffleOn: Boolean,
    repeatMode: RepeatMode,
    isFavorite: Boolean,
    onPlayPauseClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onShuffleClick: () -> Unit,
    onRepeatClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onShuffleClick) {
            Icon(
                imageVector = Icons.Default.Shuffle,
                contentDescription = "Shuffle",
                tint = if (isShuffleOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }

        IconButton(onClick = onPreviousClick) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "Previous",
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(64.dp)
        ) {
            IconButton(
                onClick = onPlayPauseClick,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        IconButton(onClick = onNextClick) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "Next",
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        IconButton(onClick = onRepeatClick) {
            val icon = when (repeatMode) {
                RepeatMode.ONE -> Icons.Default.RepeatOne
                else -> Icons.Default.Repeat
            }
            val tint = if (repeatMode != RepeatMode.OFF) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            Icon(
                imageVector = icon,
                contentDescription = "Repeat",
                tint = tint
            )
        }
    }
}
