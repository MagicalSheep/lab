package cn.magicalsheep.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class SimpleDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.simple_dialog)
                .setNeutralButton(R.string.close) { _, _ -> }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}