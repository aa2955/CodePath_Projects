package com.example.lab1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private var taps = 0
    private var tapsPerClick = 1
    private var goal = 100
    private var goalsReached = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val goalTextView = findViewById<TextView>(R.id.goalTextView)
        val upgradeButton = findViewById<Button>(R.id.upgradeButton)
        val customButtonButton = findViewById<Button>(R.id.customButtonButton)

        // Update the goal text
        goalTextView.text = "Next goal: Reach $goal taps"

        findViewById<Button>(R.id.button).setOnClickListener {
            taps += tapsPerClick
            textView.text = "Taps: $taps"

            // Check if goal is reached
            if (taps >= goal) {
                goalsReached++
                goal *= 2 // Double the next goal's requirement
                goalTextView.text = "Next goal: Reach $goal taps"
            }
        }

        // Upgrade to double taps per click for 100 taps
        upgradeButton.setOnClickListener {
            if (taps >= 100) {
                taps -= 100
                tapsPerClick = 2
                Snackbar.make(it, "Upgrade purchased: Double taps per click!", Snackbar.LENGTH_SHORT).show()
                textView.text = "Taps: $taps"
            } else {
                Snackbar.make(it, "Not enough taps for this upgrade!", Snackbar.LENGTH_SHORT).show()
            }
        }

        // Upgrade to custom button for 100 taps
        customButtonButton.setOnClickListener {
            if (taps >= 100) {
                taps -= 100
                // Apply custom icon to the button
                findViewById<Button>(R.id.button).setBackgroundResource(R.drawable.costum_icon)
                Snackbar.make(it, "Custom button purchased!", Snackbar.LENGTH_SHORT).show()
                textView.text = "Taps: $taps"
            } else {
                Snackbar.make(it, "Not enough taps for this upgrade!", Snackbar.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
