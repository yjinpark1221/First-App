package com.example.firstapp.ui.minesweeper

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.firstapp.R
import com.example.firstapp.databinding.FragmentMinesweeperBinding
import java.sql.Types.NULL
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timer
import kotlin.random.Random

class MinesweeperFragment : Fragment() {

    private var _binding: FragmentMinesweeperBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var mineMap : Array<IntArray>
    lateinit var btns : ArrayList<Button>
    lateinit var startBtn : Button
    lateinit var easyBtn : Button
    lateinit var hardBtn : Button
    var row = 0
    var col = 0
    var leftCnt = 0
    var mineCnt = 0
    lateinit var borderDrawable : Drawable
    lateinit var borderDrawableDarker : Drawable

    // timer
    private var sec: Int = 0
    private var timerTask: TimerTask? = null
    private val timer: Timer = Timer()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val minesweeperViewModel =
            ViewModelProvider(this).get(MinesweeperViewModel::class.java)

        _binding = FragmentMinesweeperBinding.inflate(inflater, container, false)
        val root: View = binding.root
        borderDrawable = resources.getDrawable(R.drawable.button_border)
        borderDrawableDarker = resources.getDrawable(R.drawable.button_border_darker)

        startBtn = binding.startBtn
        easyBtn = binding.easyBtn
        hardBtn = binding.hardBtn

        startBtn.setOnClickListener{
            startGame(row, col, mineCnt)
            startBtn.isEnabled = false
            easyBtn.isEnabled = false
            hardBtn.isEnabled = false
            startTimer()
        }
        startBtn.isEnabled = false

        easyBtn.setOnClickListener{
            row = 5
            col = 5
            mineCnt = 6
            startBtn.isEnabled = true
        }

        hardBtn.setOnClickListener{
            row = 7
            col = 6
            mineCnt = 10
            startBtn.isEnabled = true
        }
        return root
    }

    fun getRandom(range: Int, cnt: Int): MutableList<Int> {
        val randomNumbers = mutableListOf<Int>()

        while (randomNumbers.size < cnt) {
            val randomNumber = Random.nextInt(range)
            if (!randomNumbers.contains(randomNumber)) {
                randomNumbers.add(randomNumber)
            }
        }
        return randomNumbers
    }
    fun startGame(row: Int, col: Int, mine: Int) {
        leftCnt = row * col - mine
        val gridLayout = binding.gridLayout

        // TODO : 난이도 같은 경우 그리드 재활용하기
        // 현재는 매 판 지우고 그림
        gridLayout.removeAllViews()
        btns = ArrayList<Button>()
        for (i in 0 until row) {
            for (j in 0 until col) {
                val idx = i * col + j
                val button = Button(this.context, null, R.style.SquareButtonStyle)

                val params = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = resources.getDimensionPixelSize(R.dimen.button_size)
                    height = resources.getDimensionPixelSize(R.dimen.button_size)
                    setMargins(2, 2, 2, 2)
                }

                button.gravity = Gravity.CENTER
                button.background = borderDrawable
                button.layoutParams = params
                button.setPadding(8, 8, 8, 8)
                gridLayout.addView(button)
                btns.add(button)
                button.setOnClickListener {
                    open(button, i, j)
                }
            }
        }
        val mineIdxs = getRandom(row * col - 1, mine)
        mineMap = Array(row) { IntArray(col) }


        for (i in 0 until row) {
            for (j in 0 until col) {
                val idx = i * col + j
                val button = btns[idx]
                button.isEnabled = true
                if (mineIdxs.contains(idx)) {
                    mineMap[i][j] = -1
                }
                button.text = ""
                button.background = borderDrawable
            }
        }
        for (i in 0 until row) {
            for (j in 0 until col) {
                if (mineMap[i][j] == -1) continue

                var cnt = 0
                for (dx in -1 until 2) {
                    for (dy in -1 until 2) {
                        if (dx == 0 && dy == 0) continue
                        val ni = i + dx
                        val nj = j + dy
                        if (inMap(ni, nj, row, col)) {
                            if (mineMap[ni][nj] == -1) ++cnt
                        }
                    }
                }
                mineMap[i][j] = cnt
            }
        }
    }
    fun inMap(i: Int, j: Int, row: Int, col: Int) : Boolean {
        return (0 <= i && i < row) && (0 <= j && j < col)
    }

    // TODO : mineMap이 0인 경우 색 더 진하게
    // mineMap 이 -1이면 지뢰, -2이면 already opened
    fun open(view: Button, i: Int, j: Int) {
        if (mineMap[i][j] == -2) return

        view.background = borderDrawableDarker
        view.setTextColor(Color.WHITE)
        view.text = mineMap[i][j].toString()

        if (mineMap[i][j] == -1) {
            lose()
        }
        else {
            --leftCnt
            if (leftCnt == 0) win()

            if (mineMap[i][j] == 0) {
                mineMap[i][j] = -2
                for (dx in -1 until 2) {
                    for (dy in -1 until 2) {
                        if (dx == 0 && dy == 0) continue
                        val ni = i + dx
                        val nj = j + dy
                        if (inMap(ni, nj, row, col)) {
                            open(btns[ni * col + nj], ni, nj)
                        }
                    }
                }
            }
            mineMap[i][j] = -2
        }
    }

    // TODO : lose시 전체 지뢰 공개, 결과 기록
    fun lose() {
        Toast.makeText(this.context, "lose...", Toast.LENGTH_SHORT).show()
        startBtn.isEnabled = true
        easyBtn.isEnabled = true
        hardBtn.isEnabled = true
        stopTimer()
    }
    fun win() {
        Toast.makeText(this.context, "win!!!", Toast.LENGTH_SHORT).show()
        startBtn.isEnabled = true
        easyBtn.isEnabled = true
        hardBtn.isEnabled = true
        stopTimer()
    }

    private fun startTimer() {
        sec = 0

        // Schedule a task to update the timer every second
        timerTask = object : TimerTask() {
            override fun run() {
                sec++
                // Update the UI on the main thread
                activity?.runOnUiThread {
                    // Update the timer text view with the elapsed time
                    binding.timerTextView.text = formatTime(sec)
                }
            }
        }

        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    private fun stopTimer() {
        // Cancel the timer task if it's running
        timerTask?.cancel()
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format(Locale.getDefault(), "  %02d:%02d", minutes, remainingSeconds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopTimer()
        _binding = null
    }
}