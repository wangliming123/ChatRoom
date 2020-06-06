package com.wlm.chatroom.ui.activity

import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.wlm.chatroom.MyApp
import com.wlm.chatroom.R
import com.wlm.chatroom.base.ui.BaseVMActivity
import com.wlm.chatroom.common.Constant
import com.wlm.chatroom.common.utils.SharedPrefs
import com.wlm.chatroom.common.utils.ToastUtils
import com.wlm.chatroom.startKtxActivity
import com.wlm.chatroom.ui.fragment.ContactFragment
import com.wlm.chatroom.ui.fragment.DiscussFragment
import com.wlm.chatroom.ui.fragment.MineFragment
import com.wlm.chatroom.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : BaseVMActivity<MainViewModel>() {
    override val layoutId: Int = R.layout.activity_main
    override val providerVMClass: Class<MainViewModel> = MainViewModel::class.java
    private var isLogin by SharedPrefs(Constant.IS_LOGIN, false)
    private val userString by SharedPrefs(Constant.USER_STRING, "")

    private val bottomTitles = arrayOf(
        R.string.str_message,
        R.string.str_contact,
        R.string.str_mine
    )

    private val fragmentList = arrayListOf<Fragment>()
    private val messageFragment by lazy { DiscussFragment() }
    private val contactFragment by lazy { ContactFragment() }
    private val mineFragment by lazy { MineFragment() }


    init {
        fragmentList.add(messageFragment)
        fragmentList.add(contactFragment)
        fragmentList.add(mineFragment)
    }

    override fun init() {
        super.init()
        if (isLogin) {
            val userInfo = userString.split(",")
            MyApp.instance.currentUserId = userInfo[2]
        }
        setSupportActionBar(toolbar)
        initDrawerLayout()
        initNav()
        initViewPager()
        initNavBottom()
    }

    private fun initDrawerLayout() {
        drawer_layout.run {
            val toggle = object : ActionBarDrawerToggle(
                this@MainActivity,
                this,
                toolbar,
                R.string.app_name,
                R.string.app_name
            ) {
                override fun onDrawerOpened(drawerView: View) {
                    super.onDrawerOpened(drawerView)
//                    nav_view.menu.findItem(R.id.logout).isVisible = isLogin
                    val userInfo = userString.split(",")
                    nav_view.findViewById<TextView>(R.id.tv_user_name).text = userInfo[0]
                    nav_view.findViewById<TextView>(R.id.tv_nick_name).text = userInfo[3]
                    nav_view.findViewById<TextView>(R.id.tv_email).text = userInfo[4]
                    nav_view.findViewById<TextView>(R.id.tv_mobile).text = userInfo[5]
                    nav_view.findViewById<LinearLayout>(R.id.tab_user_info).setOnClickListener {
                        drawer_layout.closeDrawer(GravityCompat.START)
                        startKtxActivity<UserEditActivity>()
                    }
                }
            }
            addDrawerListener(toggle)
            toggle.syncState()
        }
    }

    private fun initNav() {
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {

//                R.id.night_mode -> {
//                }
//                R.id.setting -> {
//                }
                R.id.logout -> {
                    mViewModel.logout()
                }
                R.id.about -> {
                    AlertDialog.Builder(this).setTitle(R.string.app_name)
                        .setMessage(
                            getString(
                                R.string.str_source_code,
                                "https://github.com/wangliming123/ChatRoom"
                            )
                        )
                        .setPositiveButton(R.string.str_confirm, null)
                        .create()
                        .show()
                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun initViewPager() {
        view_pager.run {
            offscreenPageLimit = bottomTitles.size
            adapter = object : FragmentPagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
                override fun getItem(position: Int) = fragmentList[position]

                override fun getCount() = fragmentList.size

                override fun getPageTitle(position: Int) = getString(bottomTitles[position])

            }
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    bottom_navigation.selectedItemId =
                        bottom_navigation.menu.getItem(position).itemId
                    toolbar.title = getString(bottomTitles[position])
                }

            })

        }
    }

    private fun initNavBottom() {
        bottom_navigation.run {
            labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.message -> view_pager.currentItem = 0
                    R.id.contact -> view_pager.currentItem = 1
                    R.id.mine -> view_pager.currentItem = 2
                }
                true
            }
        }
    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.discuss_add -> {
                startKtxActivity<AddDiscussActivity>()
            }
//            R.id.search -> {
//                startKtxActivity<SearchActivity>()
//                return true
//            }
        }
        return super.onOptionsItemSelected(item)
    }

    private var lastExitTime = 0L
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (KeyEvent.KEYCODE_BACK == keyCode) {
            val current = System.currentTimeMillis()
            if (current - lastExitTime > 2000) {
                ToastUtils.show(getString(R.string.str_exit_hint))
                lastExitTime = current
                return true
            } else {
                finish()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            loginState.value = isLogin
            loginState.observe(this@MainActivity, Observer {
                if (!it) {
                    startKtxActivity<LoginActivity>()
                    finish()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isLogin) {
            startKtxActivity<LoginActivity>()
            finish()
        }
    }


}
