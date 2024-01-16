package site.pnpl.mira.ui.greeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import site.pnpl.mira.App
import site.pnpl.mira.data.models.ApiResult
import site.pnpl.mira.data.repositories.ExerciseRepository
import javax.inject.Inject

class GreetingViewModel: ViewModel() {

    @Inject lateinit var repository: ExerciseRepository

    private val _resultFlow = MutableSharedFlow<ApiResult<Any>>()
    val resultFlow = _resultFlow.asSharedFlow()

    init {
        App.instance.appComponent.inject(this)
    }

    fun getIntroExercise() {
        viewModelScope.launch(Dispatchers.IO) {
            produceResult(repository.getIntroExercise())
        }
    }

    private suspend fun produceResult(result: ApiResult<Any>) {
        _resultFlow.emit(result)
    }

}