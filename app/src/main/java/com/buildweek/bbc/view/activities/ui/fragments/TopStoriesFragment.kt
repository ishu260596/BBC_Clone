package com.buildweek.bbc.view.activities.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.buildweek.bbc.R
import com.buildweek.bbc.view.activities.ui.activities.DetailedNewsViewActivity
import com.buildweek.bbc.view.activities.ui.model.LocalServerNews
import com.buildweek.bbc.view.activities.ui.model.LocalServerNewsItem
import com.buildweek.bbc.view.activities.ui.recyclerviews.LocalServerRecyclerAdapter
import com.buildweek.bbc.view.activities.ui.viewmodel.MainViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.fragment_africa.*
import kotlinx.android.synthetic.main.fragment_top_stories.*
import kotlinx.android.synthetic.main.item_inshots_layout.*

class TopStoriesFragment : Fragment(), LocalServerRecyclerAdapter.OnItemClickListener {

    lateinit var viewModel: MainViewModel
    lateinit var adapter: LocalServerRecyclerAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var progressBar: ProgressBar
    lateinit var youTubePlayerView: YouTubePlayerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_stories, container, false)
    }

    companion object {
        fun newInstance() = TopStoriesFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.topNewsSwipeRefreshLayout)
        progressBar = view.findViewById<ProgressBar>(R.id.topNewsProgressBar)
        youTubePlayerView = view.findViewById<YouTubePlayerView>(R.id.youtubePlayer1)

        setRecyclerView()

        swipeRefreshLayout.setOnRefreshListener {
            setRecyclerView()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setRecyclerView() {
        progressBar.visibility = View.VISIBLE
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.worldNews()
        viewModel.getLocalServerNews().observe(viewLifecycleOwner, Observer {
            adapter = context?.let { it1 -> LocalServerRecyclerAdapter(it1, it, this) }!!
            inShotsRecyclerViewTopStories.adapter = adapter
            inShotsRecyclerViewTopStories.layoutManager = LinearLayoutManager(context)
            progressBar.visibility = View.GONE
            youTubePlayerView.visibility = View.VISIBLE

            val layoutAnimationController: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
            inShotsRecyclerViewTopStories.layoutAnimation = layoutAnimationController
            if(it == null)
                inShotsRecyclerViewAfrica.visibility = View.GONE

            adapter.notifyDataSetChanged()
            inShotsRecyclerViewTopStories.scheduleLayoutAnimation()

        })
    }

    override fun onItemClicked(article: LocalServerNewsItem) {
        val intent = Intent(activity, DetailedNewsViewActivity::class.java)
        intent.putExtra("article", article)
        startActivity(intent)
    }
}