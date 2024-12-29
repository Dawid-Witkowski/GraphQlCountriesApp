package com.plcoding.graphqlcountriesapp.presentation

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.graphqlcountriesapp.domain.DetailedCountry
import com.plcoding.graphqlcountriesapp.domain.GetCountriesUseCase
import com.plcoding.graphqlcountriesapp.domain.GetCountryUseCase
import com.plcoding.graphqlcountriesapp.domain.SimpleCountry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val getCountryUseCase: GetCountryUseCase,
    private val getCountriesUseCase: GetCountriesUseCase
): ViewModel() {
    private val _state = MutableStateFlow(CountriesState())
    val state
        get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            _state.update {
                it.copy(
                    isLoading = false,
                    countries = getCountriesUseCase.execute()
                )
            }
        }
    }

    fun selectCountry(code: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    selectedCountry = getCountryUseCase.execute(code)
                )
            }
        }
    }

    fun dismissDialog() {
        _state.update {
            it.copy(
                selectedCountry = null
            )
        }
    }

}


@Stable
data class CountriesState(
    val countries: List<SimpleCountry> = emptyList(),
    val isLoading: Boolean = false,
    val selectedCountry: DetailedCountry? = null
)