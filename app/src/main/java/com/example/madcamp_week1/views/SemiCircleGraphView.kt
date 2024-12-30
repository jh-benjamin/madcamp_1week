package com.example.madcamp_week1.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class SemiCircleGraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var currentSweepAngle = -180f // 애니메이션에서 그려질 각도

    init {
        startAnimation()
    }

    fun startAnimation() {
        val animator = ValueAnimator.ofFloat(-180f, 180f) // 부채꼴 각도를 0에서 180도로 증가
        animator.duration = 2000 // 애니메이션 지속 시간 (2초)
        animator.addUpdateListener { animation ->
            currentSweepAngle = animation.animatedValue as Float
            invalidate() // 화면을 갱신하여 onDraw 호출
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        // 화면 중심 좌표 및 반지름 계산
        val centerX = width / 2f // 화면 가로 중심
        val centerY = height / 1.125f // 화면 세로 중심
        val maxRadius = min(width, height) / 1.2f // 반지름 설정 (화면 크기 기준)

        // 층 간 간격 조정
        val totalLayers = 8 // 부채꼴 층의 개수
        val layerGap = maxRadius / (totalLayers + 4) // 층 간 간격을 균등하게 조정

        val colors = listOf(Color.BLUE, Color.RED, Color.GRAY, Color.CYAN, Color.GREEN, Color.MAGENTA)
        val colorRatios = listOf(180, 108, 12, 3, 1, 1) // 색상 비율 정의

        // 전체 원 개수 계산
        val totalCircles = colorRatios.sum()

        // 누적 색상 경계 계산
        val colorBoundaries = mutableListOf<Int>()
        var cumulativeSum = 0
        for (ratio in colorRatios) {
            cumulativeSum += ratio
            colorBoundaries.add(cumulativeSum)
        }

        var currentCircleIndex = 0 // 현재까지 그린 원의 개수

        val numCirclesPerLayer = listOf(48) + // 맨 바깥쪽
                (1 until totalLayers - 1).map { 48 - (it * 5) } + // 중간 층
                listOf(21) // 맨 안쪽

        // 각 층별 원 그리기
        for (layer in 0 until totalLayers) {
            val radius = maxRadius - (layer * layerGap) // 층별 반지름 조정
            val numCircles = numCirclesPerLayer[layer] // 해당 층의 원 개수
            val sweepAngle = 180f / (numCircles - 1) // 각도 간격 (원 사이 간격 균등화)

            for (i in 0 until numCircles) {
                // 현재 원의 각도 계산
                val angle = -180f + (i * sweepAngle)

                // 각도 제한: currentSweepAngle 이하만 그리기
                if (angle > currentSweepAngle) break

                // 색상 결정 (누적 인덱스 기반)
                val sectionIndex = colorBoundaries.indexOfFirst { currentCircleIndex < it }
                paint.color = colors[sectionIndex]

                // 페이드 인 효과: 반투명도 적용
                val alpha = ((currentSweepAngle - angle) / 20f).coerceIn(0f, 1f) * 255
                paint.alpha = alpha.toInt()

                // 좌표 계산 (중심 기준 좌우 대칭)
                val x = (centerX + radius * Math.cos(Math.toRadians(angle.toDouble()))).toFloat()
                val y = (centerY + radius * Math.sin(Math.toRadians(angle.toDouble()))).toFloat()

                // 원 크기 변화 효과 (애니메이션 효과 추가)
                val circleSize = 8f * alpha / 255f // 크기 비율에 따라 원 크기 조정

                // 원 그리기
                canvas.drawCircle(x, y, circleSize, paint)

                currentCircleIndex++ // 원 인덱스 증가
            }
        }
    }
}