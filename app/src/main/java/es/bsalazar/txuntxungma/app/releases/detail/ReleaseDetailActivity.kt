package es.bsalazar.txuntxungma.app.releases.detail

import android.annotation.TargetApi
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import es.bsalazar.txuntxungma.R
import es.bsalazar.txuntxungma.domain.entities.Release
import es.bsalazar.txuntxungma.utils.Constants
import kotlinx.android.synthetic.main.activity_release_detail.*
import java.text.SimpleDateFormat
import java.util.*

class ReleaseDetailActivity : AppCompatActivity(){

    val dateFormat : SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))
    lateinit var release: Release

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_release_detail)

        release = getIntent().getSerializableExtra(Constants.EXTRA_KEY_RELEASE) as Release

        setView()

        container_release_detail.setOnClickListener { onBackPressed() }
    }

    private fun setView() {
        release_name.text = release.title
        release_date.text = dateFormat.format(release.date)
        release_description.text = release.description
    }
}