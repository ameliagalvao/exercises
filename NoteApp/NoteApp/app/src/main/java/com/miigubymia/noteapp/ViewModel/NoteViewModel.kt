package com.miigubymia.noteapp.ViewModel

import androidx.lifecycle.*
import com.miigubymia.noteapp.Model.Note
import com.miigubymia.noteapp.Repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// vamos ter um objeto do repositório como parâmetro para poder acessar as funções de lá.
class NoteViewModel(private val repository: NoteRepository):ViewModel() {

    //função para pegar todas as notas do db:
    val myAllNotes:LiveData<List<Note>> = repository.myAllNotes.asLiveData()
    // o viewmodelscope launch cria uma nova corrotina
    // para operações de bd geralmente usa-se o dispatchers.IO
    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(note)
    }
    fun update(note: Note) = viewModelScope.launch(Dispatchers.IO){
        repository.update(note)
    }
    fun delete(note: Note) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(note)
    }
    fun deleteAllNotes() = viewModelScope.launch(Dispatchers.IO){
        repository.deleteAllNotes()
    }
}

// recebe o repositorio como construtor e herda do viewmodelprovider.factory
class NoteViewModelFactory(private var repository:NoteRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NoteViewModel::class.java)){
            return NoteViewModel(repository) as T
        }else{
            throw IllegalArgumentException("Unknown View Model")
        }
    }
}