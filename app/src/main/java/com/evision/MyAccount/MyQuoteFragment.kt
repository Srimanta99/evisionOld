package com.evision.MyAccount

import android.Manifest
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evision.BuildConfig
import com.evision.MyAccount.Adapter.AdapterQuote
import com.evision.MyAccount.Pojo.MyQuote
import com.evision.MyAccount.Pojo.QuoteItem
import com.evision.R
import com.evision.Utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_my_quote.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyQuoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyQuoteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var loader: AppDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        loader = AppDialog(activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_quote, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         val rvquotelist = view.findViewById(R.id.rvquotelist) as RecyclerView
        rvquotelist.layoutManager = LinearLayoutManager(activity)

        val params = HashMap<String, Any>()
        params.put("customer_id", ShareData(activity!!).getUser()!!.customerId)
       // params.put("customer_id", "2090")

        onHTTP().POSTCALL(URL.QUOTELIST, params, object : OnHttpResponse {
            override fun onSuccess(response: String) {
                System.out.println("quotelist " + response)
                loader.dismiss()
                val myQuote = Gson().fromJson(response, MyQuote::class.java)
                if (myQuote.status.equals("200")) {
                    llquote.visibility = View.GONE
                    ll_quotelist.visibility = View.VISIBLE
                    val adapter = AdapterQuote(activity!!, myQuote.quotation_list as ArrayList<QuoteItem>, this@MyQuoteFragment)
                    rvquotelist.adapter = adapter
                } else {
                    llquote.visibility = View.VISIBLE
                    ll_quotelist.visibility = View.GONE
                    // TXT_NOPRODUCT.setText(JSONObject(response).optString("message"))
                }

            }

            override fun onError(error: String) {
                loader.dismiss()
            }

            override fun onStart() {
                loader.show()
            }

        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyQuoteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MyQuoteFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
   public fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }
    public fun downloadfromUrl(quotationDownloadUrl: String, generatedQuotationFileName: String) {
        var pDialog:ProgressDialog = ProgressDialog(context)
        pDialog.setMessage("Please wait...")
        pDialog.setIndeterminate(false)
        pDialog.setCancelable(false)

        class someTask() : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void?): String? {
                try {
                    val url = java.net.URL(quotationDownloadUrl)
                    val c: HttpURLConnection = url.openConnection() as HttpURLConnection
                    c.setRequestMethod("GET")
                    c.setDoOutput(true)
                    c.connect()

                    val PATH = (Environment.getExternalStorageDirectory().toString()+"/" +"evision")
                    val file = File(PATH)
                    file.mkdirs()
                    val outputFile = File(file, generatedQuotationFileName)
                    val fos = FileOutputStream(outputFile)
                    val `is`: InputStream = c.inputStream

                    val buffer = ByteArray(4096)
                    var len1 = 0

                    while (`is`.read(buffer).also({ len1 = it }) != -1) {
                        fos.write(buffer, 0, len1)
                    }

                    fos.close()
                    `is`.close()

                    activity!!.runOnUiThread {
                        Toast.makeText(activity, " A new file is downloaded successfully in your internal storage Evision folder ",
                                Toast.LENGTH_LONG).show()
                        val authority = activity!!.applicationContext.packageName.toString() + ".fileprovider"
                        //val uriToFile: Uri = FileProvider.getUriForFile(activity!!, authority, outputFile)
                        val uriToFile: Uri =  FileProvider.getUriForFile(Objects.requireNonNull(activity!!),
                                BuildConfig.APPLICATION_ID + ".provider", file);
                        val shareIntent = Intent(Intent.ACTION_VIEW)
                        shareIntent.setDataAndType(uriToFile, "application/pdf")
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                        try {
                            if (shareIntent.resolveActivity(activity!!.packageManager) != null) {
                                activity!!.startActivity(shareIntent)
                            }
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                            // Instruct the user to install a PDF reader here, or something
                        }
                    }



                }catch (e: Exception){
                    e.printStackTrace()
                }
                return null
            }

            override fun onPreExecute() {
                super.onPreExecute()
                pDialog.show()
                // ...
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                pDialog.dismiss()
                // ...
            }
        }
        someTask().execute();
    }
}