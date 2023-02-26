package com.nngngn.mchsafe

import android.content.Intent
import com.nngngn.mchsafe.R
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.Channel
import com.pusher.client.channel.PusherEvent
import com.pusher.client.channel.SubscriptionEventListener
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.ServerAddress
import com.mongodb.client.MongoDatabase
import com.mongodb.client.MongoCollection
import org.bson.Document
import java.util.Arrays
import com.mongodb.Block
import com.mongodb.client.MongoCursor
import com.mongodb.client.model.Filters.*
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.model.Updates.*
import com.mongodb.client.result.UpdateResult
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList


class MainActivity : AppCompatActivity() {

    private var markerX: Int = 0
    private var markerY: Int = 0
    private var marker: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        marker = findViewById(R.id.marker1)
        marker?.setVisibility(View.GONE)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val options = PusherOptions()
        options.setCluster("us2")
        val pusher = Pusher("adf7ac7a5c51dcc45eb4", options)

        pusher.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange) {
                println("State changed from ${change.previousState} to ${change.currentState}")
            }

            override fun onError(message: String, code: String, e: java.lang.Exception) {
                println("There was a problem connecting!\nCode:$code\nMessage:$message\nException:$e")
            }
        }, ConnectionState.ALL)


        val channel: Channel = pusher.subscribe("my-channel")
        channel.bind("my-event", object : SubscriptionEventListener {
            fun onEvent(
                channelName: String?,
                eventName: String?,
                data: String?
            ) {
                println(data)
            }

            override fun onEvent(event: PusherEvent?) {
            }
        })
        channel.bind("my-event") { event -> println("Received event with data: $event") }

        reportButton.setOnClickListener {
        /*
            val connectionString = MongoClientURI("")
            val mongoClient : MongoClient = MongoClient(connectionString)
            val database = mongoClient.getDatabase("")
            var collection : MongoCollection<Document> = database.getCollection("reports");
            val doc : Document = Document("x", markerX)
                .append("y", markerY)
                .append("floor", 1)
                .append("student_no", 869812);
            // collection.insertOne(doc);
            */
            val iii = Intent(this, ReportActivity::class.java)
            this.startActivity(iii)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (marker == null) {
            println("Marker is null."); return true
        }
        var layoutParams : FrameLayout.LayoutParams = marker?.layoutParams as FrameLayout.LayoutParams
        marker?.setVisibility(View.VISIBLE);

        markerX = event.x.toInt() - 20
        markerY = event.y.toInt() - 275
        layoutParams.leftMargin = markerX;
        layoutParams.topMargin = markerY;
        marker?.setLayoutParams(layoutParams);
        marker?.bringToFront()
        return true
    }

    fun onClick(view: View) {
        view.requestPointerCapture()
    }
}
