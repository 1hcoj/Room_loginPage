package com.hyeon.studyrecord

import android.content.ContentUris
import android.content.Context
import android.graphics.DiscretePathEffect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*

private val TAG:String = "디버그"
class LoginPageFragment : Fragment() {
    private lateinit var mainActivity : MainActivity
    private lateinit var rootView : View
    private lateinit var buttons : List<Button>
    private lateinit var edits : List<EditText>
    private lateinit var texts :List<TextView>
    private lateinit var checkbox : CheckBox
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        mainActivity = activity as MainActivity

        rootView = inflater.inflate(R.layout.fragment_login_page,container,false)
        /** ( 0 ) : 로그인 화면으로 가기
         *  ( 1 ) : 로그인
         *  ( 2 ) : 비밀번호찾기
         *  ( 3 ) : 아이디찾기
         *  ( 4 ) : 회원가입하기
         * **/
        buttons = listOf(
            rootView.findViewById<Button>(R.id.HomeButton),
            rootView.findViewById<Button>(R.id.loginButton),
            rootView.findViewById<Button>(R.id.findPasswordButton),
            rootView.findViewById<Button>(R.id.findIdButton),
            rootView.findViewById<Button>(R.id.goSignUpButton),
        )
        /** ( 0 ) : 아이디 입력
         *  ( 1 ) : 비밀번호 입력
         * **/
        edits = listOf(
            rootView.findViewById(R.id.idEditText),
            rootView.findViewById(R.id.pwdEditText)
        )
        texts = listOf(
            rootView.findViewById(R.id.loginIdInforTextView),
            rootView.findViewById(R.id.loginPwdInfoTextView)
        )
        checkbox = rootView.findViewById(R.id.loginCheckBox)

        buttons[0].setOnClickListener {

        }

        buttons[1].setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val enterId: String = edits[0].text.toString()
                val enterPwd :String = edits[1].text.toString()
                var toggle : Boolean = true

                if (enterId == "") {
                    texts[0].text = "아이디를 입력하세요"
                    texts[0].setTextColor(ContextCompat.getColor(mainActivity,R.color.red))
                    toggle = false
                }
                if (enterPwd == "") {
                    texts[1].text = "비밀번호를 입력하세요"
                    texts[1].setTextColor(ContextCompat.getColor(mainActivity,R.color.red))
                    toggle = false
                }
                if (toggle){
                    val job1 = CoroutineScope(Dispatchers.Default).async {
                        var pwd : String =
                            withContext(Dispatchers.IO){
                                mainActivity.db.userDao().getPwd(enterId)
                            }
                        Log.d(TAG,"$pwd")

                        pwd == enterPwd
                    }
                    var result1 = job1.await()
                    if (result1){
                        mainActivity.changeFragment(5)
                    }
                }

            }
        }

        buttons[4].setOnClickListener {
            mainActivity.changeFragment(4)
        }

        return rootView
    }

}