package com.example.joshu.ui.settings.premium

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import butterknife.BindView
import butterknife.ButterKnife
import com.example.joshu.R
import com.google.android.material.button.MaterialButton
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class PremiumDialogFragmentImpl : DialogFragment() {

    companion object{
        fun show(context: Context){
            val fragmentTransitionImpl =
                (context as FragmentActivity).supportFragmentManager.beginTransaction()
            val premiumDialog = PremiumDialogFragmentImpl()
            premiumDialog.show(fragmentTransitionImpl, "PremiumDialogFragment")
        }
    }

    @BindView(R.id.cards)
    lateinit var cardsPager: ViewPager2

    @BindView(R.id.dots_indicator)
    lateinit var dotsIndicator: DotsIndicator

    @BindView(R.id.buyButton)
    lateinit var buyButton: MaterialButton

    @BindView(R.id.allVariantsButton)
    lateinit var allVariantsButton: MaterialButton

    @BindView(R.id.btn_close)
    lateinit var closeButton: ImageView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        initPremiumDialog(dialog)
        return dialog
    }

    private fun initPremiumDialog(dialog: Dialog) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_premium, null)
        ButterKnife.bind(this, view)
        val advertContent = listOf(
            AdvertContent(R.drawable.advert1, getString(R.string.premium_advert_1)),
            AdvertContent(R.drawable.advert2, getString(R.string.premium_advert_2)),
            AdvertContent(R.drawable.advert3, getString(R.string.premium_advert_3))
        )
        cardsPager.adapter = ViewPagerAdapter(advertContent)
        dotsIndicator.setViewPager2(cardsPager)
        buyButton.setText(R.string.premium_buy_button)
        buyButton.setOnClickListener {
            openJoshuLandingPage()
        }
        allVariantsButton.setOnClickListener {
            openJoshuLandingPage()
        }
        closeButton.setOnClickListener {
            dismiss()
        }
        dialog.setContentView(view)
    }

    private fun openJoshuLandingPage() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.premium_url)))
        startActivity(intent)
    }

}