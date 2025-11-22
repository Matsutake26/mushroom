package com.example.timerapri

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.example.timerapri.databinding.ActivityMainBinding
import kotlin.jvm.java
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // ViewModelをActivityに紐付け
    private val timerViewModel: TimerViewModel by viewModels()

    // AddTimerActivityから結果を受け取るためのランチャー
    private val addTimerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // AddTimerActivityから「OK」の結果が返ってきた場合
        if (result.resultCode == RESULT_OK) {
            // Intentから秒数を取得 (デフォルトは0)
            val durationSeconds = result.data?.getLongExtra("DURATION_SECONDS", 0L) ?: 0L
            if (durationSeconds > 0) {
                // ViewModelに新しいタイマーを開始させる
                timerViewModel.startNewTimer(durationSeconds) // (この関数は後でViewModelに作ります)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModelの残り時間を監視し、UIを更新
        timerViewModel.timeLeft.observe(this) { seconds ->
            binding.timerText.text = "$seconds 秒"
        }

        // ViewModelの実行状態を監視し、ボタンのテキストを変更
        timerViewModel.isTimerRunning.observe(this) { isRunning ->
            binding.spButton.text = if (isRunning) "一時停止" else "開始"
        }

        // スタート/一時停止ボタンのクリック処理
        binding.spButton.setOnClickListener {
            timerViewModel.toggleTimer() // ViewModelのtoggleTimerを呼ぶ
        }

        //タイマー時間追加
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddTimerActivity::class.java)
            addTimerLauncher.launch(intent)
        }
    }
}