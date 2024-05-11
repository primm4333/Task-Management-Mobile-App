package com.example.letsdo

import android.content.DialogInterface


interface DialogCloseListener {
    fun handleDialogClose(dialog: DialogInterface?)
}