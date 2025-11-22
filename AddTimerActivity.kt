package com.example.timerapri

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.timerapri.databinding.AddTimerBinding

class AddTimerActivity: AppCompatActivity() {

    private lateinit var binding: AddTimerBinding

    fun time(long: Long): Long {
        // 入力が HHMMSS 形式であると想定して計算します

        // SS (秒) 部分を取得
        val second = long % 100

        // MM (分) 部分を取得し、秒に変換
        // (例: 123456 -> 1234 -> 34)
        val minute = (long / 100) % 100 * 60

        // HH (時) 部分を取得し、秒に変換
        // (例: 123456 -> 12)
        val hour = long / 10000 * 3600

        // 合計秒数を返す
        return hour + minute + second
    }
    // 再生ボタン機構
    fun timerStartAndFinish() {
        val text = binding.editText.getText().toString()
        val num = text.toLongOrNull()

        // num が null ではない（＝正しく数値に変換できた）場合のみ処理を実行
        if (num != null) {
            val totalSeconds = time(num)
            // 成功！
            // データをIntentに入れて、呼び出し元(MainActivity)に返す
            val resultIntent = Intent()
            resultIntent.putExtra("DURATION_SECONDS", totalSeconds)
            setResult(Activity.RESULT_OK, resultIntent)

            finish() // このアクティビティを閉じてMainActivityに戻る
        } else {
            binding.editText.error = "数値を入力してください"
            // 失敗した場合（例: 入力が空、または "abc" など）
            // ここでユーザーに「数値を入力してください」とエラーメッセージを出すなどの処理ができます
            // (例: binding.editText.error = "数値を入力してください")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AddTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun numInsert(editText: EditText, text: String) {
            val position = editText.selectionStart
            editText.text.insert(position, text)
        }


        // 再生ボタン
        binding.startButton.setOnClickListener {
            timerStartAndFinish()
        }

        // 数字のボタン
        binding.button1.setOnClickListener {
            numInsert(editText = binding.editText, text = "1")
        }
        binding.button2.setOnClickListener {
            numInsert(editText = binding.editText, text = "2")
        }
        binding.button3.setOnClickListener {
            numInsert(editText = binding.editText, text = "3")
        }
        binding.button4.setOnClickListener {
            numInsert(editText = binding.editText, text = "4")
        }
        binding.button5.setOnClickListener {
            numInsert(editText = binding.editText, text = "5")
        }
        binding.button6.setOnClickListener {
            numInsert(editText = binding.editText, text = "6")
        }
        binding.button7.setOnClickListener {
            numInsert(editText = binding.editText, text = "7")
        }
        binding.button8.setOnClickListener {
            numInsert(editText = binding.editText, text = "8")
        }
        binding.button9.setOnClickListener {
            numInsert(editText = binding.editText, text = "9")
        }
        binding.button10.setOnClickListener {
            numInsert(editText = binding.editText, text = "0")
        }
        binding.button11.setOnClickListener {
            numInsert(editText = binding.editText, text = "00")
        }
        binding.button12.setOnClickListener {
            binding.editText.setText("")
        }
    }
}