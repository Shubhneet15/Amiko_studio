package com.amikostudio.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.amikostudio.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


class CommonMethods {

    companion object {
        var dialog: Dialog? = null
        private const val JPEG_FILE_PREFIX = "IMG_"
        private const val JPEG_FILE_SUFFIX = ".jpg"
        private const val MP4_FILE_SUFFIX = ".mp4"

        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS


        private var mYear = 0
        private var mMonth: Int = 0
        private var mDay: Int = 0
        private var mHour: Int = 0
        private var mMinute: Int = 0

        fun showSharingDialogAsKotlin(activity: Activity, url: String) {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, url)
            activity.startActivity(Intent.createChooser(intent, "Share with:"))
        }

        fun chooseDate(et: TextView?, context: Context) {
            val cal = Calendar.getInstance()
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    et?.text = Constants.sdf6.format(cal.time)
                }

            val dialog = DatePickerDialog(
                context, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
//            dialog.datePicker(Calendar.YEAR, +18)
            dialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
            dialog.show()
//
//            today.add(Calendar.YEAR, -18)
//            binding.datePicker1.maxDate= today.timeInMillis
        }

        fun chooseDate1(et: TextView?, context: Context) {
            val cal = Calendar.getInstance()
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    et?.text = Constants.sdf2.format(cal.time)

                }

            val dialog = DatePickerDialog(
                context, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
//            dialog.datePicker(Calendar.YEAR, +18)
//            dialog.datePicker.minDate = Calendar.getInstance().timeInMillis
            dialog.show()
//
//            today.add(Calendar.YEAR, -18)
//            binding.datePicker1.maxDate= today.timeInMillis
        }


        fun chooseDate2(et: TextView?, context: Context) {
            val cal = Calendar.getInstance()
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    et?.text = Constants.sdf2.format(cal.time)
                }

            val dialog = DatePickerDialog(
                context, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
//            dialog.datePicker(Calendar.YEAR, +18)
//            dialog.datePicker.minDate = Calendar.getInstance().timeInMillis
            dialog.show()
//
//            today.add(Calendar.YEAR, -18)
//            binding.datePicker1.maxDate= today.timeInMillis
        }


