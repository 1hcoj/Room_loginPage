package com.hyeon.studyrecord

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class LoginPageFragment : Fragment() {
    // Fragment 를 생성하면 호출되는 함수인가...?
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment가 연결된 상위 Activity 받아오기
        val mActivity = activity as MainActivity  // MainActivity 는 class이다.
        //  Layout 에서 설계한 화면 출력
        val rootView = inflater.inflate(R.layout.fragment_login_page,container,false)
        // Fragment의 Layout.xml 파일에 등록된 Button 찾기
        val btn_signUp : Button= rootView.findViewById(R.id.goSignUpButton)
        // Event Listener 등록
        btn_signUp.setOnClickListener {
            // 상위 Activity클래스의 멤버 메소드 받아오기
            mActivity.changeFragment(4)
        }

        return rootView
    }

}