package pe.edu.upc.eventcompose.ui.maps

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.edu.upc.eventcompose.model.Event
import pe.edu.upc.eventcompose.repository.EventRepository
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val eventRepository: EventRepository) :
    ViewModel() {
    private var _events = MutableLiveData<List<Event>>()
    val events get() = _events

    private var _map = MutableLiveData<GoogleMap>()
    val map get() = _map

    fun fetchEventsByLocation() {
        viewModelScope.launch {
            _events.postValue(eventRepository.fetchEventsByName(""))
        }
    }

    fun updateMap(newMap: GoogleMap) {
        _map.postValue(newMap)
        Log.d("MapsViewModel", "updating map")
    }


}