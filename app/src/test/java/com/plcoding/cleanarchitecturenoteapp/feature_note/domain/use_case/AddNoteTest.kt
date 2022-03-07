package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.plcoding.cleanarchitecturenoteapp.feature_note.data.repository.FakeNoteRepository
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.InvalidNoteException
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddNoteTest {
    private lateinit var addNote: AddNote
    private lateinit var fakeRepository: FakeNoteRepository

    @Before
    fun setUp() {
        fakeRepository = FakeNoteRepository()
        addNote = AddNote(fakeRepository)
    }

    @Test(expected = InvalidNoteException::class)
    fun `Add a note with empty title, throw InvalidNoteException`() = runBlocking {
        Note(
            title = "",
            content = "Test content",
            timestamp = System.currentTimeMillis(),
            color = 1
        ).let { addNote(it) }
    }

    @Test(expected = InvalidNoteException::class)
    fun `Add a note with empty content, throw InvalidNoteException`() = runBlocking {
        Note(
            title = "Test title",
            content = "",
            timestamp = System.currentTimeMillis(),
            color = 1
        ).let { addNote(it) }
    }

    @Test
    fun `Add a note, insert correctly`() = runBlocking {
        val note = Note(
            title = "Test title",
            content = "Test content",
            timestamp = System.currentTimeMillis(),
            color = 1
        )
        addNote(note)

        val getNotes = GetNotes(fakeRepository)
        val notes = getNotes().first()
        assertThat(notes[0]).isEqualTo(note)
    }
}