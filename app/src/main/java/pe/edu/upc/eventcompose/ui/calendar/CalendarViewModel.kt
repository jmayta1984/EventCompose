package pe.edu.upc.eventcompose.ui.calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.edu.upc.eventcompose.model.Event
import pe.edu.upc.eventcompose.repository.EventRepository
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(private val eventRepository: EventRepository) :
    ViewModel() {

    private var _event = MutableLiveData<Event>()
    val event get() = _event

    fun fetchEventByDay(day: String) {
        viewModelScope.launch {
            _event.postValue(eventRepository.fetchEventByDay(day))
        }
    }
}