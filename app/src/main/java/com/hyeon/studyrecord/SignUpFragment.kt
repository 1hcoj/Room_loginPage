package com.hyeon.studyrecord

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.hyeon.studyrecord.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpFragment : Fragment() {
    private lateinit var rootView:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val signUp_btn = rootView.findViewById<Button>(R.id.signUpButton)
        refreshUserList()
        signUp_btn.setOnClickListener {
            addUser()
        }
        val goBackHome:Button = rootView.findViewById(R.id.SignHomeButton)
        goBackHome.setOnClickListener {
            val mActivity = activity as MainActivity
            mActivity.changeFragment(1)
        }
        return rootView
    }
    private fun addUser(){
        val mActivity = activity as MainActivity
        var id :String= rootView.findViewById<EditText>(R.id.signIdEditText).text.toString()
        var pwd:String = rootView.findViewById<EditText>(R.id.signPwdEditText).text.toString()
        var nick:String = rootView.findViewById<EditText>(R.id.signNickEditText).text.toString()
        // coroutine 사용
        CoroutineScope(Dispatchers.IO).launch {
            mActivity.db.userDao().insertUser(User(id,pwd,nick))
        }
    }
    private fun refreshUserList(){
        val mActivity = activity as MainActivity
        var userList = "유저리스트\n"
        CoroutineScope(Dispatchers.Main).launch {
            val users =
                withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                    mActivity.db.userDao().getAll()
                }
            for (user in users){
                Log.d( "치현","ID :${user.userId}")
            }
        }
    }
}