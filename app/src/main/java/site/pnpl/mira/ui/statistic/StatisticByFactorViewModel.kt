package site.pnpl.mira.ui.statistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import site.pnpl.mira.App
import site.pnpl.mira.data.repositories.CheckInRepository
import site.pnpl.mira.data.database.check_in.entity.asCheckInUI
import site.pnpl.mira.models.CheckInUI
import site.pnpl.mira.utils.Event
import javax.inject.Inject

class StatisticByFactorViewModel : ViewModel() {

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

    private val _countCheckIns = MutableLiveData<Long>()
    val countCheckIns: LiveData<Long> get() = _countCheckIns

    fun getCheckInForPeriodByFactorId(startPeriod: Long, endPeriod: Long, factorId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val checkIns = repository.getCheckInForPeriodByFactorId(startPeriod, endPeriod, factorId)
                val checkInsUIMap = checkIns.map {
                    it.asCheckInUI()
                }
                val checkInSorted = checkInsUIMap.sortedByDescending { it.createdAtLong }
                newGetEvent(checkInSorted)
            }
        }
    }

    fun getCountCheckInsByFactor(factorId: Int) {
        viewModelScope.launch {
            _countCheckIns.postValue(repository.getCountCheckInsByFactor(factorId))
        }
    }
}