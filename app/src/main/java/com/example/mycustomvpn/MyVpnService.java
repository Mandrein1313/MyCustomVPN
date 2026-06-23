package com.example.mycustomvpn;

import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import libv2ray.Libv2ray; // <--- ตรวจสอบชื่อ Package/Class ใน .aar ของคุณอีกทีนะครับ

public class MyVpnService extends VpnService {
    private ParcelFileDescriptor mInterface;

    @Override
    public int onStartCommand(android.content.Intent intent, int flags, int startId) {
        // 1. ตั้งค่า Builder
        Builder builder = new Builder();
        builder.setSession("MyCustomVPN")
               .addAddress("10.0.0.2", 24)
               .addDnsServer("8.8.8.8")
               .addRoute("0.0.0.0", 0);
        
        mInterface = builder.establish();
        
        // 2. เริ่มรัน Core ใน Thread แยก
        if (mInterface != null) {
            new Thread(() -> {
                // สมมติว่าไฟล์ config.json วางอยู่ที่ /data/user/0/com.example.mycustomvpn/files/config.json
                String configPath = getFilesDir().getAbsolutePath() + "/config.json";
                
                // สั่งรัน Core โดยส่งค่า File Descriptor ของ VPN ไปด้วย
                Libv2ray.run(configPath, mInterface.getFd()); 
            }).start();
        }
        
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // สั่งหยุด Core ถ้ามีฟังก์ชันหยุด
        // Libv2ray.stop(); 
        
        if (mInterface != null) {
            try { mInterface.close(); } catch (Exception e) {}
        }
    }
}
