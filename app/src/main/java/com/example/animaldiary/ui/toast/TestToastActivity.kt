package com.example.animaldiary.ui.toast

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.animaldiary.R
import com.example.animaldiary.ui.toast.ToastHost
import com.example.animaldiary.ui.toast.ToastStyle

class TestToastActivity : AppCompatActivity() {

    private lateinit var host: ToastHost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_toast)

        // 바텀 영역(네비게이션) 앵커 전달 → 하단 16dp + 앵커 높이 + 시스템 인셋만큼 조정
        val anchor = findViewById<android.view.View>(R.id.bottom_nav)

        // attach & 스택 관리/제한은 ToastHost에서 관리
        host = ToastHost.attach(this, anchor)

        findViewById<Button>(R.id.btnNormal).setOnClickListener {
            host.show("일반 토스트입니다.", ToastStyle.NORMAL)
        }
        findViewById<Button>(R.id.btnInfo).setOnClickListener {
            host.show("정보 메시지입니다.", ToastStyle.INFORMATION)
        }
        findViewById<Button>(R.id.btnSuccess).setOnClickListener {
            host.show("성공했습니다!", ToastStyle.SUCCESS)
        }
        findViewById<Button>(R.id.btnWarning).setOnClickListener {
            host.show("주의가 필요합니다.", ToastStyle.WARNING)
        }
        findViewById<Button>(R.id.btnDanger).setOnClickListener {
            host.show("오류가 발생했습니다.", ToastStyle.DANGER)
        }

        // 스택/제한 테스트-> 5개 생성
        findViewById<Button>(R.id.btnSpam).setOnClickListener {
            val h = Handler(Looper.getMainLooper())
            val styles = listOf(
                ToastStyle.INFORMATION,
                ToastStyle.SUCCESS,
                ToastStyle.WARNING,
                ToastStyle.DANGER,
                ToastStyle.NORMAL
            )
            styles.forEachIndexed { i, style ->
                h.postDelayed({ host.show("Spam #${i + 1}", style) }, (i * 300L))
            }
        }
    }
}
