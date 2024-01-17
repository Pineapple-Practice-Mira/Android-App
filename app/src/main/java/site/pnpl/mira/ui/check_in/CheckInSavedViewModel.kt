package site.pnpl.mira.ui.check_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import site.pnpl.mira.App
import site.pnpl.mira.data.models.ApiResult
import site.pnpl.mira.data.models.doOnError
import site.pnpl.mira.data.models.doOnSuccess
import site.pnpl.mira.data.remote.dto.exercises.ExerciseDtoList
import site.pnpl.mira.data.repositories.ExerciseRepository
import javax.inject.Inject

class CheckInSavedViewModel : ViewModel() {

    private val _exerciseFlow: MutableSharedFlow<ApiResult<Any>> = MutableSharedFlow()
    val exerciseFlow: SharedFlow<ApiResult<Any>> = _exerciseFlow.asSharedFlow()

    @Inject
    lateinit var exerciseRepository: ExerciseRepository

    init {
        App.instance.appComponent.inject(this)
    }

    fun getExerciseByEmotionId(emotionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val exerciseDtoListResult = exerciseRepository.getExerciseListByEmotionId(emotionId)
            exerciseDtoListResult.doOnSuccess { exerciseDtoList ->
                exerciseDtoList as ExerciseDtoList
                _exerciseFlow.emit(exerciseRepository.getRandomExercise(exerciseDtoList))
            }
            exerciseDtoListResult.doOnError {
                _exerciseFlow.emit(exerciseDtoListResult)
            }
        }
    }
}