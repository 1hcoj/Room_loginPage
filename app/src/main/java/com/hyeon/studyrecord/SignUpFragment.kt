package com.hyeon.studyrecord

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.hyeon.studyrecord.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.Key
import kotlin.math.sign

// 하고싶은 것
// mActivity 하나로 받는 것
private val TAG :String = "디버그"
class SignUpFragment : Fragment() {
    // SignUpFragment 의 View 객체
    private lateinit var rootView:View
    private lateinit var mActivity : MainActivity
    private lateinit var signUpConditions : Array<Boolean>
    private lateinit var buttons: Array<Button>
    private lateinit var editTexts: Array<EditText>
    private lateinit var textViews : Array<TextView>
    private lateinit var errorMsg : Array<String>
    private lateinit var successMsg : Array<String>
    private lateinit var userId : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mActivity = activity as MainActivity
        rootView = inflater.inflate(R.layout.fragment_sign_up, container, false)
        /*
         * [0] : 중복 체크 버튼
         * [1] : 회원가입 버튼
         * [2] : 홈 버튼
         */
        buttons = arrayOf(
            rootView.findViewById<Button>(R.id.idCheckButton),
            rootView.findViewById<Button>(R.id.signUpButton),
            rootView.findViewById<Button>(R.id.SignHomeButton)
        )
        /*
         * [0] : 아이디
         * [1] : 비밀번호
         * [2] : 비밀번호 확인
         * [3] : 닉네임
         */
        editTexts = arrayOf(
            rootView.findViewById<EditText>(R.id.signIdEditText),
            rootView.findViewById(R.id.signPwdEditText),
            rootView.findViewById(R.id.checkPwdEditText),
            rootView.findViewById(R.id.signNickEditText)
        )
        /*
        * [0] : 아이디 안내
        * [1] : 비밀번호 안내
        * [2] : 비밀번호 확인 안내
        * [3] : 닉네임 안내
        * */
        textViews = arrayOf(
            rootView.findViewById(R.id.idInforTextView),
            rootView.findViewById(R.id.pwdInforTextView),
            rootView.findViewById(R.id.pwdCheckInforTextView),
            rootView.findViewById(R.id.nickInforTextView)
        )

        signUpConditions = arrayOf(false,false,false,true)
        /*
        * [0] : 아이디 조합
        * [1] : 아이디 중복
        * [2] : 비밀번호 필수 정보알림
        * [3] : 비밀번호 조합
        * [4] : 비밀번호 불일치
        * [6] : 닉네임 중복
        * */
        errorMsg = resources.getStringArray(R.array.error_msg)
        /*
        * [0] : 아이디 만족
        * [1] : 비밀번호 조합
        * [2] : 비밀번호 일치
        * [3] : 닉네임 만족
        * */
        successMsg = resources.getStringArray(R.array.success_msg)

