package it.services.instantpayment

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import it.services.instantpayment.databinding.ActivityMainBinding
import it.services.instantpayment.ui.dashboard.AllReportsFragment
import it.services.instantpayment.ui.dashboard.HomeFragment
import it.services.instantpayment.ui.dashboard.ProfileFragment
import it.services.instantpayment.ui.login.LoginActivity
import it.services.instantpayment.utils.SharedPref
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var context: Context
    private lateinit var activity: MainActivity

    companion object {
        lateinit var LOGIN_SESSION: String
        lateinit var LOGIN_DATA: JSONObject
        lateinit var IS_AePS_APPROVED: String
        lateinit var COMPANY_NAME: String
        lateinit var EMAIL_ID: String
        lateinit var MOBILE_NO: String
        lateinit var USERNAME: String
        lateinit var NAME: String
        lateinit var PERMISSION_ARRAY: JSONArray

        fun checkPermission( searchServiceName:String):Boolean
        {
            for (position in 0 until PERMISSION_ARRAY.length())
            {
                val permissionObject= PERMISSION_ARRAY.getJSONObject(position)
                val serviceName=permissionObject.getString("ServiceName")
                if (serviceName.equals(searchServiceName,true))
                {
                    val serviceStatus=permissionObject.getString("Status")
                    return serviceStatus.equals("Active",true)
                }
            }
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        activity = this

        val loginDataStr = SharedPref.getString(context, SharedPref.LOGIN_DATA_KEY)
        LOGIN_DATA = JSONObject(loginDataStr.toString())
        LOGIN_SESSION = LOGIN_DATA.getString("SessionKey")
        val dataArray= LOGIN_DATA.getJSONArray("Data")
        val dataObject=dataArray.getJSONObject(0)
        IS_AePS_APPROVED = dataObject.getString("AepsStatus")
        COMPANY_NAME = dataObject.getString("CompanyName")
        EMAIL_ID = dataObject.getString("EmailId")
        MOBILE_NO = dataObject.getString("Phone")
        USERNAME = dataObject.getString("Username")
        NAME = dataObject.getString("Name")
        PERMISSION_ARRAY= LOGIN_DATA.getJSONArray("Permission")


        replaceFragment(HomeFragment(), Bundle())
        handleClickEvents()

    }

    private fun handleClickEvents() {
        binding.apply {
            tvCompanyName.text= COMPANY_NAME

            homeLy.setOnClickListener {
                tvHome.visibility = View.VISIBLE
                tvProfile.visibility = View.GONE
                tvReport.visibility = View.GONE
                replaceFragment(HomeFragment(), Bundle())
            }

            profileLy.setOnClickListener {
                tvHome.visibility = View.GONE
                tvReport.visibility = View.GONE
                tvProfile.visibility = View.VISIBLE
                replaceFragment(ProfileFragment(),Bundle())
            }

            reportsLy.setOnClickListener {
                tvHome.visibility = View.GONE
                tvProfile.visibility = View.GONE
                tvReport.visibility = View.VISIBLE
                replaceFragment(AllReportsFragment(), Bundle())
            }

            logoutLy.setOnClickListener {
                val alertDialog=AlertDialog.Builder(context)
                alertDialog.setMessage("Are you sure. Do you want to logout")
                alertDialog.setPositiveButton("yes"
                ) { _, _ ->
                    SharedPref.setString(context, SharedPref.USER_NAME, "")
                    SharedPref.setString(context, SharedPref.PASSWORD, "")
                    startActivity(Intent(this@MainActivity,LoginActivity::class.java))
                    finish()
                }
                alertDialog.setNegativeButton("Cancel",null)
                alertDialog.show()
            }

            imgMenu.setOnClickListener {
                tvHome.visibility = View.GONE
                tvReport.visibility = View.GONE
                tvProfile.visibility = View.VISIBLE
                 replaceFragment(ProfileFragment(),Bundle())
            }


        }
    }


    private fun replaceFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_container, fragment)
        fragmentTransaction.commit()
    }
}