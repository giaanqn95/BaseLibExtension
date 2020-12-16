package vn.com.baselibextension

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import vn.com.baselibextension.extensions.launchWhenStartedUntilStopped

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.isSuccess.observe(this){
            Toast.makeText(this,"$it", Toast.LENGTH_LONG).show()
        }
        viewModel.mergeFunc()
        viewModel.stateFlow.onEach {

        }.launchWhenStartedUntilStopped(this)
    }
}