package com.example.graduation_at_project.signup

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.graduation_at_project.MainActivity
import com.example.graduation_at_project.databinding.FragmentSingUpBinding
import com.example.graduation_at_project.model.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SingUpFragment : Fragment() {
    private lateinit var binding : FragmentSingUpBinding
    private lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSingUpBinding.inflate(inflater, container, false).let {
            binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Creating Acount")
        progressDialog.setMessage("We' re creating your acount")

        binding.btnsignup.setOnClickListener {
            auth.createUserWithEmailAndPassword(
                binding.edtEmailUser.text.toString(),
                binding.edtPasswordUser.text.toString()
            ).addOnCompleteListener(this::onComplete);
        }
    }

    private fun onComplete(task: Task<AuthResult>) {
        progressDialog.dismiss()
        if (task.isSuccessful) {
            val user = Users(
                binding.edtNameUser.text.toString(),
                binding.edtEmailUser.text.toString(),
                binding.edtPasswordUser.text.toString()
            )
            val id = task.result!!.user!!.uid
            database.reference.child("Users").child(id).setValue(user)
            Toast.makeText(requireContext(), "thêm tài khoản thành công", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(
                requireContext(),
                "Thất bại!! Vui lòng kiểm tra lại thông tin",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}