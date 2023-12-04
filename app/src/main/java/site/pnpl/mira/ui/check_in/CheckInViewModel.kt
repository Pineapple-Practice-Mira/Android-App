package site.pnpl.mira.ui.check_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import site.pnpl.mira.App
import site.pnpl.mira.data.CheckInRepository
import site.pnpl.mira.data.SettingsProvider
import site.pnpl.mira.data.entity.CheckIn
import javax.inject.Inject

class CheckInViewModel : ViewModel() {
    private val _isSaved: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean>
        get() = _isSaved

    @Inject lateinit var repository: CheckInRepository
    @Inject lateinit var settingsProvider: SettingsProvider

    init {
        App.instance.appComponent.inject(this)
    }

    fun saveCheckIn(checkIn: CheckIn) {
        viewModelScope.launch {
            val save = async {
                settingsProvider.firstCheckInCreated()
                repository.insertCheckIn(checkIn)
                return@async true
            }
            this@CheckInViewModel._isSaved.postValue(save.await())
        }
    }
}