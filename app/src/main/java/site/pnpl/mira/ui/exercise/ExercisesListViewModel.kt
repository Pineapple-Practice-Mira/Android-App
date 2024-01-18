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

    private val _selectedExercise: MutableSharedFlow<ApiResult<Any>> = MutableSharedFlow()
    val selectedExercise: SharedFlow<ApiResult<Any>> = _selectedExercise.asSharedFlow()

    private val _exerciseList: MutableSharedFlow<ApiResult<Any>> = MutableSharedFlow()
    val exerciseList: SharedFlow<ApiResult<Any>> = _exerciseList.asSharedFlow()

    @Inject
    lateinit var repository: ExerciseRepository

    init {
        App.instance.appComponent.inject(this)
    }

    fun getExerciseList() {
        viewModelScope.launch(Dispatchers.IO) {
            _exerciseList.emit(repository.getAllExercisesFromApi())
        }
    }

    fun getExerciseById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedExercise.emit(repository.getExerciseById(id))
        }
    }

}