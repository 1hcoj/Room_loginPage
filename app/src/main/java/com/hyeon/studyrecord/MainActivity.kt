package com.hyeon.studyrecord

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hyeon.studyrecord.database.UserDataBase
import com.hyeon.studyrecord.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var db: UserDataBase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 이전에 저장된 fragment가 있으면 , fragment 추가필요 x
        if (binding.LoginFragment != null){
            if (savedInstanceState != null)
                return
        }
        supportFragmentManager.beginTransaction().apply {
            add(R.id.LoginFragment,LoginPageFragment())
            commit()
        }
        // db 생성
        db = UserDataBase.getInstance(applicationContext)!!

    }
    fun changeFragment(index :Int) {
        when(index){
            1->{
                setFragment(LoginPageFragment())
            }
            2->{

            }
            3->{

            }
            4->{
                setFragment(SignUpFragment())
            }
        }

    }
    private fun setFragment(toChangeFrag : Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.LoginFragment,toChangeFrag).commit()
    }
}