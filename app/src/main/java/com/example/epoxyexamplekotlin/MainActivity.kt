package com.example.epoxyexamplekotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epoxyexamplekotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater, null, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var newsEpoxyController = NewsEpoxyController { newsId ->
            // Do what you want to do in the OnClick of the
            // news like navigate to the new screen
            Toast.makeText(baseContext, "Pressed $newsId News", Toast.LENGTH_SHORT).show()
        }

        //binding.myEpoxyRecyclerView.adapter = newsEpoxyController.adapter  // Do this if you are using normal recyclerAdapter with Epoxy
        binding.myEpoxyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.myEpoxyRecyclerView.setController(newsEpoxyController) // Its like setting adapter to the EpoxyRecyclerView
        newsEpoxyController.isLoading = true
        GlobalScope.launch {
            delay(3000)
            newsEpoxyController.listOfNews = DummyData.listOfNews as ArrayList<News>
        }
    }

}