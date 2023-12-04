package site.pnpl.mira.ui.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import site.pnpl.mira.App
import site.pnpl.mira.data.CheckInRepository
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

}