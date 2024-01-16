package site.pnpl.mira.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import site.pnpl.mira.App
import site.pnpl.mira.data.models.ApiResult
import site.pnpl.mira.data.repositories.ExerciseRepository
import javax.inject.Inject

class ExercisesListViewModel : ViewModel() {

    private val _exerciseInto: MutableSharedFlow<ApiResult<Any>> = MutableSharedFlow()
    val exerciseIntro: SharedFlow<ApiResult<Any>> = _exerciseInto.asSharedFlow()

    @Inject
    lateinit var repository: ExerciseRepository

    init {
        App.instance.appComponent.inject(this)
    }

    fun getExerciseIntro() {
        viewModelScope.launch(Dispatchers.IO) {
            _exerciseInto.emit(repository.getIntroExercise())
        }
    }

}