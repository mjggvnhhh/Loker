package com.clawd.locker

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(50, 50, 50, 50) }
        
        val btnAdmin = Button(this).apply {
            text = "1. Дать права Админа"
            setOnClickListener {
                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, ComponentName(this@MainActivity, MyDeviceAdminReceiver::class.java))
                startActivity(intent)
            }
        }
        
        val btnOverlay = Button(this).apply {
            text = "2. Разрешить поверх окон"
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this@MainActivity)) {
                        startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                    }
                }
            }
        }
        
        val btnLock = Button(this).apply {
            text = "3. ЗАБЛОКИРОВАТЬ"
            setOnClickListener {
                val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                val admin = ComponentName(this@MainActivity, MyDeviceAdminReceiver::class.java)
                if (dpm.isAdminActive(admin)) {
                    dpm.lockNow()
                    startService(Intent(this@MainActivity, LockOverlayService::class.java))
                } else {
                    Toast.makeText(this@MainActivity, "Сначала дай права!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        layout.addView(btnAdmin)
        layout.addView(btnOverlay)
        layout.addView(btnLock)
        setContentView(layout)
    }
} 
