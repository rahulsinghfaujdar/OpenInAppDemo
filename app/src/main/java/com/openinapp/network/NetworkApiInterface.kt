package com.openinapp.network

import com.openinapp.datamodel.ResDashboard
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface NetworkApiInterface {

    @GET("{DASHBOARD_PATH}")
    suspend fun getDashboardData(
        @Header("authorization") token: String,
        @Path("DASHBOARD_PATH", encoded = false) dashboardPath: String
    ): Response<ResDashboard>

}