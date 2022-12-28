package com.example.timeliner.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timeliner.model.Note
import com.example.timeliner.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteActivityViewModel(private val repository: NoteRepository) : ViewModel() {

    fun saveNote(newNote : Note) = viewModelScope.launch(Dispatchers.IO){
        repository.addNote(newNote)
    }

    fun updateNote(exixtingNote : Note) = viewModelScope.launch(Dispatchers.IO){
        repository.updateNote(exixtingNote)
    }

    fun deleteNote(exixtingNote : Note) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteNote(exixtingNote)
    }

    fun searchNote(query : String) : LiveData<List<Note>>
    {
        return repository.searchNote(query)
    }

    fun getAllNotes() : LiveData<List<Note>> = repository.getNote()

}