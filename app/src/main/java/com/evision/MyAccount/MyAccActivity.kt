package com.evision.MyAccount

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle

import android.view.MenuItem
import android.view.View
import android.view.WindowManager

import androidx.appcompat.app.AppCompatActivity
import com.evision.R
import com.evision.mainpage.MainActivity
import kotlinx.android.synthetic.main.activity_my_acc.*
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MyAccFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MyAccFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MyAccActivity : AppCompatActivity() {

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_acc)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        var toolbar:Toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.menu_accountdetails)
        toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_back)

        ll_profile.setOnClickListener {
            TXT_profile.setTextColor(R.color.colorPrimary)
            TXT_address.setTextColor(Color.BLACK)
            TXT_MyOrder.setTextColor(Color.BLACK)
            TXT_myquote.setTextColor(Color.BLACK)
            v_profile.visibility=View.VISIBLE
            v_address.visibility=View.INVISIBLE
            v_MyOrder.visibility = View.INVISIBLE
            v_quote.visibility=View.INVISIBLE
            supportFragmentManager.beginTransaction().replace(R.id.acc_container, ProfileFragment.newInstance("","")).commit()

        }

        ll_address.setOnClickListener {
            TXT_address.setTextColor(R.color.colorPrimary)
            TXT_profile.setTextColor(Color.BLACK)
            TXT_MyOrder.setTextColor(Color.BLACK)
            TXT_myquote.setTextColor(Color.BLACK)
            v_profile.visibility=View.INVISIBLE
            v_MyOrder.visibility = View.INVISIBLE
            v_address.visibility=View.VISIBLE
            v_quote.visibility=View.INVISIBLE
            supportFragmentManager.beginTransaction().replace(R.id.acc_container, SavedAddressFragment.newInstance("","")).commit()

        }
        ll_MyOrder.setOnClickListener {
            TXT_MyOrder.setTextColor(R.color.colorPrimary)
            TXT_profile.setTextColor(Color.BLACK)
            TXT_address.setTextColor(Color.BLACK)
            TXT_myquote.setTextColor(Color.BLACK)
            v_profile.visibility = View.INVISIBLE
            v_address.visibility = View.INVISIBLE
            v_MyOrder.visibility = View.VISIBLE
            v_quote.visibility=View.INVISIBLE
            supportFragmentManager.beginTransaction().replace(R.id.acc_container, MyorderFragment.newInstance("", "")).commit()

        }
        ll_myquote.setOnClickListener {
            TXT_myquote.setTextColor(R.color.colorPrimary)
            TXT_profile.setTextColor(Color.BLACK)
            TXT_address.setTextColor(Color.BLACK)
            TXT_MyOrder.setTextColor(Color.BLACK)
            v_profile.visibility = View.INVISIBLE
            v_address.visibility = View.INVISIBLE
            v_MyOrder.visibility = View.INVISIBLE
            v_quote.visibility=View.VISIBLE
            supportFragmentManager.beginTransaction().replace(R.id.acc_container, MyQuoteFragment.newInstance("", "")).commit()

        }

        ll_profile.performClick()


    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId)
        {
            android.R.id.home->{
                startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
