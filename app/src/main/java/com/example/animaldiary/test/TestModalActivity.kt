package com.example.animaldiary.test

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.animaldiary.R
import com.example.animaldiary.ui.components.ActionButtonView
import com.example.animaldiary.ui.components.ModalView

class TestModalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_modal)

        val showModalButton: ActionButtonView = findViewById(R.id.btn_show_modal)
        val modalContainer: FrameLayout = findViewById(R.id.modal_container)

        // modalContainer의 배경색을 반투명한 검은색으로 설정
        modalContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.surface_overlay))
        modalContainer.visibility = View.GONE

        showModalButton.setOnClickListener {
            if (modalContainer.childCount > 0) {
                modalContainer.removeAllViews()
            }

            // modalContainer에 padding/4xl 적용
            val padding4xl = resources.getDimensionPixelSize(R.dimen.padding_4xl)
            modalContainer.setPadding(0, padding4xl, 0, padding4xl)

            val modalView = ModalView(this).apply {
//                setTitle("입력한 내용이 저장되지 않아요")
//                setDescription("지금까지 입력한 내용은 저장되지 않습니다.\n그래도 둘러보시겠어요?")
//                setLeftButtonText("취소")
//                setRightButtonText("확인")
//                setAlignment(0)
//                showContent(false)

                setTitle("개인정보 보호법")
                setDescription("스크롤이 활성화되는지 확인합니다.")
                val lawContent = """
                    이 법은 개인정보의 처리 및 보호에 관한 사항을 정함으로써 개인의 자유와 권리를 보호하고, 나아가 개인의 존엄과 가치를 구현함을 목적으로 한다. <개정 2014. 3. 24.>

                    조문체계도버튼연혁생활법령버튼
                    제2조(정의) 이 법에서 사용하는 용어의 뜻은 다음과 같다. <개정 2014. 3. 24., 2020. 2. 4., 2023. 3. 14.>

                    이 법은 개인정보의 처리 및 보호에 관한 사항을 정함으로써 개인의 자유와 권리를 보호하고, 나아가 개인의 존엄과 가치를 구현함을 목적으로 한다. <개정 2014. 3. 24.>
                    제2조(정의) 이 법에서 사용하는 용어의 뜻은 다음과 같다. <개정 2014. 3. 24., 2020. 2. 4., 2023. 3. 14.>
                    1. “개인정보”란 살아 있는 개인에 관한 정보로서 다음 각 목의 어느 하나에 해당하는 정의합니다.

                    이 법의 목적은 개인정보의 처리 및 보호를 통해 개인의 자유와 권리를 보호하는 데 있습니다. 이는 개인의 존엄과 가치를 구현하는 중요한 수단이 됩니다.
                    개인정보 보호법 제2조에서는 이 법에서 사용되는 주요 용어들의 정의를 상세하게 규정하고 있습니다. 이는 법률의 해석과 적용에 있어 혼란을 방지하기 위함입니다.
                    특히, "개인정보"에 대한 정의는 이 법이 보호하고자 하는 정보의 범위를 명확히 합니다. "살아 있는 개인에 관한 정보"는 식별 가능한 개인과 직접적으로 관련되는 모든 정보를 포함합니다.
                    또한, 이 법은 시대적 변화에 맞춰 여러 차례 개정되었습니다. 2014년 3월 24일 개정은 법의 목적을 더욱 명확히 했으며, 이후 2020년과 2023년의 개정을 통해 새로운 기술 환경과 사회적 요구를 반영했습니다.
                    이러한 개정들은 개인정보 보호에 대한 법적 기준을 강화하고, 정보 주체의 권리를 더욱 폭넓게 보장하기 위한 노력이었습니다.
                    (더 많은 내용을 추가하여 스크롤 가능하게 만듭니다.)
                """.trimIndent()

                setContentText(lawContent)
                setAlignment(1)
                setLeftButtonText("취소")
                setRightButtonText("확인")

                showLeftButton(true)
                showRightButton(true)

                setOnLeftButtonClickListener {
                    modalContainer.visibility = View.GONE
                }

                setOnRightButtonClickListener {
                    modalContainer.visibility = View.GONE
                }
            }

            val modalLayoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = android.view.Gravity.CENTER
            }

            modalContainer.addView(modalView, modalLayoutParams)
            modalContainer.visibility = View.VISIBLE
        }
    }
}