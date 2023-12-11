package site.pnpl.mira.ui.check_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import site.pnpl.mira.App
import site.pnpl.mira.data.CheckInRepository
import site.pnpl.mira.model.CheckInUI
import site.pnpl.mira.model.asCheckIn
import javax.inject.Inject

class CheckInDetailsViewModel() : ViewModel() {

    private val _isSaved: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean>
        get() = _isSaved

    private val _isDelete: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isDelete: LiveData<Boolean>
        get() = _isDelete

    @Inject
    lateinit var repository: CheckInRepository

    init {
        App.instance.appComponent.inject(this)
    }

    fun saveCheckIn(checkInUI: CheckInUI) {
        val checkIn = checkInUI.asCheckIn()
        viewModelScope.launch {
            val save = async {
                repository.insertCheckIn(checkIn)
                return@async true
            }
            this@CheckInDetailsViewModel._isSaved.postValue(save.await())
        }
    }

    fun deleteCheckInById(checkInUI: CheckInUI) {
        val checkIn = checkInUI.asCheckIn()
        viewModelScope.launch {
            val delete = async {
                repository.deleteListOfCheckIns(listOf(checkIn))
                return@async true
            }
            this@CheckInDetailsViewModel._isDelete.postValue(delete.await())
        }
    }
}