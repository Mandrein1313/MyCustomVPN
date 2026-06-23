package com.example.mycustomvpn;

import android.net.VpnService;
import android.os.ParcelFileDescriptor;

public class MyVpnService extends VpnService {
    private ParcelFileDescriptor mInterface;

    @Override
    public int onStartCommand(android.content.Intent intent, int flags, int startId) {
        // สร้างการเชื่อมต่อเสมือน
        Builder builder = new Builder();
        builder.setSession("MyCustomVPN")
               .addAddress("10.0.0.2", 24)
               .addDnsServer("8.8.8.8")
               .addRoute("0.0.0.0", 0);
        
        mInterface = builder.establish();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // ปิดการเชื่อมต่อเมื่อ Service หยุดทำงาน
        if (mInterface != null) {
            try { mInterface.close(); } catch (Exception e) {}
        }
    }
}
