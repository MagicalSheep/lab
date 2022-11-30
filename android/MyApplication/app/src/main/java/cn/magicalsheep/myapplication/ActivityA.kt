package cn.magicalsheep.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActivityA : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a)

        val startB: Button = findViewById(R.id.a_start_b)
        val startC: Button = findViewById(R.id.a_start_c)
        val finishA: Button = findViewById(R.id.finish_a)
        val dialogButton: Button = findViewById(R.id.a_dialog)

        lifecycle.addObserver(
            Observer(
                resources.getString(R.string.A_text),
                findViewById(R.id.a_method_list),
                findViewById(R.id.a_status)
            )
        )

        startB.setOnClickListener {
            val intent = Intent(this, ActivityB::class.java)
            startActivity(intent)
        }

        startC.setOnClickListener {
            val intent = Intent(this, ActivityC::class.java)
            startActivity(intent)
        }

        finishA.setOnClickListener { finish() }

        dialogButton.setOnClickListener {
            val dialog = SimpleDialog()
            dialog.show(supportFragmentManager, "dialog")
        }

    }

    override fun onStop() {
        super.onStop()
        println("This has been called")
    }
}