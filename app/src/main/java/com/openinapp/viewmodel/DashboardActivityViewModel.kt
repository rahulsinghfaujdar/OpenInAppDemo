package com.openinapp.viewmodel

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openinapp.datamodel.ResDashboard
import com.openinapp.extension.Preference
import com.openinapp.network.CustomCallback
import com.openinapp.network.NetworkApiInterface
import com.openinapp.network.NetworkConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DashboardActivityViewModel @Inject constructor(
    private val preferences: Preference,
    private val networkConnection: NetworkConnection,
    private val networkApiInterface: NetworkApiInterface,
) : ViewModel() {

    private val _dashboardApiData = MutableLiveData<CustomCallback<ResDashboard>>()
    val dashboardApiData: LiveData<CustomCallback<ResDashboard>> get() = _dashboardApiData

    fun initApi(resDashboard: ResDashboard?) {
        viewModelScope.launch {
            _dashboardApiData.postValue(CustomCallback.loading(data = null, "Please Wait..."))
            if (resDashboard!=null) {
                Handler().postDelayed({
                    _dashboardApiData.postValue(CustomCallback.success(resDashboard))
                }, 2000)
            } else {
                if (networkConnection.isNetworkConnected()) {
                    val getDashboardApiResponse =
                        networkApiInterface.getDashboardData(
                            preferences.getApiToken(),
                            "dashboardNew"
                        )
                    handleResponse(getDashboardApiResponse)
                } else {
                    _dashboardApiData.postValue(
                        CustomCallback.error(
                            data = null,
                            "No internet connection"
                        )
                    )
                }
            }
        }
    }

    private fun handleResponse(dashboardApiResponse: Response<ResDashboard>) {
        if (dashboardApiResponse.isSuccessful) {
            dashboardApiResponse.body()?.let {
                _dashboardApiData.postValue(CustomCallback.success(dashboardApiResponse.body()!!))
            }
        } else {
            _dashboardApiData.postValue(
                CustomCallback.error(
                    data = null,
                    message = dashboardApiResponse.message()
                        ?: "Error Occurred! - ${dashboardApiResponse.message()}"
                )
            )
        }
    }

}