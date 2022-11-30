package cn.magicalsheep.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActivityC : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c)

        val startA: Button = findViewById(R.id.c_start_a)
        val startB: Button = findViewById(R.id.c_start_b)
        val finishC: Button = findViewById(R.id.finish_c)
        val dialogButton: Button = findViewById(R.id.c_dialog)

        lifecycle.addObserver(
            Observer(
                resources.getString(R.string.C_text),
                findViewById(R.id.c_method_list),
                findViewById(R.id.c_status)
            )
        )

        startA.setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            startActivity(intent)
        }

        startB.setOnClickListener {
            val intent = Intent(this, ActivityB::class.java)
            startActivity(intent)
        }

        finishC.setOnClickListener { finish() }

        dialogButton.setOnClickListener {
            val dialog = SimpleDialog()
            dialog.show(supportFragmentManager, "dialog")
        }

    }
}