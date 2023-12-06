package site.pnpl.mira.ui.statistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import site.pnpl.mira.App
import site.pnpl.mira.data.CheckInRepository
import site.pnpl.mira.data.entity.asCheckInUI
import site.pnpl.mira.model.CheckInUI
import site.pnpl.mira.utils.Event
import javax.inject.Inject

class StatisticViewModel : ViewModel() {

    @Inject lateinit var repository: CheckInRepository

    init {
        App.instance.appComponent.inject(this)
    }

    private fun newGetEvent(checkIns: List<CheckInUI>) {
        getEvent.postValue(Event(checkIns))
    }

    private val getEvent: MutableLiveData<Event<List<CheckInUI>>> = MutableLiveData<Event<List<CheckInUI>>>()

    fun onGetEvent(): LiveData<Event<List<CheckInUI>>> {
        return getEvent
    }

    fun getCheckInForPeriod(startPeriod: Long, endPeriod: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val checkIns = repository.getCheckInForPeriod(startPeriod, endPeriod)
                val checkInsUIMap = checkIns.map {
                    it.asCheckInUI()
                }
                val checkInSorted = checkInsUIMap.sortedByDescending { it.createdAtLong }
                newGetEvent(checkInSorted)
            }
        }
    }
}