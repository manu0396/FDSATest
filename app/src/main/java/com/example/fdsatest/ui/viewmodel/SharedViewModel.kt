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
import com.example.fdsatest.domain.useCase.GetResultByIdUseCase
import com.example.fdsatest.domain.useCase.InsertLocalDestinationUseCase
import com.example.fdsatest.domain.useCase.RemoveLocalDestinationUseCase
import com.example.fdsatest.utils.WrapperResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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

    @Inject
    lateinit var GetResultByIdUseCase: GetResultByIdUseCase

    @Inject
    lateinit var InsertLocalDestinationUseCase: InsertLocalDestinationUseCase

    @Inject
    lateinit var RemoveLocalDestinationUseCase: RemoveLocalDestinationUseCase

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
                    //Trigger recomposition
                    getLocalData(context)
                    delay(1000)
                }
                _data.value = mutableMockData

                _localData.value = mutableMockData

                //InsertData into localDatabase
                mutableMockData.map {destinationDomain ->
                    if (destinationDomain != null) {
                        MainMapper.destinationDomainToDestinationData(destinationDomain)
                    }
                }

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
        viewModelScope.launch(Dispatchers.IO) {
            val updatedData = _data.value.toMutableList()
            updatedData.add(newDestination)
            _data.value = updatedData.toList() // Trigger recomposition

            val updatedLocalData = _localData.value.toMutableList()
            updatedLocalData.add(newDestination)
            _localData.value = updatedLocalData.toList() // Trigger recomposition

            mutableMockData = updatedData.toMutableList() // Trigger recomposition
            InsertLocalDestinationUseCase.insert(MainMapper.destinationDomainToDestinationData(newDestination))
        }
    }

    fun updateDestination(index: Int, updatedDestination: DestinationDomain) {
        viewModelScope.launch(Dispatchers.IO) {
            val newData = _data.value.toMutableList()
            val newLocalData = _localData.value.toMutableList()

            if (index in newData.indices) {
                val existingItem = newData[index]
                val safeUpdatedDestination = updatedDestination.copy(
                    id = existingItem?.id ?: updatedDestination.id
                )

                newData[index] = safeUpdatedDestination
                _data.value = newData.toList() // Trigger recomposition
                mutableMockData = newData.toMutableList()
            }

            if (index in newLocalData.indices) {
                val existingItem = newLocalData[index]
                val safeUpdatedDestination = updatedDestination.copy(
                    id = existingItem?.id ?: updatedDestination.id
                )

                newLocalData[index] = safeUpdatedDestination
                _localData.value = newLocalData.toList() // Trigger recomposition
                //Remove item and add again
                if (existingItem != null){
                    RemoveLocalDestinationUseCase.delete(MainMapper.destinationDomainToDestinationData(existingItem))
                    InsertLocalDestinationUseCase.insert(MainMapper.destinationDomainToDestinationData(updatedDestination))
                }
            } else {
                Log.e("Update", "Invalid index $index for data or localData list")
                _showDialog.value = true
                _messageDialog.value = "Invalid index: row $index"
            }
        }
    }

    fun deleteDestination(rowIndex: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            rowIndex?.let {
                val currentData = _data.value.toMutableList()

                if (it in currentData.indices) {
                    //Remove from local
                    currentData[rowIndex]?.let { destinationDomain ->
                        MainMapper.destinationDomainToDestinationData(
                            destinationDomain
                        )
                    }?.let { destinationData ->
                        RemoveLocalDestinationUseCase.delete(
                            destinationData
                        )
                    }

                    currentData.removeAt(it)
                    _data.value = currentData.toList() // Trigger recomposition
                    mutableMockData = currentData.toMutableList() // Update mock data
                    _localData.value = currentData.toList()// Trigger recomposition

                } else {
                    Log.e("Delete", "Index out of bounds: $it. Size=${currentData.size}")
                    showError("Failed to delete. Index out of bounds.")
                }
            } ?: run {
                Log.e("Delete", "Row index is null")
                showError("No row selected for deletion.")
            }
        }
    }



    fun showDialog(string: String) {
        _showDialog.value = true
        _messageDialog.value = string
    }

    fun setSelectedDestinationIndex(index: Int?) {
        _selectedRowIndex.value = index
    }

    fun showError(string: String) {
        _showDialog.value = true
        _messageDialog.value = string
    }
}
