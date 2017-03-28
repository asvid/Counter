package asvid.counter.modules.app_info

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asvid.counter.R
import asvid.counter.R.string
import kotlinx.android.synthetic.main.activity_app_info.mailButton
import kotlinx.android.synthetic.main.activity_app_info.rateButton

class AppInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info)
        setActionBar()
        setView()
    }

    private fun setView() {
        rateButton.setOnClickListener {
            openGooglePlay()
        }
        mailButton.setOnClickListener {
            openEmail()
        }
    }

    private fun openEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto", "adam.swiderski89@gmail.com", null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Counter")
        emailIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.email_text))
        startActivity(Intent.createChooser(emailIntent, ""))
    }

    private fun openGooglePlay() {
        val appPackageName: String = packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appPackageName)))
        } catch (anfe: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)))
        }
    }

    private fun setActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = resources.getString(string.app_info)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
