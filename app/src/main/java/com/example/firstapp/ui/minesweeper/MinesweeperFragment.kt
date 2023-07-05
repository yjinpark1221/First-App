package com.example.firstapp.ui.minesweeper

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.firstapp.R
import com.example.firstapp.databinding.FragmentMinesweeperBinding
import kotlinx.coroutines.delay
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
    var row = 5
    var col = 5
    var leftCnt = 0
    var mineCnt = 6
    var userCnt = 0
    lateinit var borderDrawable : Drawable
    lateinit var borderDrawableDarker : Drawable

    // timer
    private var sec: Int = 0
    private var timerTask: TimerTask? = null
    private val timer: Timer = Timer()

    // handler
    val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val minesweeperViewModel =
            ViewModelProvider(this).get(MinesweeperViewModel::class.java)

        _binding = FragmentMinesweeperBinding.inflate(inflater, container, false)
        val root: View = binding.root
        borderDrawable = resources.getDrawable(R.drawable.upopened_square)
        borderDrawableDarker = resources.getDrawable(R.drawable.button_border_darker)

        startBtn = binding.startBtn
        easyBtn = binding.easyBtn
        hardBtn = binding.hardBtn
        startBtn.setBackgroundResource(R.drawable.upopened_square)

//        binding.timerTextView.setVisibility(View.INVISIBLE)
        startBtn.setOnClickListener{
            startGame(row, col, mineCnt)
            startBtn.isEnabled = false
            startBtn.background = borderDrawableDarker

            easyBtn.isEnabled = false
            hardBtn.isEnabled = false
//            binding.timerTextView.setVisibility(View.VISIBLE)
            startTimer()
        }




        binding.toggleButtonGroup.addOnButtonCheckedListener{ toggleButtonGroup, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.easy_btn -> {
                        row = 5
                        col = 5
                        mineCnt = 6
                        startBtn.isEnabled = true
                    }
                    R.id.hard_btn -> {
                        row = 7; col = 6; mineCnt = 10;
                        startBtn.isEnabled = true
                    }
                }
            }
        }
//        easyBtn.setOnClickListener{
//            row = 5
//            col = 5
//            mineCnt = 6
//            startBtn.isEnabled = true
//        }

