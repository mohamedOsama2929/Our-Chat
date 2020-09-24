package com.example.ososchat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {
    private val messageAdapter = GroupAdapter<GroupieViewHolder>()

    lateinit var user: FirebaseUser
    val rootRef =
        FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        linearLayoutManager.stackFromEnd = true
        linearLayoutManager.isSmoothScrollbarEnabled = true
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = messageAdapter
        populateData()
        // Write a message to the database
        if (!intent.extras?.isEmpty!!) {
//            user= Gson().fromJson(intent.getStringExtra("user"), FirebaseUser::class.java)
        }

        user = FirebaseAuth.getInstance().currentUser!!
        button.setOnClickListener {
            val message = Message(
                text = editText.text.toString(),
                sendBy = user.displayName.toString()
            )
            val sendMessageItem = SendMessageItem(message)
            // Write a message to the database

            val cineIndustryRef =
                rootRef.child("chat").push()
            val key = cineIndustryRef.key
            val map: MutableMap<String?, Any> =
                HashMap()
            map[key] = editText.text.toString() + " " + message.sendBy
            cineIndustryRef.updateChildren(map)

            messageAdapter.add(sendMessageItem)
            recyclerView.scrollToPosition(messageAdapter.itemCount - 1)
            editText.text.clear()

            receiveAutoResponse()
            hideKeyboard()
        }


        listenToFireBase()
    }

    private fun listenToFireBase() {
        // Read from the database
        // Read from the database
        rootRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // val value = dataSnapshot.getValue(String::class.java)
                Log.e("TAG", "Value is: ${dataSnapshot.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    private fun populateData() {
        val data = listOf<Message>()
        data.forEach {
            if (it.sendBy == user.displayName.toString()) {
                messageAdapter.add(SendMessageItem(it))
            } else {
                messageAdapter.add(ReceiveMessageItem(it))
            }
        }
    }

    private fun receiveAutoResponse() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            val receive = Message(
                text = "Salut l'amis j'espere que vous allez bien, je suis tres bien j'ai manger to day",
                sendBy = "me"
            )
            val receiveItem = ReceiveMessageItem(receive)

            messageAdapter.add(receiveItem)
            recyclerView.scrollToPosition(messageAdapter.itemCount - 1)
        }
    }

    private fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
    }
}