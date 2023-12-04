package com.amikostudio.fragments

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.Window
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.amikostudio.databinding.DialogProfileBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import java.io.*





    abstract class BasePhotoUplaodFragment:Fragment() {




        var mPicturePath: String? = null
        var mIsGallery: Boolean? = null
        val bitmap: Bitmap? = null
        var is_video = false

        @RequiresApi(Build.VERSION_CODES.M)




        fun showImageDialog() {
            val dialog: Dialog = Dialog(requireActivity())
            var view = DialogProfileBinding.inflate(layoutInflater)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(view.root)

            view.tvGallery.setOnClickListener {
                mIsGallery = true
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU ){
                    if (checkForReadPermission33()) {
                        if (checkForWritePermission33()) openGallery()
                    }

                }
                else{
                    if (checkForReadPermission()) {
                        if (checkForWritePermission()) openGallery()
                    }

                }
                dialog.dismiss()
            }

            view.ivClose.setOnClickListener { dialog.dismiss() }

            view.tvCamera.setOnClickListener {
                mIsGallery = false




                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU ){
                    if (checkForCameraPermission()) {
                        if (checkForWritePermission33()) startCameraIntent(requireActivity())
                    }

                }
                else{
                    if (checkForCameraPermission()) {
                        if (checkForWritePermission()) startCameraIntent(requireActivity())
                    }
                }
                dialog.dismiss()
            }
            dialog.show()
        }

        open fun openPdf() {
            var chooseFileIntent = Intent(Intent.ACTION_GET_CONTENT)
            chooseFileIntent.type = "*/*"
            // Only return URIs that can be opened with ContentResolver
            chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE)
            chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file")
            startActivityForResult(chooseFileIntent, 3)
        }


        @RequiresApi(Build.VERSION_CODES.M)
        override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<out String>,
            grantResults: IntArray
        ) {
            // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            when (requestCode) {
                1 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkForWritePermission()) {
                        if (mIsGallery!!) openGallery() else startCameraIntent(requireActivity())
                    }
                }
                2 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) if (checkForWritePermission()) {
                    if (mIsGallery!!) openGallery() else startCameraIntent(requireActivity())
                }
                3 ->  if (checkForReadPermission()) {
                    if (checkForWritePermission()) openPdf()
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun checkForReadPermission(): Boolean {
            return if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
                false
            } else {
                true
            }
        }

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun checkForReadPermission33(): Boolean {
            return if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    1
                )
                false
            } else {
                true
            }
        }

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun checkForWritePermission33(): Boolean {
            return if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    1
                )
                false
            } else {
                true
            }
        }


        @RequiresApi(Build.VERSION_CODES.M)
        fun checkForWritePermission(): Boolean {
            return if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            } else {
                true
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun checkForCameraPermission(): Boolean {
            return if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 2)
                false
            } else {
                true
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == 1) {
                if (data != null) {
                    if (CommonMethods.getRealPath(data.data!!, requireActivity()) != null) {
                        getImage(CommonMethods.getRealPath(data.data!!, requireActivity()), data.data!!)
                    } else {
                        Glide.with(this)
                            .asBitmap()
                            .load(data.data)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(object : CustomTarget<Bitmap?>() {

                                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?
                                ) {
                                    val file: String = CommonMethods.getRealPath(
                                        Uri.fromFile(CommonMethods.saveImageToExternalStorage(resource)),
                                        requireActivity()
                                    )!!
                                    getImage(
                                        file,
                                        Uri.fromFile(CommonMethods.saveImageToExternalStorage(resource))
                                    )
                                }
                            })
                    }
                }
            } else if (requestCode == 2 && resultCode == -1) {
                val file: String = CommonMethods.getRealPath(
                    Uri.fromFile(
                        CommonMethods.saveImageToExternalStorage(
                            CommonMethods.getFile(mPicturePath, requireActivity())!!

                        )
                    ), requireActivity()
                )!!
                getImage(
                    file, Uri.fromFile(
                        CommonMethods.saveImageToExternalStorage(
                            CommonMethods.getFile(mPicturePath, requireActivity())!!
                        )
                    )
                )

            }
        }

        abstract fun getImage(uri: String?, data: Uri)

        fun openGallery() {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, 1)
        }


        open fun startCameraIntent(context: Context) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var f: File? = null
            try {
                val LOCAL_STORAGE_BASE_PATH_FOR_MEDIA = context.getExternalFilesDir("")
                    .toString() + "/" + Constants.APP_NAME
                val LOCAL_STORAGE_BASE_PATH_FOR_POSTED_IMAGES: String =
                    LOCAL_STORAGE_BASE_PATH_FOR_MEDIA + "/User/Images/"
                f = CommonMethods.setUpImageFile(LOCAL_STORAGE_BASE_PATH_FOR_POSTED_IMAGES)
                mPicturePath = f!!.absolutePath
                /* add provider in xml and
                 * manifest then add following code for Nougat devices
                 * to overcome file uri exposed app crash
                 */if (CommonMethods.isNougatDevice()) {
                    takePictureIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    val contentUri: Uri = FileProvider.getUriForFile(
                        requireActivity(), "com.petcare", f
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
                } else {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                f = null
                mPicturePath = null
            }
            startActivityForResult(takePictureIntent, 2)
        }
    }

