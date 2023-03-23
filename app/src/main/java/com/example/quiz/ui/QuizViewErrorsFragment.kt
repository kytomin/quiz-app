package com.example.quiz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quiz.R
import com.example.quiz.databinding.FragmentQuizViewErrorsBinding
import com.example.quiz.viewmodel.QuizViewModel

class QuizViewErrorsFragment : Fragment() {
    private lateinit var binding: FragmentQuizViewErrorsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_quiz_view_errors, container, false
        )
        binding.viewmodel = ViewModelProvider(requireActivity())[QuizViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeOnBack(view)
        pushItemsToScrollView(view)
    }

    private fun observeOnBack(view: View) {

        val toolbar = view.findViewById<Toolbar>(R.id.quiz_view_errors_toolbar)

        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun pushItemsToScrollView(view: View) {
        val scrollView = view.findViewById<LinearLayout>(R.id.quiz_view_errors_scroll_view)

        val size = binding.viewmodel!!.numberOfQuestions.value
        for (index: Int in 0 until size!!) {
            val question = binding.viewmodel!!.getQuestionByIndex(index)
            if (question.userAnswerIndex != question.correctAnswerIndex) {
                val tv = TextView(view.context)
                tv.textSize = 20F
                var text = "${getString(R.string.question)}: ${question.text}\n" +
                        "${getString(R.string.your_answer)}: ${question.answers[question.userAnswerIndex!!]}\n" +
                        "${getString(R.string.correct_answer)}: ${question.answers[question.correctAnswerIndex]}\n"

                if (question.correctAnswerComment != null) {
                    text += "${getString(R.string.comment)}: ${question.correctAnswerComment}\n"
                }
                tv.text = text
                scrollView.addView(tv)

            }
        }


    }
}