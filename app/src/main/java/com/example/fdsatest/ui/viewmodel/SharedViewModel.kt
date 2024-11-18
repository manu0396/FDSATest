package com.example.fdsatest.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fdsatest.R
import com.example.fdsatest.data.remote.models.DestinationType
import com.example.fdsatest.data.remote.models.Timestamp
import com.example.fdsatest.domain.mapper.MainMapper
import com.example.fdsatest.domain.models.DestinationDomain
import com.example.fdsatest.domain.useCase.GetLocalDestinationsUseCase
import com.example.fdsatest.domain.useCase.GetRemoteDestinationsUseCase
import com.example.fdsatest.utils.WrapperResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    @ApplicationContext context: Context,
) : ViewModel() {

    private val _showLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _messageDialog = MutableStateFlow(context.getString(R.string.textError))
    val messageDialog: StateFlow<String> = _messageDialog.asStateFlow()

    private val _data: MutableStateFlow<List<DestinationDomain?>> = MutableStateFlow(listOf())
    val data: StateFlow<List<DestinationDomain?>> = _data.asStateFlow()

    private val _localData: MutableStateFlow<List<DestinationDomain?>> = MutableStateFlow(listOf())
    val localData: StateFlow<List<DestinationDomain?>> = _localData.asStateFlow()

    private val _selectedRowIndex = MutableStateFlow<Int?>(null)
    val selectedRowIndex: StateFlow<Int?> = _selectedRowIndex.asStateFlow()

    private val _showDialogCreate = MutableStateFlow(false)
    val showDialogCreate: StateFlow<Boolean> = _showDialogCreate.asStateFlow()

    private val _showDialogModify = MutableStateFlow(false)
    val showDialogModify: StateFlow<Boolean> = _showDialogModify.asStateFlow()

    private val _showDialogDelete = MutableStateFlow(false)
    val showDialogDelete: StateFlow<Boolean> = _showDialogDelete.asStateFlow()

    private val _selectedDestination = MutableStateFlow<DestinationDomain?>(null)
    val selectedDestination: StateFlow<DestinationDomain?> = _selectedDestination.asStateFlow()



    // REMOVE THIS VARIABLE WHEN PRODUCTION SERVER ARE OK
    private var mutableMockData : MutableList<DestinationDomain?> = mutableListOf(
        DestinationDomain(
            id = "1",
            name = "Example Destination 1",
            description = "A sample description for the destination.",
            countryMode = "Country Mode 1",
            type = DestinationType.Country,
            picture = "https://example.com/image.jpg",
            lastModify = Timestamp(System.currentTimeMillis())
        ),
        DestinationDomain(
            id = "2",
            name = "Example Destination 2",
            description = "A sample description for the destination.",
            countryMode = "Country Mode 2",
            type = DestinationType.Country,
            picture = "https://example.com/image.jpg",
            lastModify = Timestamp(System.currentTimeMillis())
        ),
        DestinationDomain(
            id = "3",
            name = "Example Destination 3",
            description = "A sample description for the destination.",
            countryMode = "Country Mode 3",
            type = DestinationType.City,
            picture = "https://example.com/image.jpg",
            lastModify = Timestamp(System.currentTimeMillis())
        )
    )

    @Inject
    lateinit var GetRemoteDestinationUseCase: GetRemoteDestinationsUseCase

    @Inject
    lateinit var GetLocalDestinationsUseCase: GetLocalDestinationsUseCase

    fun setSelectedDestination(destination: DestinationDomain){
        _selectedDestination.value = destination
    }

    fun showDialogDelete(show: Boolean){
        _showDialogDelete.value = show
    }

    fun showDialogCreate(show: Boolean){
        _showDialogCreate.value = show
    }

    fun showDialogModify(show: Boolean){
        _showDialogModify.value = show
    }

    fun getResults(context: Context, isRefreshing: Boolean? = null) {
        try {
            _showLoading.value = true
            viewModelScope.launch {
                /**
                 * MOCK DATA: Descomentar este código y comentar el 'when' una vez el servidor de produccion este disponible.
                 */
                if(isRefreshing == true){
                    delay(1000)
                }
                _data.value = mutableMockData

                _localData.value = mutableMockData

                _showLoading.value = false

                //END MOCK DATA
                 // COMENTAR ESTA LINEA PARA TESTEAR LA UI
                    /*
                                when (val resp = GetRemoteDestinationUseCase.getAll()) {
                                    is WrapperResponse.Success -> {
                                        resp.data?.let { destinations ->
                                            _data.value =
                                                destinations.map {
                                                    MainMapper.destinationToDestinationDomain(it)
                                                }
                                            resp.data.map {
                                                GetLocalDestinationsUseCase.insert(MainMapper.destinationToDestinationData(it))
                                            }
                                            _localData.value = GetLocalDestinationsUseCase.getResults().data?.map { MainMapper.destinationDataToDestinationDomain(it) } ?: listOf()
                                            _showLoading.value = false
                                        }
                                    }

                                    is WrapperResponse.Error -> {
                                        _messageDialog.value = resp.message ?: context.getString(R.string.textError)
                                        _showDialog.value = true
                                        _showLoading.value = false
                                    }
                                }
                */
                 //COMENTAR ESTA LINEA PARA TESTEAR LA UI
            }
        } catch (e: Exception) {
            _messageDialog.value = e.message ?: context.getString(R.string.textError)
            _showDialog.value = true
            _showLoading.value = false
        }
    }



    fun onDialogDismiss() {
        _showDialog.value = false
    }

    fun onDialogConfirm() {
        _showDialog.value = false
    }

    fun getLocalData(context: Context) {

        try {
            _showLoading.value = true
            viewModelScope.launch {
                when (val resp = GetLocalDestinationsUseCase.getResults()) {
                    is WrapperResponse.Success -> {
                        resp.data?.let { localData ->
                            _localData.value =
                                localData.map { MainMapper.destinationDataToDestinationDomain(it) }
                            _showLoading.value = false
                        }
                    }

                    is WrapperResponse.Error -> {
                        _messageDialog.value =
                            resp.message ?: context.getString(R.string.textError)
                        _showDialog.value = true
                        _showLoading.value = false
                    }

                }
            }
        } catch (e: Exception) {
            _messageDialog.value = e.message ?: context.getString(R.string.textError)
            _showDialog.value = true
            _showLoading.value = false

        }
    }

    fun createDestination(newDestination: DestinationDomain) {
        val updatedData = _data.value.toMutableList()
        updatedData.add(newDestination)
        _data.value = updatedData // Trigger recomposition

        val updatedLocalData = _localData.value.toMutableList()
        updatedLocalData.add(newDestination)
        _localData.value = updatedLocalData // Trigger recomposition

        mutableMockData.add(newDestination) // Sync mock data
    }

    fun updateDestination(index: Int, updatedDestination: DestinationDomain) {
        val newData = _data.value.toMutableList()
        val newLocalData = _localData.value.toMutableList()

        if (index in newData.indices) {
            Log.d("Update", "Existing item before update: ${newData[index]}")
            val existingItem = newData[index]
            val safeUpdatedDestination = updatedDestination.copy(
                id = existingItem?.id ?: updatedDestination.id
            )

            newData[index] = safeUpdatedDestination
            _data.value = newData.toList() // Trigger recomposition
            mutableMockData = newData.toMutableList()
            Log.d("Update", "Updated _data after modification: ${_data.value}")
        }

        if (index in newLocalData.indices) {
            Log.d("Update", "Existing local item before update: ${newLocalData[index]}")
            val existingItem = newLocalData[index]
            val safeUpdatedDestination = updatedDestination.copy(
                id = existingItem?.id ?: updatedDestination.id
            )

            newLocalData[index] = safeUpdatedDestination
            _localData.value = newLocalData.toList() // Trigger recomposition
            Log.d("Update", "Updated _localData after modification: ${_localData.value}")
        } else {
            Log.e("Update", "Invalid index $index for data or localData list")
            _showDialog.value = true
            _messageDialog.value = "Invalid index: row $index"
        }
    }



    fun deleteDestination(rowIndex: Int?) {
        // Ensure that the provided index is valid and not null
        rowIndex?.let {
            val currentData = _data.value.toMutableList()
            val currentLocalData = _localData.value.toMutableList()

            // Check if the index is within the bounds of the _data and _localData lists
            if (it in currentData.indices) {
                // Remove the item at the specified index in both lists
                currentData.removeAt(it)
                _data.value = currentData // Update the state to trigger recomposition
            } else {
                Log.e("Delete", "Invalid index $it for _data list")
                _showDialog.value = true
                _messageDialog.value = "Invalid index: row $it"
            }

            if (it in currentLocalData.indices) {
                currentLocalData.removeAt(it)
                _localData.value = currentLocalData // Update the state to trigger recomposition
            } else {
                Log.e("Delete", "Invalid index $it for _localData list")
            }

            if (it in mutableMockData.indices) {
                mutableMockData.removeAt(it) // Sync mock data if used for testing
            }
        } ?: run {
            // Handle null index case (e.g., if no row was selected)
            Log.e("Delete", "Row index is null")
            _showDialog.value = true
            _messageDialog.value = "No row selected for deletion"
        }
    }



    fun showDialog(string: String) {
        _showDialog.value = true
        _messageDialog.value = string
    }

    fun setSelectedDestinationIndex(index: Int?) {
        Log.d("VerticalDataSelector", "setSelectedDestination:$index")
        _selectedRowIndex.value = index
    }

    fun showError(string: String) {
        _showDialog.value = true
        _messageDialog.value = string
    }
}