//        hardBtn.setOnClickListener{
//            row = 7
//            col = 6
//            mineCnt = 10
//            startBtn.isEnabled = true
//        }
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
        startBtn.text = "🙂"
        leftCnt = row * col - mine
        userCnt = mine
        binding.bombTextView.setText(threeDigits(userCnt.toString()))
        val gridLayout = binding.gridLayout

        // TODO : 난이도 같은 경우 그리드 재활용하기
        // 현재는 매 판 지우고 그림
        gridLayout.removeAllViews()
        btns = ArrayList<Button>()
        for (i in 0 until row) {
            for (j in 0 until col) {
                val idx = i * col + j
                val button = Button(this.context, null, R.style.SquareButtonStyle)

//                val imgbutton = ImageButton(this.activity, null, R.drawable.upopened_square)
                val params = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = resources.getDimensionPixelSize(R.dimen.button_size)
                    height = resources.getDimensionPixelSize(R.dimen.button_size)
                    //setMargins(2, 2, 2, 2)
                }

                button.gravity = Gravity.CENTER
                button.setBackgroundResource(R.drawable.upopened_square)
                button.layoutParams = params
                gridLayout.addView(button)

                button.isEnabled = false
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f)
                button.setTypeface(null, Typeface.BOLD)
                btns.add(button)

                // button reveal effect
                button.visibility = View.INVISIBLE
                val visible_delay = idx * 250L
                val enable_delay = col * row * 250L
                handler.postDelayed({
                    button.visibility = View.VISIBLE // Set the button's visibility to visible after the delay
                }, visible_delay)
                handler.postDelayed({
                    button.isEnabled = true // Set the button's visibility to visible after the delay
                }, enable_delay)

                button.setOnTouchListener { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if (button.isEnabled == true)
                                button.background = borderDrawableDarker
                        }
                        MotionEvent.ACTION_UP -> {
                            if (button.isEnabled == true)
                                button.setBackgroundResource(R.drawable.upopened_square)
                        }
                    }
                    false
                }

                button.setOnClickListener {
                    if (button.text == "🚩") {
                    }
                    else if (open(i, j) == 0) {
                        lose()
                    }
                }
                button.setOnLongClickListener {
                    if (button.text == "🚩") {
                        eraseFlag(i, j)
                        userCnt++
                        binding.bombTextView.setText(threeDigits(userCnt.toString()))
                    }
                    else {
                        markFlag(i, j)
                        userCnt--
                        binding.bombTextView.setText(threeDigits(userCnt.toString()))
                    }
                    true
                }
            }
        }
        val mineIdxs = getRandom(row * col - 1, mine)
        mineMap = Array(row) { IntArray(col) }


        for (i in 0 until row) {
            for (j in 0 until col) {
                val idx = i * col + j
                val button = getButton(i, j)
                //button.isEnabled = true
                if (mineIdxs.contains(idx)) {
                    mineMap[i][j] = -1
                }
                button.text = ""
                button.setBackgroundResource(R.drawable.upopened_square)
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

    // mineMap 이 -1이면 지뢰, -2이면 already opened
    fun open(i: Int, j: Int) : Int {
        val button = getButton(i, j)
        button.isEnabled = false
        if (mineMap[i][j] == -2) return -1

        button.background = borderDrawableDarker

        if (mineMap[i][j] == -1) {
            openMine(button)
            return 0
        }
        else {
            when (mineMap[i][j]) {
                1 -> {
                    button.setTextColor(Color.rgb(0, 0, 230))
                }
                2 -> {
                    button.setTextColor(Color.rgb(0, 127, 0))
                }
                3 -> {
                    button.setTextColor(Color.rgb(230, 0, 0))
                }
                4 -> {
                    button.setTextColor(Color.rgb(0, 0, 127))
                }
                5 -> {
                    button.setTextColor(Color.rgb(127, 63, 0))
                }
                6 -> {
                    button.setTextColor(Color.rgb(0, 127, 127))
                }
                7 -> {
                    button.setTextColor(Color.rgb(0, 0, 0))
                }
                8 -> {
                    button.setTextColor(Color.rgb(63, 63, 63))
                }
                else -> {
                }
            }
            button.text = mineMap[i][j].toString()
            --leftCnt
            if (leftCnt == 0) win()

            if (mineMap[i][j] == 0) {
                button.text = ""
                mineMap[i][j] = -2
                for (dx in -1 until 2) {
                    for (dy in -1 until 2) {
                        if (dx == 0 && dy == 0) continue
                        val ni = i + dx
                        val nj = j + dy
                        if (inMap(ni, nj, row, col)) {
                            open(ni, nj)
                        }
                    }
                }
            }
            mineMap[i][j] = -2
            return 1
        }
    }

    fun lose() {
        startBtn.text = "😖"
        stopTimer()
        endGame()
    }
    fun win() {
        startBtn.text = "😎"
        endGame()
    }

    fun openMine(button : Button) {
        button.text = "💣"
        button.background = borderDrawableDarker
        button.setTextColor(Color.BLACK)
    }

    fun eraseFlag(i : Int, j : Int) {
        val button = getButton(i, j)
        button.text = ""
    }
    fun markFlag(i : Int, j : Int) {
        val button = getButton(i, j)
        button.text = "🚩"
    }

    fun getButton(i : Int, j : Int) : Button {
        return btns[i * col + j]
    }

    fun endGame() {
        startBtn.isEnabled = true
        startBtn.setBackgroundResource(R.drawable.upopened_square)
        easyBtn.isEnabled = true
        hardBtn.isEnabled = true
        stopTimer()
        for (i in 0 until row) {
            for (j in 0 until col) {
                val button = getButton(i, j)
                button.isEnabled = false
                if (mineMap[i][j] == -1) {
                    openMine(button)
                }
            }
        }
    }

    private fun startTimer() {
        sec = -1

        // Schedule a task to update the timer every second
        timerTask = object : TimerTask() {
            override fun run() {
                // Update the UI on the main thread
                activity?.runOnUiThread {
                    // Update the timer text view with the elapsed time
                    binding.timerTextView.text = threeDigits(sec.toString())
                }
                if (sec < 999) sec++
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
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
    }

    private fun threeDigits(value: String): String {
        var ret: String = "000"
        if (value.length == 1) ret = "00" + value
        else if (value.length == 2) ret = "0" + value
        return ret
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopTimer()
        _binding = null
    }
}