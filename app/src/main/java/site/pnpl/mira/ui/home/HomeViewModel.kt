package site.pnpl.mira.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import site.pnpl.mira.App
import site.pnpl.mira.data.CheckInRepository
import site.pnpl.mira.data.entity.CheckIn
import site.pnpl.mira.data.entity.asCheckInUI
import site.pnpl.mira.model.CheckInUI
import site.pnpl.mira.model.EmotionsList
import site.pnpl.mira.model.FactorsList
import site.pnpl.mira.model.asCheckIn
import site.pnpl.mira.utils.Event
import site.pnpl.mira.utils.MiraDateFormat
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random

class HomeViewModel : ViewModel() {

    var cachedPeriod: Pair<Long, Long> = Pair(0L, 0L)

    private val _countCheckIns: MutableLiveData<Long> = MutableLiveData<Long>()
    val countCheckIns: LiveData<Long> get() = _countCheckIns

    @Inject
    lateinit var repository: CheckInRepository

    init {
        App.instance.appComponent.inject(this)
    }

    private fun newSaveEvent(checkIns: List<CheckInUI>) {
        saveEvent.postValue(Event(checkIns))
    }

    private val saveEvent: MutableLiveData<Event<List<CheckInUI>>> = MutableLiveData<Event<List<CheckInUI>>>()

    fun onSaveEvent(): LiveData<Event<List<CheckInUI>>> {
        return saveEvent
    }

    fun getAllCheckIns() {
        viewModelScope.launch {
            repository.getAllCheckIns()
        }
    }

    fun getCheckInForPeriod(startPeriod: Long, endPeriod: Long) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val checkIns = repository.getCheckInForPeriod(startPeriod, endPeriod)
                val checkInsUIMapped = checkIns.map {
                    it.asCheckInUI()
                }
                val checkInSorted = checkInsUIMapped.sortedByDescending { it.createdAtLong }
                newSaveEvent(checkInSorted)
            }
        }
    }

    fun deleteListOfCheckIns(checkInsUI: List<CheckInUI>){
        viewModelScope.launch {
            val checkIns = checkInsUI.map { it.asCheckIn() }
            repository.deleteListOfCheckIns(checkIns)
            getCountCheckIns()
        }
    }

    private fun getCountCheckIns() {
        viewModelScope.launch {
            val count = repository.getCountCheckIns()
            println("count checkIns: $count")
            _countCheckIns.postValue(count)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun insertListOfCheckIns() {
        val list = mutableListOf<CheckIn>()
        viewModelScope.launch {
            repeat(50) {
                val date = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    add(Calendar.DAY_OF_YEAR, -it)
                }.timeInMillis

                val checkIn = CheckIn(
                    id = 0,
                    emotionId = EmotionsList.emotions[Random.nextInt(0, EmotionsList.emotions.size - 1)].id,
                    factorId = FactorsList.factors[Random.nextInt(0, FactorsList.factors.size - 1)].id,
                    exercisesId = 0,
                    note = "",
                    createdAt = MiraDateFormat(date).convertToDataTimeISO8601(),
                    createdAtLong = date,
                    editedAt = "",
                    isSynchronized = 0
                )
                list.add(checkIn)
            }
            repository.insertListOfCheckIns(list)
        }
    }
}