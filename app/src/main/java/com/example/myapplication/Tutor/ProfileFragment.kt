package com.example.myapplication.Tutor

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.Data.Prefs
import com.example.myapplication.Functions.CommonFunctions.getToastShort
import com.example.myapplication.databinding.FragmentProfileBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.yalantis.ucrop.UCrop
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Hashtable

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    private var isVisible = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val uid = Prefs.getUID(requireContext())
        val qrCodeBitmap = generateQRCode(uid!!)
        binding.imgEditImageProfile.setOnClickListener {
            checkPermission()
        }
        binding.txtGenerateQR.setOnClickListener {
            showQR(qrCodeBitmap!!)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDefaultProfileImage()
    }

    private fun generateQRCode(uid: String): Bitmap? {
        val width = 500
        val height = 500
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = try {
            qrCodeWriter.encode(uid, BarcodeFormat.QR_CODE, width, height, Hashtable<EncodeHintType, String>())
        } catch (e: WriterException) {
            Log.d("ProfileFragment", "Exception in generating QR Code : ${e.message}")
            return null
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }
    private fun showQR(bMap: Bitmap){
        binding.imgProfileQR.setImageBitmap(bMap)
        if(isVisible){
            binding.txtGenerateQR.setText("Show profile QR Code ")
            binding.imgProfileQR.visibility = View.GONE
        }else{
            binding.txtGenerateQR.setText("Hide Profile QR")
            binding.imgProfileQR.visibility = View.VISIBLE
        }
        isVisible = !isVisible
    }

    private fun checkPermission(){
        if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) != PackageManager.PERMISSION_GRANTED){
            requestStoragePermissions()
        }else{
            pickProfilePhoto()
        }
    }

    private fun requestStoragePermissions(){
        requestPermissions(
            arrayOf(android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED),
            STORAGE_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            STORAGE_PERMISSION_REQUEST_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickProfilePhoto()
                }else{
                    showPermissionDeniedDialog()
                    getToastShort(requireContext(),"Please provide storage permissions")
                }
            }
        }
    }

    private fun showPermissionDeniedDialog(){
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Permissions Required")
            .setMessage("Permission required to access photos")
            .setPositiveButton("Open Settings"){dialog,_ ->
                openAppSettings()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel"){dialog, _ ->
                dialog.dismiss()
            }.setCancelable(false)
            .create()

        alertDialog.show()
        getToastShort(requireContext(), "Please provide storage permissions manually.")
    }

    private fun openAppSettings(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivity(intent)
    }
    private fun getDefaultProfileImage(){
        val userImageUri = Prefs.getUserImageURI(requireContext())
        if(userImageUri != null && userImageUri.isNotEmpty()){
            Glide.with(requireContext()).load(userImageUri).apply(RequestOptions.circleCropTransform())
                .into(binding.imgUserImageProfile)
        }else{
            val defaultProfileImageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://tutorapp-c7511.appspot.com/Default_Resources/default_user_profile_image.png")
            defaultProfileImageRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(requireContext()).load(uri).apply(RequestOptions.circleCropTransform())
                    .into(binding.imgUserImageProfile)
            }.addOnFailureListener { exception->
                Log.e("ProfileFragment","Error loading default profile image : ${exception.message}")
            }
        }

    }
    private fun pickProfilePhoto(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type ="image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if (resultCode == PICK_IMAGE_REQUEST_CODE){
                data?.data?.let { uri ->
                    startCrop(uri)
                }
            }else if (requestCode == UCrop.REQUEST_CROP){
                val resultUri = UCrop.getOutput(data!!)
                resultUri?.let {
                    uploadProfilePhoto(it)
                }
            }
        }else if(resultCode == UCrop.RESULT_ERROR){
            val error = UCrop.getError(data!!)
            Log.e("ProfileFragment","Crop error: ${error?.localizedMessage}")
        }
    }
    private fun startCrop(uri: Uri){
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir,"cropped"))
        UCrop.of(uri,destinationUri)
            .withAspectRatio(1f,1f).start(requireContext(), requireParentFragment())
    }

    private fun uploadProfilePhoto(imageUri: Uri){
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        val data = outputStream.toByteArray()
        val email = Prefs.getUSerEmailEncoded(requireContext())
        val fileName = "${email}${System.currentTimeMillis()}.jpg"//pending
        val profilePhotoRef = FirebaseStorage.getInstance().reference.child("profile_photos/$fileName")
        val uploadTask = profilePhotoRef.putBytes(data)
        uploadTask.addOnSuccessListener {taskSnapshot->
            profilePhotoRef.downloadUrl.addOnSuccessListener { uri->
                //SharedPrefs
                Prefs.saveUserImageURI(requireContext(), uri.toString())

                Glide.with(requireContext()).load(uri).apply(RequestOptions.circleCropTransform())
                    .into(binding.imgUserImageProfile)
                getToastShort(requireContext(),"Profile photo uploaded")
            }
        }.addOnFailureListener{e ->
            Log.e("ProfileFragment", "Error uploading profile photo: ${e.message}")
        }
    }
    companion object{
        private const val STORAGE_PERMISSION_REQUEST_CODE = 2001
        private const val PICK_IMAGE_REQUEST_CODE = 2002
    }

}