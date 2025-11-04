package com.example.mydailyhub.features.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun NotesScreen(
    viewModel: NotesViewModel,
    previousRoute: String,
    modifier: Modifier = Modifier,
) {
    val noteInput = viewModel.noteInput
    val notes = viewModel.notes

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Notes",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Previously viewed: ${previousRoute.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        OutlinedTextField(
            value = noteInput,
            onValueChange = viewModel::onNoteInputChanged,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("notes_input"),
            label = { Text("New note") },
            placeholder = { Text("Write a quick reminder...") },
            maxLines = 3,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = viewModel::addNote,
                enabled = noteInput.isNotBlank(),
            ) {
                Text("Add note")
            }
            Text(
                text = "${notes.size} saved",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (notes.isEmpty()) {
            Text(
                text = "No notes yet. Add your first entry to keep track of quick ideas.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(notes, key = NoteEntry::id) { note ->
                    NoteCard(
                        note = note,
                        onRemove = viewModel::removeNote,
                    )
                }
            }
        }
    }
}

@Composable
private fun NoteCard(
    note: NoteEntry,
    onRemove: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val formatter = rememberFormatter()
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = formatter.format(note.createdAt),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = note.body,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            IconButton(onClick = { onRemove(note.id) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete note",
                )
            }
        }
    }
}

@Composable
private fun rememberFormatter(): DateTimeFormatter =
    remember {
        DateTimeFormatter.ofPattern("MMM d, yyyy • h:mm a")
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }
