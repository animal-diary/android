package com.example.animaldiary.ui.components

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.animaldiary.R
import kotlin.math.min

class AvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var showProfile: Boolean = true
    private var showCamera: Boolean = false
    private var avatarSize: Int = 80 // 기본값 80dp

    private var profileImage: Drawable? = null
    private var cameraIcon: Drawable? = null

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val defaultBackgroundColor = ContextCompat.getColor(context, R.color.color_neutral_200)
    private val borderColor = ContextCompat.getColor(context, R.color.color_neutral_300)
    private val cameraBackgroundColor = ContextCompat.getColor(context, R.color.bg_base)

    private var onCameraClickListener: (() -> Unit)? = null

    init {
        // 기본 설정
        backgroundPaint.color = defaultBackgroundColor
        borderPaint.apply {
            color = borderColor
            style = Paint.Style.STROKE
            strokeWidth = dpToPx(1f)
        }

        // 기본 아이콘 설정
        profileImage = ContextCompat.getDrawable(context, R.drawable.ic_profile_solid)
        cameraIcon = ContextCompat.getDrawable(context, R.drawable.ic_camera_solid)

        // 클릭 이벤트 설정
        setOnClickListener {
            if (showCamera) {
                onCameraClickListener?.invoke()
            }
        }
    }

    //    프로필 이미지 표시 여부 설정
    fun setShowProfile(show: Boolean) {
        showProfile = show
        invalidate()
    }


    // 카메라 아이콘 표시 여부 설정
    fun setShowCamera(show: Boolean) {
        showCamera = show
        invalidate()
    }

    // 아바타 크기 설정
    fun setAvatarSize(size: Int) {
        avatarSize = size
        requestLayout()
    }


    // 프로필 이미지 설정
    fun setProfileImage(drawable: Drawable?) {
        profileImage = drawable
        invalidate()
    }

    // 카메라 클릭 리스너 설정
    fun setOnCameraClickListener(listener: () -> Unit) {
        onCameraClickListener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = dpToPx(avatarSize.toFloat()).toInt()
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(width, height) / 2f

        // 배경 원 그리기
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)

        // 프로필 이미지를 먼저 그리고 클리핑 적용
        if (showProfile && profileImage != null) {
            drawProfileImage(canvas, centerX, centerY, radius)
        } else {
            // 프로필이 없을 때만 테두리 그리기
            canvas.drawCircle(centerX, centerY, radius - borderPaint.strokeWidth / 2, borderPaint)
        }

        // 카메라 아이콘은 마지막에 그려 겹침 해결!
        if (showCamera) {
            drawCameraIcon(canvas, centerX, centerY, radius)
        }
    }

    private fun drawProfileImage(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        profileImage?.let { drawable ->
            // Canvas 상태 저장
            val saveCount = canvas.save()

            // 원형 클리핑 패스 생성 테두리 두께만큼 안쪽으로
            val clipRadius = radius - borderPaint.strokeWidth
            val clipPath = Path().apply {
                addCircle(centerX, centerY, clipRadius, Path.Direction.CW)
            }
            // 클리핑 적용
            canvas.clipPath(clipPath)

            // 프로필 이미지를 원 전체 크기로설정
            val imageSize = (clipRadius * 2).toInt()
            val left = (centerX - imageSize / 2).toInt()
            val top = (centerY - imageSize / 2).toInt()
            val right = left + imageSize
            val bottom = top + imageSize

            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)

            // Canvas 상태 복원 (클리핑 해제)
            canvas.restoreToCount(saveCount)
        }
    }

    private fun drawCameraIcon(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        cameraIcon?.let { drawable ->
            val cameraRadius = radius * 0.35f
            val cameraX = centerX + radius * 0.6f
            val cameraY = centerY + radius * 0.6f

            // 카메라 배경 원 그리기
            val cameraBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = cameraBackgroundColor
            }
            canvas.drawCircle(cameraX, cameraY, cameraRadius, cameraBgPaint)

            // 카메라 배경 테두리 그리기
            canvas.drawCircle(cameraX, cameraY, cameraRadius - borderPaint.strokeWidth / 2, borderPaint)

            // 카메라 아이콘 색상
            val tintedCameraDrawable = drawable.mutate()
            tintedCameraDrawable.setTint(0xFFB6B6B6.toInt())

            // 카메라 아이콘 그리기
            val iconSize = (cameraRadius * 1.2f).toInt()
            val left = (cameraX - iconSize / 2).toInt()
            val top = (cameraY - iconSize / 2).toInt()
            val right = left + iconSize
            val bottom = top + iconSize

            tintedCameraDrawable.setBounds(left, top, right, bottom)
            tintedCameraDrawable.draw(canvas)
        }
    }

    private fun dpToPx(dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}

// Avatar 크기들 (4의 배수)
object AvatarSize {
    const val SMALL = 32
    const val MEDIUM = 48
    const val LARGE = 64
    const val EXTRA_LARGE = 80
    const val XXL = 96
}

//Avatar 빌더 클래스
class AvatarBuilder(private val context: Context) {
    private var showProfile: Boolean = true
    private var showCamera: Boolean = false
    private var size: Int = AvatarSize.EXTRA_LARGE
    private var profileImage: Drawable? = null
    private var onCameraClick: (() -> Unit)? = null

    fun showProfile(show: Boolean) = apply { this.showProfile = show }
    fun showCamera(show: Boolean) = apply { this.showCamera = show }
    fun size(size: Int) = apply { this.size = size }
    fun profileImage(drawable: Drawable?) = apply { this.profileImage = drawable }
    fun onCameraClick(listener: () -> Unit) = apply { this.onCameraClick = listener }

    fun build(): AvatarView {
        return AvatarView(context).apply {
            setShowProfile(showProfile)
            setShowCamera(showCamera)
            setAvatarSize(size)
            profileImage?.let { setProfileImage(it) }
            onCameraClick?.let { setOnCameraClickListener(it) }
        }
    }
}