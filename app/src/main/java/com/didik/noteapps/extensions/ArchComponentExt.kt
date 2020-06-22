package com.didik.noteapps.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.post(data: T) = (this as MutableLiveData<T>).postValue(data)

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (result: T) -> Unit) {
    liveData.observe(this, Observer {
        it?.let {
            action(it)
        }
    })
}