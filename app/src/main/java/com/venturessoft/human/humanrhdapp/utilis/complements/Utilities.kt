package com.venturessoft.human.humanrhdapp.utilis.complements

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.auth0.android.jwt.JWT
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.AnimationExpanded
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.Permisos.ui.permisos
import com.venturessoft.human.humanrhdapp.jwt.JWTUtils
import java.text.SimpleDateFormat
import java.util.*

class Utilities {
    companion object {
        fun showDialog(title: String, message: String, positiveButtonText: String, context: Context, listener: DialogListener?) {
            val dialog = GenericDialog(title, message, positiveButtonText, context, listener)
            dialog.setCancelable(false)
            dialog.show()
        }
        fun showDialog(title: String, message: String, positiveButtonText: String, negativeButtonText: String, context: Context, listener: DialogListener?) {
            val dialog = GenericDialog(title, message, positiveButtonText, negativeButtonText, context, listener)
            dialog.show()
        }
        fun showErrorDialog(message: String, context: Context) {
            GenericDialog(title = context.getString(R.string.dialog_title), message = message, positiveButtonText = context.getString(R.string.dialog_positive), context = context, listener = null).show()
        }
        fun loadMessageError(codigo: String, activity: Activity) {
            val contextoPaquete: String = activity.packageName
            val identificadorMensaje = activity.applicationContext.resources.getIdentifier(codigo, "string", contextoPaquete)
            if (identificadorMensaje > 0) {
                showErrorDialog(activity.getString(identificadorMensaje), activity)
            } else {
                showErrorDialog(codigo, activity)
            }
        }
        @SuppressLint("DiscouragedApi")
        fun textcode(codigo: String, context: Context): String {
            val textvalue: String = try {
                val contextoPaquete: String = context.packageName
                val indentificadorMensaje = context.resources.getIdentifier(codigo, "string", contextoPaquete)
                if (indentificadorMensaje > 0) {
                    context.getString(indentificadorMensaje)
                } else {
                    "Error en el servicio"
                }
            } catch (exception: java.lang.Exception) {
                codigo
            }
            return textvalue
        }
        fun jwt() {
            val token = User.token.replace("Bearer ", "")
            val jwt = JWT(token)
            val isExpired = jwt.isExpired(10)
            if (isExpired) {
                Log.i(ContentValues.TAG, "El token expiro: true")
            } else {
                Log.i(ContentValues.TAG, "El token NO expiro")

            }
        }
        fun validateAndRefreshJWT() {
            val jwtUtils = JWTUtils(User.token)
            if (jwtUtils.isExpired(10)) {
                Log.i(ContentValues.TAG, "JWTutils El token expiro")
            } else {
                Log.i(ContentValues.TAG, "JWTutils El token no expiro")
            }
        }
        fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
            observe(lifecycleOwner, object : Observer<T> {
                override fun onChanged(value: T) {
                    observer.onChanged(value)
                    if (value != null) {
                        removeObserver(this)
                    }
                }
            })
        }
        fun toggleLayout(isExpanded: Boolean, v: View, layoutExpand: View): Boolean {
            AnimationExpanded().toggleArrow(v, isExpanded)
            if (isExpanded) {
                AnimationExpanded().expand(layoutExpand)
            } else {
                AnimationExpanded().collapse(layoutExpand)
            }
            return isExpanded
        }
        fun comentEdit(comentarios: EditText) {
            comentarios.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val a: String = comentarios.text.toString()
                    permisos.comentarios = a
                }
                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
        fun formatYearMonthDay(strDate: String): String? {
            return try {
                val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val requiredSdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                return sourceSdf.parse(strDate)?.let { requiredSdf.format(it) }
            } catch (_: Exception) {
                try {
                    val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
                    val requiredSdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    return sourceSdf.parse(strDate)?.let { requiredSdf.format(it) }
                }catch (_: Exception){
                    ""
                }
            }
        }
        fun formatYearMonth(strDate: String): String? {
            return try {
                val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val requiredSdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
                return sourceSdf.parse(strDate)?.let { requiredSdf.format(it) }
            } catch (_: Exception) {
                try {
                    val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
                    val requiredSdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
                    return sourceSdf.parse(strDate)?.let { requiredSdf.format(it) }
                }catch (_: Exception){
                    ""
                }
            }
        }
        fun formatYear(strDate: String): String? {
            return try {
                val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val requiredSdf = SimpleDateFormat("yyyy", Locale.getDefault())
                return sourceSdf.parse(strDate)?.let { requiredSdf.format(it) }
            } catch (_: Exception) {
                try {
                    val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
                    val requiredSdf = SimpleDateFormat("yyyy", Locale.getDefault())
                    return sourceSdf.parse(strDate)?.let { requiredSdf.format(it) }
                }catch (_: Exception){
                    ""
                }
            }
        }
        fun formatMonth(strDate: String): String? {
            return try {
                val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val requiredSdf = SimpleDateFormat("M", Locale.getDefault())
                return sourceSdf.parse(strDate)?.let { requiredSdf.format(it) }
            } catch (_: Exception) {
                try {
                    val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
                    val requiredSdf = SimpleDateFormat("M", Locale.getDefault())
                    return sourceSdf.parse(strDate)?.let { requiredSdf.format(it) }
                }catch (_: Exception){
                    ""
                }
            }
        }

        fun formatHourMin(strDate: String): String? {
            return try {
                val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val requiredSdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                sourceSdf.parse(strDate)?.let { requiredSdf.format(it) }
            } catch (_: Exception) {
                ""
            }
        }
        @SuppressLint("SimpleDateFormat")
        fun setformatYearMonthDay(date: Date): String? {
            return try {
                val format1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val requiredSdf = SimpleDateFormat("yyyy", Locale.getDefault())
                return  sourceSdf.parse(format1.format(date))?.let { requiredSdf.format(it) }
            } catch (_: Exception) {
                ""
            }
        }
        fun getDateFromMiliseconds(timestamp: Long, format : String): String {
            val timeZoneUTC = TimeZone.getDefault()
            val offsetFromUTC = timeZoneUTC.getOffset(Date().time) * -1
            val simpleFormat = SimpleDateFormat(format, Locale.US)
            val date = Date(timestamp + offsetFromUTC)
            return simpleFormat.format(date)
        }
        @SuppressLint("SimpleDateFormat")
        fun getDateLocal(): String? {
            return try {
                val calendar = Calendar.getInstance(Locale.getDefault())
                val format1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val requiredSdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                return  sourceSdf.parse(format1.format(calendar.time))?.let { requiredSdf.format(it) }
            } catch (_: Exception) {
                ""
            }
        }
    }
}