        fun timePicker(et: TextView?, context: Context) {
            // Get Current Time
            val c = Calendar.getInstance()
            mHour = c[Calendar.HOUR_OF_DAY]
            mMinute = c[Calendar.MINUTE]

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    et?.setText(pad(hourOfDay) + ":" + pad(minute))
                },
                mHour,
                mMinute,
                true
            )
            timePickerDialog.show()
        }

        fun pad(input: Int): String? {
            return if (input >= 10) {
                input.toString()
            } else {
                "0$input"
            }
        }


        fun getStringToDate(dtStart: String): Date {

            val format = SimpleDateFormat("yyyy-MM-dd")

            return format.parse(dtStart)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun convertTime(date: String): String {
            val zonedDateTime = ZonedDateTime.parse(date.toString())
            val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale("pt", "BR"))
            return dtf.format(zonedDateTime)
        }

        fun getTimeAgo(date: Date): String {

            var time = date.time
            if (time < 1000000000000L)
                time *= 1000

            val now = Calendar.getInstance().timeInMillis
            if (time > now || time <= 0)
                return "in the future"

            val diff = now - time
            return when {
                diff < MINUTE_MILLIS -> "moments ago"
                diff < 2 * MINUTE_MILLIS -> "1min ago"
                diff < 60 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS}min ago"
                diff < 2 * HOUR_MILLIS -> "1hr"
                diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS}hr ago"
                diff < 48 * HOUR_MILLIS -> "1d ago"
                else -> "${diff / DAY_MILLIS}d ago"
            }
        }


        fun timeAgoWithTime(time: String): String {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val past: Date = format.parse(time)
            val now = Date()
            val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
            val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
            val hours: Long = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
            val days: Long = TimeUnit.MILLISECONDS.toDays(now.time - past.time)

            return when {
                seconds < 60 -> {
                    "$seconds seconds ago"
                }
                minutes < 60 -> {
                    "$minutes minutes ago"
                }
                hours < 24 -> {
                    "$hours hours ago"
                }
                else -> {
                    "$days days ago"
                }
            }
        }

        fun showToast(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

//        fun apiError(activity: Activity, error: ResponseBody?) {
//            val converter =
//                RestClient.getRetrofitInstance().responseBodyConverter<Common>(
//                    Common::class.java, arrayOfNulls<Annotation>(0)
//                )
//            try {
//                val error = converter.convert(error)
//                when (error!!.statusCode) {
//                    510 -> ApplicationGlobal.prefsManager.logout()
//                    else -> showToast(activity, error.message)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }

        fun isValidEmail(target: CharSequence?): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }

        fun convert24HourTimeTo12Hour(timeStr: String): String {
            try {
                val inFormat = SimpleDateFormat("hh:mm:ss")
                val outFormat = SimpleDateFormat("hh:mm a")
                val date = inFormat.parse(timeStr)
                return outFormat.format(date)
            } catch (e: Exception) {
            }
            return ""
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun getCurrentDate(): String {
            return Constants.sdf1.format(Calendar.getInstance().time)
        }

//        fun showErrorMessage(activity: Activity, responseBody: ResponseBody) {
//            val errorConverter = RestClient.getRetrofitInstance()
//                .responseBodyConverter<com.petcare.webservice.Common>(
//                    Common::class.java, arrayOfNulls<Annotation>(0)
//                )
//            val error = errorConverter.convert(responseBody)
//
//            if (error!!.statusCode == 510) ApplicationGlobal.prefsManager.logout()
//            else showToast(activity, error.message)
//        }

        fun showProgressDialog(context: Context) {
            dialog = Dialog(context)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(false)
            dialog!!.setContentView(R.layout.dialog_prograss_bar)
            dialog!!.show()
        }

        fun convertDpToPixel(dp: Float, context: Context): Float {
            return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }

        fun dismissProgressDialog() {
            if (dialog != null) {
                dialog!!.dismiss()
                dialog = null
            }
        }


        fun date(dtStart: String): String {
            val input = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val output = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            var d: Date? = null
            try {
                d = input.parse(dtStart)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return output.format(d)
        }

        fun getDateFromMilli(millisec: Long, pattren: String): String {
            val date = Date()
            date.time = millisec + 86400000
            return SimpleDateFormat(pattren, Locale.getDefault()).format(date)
        }

        fun getDateFromMilli1(millisec: Long, pattren: String): String {
            val date = Date()
            date.time = millisec
            return SimpleDateFormat(pattren, Locale.getDefault()).format(date)
        }

        fun getFile(imgPath: String?, mContext: Context?): Bitmap? {
            val mOrientation: Int
            var bMapRotate: Bitmap? = null
            try {
                if (imgPath != null) {
                    val exif = ExifInterface(imgPath)
                    mOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    BitmapFactory.decodeFile(imgPath, options)
                    options.inSampleSize = calculateInSampleSize(options, 400, 400)
                    options.inJustDecodeBounds = false
                    bMapRotate = BitmapFactory.decodeFile(imgPath, options)
                    when (mOrientation) {
                        6 -> {
                            val matrix = Matrix()
                            matrix.postRotate(90f)
                            bMapRotate = Bitmap.createBitmap(
                                bMapRotate, 0, 0,
                                bMapRotate.width, bMapRotate.height, matrix, true
                            )
                        }
                        8 -> {
                            val matrix = Matrix()
                            matrix.postRotate(270f)
                            bMapRotate = Bitmap.createBitmap(
                                bMapRotate, 0, 0,
                                bMapRotate.width, bMapRotate.height, matrix, true
                            )
                        }
                        3 -> {
                            val matrix = Matrix()
                            matrix.postRotate(180f)
                            bMapRotate = Bitmap.createBitmap(
                                bMapRotate, 0, 0,
                                bMapRotate.width, bMapRotate.height, matrix, true
                            )
                        }
                    }
                } else {
                    showToast(
                        mContext!!,
                        "There might be some problem in fetching photo.. please try again."
                    )
                }
            } catch (e: OutOfMemoryError) {
                bMapRotate = null
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: java.lang.Exception) {
                bMapRotate = null
                e.printStackTrace()
            }
            return bMapRotate
        }

        fun saveImageToExternalStorage(finalBitmap: Bitmap): File? {
            val file: File
            val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString()
            val myDir = File("$root/Mojo")
            myDir.mkdirs()
            val fname = (JPEG_FILE_PREFIX + System.currentTimeMillis() + "_" + JPEG_FILE_SUFFIX)

            file = File(myDir, fname)
            if (file.exists()) file.delete()
            try {
                val out = FileOutputStream(file)
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
                return file
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return file
        }

        private fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        private fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        private fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        fun isNougatDevice(): Boolean {
            return Build.VERSION.SDK_INT >= 24
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        fun getRealPath(uri: Uri, context: Context): String? {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        return (Environment.getExternalStorageDirectory()
                            .toString() + "/" + split[1])
                    } else {
                        val splitIndex = docId.indexOf(':', 1)
                        val tag = docId.substring(0, splitIndex)
                        val path = docId.substring(splitIndex + 1)
                        val nonPrimaryVolume =
                            getPathToNonPrimaryVolume(context, tag)
                        if (nonPrimaryVolume != null) {
                            val result = "$nonPrimaryVolume/$path"
                            val file = File(result)
                            if (file.exists() && file.canRead()) {
                                return result
                            }
                        }
                    }
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)

                    )
                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs =
                        arrayOf(split[1])
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
            return null
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        fun getPathToNonPrimaryVolume(context: Context, tag: String): String? {
            val volumes = context.externalCacheDirs
            if (volumes != null) {
                for (volume in volumes) {
                    if (volume != null) {
                        val path = volume.absolutePath
                        if (path != null) {
                            val index = path.indexOf(tag)
                            if (index != -1) {
                                return path.substring(0, index) + tag
                            }
                        }
                    }
                }
            }
            return null
        }

        fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int
        ): Int {
            try {
                val height = options.outHeight
                val width = options.outWidth
                var inSampleSize = 1
                if (height > reqHeight || width > reqWidth) {
                    val halfHeight = height / 2
                    val halfWidth = width / 2
                    while (halfHeight / inSampleSize > reqHeight
                        && halfWidth / inSampleSize > reqWidth
                    ) {
                        inSampleSize *= 2
                    }
                }
                return inSampleSize
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return 0
        }

        fun setUpImageFile(imageDirectory: String?): File? {
            var imageFile: File? = null
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                val storageDir = File(imageDirectory)
                if (null != storageDir) {
                    if (!storageDir.mkdirs()) {
                        if (!storageDir.exists()) {
                            Log.d("CameraSample", "failed to create directory")
                            return null
                        }
                    }
                }
                imageFile = File.createTempFile(
                    JPEG_FILE_PREFIX
                            + System.currentTimeMillis() + "_",
                    JPEG_FILE_SUFFIX, storageDir
                )
            }
            return imageFile
        }

        fun setUpVideoFile(imageDirectory: String?): File? {
            var imageFile: File? = null
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                val storageDir = File(imageDirectory)
                if (null != storageDir) {
                    if (!storageDir.mkdirs()) {
                        if (!storageDir.exists()) {
                            Log.d("CameraSample", "failed to create directory")
                            return null
                        }
                    }
                }
                imageFile = File.createTempFile(
                    JPEG_FILE_PREFIX
                            + System.currentTimeMillis() + "_",
                    MP4_FILE_SUFFIX, storageDir
                )
            }
            return imageFile
        }

        private fun getDataColumn(
            context: Context, uri: Uri?, selection: String?, selectionArgs:
            Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)
            try {
                cursor = context.contentResolver.query(
                    uri!!, projection,
                    selection, selectionArgs, null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(column_index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }


        fun validateLetters(txt: String): Boolean {
            val regex = "^[a-zA-Z ]+$"
            val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(txt)
            return matcher.find()
        }

        fun closeKeyboard(context: Activity) {


            val view = context?.currentFocus
            if (view != null) {
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
            }
        }

        fun printDifference(startDate: Date, endDate: Date): Int {
            //milliseconds
            var different: Long = endDate.time - startDate.time
            println("startDate : $startDate")
            println("endDate : $endDate")
            println("different : $different")
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24
            val elapsedDays = different / daysInMilli
            different = different % daysInMilli
            val elapsedHours = different / hoursInMilli
            different = different % hoursInMilli
            val elapsedMinutes = different / minutesInMilli
            different = different % minutesInMilli
            val elapsedSeconds = different / secondsInMilli

            Log.d("addEvents", "elapsedDays: " + elapsedDays)
            System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds
            )
            return elapsedDays.toInt()
        }

    }
}





