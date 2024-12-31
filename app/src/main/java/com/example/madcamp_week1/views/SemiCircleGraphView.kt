package com.example.madcamp_week1.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.madcamp_week1.data.PartyInfoData.partyList_22
import com.example.madcamp_week1.model.PartyInfo
import kotlin.math.floor
import kotlin.math.min

class SemiCircleGraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var currentSweepAngle = -180f // 애니메이션에서 그려질 각도

    private var partyList = partyList_22

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

    fun updatePartyList(newPartyList: List<PartyInfo>) {
        partyList = newPartyList // PartyList 변경
        invalidate() // 변경된 데이터를 반영하여 다시 그리기
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        // 화면 중심 좌표 및 반지름 계산
        val centerX = width / 2f
        val centerY = height / 1.125f
        val maxRadius = min(width, height) / 1.2f

        // 층 간 간격
        val totalLayers = 8
        val layerGap = maxRadius / (totalLayers + 4)

        // 전체 좌석 수 계산
        val totalSeats = partyList.sumOf { it.seats }

        // 각 레이어에 배분된 좌석 수 계산
        val layerSeats = calculateCircles(totalSeats, totalLayers, 20, 6)

        // 정당별 좌석 비율 계산
        val partyRatios = partyList.map { it.seats.toDouble() / totalSeats }

        // 정당별로 실제 배분된 좌석 수를 추적
        val allocatedSeats = MutableList(partyList.size) { 0 }

        // 2차원 배열로 좌석 데이터 초기화
        val seatColors = MutableList(totalLayers) { mutableListOf<Int>() }

        // 레이어별로 좌석 색상 배치
        for (layer in totalLayers - 1 downTo 0) {
            val layerSeatCount = layerSeats[layer] // 해당 레이어의 좌석 수

            // 레이어 내 정당별 좌석 수 계산 (반올림 처리)

            val layerPartySeatsWithRounding = partyRatios.map { floor(it * layerSeatCount).toInt() }.toMutableList()

            // 레이어 내 배정된 좌석 리스트 계산
            for (i in layerPartySeatsWithRounding.indices) {
                allocatedSeats[i] += layerPartySeatsWithRounding[i]
            }

            // 초과 좌석 검증 및 수정
            for (index in allocatedSeats.indices) {
                val maxSeatsForParty = partyList[index].seats
                if (allocatedSeats[index] > maxSeatsForParty) {
                    val excessSeats = allocatedSeats[index] - maxSeatsForParty
                    layerPartySeatsWithRounding[index] -= excessSeats
                    allocatedSeats[index] -= excessSeats // 초과 좌석 조정
                }
            }

            // 전체 배정된 좌석 수 계산 및 남은 좌석 수 계산
            var totalAssignedSeats = layerPartySeatsWithRounding.sum()
            var remainingSeats = layerSeatCount - totalAssignedSeats

            if (remainingSeats != 0) {
                // 초과 또는 부족한 좌석을 조정
                val adjustments = partyRatios.mapIndexed { index, ratio ->
                    Pair(index, ratio * layerSeatCount - layerPartySeatsWithRounding[index])
                }.filter { allocatedSeats[it.first] < partyList[it.first].seats }

                while (remainingSeats > 0) {
                    // 부족한 경우: 남은 좌석을 잔여값이 큰 순서대로 추가
                    adjustments.sortedByDescending { it.second }.forEach { (index, _) ->
                        if (remainingSeats <= 0) return@forEach
                        if (allocatedSeats[index] < partyList[index].seats) {
                            layerPartySeatsWithRounding[index] += 1
                            allocatedSeats[index] += 1
                            remainingSeats--
                        }
                    }
                }
                while (remainingSeats < 0) {
                    // 초과된 경우: 남은 좌석을 잔여값이 작은 순서대로 제거
                    adjustments.sortedBy { it.second }.forEach { (index, _) ->
                        if (remainingSeats >= 0) return@forEach
                        if (layerPartySeatsWithRounding[index] > 0) {
                            layerPartySeatsWithRounding[index] -= 1
                            allocatedSeats[index] -= 1
                            remainingSeats++
                        }
                    }
                }
            }

            if (remainingSeats > 0) {
                Log.e("Error", "Remaining seats not fully assigned: $remainingSeats")
            }

            // **디버그용 로그 추가**
            Log.d("SemiCircleGraphView", "Layer $layer - Assigned Seats After Adjustment: $layerPartySeatsWithRounding")
            Log.d("SemiCircleGraphView", "Layer $layer - Remaining Seats After Adjustment: $remainingSeats")

            // 정당별로 좌석 색상 할당 및 전역 추적 업데이트
            var currentPartyIndex = 0
            var remainingSeatsInLayer = layerSeatCount
            while (remainingSeatsInLayer > 0) {
                if (layerPartySeatsWithRounding[currentPartyIndex] > 0) {
                    // 해당 정당의 색상을 추가
                    seatColors[layer].add(Color.parseColor(partyList[currentPartyIndex].color))
                    layerPartySeatsWithRounding[currentPartyIndex]--
                    remainingSeatsInLayer--
                } else {
                    // 현재 정당의 좌석이 모두 배치되면 다음 정당으로 이동
                    currentPartyIndex++
                }
            }

            // 배정 상태를 검증
            if (allocatedSeats.sum() > totalSeats) {
                Log.e("SemiCircleGraphView", "Error: Allocated seats exceed total seats!")
            }
        }

        // 각 층별 원 그리기
        for (layer in 0 until totalLayers) {
            val radius = maxRadius - (layer * layerGap)
            val numCircles = layerSeats[layer]
            val sweepAngle = 180f / (numCircles - 1)

            for (position in 0 until numCircles) {
                // 현재 원의 각도 계산
                val angle = -180f + (position * sweepAngle)

                // 각도 제한: currentSweepAngle 이하만 그리기
                if (angle > currentSweepAngle) break

                // 좌석 색상 설정
                paint.color = seatColors[layer][position]

                // 좌표 계산
                val x = (centerX + radius * Math.cos(Math.toRadians(angle.toDouble()))).toFloat()
                val y = (centerY + radius * Math.sin(Math.toRadians(angle.toDouble()))).toFloat()

                // 원 크기 설정
                val circleSize = 8f
                canvas.drawCircle(x, y, circleSize, paint)
            }
        }
    }

    private fun calculateCircles(totalSeats: Int, totalLayers: Int, a1: Int, d: Int): List<Int> {
        // 1. 등차수열로 각 층의 원 개수 생성
        val rawCircleCounts = (0 until totalLayers).map { a1 + it * d } // 등차수열 생성

        // 2. 등차수열 총합 계산
        val sumOfRawCircles = rawCircleCounts.sum()

        // 3. 전체 원 개수를 totalSeats에 맞게 조정 (소수점 포함)
        val scalingFactor = totalSeats.toDouble() / sumOfRawCircles
        val scaledCircleCountsWithDecimals = rawCircleCounts.map { it * scalingFactor }

        // 4. 정수로 변환하면서 누적 손실 계산
        val scaledCircleCounts = scaledCircleCountsWithDecimals.map { it.toInt() }.toMutableList()
        val totalAssignedSeats = scaledCircleCounts.sum()

        // 5. 손실 보정
        var remainingSeats = totalSeats - totalAssignedSeats // 남은 좌석 수
        while (remainingSeats > 0) {
            // 가장 큰 소수점 부분을 가진 층에 1 추가
            val maxDecimalIndex = scaledCircleCountsWithDecimals
                .mapIndexed { index, value -> value - scaledCircleCounts[index] }
                .withIndex()
                .maxByOrNull { it.value }!!.index
            scaledCircleCounts[maxDecimalIndex] += 1
            remainingSeats -= 1
        }

        // 6. 조정된 리스트 반환
        return scaledCircleCounts.reversed()
    }
}