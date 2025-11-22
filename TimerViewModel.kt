package com.example.timerapri

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.collections.map

data class TimerModel(
    val id: Long = System.currentTimeMillis(), // 識別ID
    val initialTime: Long,     // 設定時間 (例: 10000ms)
    val remainingTime: Long,   // 残り時間 (例: 5000ms)
    val isRunning: Boolean     // 動いているか
)
class TimerViewModel : ViewModel() {

    // 【変更点1】単一のLongではなく、リスト全体を公開する
    private val _timerList = MutableLiveData<List<TimerModel>>(emptyList())
    val timerList: LiveData<List<TimerModel>> = _timerList

    init {
        // ViewModelが作成されたら、全体の管理ループを開始
        startGlobalTimerLoop()
    }

    // 【変更点2】「個別のタイマー」ではなく「全体のループ」を作る
    private fun startGlobalTimerLoop() {
        viewModelScope.launch {
            while (isActive) { // ViewModelが生きている限り回る
                delay(1000)    // 1秒待機
                updateTimers() // 時間更新処理へ
            }
        }
    }

    // リストの中を見て、動いているタイマーだけ時間を減らす
    private fun updateTimers() {
        val currentList = _timerList.value ?: return

        // mapを使って新しいリストを作成（ReactやComposeのような不変更新の考え方）
        val newList = currentList.map { timer ->
            if (timer.isRunning && timer.remainingTime > 0) {
                // 実行中で時間が残っていれば 1000ms 減らす
                timer.copy(remainingTime = timer.remainingTime - 1000)
            } else if (timer.isRunning && timer.remainingTime <= 0L) {
                // 時間切れになったら停止状態にする
                timer.copy(isRunning = false, remainingTime = 0)
            } else {
                // それ以外（一時停止中など）はそのまま
                timer
            }
        }
        _timerList.postValue(newList)
    }

    // 【変更点3】IDを指定して操作する
    fun toggleTimer(id: Long) {
        val currentList = _timerList.value ?: return

        // 指定されたIDのタイマーだけ isRunning を反転させる
        val newList = currentList.map { timer ->
            if (timer.id == id) {
                // 残り時間がある場合のみ再開可能
                if (timer.remainingTime > 0) {
                    timer.copy(isRunning = !timer.isRunning)
                } else {
                    // 時間切れならリセットして再開などのロジック（今回はそのまま）
                    timer
                }
            } else {
                timer
            }
        }
        _timerList.value = newList
    }

    // 新しいタイマーを追加する関数
    fun addTimer(durationSeconds: Long) {
        val currentList = _timerList.value ?: emptyList()
        val durationMillis = durationSeconds * 1000

        val newTimer = TimerModel(
            id = System.currentTimeMillis(), // 一意なID
            initialTime = durationMillis,
            remainingTime = durationMillis,
            isRunning = false // 最初は停止状態で追加
        )

        _timerList.value = currentList + newTimer
    }
}
