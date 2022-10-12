package com.example.chapter6top2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import com.example.chapter6top2.databinding.ActivityMainBinding
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.btnSaveKey.setOnClickListener { 
            lifecycleScope.launch {
                save(
                    binding.etInputEnterKeySave.text.toString(),
                    binding.etInputEnterValueSave.text.toString()
                )
            }
        }

        binding.btnReadKey.setOnClickListener {
            lifecycleScope.launch {
                val value = read(binding.etInputEnterKeyRead.text.toString())
                binding.tvReadValue.text = value?: "No Value found"
            }
        }

        binding.tvGotoProtoDataStore.setOnClickListener {
            startActivity(Intent(this, ProtoActivity::class.java))
        }

    }

    private suspend fun save(key: String, value: String){
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String?{
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}