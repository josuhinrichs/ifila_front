package com.example.ifila_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.ifila_app.databinding.ActivityRegisterScreen1Binding
import com.example.ifila_app.databinding.ActivityUserHomeBinding

class UserHome : AppCompatActivity() {
    private lateinit var binding: ActivityUserHomeBinding
    var bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)
        val token = intent.extras?.get("token").toString()

        binding = ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bundle.putString("token", token)
        val starter_fragment = UserHomeFragment()
        starter_fragment.arguments = bundle
        replaceFragment( starter_fragment )

        binding.bottomNavigationView.setOnItemSelectedListener{

            when(it.itemId){
                R.id.nav_home -> {
                    val fragment = UserHomeFragment()
                    bundle.putString("token", token)
                    fragment.arguments = bundle
                    replaceFragment( fragment )}
                R.id.nav_queue -> {
                    val fragment = UserCodeQueueFragment()
                    bundle.putString("token", token)
                    fragment.arguments = bundle
                    replaceFragment( fragment )}
                R.id.nav_profile -> {
                    val fragment = UserSettingsFragment()
                    bundle.putString("token", token)
                    fragment.arguments = bundle
                    replaceFragment( fragment )}
                else -> true
            }
            true
        }
    }

    private fun replaceFragment( fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}