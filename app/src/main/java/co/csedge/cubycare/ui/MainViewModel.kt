package co.csedge.cubycare.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.data.repository.ChildRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ChildRepository(application.applicationContext)
    
    private val _children = MutableStateFlow<List<Child>>(emptyList())
    val children: StateFlow<List<Child>> = _children.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        refreshChildren()
    }

    fun refreshChildren() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetched = repository.getChildren()
                if (fetched.isNotEmpty()) {
                    val grouped = fetched.groupBy { it.name }
                    val uniqueChildren = mutableListOf<Child>()
                    val toDelete = mutableListOf<Child>()
                    
                    for ((_, list) in grouped) {
                        uniqueChildren.add(list.first())
                        if (list.size > 1) {
                            toDelete.addAll(list.drop(1))
                        }
                    }
                    
                    toDelete.forEach { 
                        repository.deleteChild(it.id)
                    }
                    
                    _children.value = uniqueChildren
                } else {
                    _children.value = emptyList()
                }
            } catch (e: Exception) {
                _children.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveChild(child: Child, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val updatedList = _children.value.filter { it.id != child.id }.toMutableList()
            updatedList.add(child)
            _children.value = updatedList

            try {
                repository.saveChild(child)
            } catch (e: Exception) {
                // Ignore network errors
            } finally {
                _isLoading.value = false
                onComplete()
            }
        }
    }

    fun deleteChild(childId: String, onComplete: () -> Unit) {
        if (childId == "default_general_baby") {
            onComplete()
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _children.value = _children.value.filter { it.id != childId }
            try {
                repository.deleteChild(childId)
            } catch (e: Exception) {
                // Ignore network errors
            } finally {
                _isLoading.value = false
                onComplete()
            }
        }
    }
}
