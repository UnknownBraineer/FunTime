
package com.raywenderlich.funtime.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.raywenderlich.funtime.R
import com.raywenderlich.funtime.data.network.model.ApiVideo

class MainActivity : AppCompatActivity(), MainContract.View {

  private lateinit var progressBar: ProgressBar
  private lateinit var videosList: RecyclerView
  private lateinit var emptyText: TextView
  private lateinit var presenter: MainContract.Presenter
  private lateinit var videosAdapter: MainAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    init()
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.deactivate()
  }

  override fun renderVideos(videos: List<ApiVideo>) {
    hideLoadingIndicator()
    hideEmptyView()
    videosAdapter.onVideosUpdate(videos)
  }

  override fun showErrorMessage() {
    hideLoadingIndicator()
    showEmptyView()
  }

  private fun init() {
    progressBar = findViewById(R.id.pb_main)
    videosList = findViewById(R.id.rv_videos)
    emptyText = findViewById(R.id.tv_empty)

    initializeRecyclerView()

    presenter = MainPresenter(this)

    presenter.fetchSampleVideos()
    showLoadingIndicator()
    hideEmptyView()
  }

  private fun initializeRecyclerView() {
    videosList.layoutManager = LinearLayoutManager(this)
    videosList.setHasFixedSize(true)

    videosAdapter = MainAdapter()
    videosAdapter.onItemClick().subscribe(this::onVideoItemClick)
    videosList.adapter = videosAdapter
  }

  private fun onVideoItemClick(video: ApiVideo) {
    presenter.showVideoScreen(createVideoUrl(video))
  }

  private fun showLoadingIndicator() {
    progressBar.visibility = View.VISIBLE
  }

  private fun hideLoadingIndicator() {
    progressBar.visibility = View.GONE
  }

  private fun hideEmptyView() {
    emptyText.visibility = View.GONE
  }

  private fun showEmptyView() {
    emptyText.visibility = View.VISIBLE
  }

  private fun createVideoUrl(video: ApiVideo) =
      "https://res.cloudinary.com/demo/video/${video.type}/v${video.version}/${video.publicId}.${video.format}"
}
