package com.example.epoxyexamplekotlin

import com.airbnb.epoxy.EpoxyController
import com.example.epoxyexamplekotlin.databinding.HeaderListItemBinding
import com.example.epoxyexamplekotlin.databinding.MyLoadingStateBinding
import com.example.epoxyexamplekotlin.databinding.NewsListItemBinding

// First see the 'NewsRecyclerViewAdapter' class then come back here
class NewsEpoxyController(
    private val onClickCallBack: (Int) -> Unit
) : EpoxyController() { // See we are extending EpoxyController which very similar to RecyclerViewAdapter

    var isLoading: Boolean = false
        set(value) {
            field = value//field is 'isLoading' and 'value' is what we pass to the setter
            if (field) {
                requestModelBuild()// this is same as notifyDataSetChanged() of recycler view ie. it updates the recycler view with new data
            }
        }

    var listOfNews = ArrayList<News>()
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    override fun buildModels() { // This is the function which runs when
        // we call 'requestModelBuild()' . I guess Its like build function
        // of flutter like it changes the state by I guess rebuilding the view

        if (isLoading) { // So if its loading then it will show
            // LoadingEpoxyModel ( which basically contains
            // just a circular progress bar) instead of List Of News
            LoadingEpoxyModel().id("loading_state").addTo(this) // Here since we
            // only showing 1 view item (ie. circular Bar) therefore I guess Id doesn't matter therefore we set id to any random string
            return
        }
        //todo
        // LoadingEpoxyModel().id("loading_state").addIf(isLoading == true , this) // This will do same
        // thing as above statement. It is basically like wrapping the model
        // with an if condition. This makes code much more readable

        if (listOfNews.isEmpty()) {
            //todo show empty list state , These states are I guess other EpoxyModels containing UI that we want to show if like here listOfNews is Empty
            return
        }

        val list1 = listOfNews.filter {
            it.id <= 5
        }
        val list2 = listOfNews.filter {
            it.id >= 5
        }

        // observe below how beautiful it is like you
        // just have to write your models in the order in which
        // you want it to be shown in the list like here I want my header
        // to be shown before my list 1 so I put i before list1
        // And we can easily reuse the models too like see how we
        // used Header model 3 times here
        // You can relate Models to UI element of
        // the List ie. list items its basically a ViewHolder
        // Also It is so much more readible now

        HeaderEpoxyModel("1st List").id("h1").addTo(this)

        list1.forEach {
            NewsEpoxyModel(
                it,
                onClickCallBack
            ) // These models are I guess the UI that we want to show like here we want to show News Item
                .id(it.id)
                .addTo(this)
            // we have to provide id because its used by epoxy to load items , show animation and various other things
            //addTo() adds the model to the controller. So
            // since we are inside the controller ie. 'ActualNewsAdapter'
            // therefore 'this' will attach it to this model object
        }

        HeaderEpoxyModel("2nd List").id("h2").addTo(this)

        list2.forEach {
            NewsEpoxyModel(it, onClickCallBack).id(it.id).addTo(this)
        }

        HeaderEpoxyModel("List End").id("h3").addTo(this)

    }

    data class NewsEpoxyModel(
        val news: News,
        val onClicked: (Int) -> Unit
    ) : ViewBindingKotlinModel<NewsListItemBinding>(R.layout.news_list_item) { // we can describe
        // these models in different class files but I'm Doing it in same class
        // right now so that I could understand how it works
        override fun NewsListItemBinding.bind() {
            tvTitle.text = news.title
            tvSubTitle.text = news.subTitle

            root.setOnClickListener {
                onClicked(news.id)
            }
        }
    }

    data class HeaderEpoxyModel(val headerString: String) :
        ViewBindingKotlinModel<HeaderListItemBinding>(R.layout.header_list_item) {

        override fun HeaderListItemBinding.bind() {
            tvHeader.text = headerString
        }

    }

     class LoadingEpoxyModel :
        ViewBindingKotlinModel<MyLoadingStateBinding>(R.layout.my_loading_state) {
        override fun MyLoadingStateBinding.bind() {
            // nothing to bind here since we are just showing circular progress bar here
        }
    }

}