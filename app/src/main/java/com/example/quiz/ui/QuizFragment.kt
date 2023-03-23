package com.example.quiz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quiz.R
import com.example.quiz.databinding.FragmentQuizBinding
import com.example.quiz.states.UIState
import com.example.quiz.viewmodel.QuizViewModel


class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = ViewModelProvider(requireActivity())[QuizViewModel::class.java]

        val quizId = requireArguments().getString("quiz_id")
        binding.viewmodel!!.loadQuiz(quizId!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState(view)
        observeQuizIsCompleted()
    }

    private fun observeUiState(view: View) {

        val layout = view.findViewById<ConstraintLayout>(R.id.quiz_layout)
        val pb = ProgressBar(view.context, null, android.R.attr.progressBarStyleLarge)
        pb.visibility = View.INVISIBLE
        pb.setPadding(400)
        pb.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        layout.addView(pb)

        binding.viewmodel?.uiState?.observe(viewLifecycleOwner) { state ->
            if (state == UIState.Loading) {
                pb.visibility = View.VISIBLE

            }
            if (state == UIState.Failure) {
                pb.visibility = View.INVISIBLE
                findNavController().navigate(R.id.errorFragment)
            }
            if (state == UIState.Success) {
                pb.visibility = View.INVISIBLE
            }

        }
    }

    private fun observeQuizIsCompleted() {
        binding.viewmodel?.quizCompleted?.observe(viewLifecycleOwner) { quizCompleted ->
            if (quizCompleted == true) {
                findNavController().navigate(R.id.quizResultFragment)
            }
        }
    }


}

