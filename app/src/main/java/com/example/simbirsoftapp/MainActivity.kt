package com.example.simbirsoftapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.simbirsoftapp.databinding.ActivityMainBinding
import com.example.simbirsoftapp.fragments.TasksFragment
import io.realm.Realm
import io.realm.RealmConfiguration

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mRealm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            .schemaVersion(7)
            .deleteRealmIfMigrationNeeded()
            .build()
        mRealm = Realm.getInstance(realmConfiguration)
        setContentView(binding.root)
        begin()
    }

    private fun begin() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, TasksFragment(mRealm))
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        mRealm.close()
    }
}