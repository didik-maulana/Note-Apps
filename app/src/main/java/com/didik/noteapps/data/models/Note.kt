package com.didik.noteapps.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
    var key: String? = null,
    var title: String? = null,
    var description: String? = null,
    var isSelected: Boolean = false
) : Parcelable