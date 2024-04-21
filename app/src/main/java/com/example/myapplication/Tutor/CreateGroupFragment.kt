package com.example.myapplication.Tutor

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.Data.DataModel
import com.example.myapplication.Data.Prefs
import com.example.myapplication.Functions.CommonFunctions
import com.example.myapplication.Functions.CommonFunctions.getToastShort
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentCreateGroupBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.yalantis.ucrop.UCrop
import java.io.ByteArrayOutputStream
import java.io.File

class CreateGroupFragment : Fragment() {
    lateinit var binding: FragmentCreateGroupBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var displayImageUrl: Uri
    lateinit var coverImageUrl: Uri
    private var currentImageType: ImageType? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateGroupBinding.inflate(inflater, container, false)
        firebaseDatabase = Firebase.database
        databaseReference = firebaseDatabase.getReference("Groups")
        binding.imgCreateGroupEditCoverImage.setOnClickListener {
            checkPermission(ImageType.COVER)
            currentImageType = ImageType.COVER
        }
        binding.imgCreateGroupEditDP.setOnClickListener {
            checkPermission(ImageType.DISPLAY)
            currentImageType = ImageType.DISPLAY
        }
        binding.btnCreateGroup.setOnClickListener {
            createGroup()
        }

        return binding.root
    }

    private fun checkPermission(imageType: ImageType){
        if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) != PackageManager.PERMISSION_GRANTED){
            requestStoragePermissions(imageType)
        }else{
            pickImage(imageType)
        }
    }

    private fun requestStoragePermissions(imageType: ImageType){
        requestPermissions(
            arrayOf(android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED),
            when(imageType){
                ImageType.COVER -> COVER_STORAGE_PERMISSION_REQUEST_CODE
                ImageType.DISPLAY -> DISPLAY_STORAGE_PERMISSION_REQUEST_CODE
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            COVER_STORAGE_PERMISSION_REQUEST_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImage(ImageType.COVER)
                }else{
                    showPermissionDeniedDialog()
                    CommonFunctions.getToastShort(
                        requireContext(),
                        "Please provide storage permissions"
                    )
                }
            }
            DISPLAY_STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage(ImageType.DISPLAY)
                } else {
                    showPermissionDeniedDialog()
                    CommonFunctions.getToastShort(
                        requireContext(),
                        "Please provide storage permissions"
                    )
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
        CommonFunctions.getToastShort(
            requireContext(),
            "Please provide storage permissions manually."
        )
    }

    private fun openAppSettings(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun pickImage(imageType: ImageType){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, when(imageType){
            ImageType.COVER -> COVER_PICK_IMAGE_REQUEST_CODE
            ImageType.DISPLAY -> DISPLAY_PICK_IMAGE_REQUEST_CODE
        })
    }

    private fun startCrop(uri: Uri, imageType: ImageType){
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir,"cropped"))
        val options = UCrop.Options().apply {
            setCompressionFormat(Bitmap.CompressFormat.JPEG)
            setCompressionQuality(90)
        }
        val uCrop = UCrop.of(uri, destinationUri)
        uCrop.withOptions(options)
        uCrop.withAspectRatio(1f, 1f)
        uCrop.start(requireContext(), this)

        when(imageType) {
            ImageType.COVER -> coverImageUrl = uri
            ImageType.DISPLAY -> displayImageUrl = uri
            else -> displayImageUrl = uri
        }

    }

    private fun uploadImageToFirebaseStorage(grpId: String,imageUrl: Uri, imageType: ImageType) {
        val storageRef = FirebaseStorage.getInstance().reference
        val groupName = binding.edtCreateGroupName.text.toString().trim()
        val imageName = if (imageType == ImageType.COVER) "cover_image.jpg" else "display_image.jpg"
        val imgRef = storageRef.child("group_resources/$groupName/$imageName")

        val uploadTask = imgRef.putFile(imageUrl)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            imgRef.downloadUrl.addOnSuccessListener { uri ->
                saveImageUrlToDatabase(grpId,imageType, uri.toString())
            }.addOnFailureListener { exception ->
                Log.e("CreateGroupFragment", "Error getting download URL: ${exception.message}")
            }
        }.addOnFailureListener { exception ->
            Log.e("CreateGroupFragment", "Error uploading image: ${exception.message}")
        }
    }
    private fun saveImageUrlToDatabase(grpId: String,imageType: ImageType, imageUrl: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Groups")

        val imageName = if (imageType == ImageType.COVER) "coverImage" else "displayImage"
        val imageUpdate = hashMapOf<String, Any>(imageName to imageUrl)

        databaseRef.child(grpId).updateChildren(imageUpdate)
            .addOnSuccessListener {
                Log.d("CreateGroupFragment", "Image URL saved to database successfully")
            }
            .addOnFailureListener { e ->
                Log.e("CreateGroupFragment", "Error saving image URL to database: ${e.message}")
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("CreateGroupFragment","onActivityResult: requestCode=$requestCode, resultCode=$resultCode")

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                DISPLAY_PICK_IMAGE_REQUEST_CODE -> {
                    Log.d("CreateGroupFragment","Picked display image successfully")
                    data?.data?.let { uri ->
                        startCrop(uri, ImageType.DISPLAY)
                    }
                }
                COVER_PICK_IMAGE_REQUEST_CODE -> {
                    Log.d("CreateGroupFragment","Picked cover image successfully")
                    data?.data?.let { uri ->
                        startCrop(uri, ImageType.COVER)
                    }
                }
                UCrop.REQUEST_CROP -> {
                    Log.d("CreateGroupFragment","UCrop.REQUEST CROP")
                    val resultUri = UCrop.getOutput(data!!)
                    resultUri?.let {
                        Log.d("CreateGroupFragment","Cropped image URI: $it")
                        when (currentImageType) {
                            ImageType.COVER -> loadCroppedImageIntoView(it, binding.imgCreateGroupCoverImage)
                            ImageType.DISPLAY -> loadCroppedImageIntoView(it, binding.imgGroupDP)
                            else -> return
                        }
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("CreateGroupFragment","Activity result canceled")
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val error = UCrop.getError(data!!)
            Log.e("CreateGroupFragment","Crop error: ${error?.localizedMessage}")
        }
    }

    private fun loadCroppedImageIntoView(uri: Uri, imageView: ImageView) {
        Glide.with(requireContext())
            .load(uri)
            .apply(RequestOptions().placeholder(R.drawable.profile_icon)) // Placeholder image if needed
            .into(imageView)
    }
    private fun createGroup(){
        if(!isInputInvalid()){
            val groupId = "grp"+ databaseReference.push().key!!
            val name = binding.edtCreateGroupName.text.toString().trim()
            val description = binding.edtCreateGroupDescription.text.toString().trim()
            val subject = binding.edtCreateGroupSubject.text.toString().trim()
            val tutor_uid = Prefs.getUID(requireContext())
            val students = listOf("Student1")

            val group = DataModel.Group(groupId,name, description,subject,tutor_uid!!,"","", students)
            databaseReference.child(groupId).setValue(group).addOnSuccessListener {
                Log.d("CreateGroupFragment", "Group created successfully")
                uploadImageToFirebaseStorage(groupId,displayImageUrl, ImageType.DISPLAY)
                uploadImageToFirebaseStorage(groupId,coverImageUrl, ImageType.COVER)
                getToastShort(requireContext(),"Group created")
            }.addOnFailureListener {
                Log.e("CreateGroupFragment", "Error creating group")
            }
        }

    }

    private fun isInputInvalid(): Boolean{
        var isInvalid = false
        if (binding.edtCreateGroupName.text.toString().trim().isEmpty()){
            binding.edtCreateGroupName.error = "Required field"
            isInvalid = true
        }
        if(binding.edtCreateGroupDescription.text.toString().trim().isEmpty()){
            binding.edtCreateGroupDescription.error = "Required field"
            isInvalid = true
        }
        if(binding.edtCreateGroupSubject.text.toString().trim().isEmpty()){
            binding.edtCreateGroupSubject.error = "Required field"
            isInvalid = true
        }
        return isInvalid
    }
    enum class ImageType{
        COVER,
        DISPLAY
    }
    companion object{
        private const val COVER_STORAGE_PERMISSION_REQUEST_CODE = 3001
        private const val DISPLAY_STORAGE_PERMISSION_REQUEST_CODE = 3002
        private const val PICK_IMAGE_REQUEST_CODE = 3003
        private const val COVER_PICK_IMAGE_REQUEST_CODE = 3004
        private const val DISPLAY_PICK_IMAGE_REQUEST_CODE = 3005
    }
}