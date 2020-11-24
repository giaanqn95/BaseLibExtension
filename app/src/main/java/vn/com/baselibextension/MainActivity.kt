package vn.com.baselibextension

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import vn.com.baselibextension.extensions.launchWhenStartedUntilStopped

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var listA = arrayListOf<String>()
    private var listB = arrayListOf<Int>()

    private val flowS: Flow<Int> = flow {
        for (i in 0..3) {
            emit(i)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.mergeFunc()
        viewModel.stateFlow.onEach {

        }.launchWhenStartedUntilStopped(this)
        runBlocking {
            viewModel.stateFlow.onEach {
                print(it)
            }.collect {

            }
            flowS.filter { it % 2 == 0 }.collect { }

            val result = sequence {
                val first = listA.iterator()
                val second = listB.iterator()
                while (first.hasNext() && second.hasNext()) {
                    yield(NewObject(first.next(), second.next()))
                }
            }.toList()
        }
    }
}

class NewObject(val a: String, val b: Int)