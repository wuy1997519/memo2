package com.example.memo.viewModel

import android.app.Application
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.example.memo.model.Memo
import com.example.memo.model.MemoRepository
import kotlinx.coroutines.launch

class MemoViewModel(application: Application): AndroidViewModel(application) {

    private val _currentTitle = MutableLiveData<String>()
    private val _currentContent = MutableLiveData<String>()

    private val _currentEditEnable = MutableLiveData<Boolean>()

    val currentTitle : LiveData<String>
        get() = _currentTitle
    val currentContent : LiveData<String>
        get() = _currentContent
    val currentEditEnabled : LiveData<Boolean>
        get() = _currentEditEnable

    init {
        _currentTitle.value = ""
        _currentContent.value = ""
        _currentEditEnable.value = true
    }

    private val repository = MemoRepository(application)

    val allMemo: LiveData<List<Memo>> = repository.allMemo

    fun getMemo(no: Int): Memo {
        var memo = Memo()
        viewModelScope.launch {
            memo = repository.getMemo(no)

            _currentTitle.value = memo.title
            _currentContent.value = memo.content
        }
        return memo
    }

    fun insert(memo: Memo){
        viewModelScope.launch {
            repository.insert(memo)
        }
    }

    fun update(memo: Memo){
        viewModelScope.launch {
            repository.update(memo)
        }
    }

    fun delete(no: Int){
        viewModelScope.launch {
            repository.delete(no)
        }
    }

    fun isEnabled(checked: Int){
        when (checked){
            0 -> _currentEditEnable.value = true
            1 -> _currentEditEnable.value = false
        }
    }


}