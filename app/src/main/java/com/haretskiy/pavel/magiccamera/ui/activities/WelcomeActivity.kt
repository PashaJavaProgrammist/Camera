package com.haretskiy.pavel.magiccamera.ui.activities

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Html.fromHtml
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import kotlinx.android.synthetic.main.activity_welcome.*
import org.koin.android.ext.android.inject

class WelcomeActivity : AppCompatActivity() {

    private val router: Router by inject()
    private val prefs: Prefs by inject()

    private val myViewPagerAdapter: MyViewPagerAdapter by lazy {
        MyViewPagerAdapter(this)
    }

    private lateinit var dots: Array<TextView>
    private var layouts = intArrayOf(R.layout.welcome_slide1, R.layout.welcome_slide2, R.layout.welcome_slide3, R.layout.welcome_slide4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (prefs.isUserLogIn()) {
            startLoginActivity()
        }

        setContentView(R.layout.activity_welcome)

        addBottomDots(0)

        changeStatusBarColor()

        view_pager.adapter = myViewPagerAdapter
        view_pager.addOnPageChangeListener(viewPagerPageChangeListener)

        btn_skip.setOnClickListener { startLoginActivity() }

        btn_next.setOnClickListener {
            val current = getItem(+1)
            if (current < layouts.size) {
                view_pager.currentItem = current
            } else {
                startLoginActivity()
            }
        }
    }

    private fun addBottomDots(currentPage: Int) {
        dots = Array(layouts.size, {
            TextView(this)
        })

        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        layout_dots.removeAllViews()
        for (i in 0 until dots.size) {
            dots[i] = TextView(this)
            dots[i].text = fromHtml(getString(R.string.dot_code))
            dots[i].textSize = 35F
            dots[i].setTextColor(colorsInactive[currentPage])
            layout_dots.addView(dots[i])
        }

        if (dots.isNotEmpty()) {
            dots[currentPage].setTextColor(colorsActive[currentPage])
        }
    }

    private fun getItem(i: Int): Int {
        return view_pager.currentItem + i
    }

    private fun startLoginActivity() {
        router.startLoginActivity()
        finish()
    }

    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            addBottomDots(position)

            if (position == layouts.size - 1) {
                btn_next.text = getString(R.string.start)
                btn_skip.visibility = View.GONE
            } else {
                btn_next.text = getString(R.string.next)
                btn_skip.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}

        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    inner class MyViewPagerAdapter(private val context: Context) : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(context).inflate(layouts[position], container, false)
            container.addView(view)

            return view
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            val view = obj as View
            container.removeView(view)
        }
    }
}
