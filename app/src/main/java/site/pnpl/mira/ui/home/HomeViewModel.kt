package site.pnpl.mira.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import site.pnpl.mira.App
import site.pnpl.mira.data.CheckInRepository
import site.pnpl.mira.data.entity.CheckIn
import javax.inject.Inject

class HomeViewModel : ViewModel() {

    @Inject lateinit var repository: CheckInRepository

    val checkInData: Flow<List<CheckIn>>

    init {
        App.instance.appComponent.inject(this)
        checkInData = repository.checkInFlow
    }

    fun getCheckIns() {
        viewModelScope.launch {
            repository.getAllCheckIns()
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}