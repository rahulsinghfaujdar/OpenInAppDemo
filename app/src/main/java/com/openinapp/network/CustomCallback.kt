package com.openinapp.network

data class CustomCallback<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): CustomCallback<T>
                = CustomCallback(status = Status.SUCCESS, data = data, message = null)
        fun <T> error(data: T?, message: String): CustomCallback<T>
                = CustomCallback(status = Status.ERROR, data = data, message = message)
        fun <T> loading(data: T?,message: String): CustomCallback<T>
                = CustomCallback(status = Status.LOADING, data = data, message = message)
    }
}
