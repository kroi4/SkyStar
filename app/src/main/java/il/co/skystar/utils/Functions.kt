package il.co.skystar.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext


fun showToast(context: Context, text: String) {
    val toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
    toast.show()
}


fun showAlertDialog(context: Context, title:String, message: String, button: String) {
    val alertDialog = AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setNeutralButton(button, DialogInterface.OnClickListener { dialog, _ ->
            dialog.cancel()
        })
    alertDialog.show()
}

fun isEmpty(textview: TextView) : Boolean {
    return textview.text.toString() == ""
}

fun setStatusColor(textview: TextView) {
    when (textview.text.toString()){
        "המריאה" -> textview.setTextColor(Color.parseColor("#00FF0A"))
        "DEPARTED" -> textview.setTextColor(Color.parseColor("#00FF0A"))
        "בזמן" -> textview.setTextColor(Color.parseColor("#00FF0A"))
        "ON TIME" -> textview.setTextColor(Color.parseColor("#00FF0A"))
        "נחתה" -> textview.setTextColor(Color.parseColor("#00FF0A"))
        "LANDED" -> textview.setTextColor(Color.parseColor("#00FF0A"))
        "סופי" -> textview.setTextColor(Color.parseColor("#00FF0A"))
        "FINAL" -> textview.setTextColor(Color.parseColor("#00FF0A"))
        "לא סופי" -> textview.setTextColor(Color.parseColor("#EAA550"))
        "NOT FINAL" -> textview.setTextColor(Color.parseColor("#EAA550"))
        "בנחיתה" -> textview.setTextColor(Color.parseColor("#EAA550"))
        "LANDING" -> textview.setTextColor(Color.parseColor("#EAA550"))
        else -> textview.setTextColor(Color.parseColor("#FF7777"))
    }
}