        buttons[0].setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                signUpConditions[0] =
                    withContext(Dispatchers.Default) {
                        checkId(editTexts[0].text.toString())
                    }
                if (signUpConditions[0]) userId = editTexts[0].text.toString()
            }
        }
        buttons[1].setOnClickListener {
            var result = StringBuilder()

            if (editTexts[0].text.isNotEmpty()
                &&userId == editTexts[0].text.toString()
                && signUpConditions[0] && signUpConditions[1]
                && signUpConditions[2] &&signUpConditions[3]){
                addUser()
                result.append("회원가입 성공")
                mActivity.changeFragment(1)
            }
            else if (editTexts[0].text.isEmpty() ||userId != editTexts[0].text.toString() || !signUpConditions[0]){
                result.append( "아이디 중복 확인하세요\n")
            }
            else if (!signUpConditions[1]){
                result.append("비밀번호 조건확인\n")
            }
            else if (!signUpConditions[2]){
                result.append(errorMsg[4]+"\n")
            }
            else {
                result.append(errorMsg[6]+"\n")
            }
            Toast.makeText(mActivity,result.toString(),Toast.LENGTH_LONG).show()
        }
        buttons[2].setOnClickListener {
            mActivity.changeFragment(1)
        }
        // 비밀번호 확인
        editTexts[2].setOnClickListener {
            if (editTexts[1].text.isEmpty()) {
                signUpConditions[1] = false
                textViews[1].text = errorMsg[2]
                textViews[1].setTextColor(ContextCompat.getColor(mActivity,R.color.red))
            }
            else if (checkCondition(editTexts[1].text.toString(), "pwd"))
            {
                textViews[1].text = successMsg[1]
                textViews[1].setTextColor(ContextCompat.getColor(mActivity,R.color.green))
                signUpConditions[1] = true
            }
            else {
                textViews[1].text = errorMsg[3]
                textViews[1].setTextColor(ContextCompat.getColor(mActivity,R.color.red))
                signUpConditions[1] = false
            }
        }
        // 닉네임 확인
        editTexts[3].setOnClickListener {
            // 비밀번호 확인이 공백인 경우 ->
            if (editTexts[2].text.isEmpty()) {
                textViews[2].text = errorMsg[4]
                textViews[2].setTextColor(ContextCompat.getColor(mActivity,R.color.red))
                signUpConditions[2] = false
            }
            else if (editTexts[2].text.toString() == editTexts[1].text.toString()) {
                textViews[2].text = successMsg[2]
                textViews[2].setTextColor(ContextCompat.getColor(mActivity,R.color.green))
                signUpConditions[2] = true
            }
            else{
                textViews[2].text = errorMsg[4]
                textViews[2].setTextColor(ContextCompat.getColor(mActivity,R.color.red))
                signUpConditions[2] = false
            }
        }

        return rootView
    }

    private suspend fun checkId(id :String) : Boolean{
        var toggle : Boolean = false
        if (checkCondition(id,"id")){
            toggle =
                withContext(Dispatchers.IO){
                    !(mActivity.db.userDao().checkUser(id))
                }
            // 아이디 검사 성공
            if (toggle){
                textViews[0].text = successMsg[0]
                textViews[0].setTextColor(ContextCompat.getColor(mActivity,R.color.green))
                // error 발생구간 : AlertDialog -> Dispatchers.Default ( 백그라운드 ) 에서 생성 불가
                makeAlert(1,true,0)
            }
            // 아이디 중복
            else{
                textViews[0].text = errorMsg[1]
                textViews[0].setTextColor(ContextCompat.getColor(mActivity,R.color.red))
                makeAlert(1,false,1)
            }
        }
        // 아이디 검사 실패 ( 형식 미준수 )
        else {
            textViews[0].text = errorMsg[0]
            textViews[0].setTextColor(ContextCompat.getColor(mActivity,R.color.red))
            makeAlert(1,false,0)
        }
        return toggle
    }

    private fun makeAlert(type:Int,check :Boolean ,index :Int){
        var str :String = ""
        var str2 :String = ""
        when (type){
            1-> str = "아이디 검사"
            2-> str = "비밀번호 검사"
            3-> str = "닉네임 체크"
        }
        str2 = when(check) {
            true -> successMsg[index]
            false -> errorMsg[index]
        }
        CoroutineScope(Dispatchers.Main).launch {
            AlertDialog.Builder(mActivity).apply {
                setTitle(str)
                setMessage(str2)
                setNegativeButton("Cancel", null)
                show()
            }
        }
    }

    private fun checkCondition(id :String, type: String) : Boolean{
        var toggle :Boolean = false
        var basicLength = 0
        when (type){
            "id"-> basicLength = 6
            "pwd"-> basicLength =8
        }
        if (id.length >= basicLength){
            for (c : Char in id){
                if (c.isLetterOrDigit()) toggle = true
                else {
                    toggle = false
                    break
                }
            }
        }
        return toggle
    }


    private fun addUser(){

        var userInfor: List<String> = listOf(
            rootView.findViewById<EditText>(R.id.signIdEditText).text.toString(),
            rootView.findViewById<EditText>(R.id.signPwdEditText).text.toString(),
            rootView.findViewById<EditText>(R.id.signNickEditText).text.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            launch{ mActivity.db.userDao().insertUser(User(userInfor[0],userInfor[1],userInfor[2]))}
        }
        /*
        *   var toggle : Boolean = CoroutineScope(Dispatchers.IO).launch{ Boolean 값을 반환하는 함수} ( error )
        *   CoroutineScope 는 Job (작업)을 반환하는 함수이다...!!
        *
        *   왜 안될까?
        *       > 1. toggle 은 Main Thread 의 변수이다.
        *       > 2. userDao.checkUser()는 IO Thread 의 함수이다.
        *       > 3. toggle 에 checkUser 의 결과를 사용하기 위해서는 Main Thread 를 일시 정지하고,
        *           IO Thread 의 checkUser 를 완료하여야한다.
        *       > 4. withContext : suspend function -> CoroutineScope 내부에서만 작동한다 !!
        *       > 5. CoroutineScope(Dispatchers.Main).launch { } 를 사용하여 wrap 과정을 거친다.
        */
//        CoroutineScope(Dispatchers.Main).launch {
//            var toggle :Boolean =
//                withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
//                    mActivity.db.userDao().checkUser(userInfor[0])
//                }
            /*
            * 함수명 : CoroutineScope
            * 매개변수 : context : Context
            * 리턴형 : CoroutineScope
            * 함수설명 :
            *      > 매개변수 coroutine context를 wraps 하는 CoroutineScope를 생성한다.
            *      > Scope가 취소되면 하위 블럭도 취소된다...?
            */

            /*
             *  사용자를 DB에 저장하는 것은 UI와 동시에 진행해도 관계 없다
             *      > why? Thread.IO 의 결과가 Thread.Main 의 결과에 영향을 미치지 않기 때문
             */
//            if (toggle) {
//                CoroutineScope(Dispatchers.IO).launch {
//
//                    mActivity.db.userDao().insertUser(User(userInfor[0], userInfor[1], userInfor[2]))
//               }
//            }
//            else {
//                Log.d(TAG,"중복된 아이디가 있습니다.")
//            }
//        }


    }

    private fun refreshUserList(){
        val mActivity = activity as MainActivity
        var userList = "유저리스트\n"
        /* CoroutineScope :
         * withContext
         * Dispatchers : coroutine 실행에 사용되는 thread 확인
         *      > Dispatchers.Main
         *      > Dispatchers.IO
         *      > Dispatchers.Default
         * CoroutineContext
         */
        CoroutineScope(Dispatchers.Main).launch {
            val users : List<User> =
                /*
                 * 함수명 : withContext ( suspend funtion ) what is suspend function
                 * 매개변수 : context : CoroutineContext , block : suspend coroutineScope.() -> T
                 * 리턴형 : T
                 * 함수설명 :
                 *      > parameter context 를 사용하여 block 코드( suspend function )를 실행
                 *      > block 이 완료될 때까지 ( Dispatchers.Main : parent coroutineScope )일시정지
                 *      > 결과 return
                 * 왜 사용하는가?
                 *      > Thread.IO 의 실행결과가 Thread.Main 의 출력 결과에 영향을 미치기 때문
                 */

                /*
                 * 호출 -> 기본 안전 함수  ?? ( 해결할 문제 )
                 */
                withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                    mActivity.db.userDao().getAll()
                }
            for (user in users){
                Log.d( TAG,"ID :${user.userId}")
//                CoroutineScope(Dispatchers.IO).launch {
//                    mActivity.db.userDao().deleteUser(user)
//                }
            }
        }
    }
}