package site.pnpl.mira.ui.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import site.pnpl.mira.App
import site.pnpl.mira.data.CheckInRepository
import site.pnpl.mira.data.entity.CheckIn
import javax.inject.Inject

class ExercisesListViewModel : ViewModel() {

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean>
        get() = _isSaved

    @Inject
    lateinit var repository: CheckInRepository

    init {
        App.instance.appComponent.inject(this)
    }

    fun saveCheckIn(checkIn: CheckIn) {
        viewModelScope.launch {
            val save = async {
                repository.insertCheckIn(checkIn)
            }
            _isSaved.postValue(save.await())
        }
    }
}