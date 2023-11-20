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
import site.pnpl.mira.entity.EmotionsList
import site.pnpl.mira.entity.FactorsList
import site.pnpl.mira.utils.MiraDateFormat
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random

class HomeViewModel : ViewModel() {

    @Inject
    lateinit var repository: CheckInRepository


    private var _checkInLiveData = MutableLiveData<List<CheckIn>>()
    val checkInLiveData: LiveData<List<CheckIn>>
        get() = _checkInLiveData

    init {
        App.instance.appComponent.inject(this)
    }

    fun getAllCheckIns() {
        viewModelScope.launch {
            repository.getAllCheckIns()
        }
    }

    fun getCheckInForPeriod(startPeriod: Long, endPeriod: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _checkInLiveData.postValue(repository.getCheckInForPeriod(startPeriod, endPeriod))
            }
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
            repeat(100) {
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