package com.example.spamdetector

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.spamdetector.databinding.ActivityMainBinding
import org.json.JSONObject
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    private var currentPosition: Int = 0
    //lateinit var videoView : VideoView
    var url : String = "http://192.168.1.8:5000/rf/predict"
    lateinit var progressbr : ProgressBar

   lateinit var apiKey : String


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title=""
        progressbr = findViewById(R.id.progressbar)


        //Soft keyboard setting for Edittext
        binding.etMsg.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.etMsg.setRawInputType(InputType.TYPE_CLASS_TEXT)
        binding.etMsg.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Close the soft keyboard
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etMsg.windowToken, 0)
                return@setOnEditorActionListener true
            }
            false
        }

        val intent :Intent = intent
        if(intent != null) {
            apiKey = intent.getStringExtra("api").toString()
        }

        if(apiKey != null && !apiKey.equals("null") && !apiKey.equals("")){
            url = apiKey +"/rf/predict"

        }

        val queue : RequestQueue = Volley.newRequestQueue(this)

        binding.etMsg.setHint("Enter Your Message Here")


        binding.predict.setOnClickListener {




            if(binding.etMsg.text.isEmpty()){
                binding.etMsg.error = "Message Cannot be Empty"
            }else{
                binding.etMsg.error = null
                binding.result.visibility = View.GONE
                progressbr.visibility = View.VISIBLE



                val jsonObjectRequest = object: JsonObjectRequest(
                    Request.Method.POST,url,null,
                    Response.Listener{
                        val type = it.getString("type")

                        Log.e("test", type)

                        progressbr.visibility = View.GONE


                        if(type.equals("1")){
                            binding.result.text = "Good ✅"

                            //Set Lottie Animation for Good
                            binding.lottie.visibility = View.VISIBLE
                            binding.lottie.setAnimation(R.raw.bg_effect)
                            binding.lottie.speed = 2.0f
                            binding.lottie.playAnimation()
                        }
                        else{
                            binding.result.text = "Spam 🚫"
                        }
                        binding.result.visibility = View.VISIBLE
                    },
                    Response.ErrorListener{
                        if (it is ServerError) {
                            val errorResponse = String(it.networkResponse.data)
                            Log.e("ServerError", "Error Response: $errorResponse")
                            Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("Volley Error", "An error occurred: ${it.message}")
                            progressbr.visibility = View.GONE
                            binding.result.visibility = View.VISIBLE
                            binding.result.text = it.toString()
                        }
                    }
                    ){

                    override fun getBodyContentType(): String {
                        return "application/x-www-form-urlencoded; charset=UTF-8"
                    }

                    override fun getBody(): ByteArray {
                        val params = HashMap<String, String>()
                        params["message"] = binding.etMsg.text.toString()
                        Log.d("test",params["message"].toString())
                        // Convert the parameters to a URL-encoded string
                        val requestBody = StringBuilder()
                        for ((key, value) in params) {
                            if (requestBody.isNotEmpty()) {
                                requestBody.append("&")
                            }
                            requestBody.append(key)
                            requestBody.append("=")
                            requestBody.append(URLEncoder.encode(value, "UTF-8"))
                        }

                        return requestBody.toString().toByteArray(Charsets.UTF_8)
                    }
                }

                queue.add(jsonObjectRequest)

            }



        }



    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.setting->{
                startActivity(Intent(this,SettingActivity::class.java))
                return true
            }
            R.id.randomForest ->{
                Toast.makeText(this, "Random Forest Model Activated", Toast.LENGTH_SHORT).show()
                binding.msgBox.setText("Random Forest Model")
                binding.result.text = ""
                url = apiKey +"/rf/predict"
                binding.lottie.visibility = View.GONE
                return true
                }
            R.id.decisionTree ->{
                Toast.makeText(this, "Decision Tree Model Activated", Toast.LENGTH_SHORT).show()
                url = apiKey +"/dt/predict"
                binding.msgBox.text = "Decsion Tree Model"
                binding.lottie.visibility = View.GONE
                binding.result.text = ""
                return true
            }
            R.id.logistic ->{
                Toast.makeText(this, "Logistic Regression Model Activated", Toast.LENGTH_SHORT).show()
                url = apiKey +"/lr/predict"
                binding.msgBox.text = "Logistic Regression Model"
                binding.lottie.visibility = View.GONE
                binding.result.text = ""
                return true
            }
            R.id.naiveBayes->{
                Toast.makeText(this, "Naive Bayes Model Activated", Toast.LENGTH_SHORT).show()
                url = apiKey +"/nb/predict"
                binding.msgBox.text = "Naive Bayes Model"
                binding.lottie.visibility = View.GONE
                binding.result.text = ""
                return true
            }
            R.id.svm->{
                Toast.makeText(this, "SVM Model Activated", Toast.LENGTH_SHORT).show()
                url = apiKey +"/svm/predict"
                binding.msgBox.text = "SVM Model"
                binding.lottie.visibility = View.GONE
                binding.result.text = ""
                return true
            }
            R.id.knn ->{
                Toast.makeText(this, "KNN Model Activited", Toast.LENGTH_SHORT).show()
                url = apiKey +"/knn/predict"
                binding.msgBox.text = "KNN Model"
                binding.lottie.visibility = View.GONE
                binding.result.text = ""
                return true
            }
            else->{
                return super.onOptionsItemSelected(item)
            }
        }

    }












    private fun VolleyRequest(url: String) {

        val stringRequest :StringRequest =  object: StringRequest(
            Request.Method.POST,
            url,
            Response.Listener<String>{  response ->

                try {
                    var jsonObject: JSONObject = JSONObject(response)
                    var data :String =  jsonObject.getString("type")

                    Log.d("test","responce listener")

                    if(data.equals("1")){
                        binding.etMsg.setText("Ham")

                    }
                    else{
                        if(data.equals("0")){
                            binding.etMsg.setText("Spam")
                        }
                    }
                }
                catch (e : Exception){
                    Toast.makeText(this, "Slow Internet", Toast.LENGTH_SHORT).show()
                }

            },
            Response.ErrorListener {

            }
        ){
            override fun getParams(): MutableMap<String, String>? {

                val params: MutableMap<String,String> = HashMap()
                params.put("message",binding.etMsg.text.toString())
                Log.d("test",params.toString())
                return params

            }
        }


       // requestQueue.add(stringRequest)
        Log.d("test",stringRequest.toString())

    }



    override fun onResume() {
        super.onResume()
        // Resume video playback from the saved position when the app is resumed
//        videoView.seekTo(currentPosition)
//        videoView.start()
    }

    override fun onRestart() {
//        videoView.start()
        super.onRestart()
    }

    override fun onPause() {
//        currentPosition = binding.videoView.currentPosition
//     videoView.pause()
        super.onPause()
    }

    override fun onDestroy() {
//        videoView.stopPlayback()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the current position of the video in case the activity is destroyed
//        outState.putInt("currentPosition", currentPosition)
    }

    fun videoSetting(){
//        videoView = findViewById(R.id.videoView)
//
//        //Implementing Video View
//        var uri : Uri = Uri.parse("android.resource://"+packageName+"/"+R.raw.bg_video)
//        videoView.setVideoURI(uri)
//        videoView.start()
//
//        videoView.setOnPreparedListener { object : MediaPlayer.OnPreparedListener{
//            override fun onPrepared(mp: MediaPlayer?) {
//
//                if (savedInstanceState != null) {
//                    currentPosition = savedInstanceState!!.getInt("currentPosition", 0)
//                    mp!!.seekTo(currentPosition)
//                }
//                mp!!.isLooping = true
//                binding.videoView.start()
//            }
//
//        }}
//
//
//        videoView.setOnCompletionListener { mp ->
//            mp.start() // Start the video again when it completes
//            mp.isLooping = true // Ensure looping is enabled
//        }
//
//
    }
}