
package com.raywenderlich.funtime.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raywenderlich.funtime.R
import com.raywenderlich.funtime.data.network.model.ApiVideo
import com.raywenderlich.funtime.databinding.VideoItemViewBinding
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

class MainAdapter : RecyclerView.Adapter<MainAdapter.VideoViewHolder>() {

  companion object {
    const val CLICK_THROTTLE_WINDOW_MILLIS = 300L
  }

  private val onVideoClickSubject: Subject<ApiVideo> = BehaviorSubject.create()

  private var videos: List<ApiVideo> = ArrayList()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
    val binding = VideoItemViewBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false)
    return VideoViewHolder(binding, onVideoClickSubject)
  }

  override fun onBindViewHolder(holder: VideoViewHolder, position: Int) = holder.setVideo(videos[position])

  override fun getItemCount() = videos.size

  fun onVideosUpdate(videos: List<ApiVideo>) {
    this.videos = videos
    notifyDataSetChanged()
  }

  fun onItemClick() = onVideoClickSubject.throttleFirst(CLICK_THROTTLE_WINDOW_MILLIS, TimeUnit.MILLISECONDS)

  class VideoViewHolder(val binding: VideoItemViewBinding,
                        private val clickSubject: Subject<ApiVideo>
                        ) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var video: ApiVideo

    fun setVideo(video: ApiVideo) {
      this.video = video
      binding.tvMainVideoTitle.text = video.publicId
      binding.mainVideoItemContainer.setOnClickListener { onMovieClick() }
    }

    private fun onMovieClick() = clickSubject.onNext(video)
  }
}