package pe.edu.upc.eventcompose.ui.events

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.edu.upc.eventcompose.model.Event
import pe.edu.upc.eventcompose.repository.EventRepository
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val eventRepository: EventRepository) :
    ViewModel() {
    private var _events = MutableLiveData<List<Event>>()
    val events get() = _events

    private var _query = MutableLiveData<String>()
    val query get() = _query

    fun fetchEvents() {
        viewModelScope.launch {
            _events.postValue(eventRepository.fetchEvents())
        }
    }

    fun fetchEventsByName(query: String) {
        viewModelScope.launch {
            _events.postValue(eventRepository.fetchEventsByName(query))
        }
    }

    fun updateQuery(newQuery: String) {
        _query.postValue(newQuery)
    }

}