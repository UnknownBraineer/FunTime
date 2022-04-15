
package com.raywenderlich.funtime.ui.video

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.raywenderlich.funtime.databinding.ActivityVideoViewBinding

class VideoViewActivity : AppCompatActivity(), VideoViewContract.View {

  companion object {
    const val VIDEO_URL_EXTRA = "video_url_extra"
  }

  private lateinit var binding: ActivityVideoViewBinding
  private lateinit var presenter: VideoViewContract.Presenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityVideoViewBinding.inflate(layoutInflater)
    setContentView(binding.root)
    init()
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.deactivate()
  }


  override fun onPause() {
    super.onPause()
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
      presenter.releasePlayer()
    }
  }

  override fun onStop() {
    super.onStop()
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
      presenter.releasePlayer()
  }

  private fun init() {
    presenter = VideoViewPresenter(this)
    binding.epVideoView.player = presenter.getPlayer().getPlayerImpl(this)

    val videoUrl = intent.getStringExtra(VIDEO_URL_EXTRA)
    presenter.play(videoUrl!!)
  }
}
