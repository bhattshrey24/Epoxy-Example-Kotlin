package com.example.epoxyexamplekotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.epoxyexamplekotlin.databinding.NewsListItemBinding

//todo
// Made this app by referring this playlist : https://www.youtube.com/watch?v=YrubJxocEAQ&list=PLLgF5xrxeQQ3qE_bIE4h6_YLedATiVqK0&index=22
// I will do a comparison as to how epoxy model makes it easier to bind data compared to recycler view
// This implementation of recyclerview is quite different since this way its easier to see the difference between recycler view and Epoxy
// In this Example Lets assume we are making list of news where user can click on a news and go to a new page

class NewsRecyclerViewAdapter(
    private val onClickCallBack: (Int) -> Unit // simple call back mechanism
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() { // OBSERVE here we are not specifying our
// ViewHolder here Like generally we would have
// done "RecyclerView.Adapter<NewsRecyclerViewAdapter.MyViewHolder>() ",
// Just compare it with normal recycler view adapter implementation in my news app

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(parent) // we are not inflating layout here instead we are
        // doing it in our view holder class. OBSERVE :  we are passing the 'ViewGroup' to "MyViewHolder"
        // constructor which we will use in our view holder inner class to inflate the view
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //todo: we have to cast our Holder (ie. holder as MyViewHolder) because above we haven't specified the type of view holder therefore we have to do it here explicitly
        (holder as MyViewHolder).onBind(listOfNews[position], onClickCallBack) // We are not binding our view holder with data here instead we are doing it in our view holder class
    }

    override fun getItemCount(): Int {
        return listOfNews.size
    }

    private val listOfNews= ArrayList<News>()

    inner class MyViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.news_list_item, parent, false) // This is where we are
        // inflating our view , This statement will simply return the inflated
        // view which we can use inside our inner class using 'itemView' variable
    ) {
        private val binding = NewsListItemBinding.bind(itemView) // this is how we can use
        // binding in our view holder . This 'itemView' is the view that
        // we inflated using this
        // statement in above constructor 'LayoutInflater.from(parent.context).inflate(R.layout.news_list_item, parent, false) // This is where we are'


        fun onBind(model: News, onClicked: (Int) -> Unit) {  // this will bind our view holders with data since we are calling this function in 'onBindViewHolder'
            binding.tvTitle.text = model.title
            binding.tvSubTitle.text = model.subTitle

            binding.root.setOnClickListener {
                onClicked(model.id)
            }

        }
    }

    // Compare this with the above inner class
    data class NewsEpoxyModel(
        val news: News,
        val onClicked: (Int) -> Unit
    ) : ViewBindingKotlinModel<NewsListItemBinding>(R.layout.news_list_item) { // this is how we connect ViewBinding With our EpoxyModel

        override fun NewsListItemBinding.bind() { // This is an inbuilt function provided by Epoxy Library
            tvTitle.text = news.title // Observe here we do not have to use 'binding' instead we can directly refer to the views
            tvSubTitle.text = news.subTitle

            root.setOnClickListener { // this root is actually 'binding.root'
                onClicked(news.id)
            }
        }
    }


}