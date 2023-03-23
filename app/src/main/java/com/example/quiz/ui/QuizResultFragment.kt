package com.example.quiz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quiz.R
import com.example.quiz.databinding.FragmentQuizResultBinding
import com.example.quiz.viewmodel.QuizViewModel


class QuizResultFragment : Fragment() {

    private lateinit var binding: FragmentQuizResultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz_result, container, false)
        binding.viewmodel = ViewModelProvider(requireActivity())[QuizViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeOnAgain()
        observeOnViewErrors(view)
        observeOnBack(view)
    }

    private fun observeOnAgain() {
        binding.viewmodel?.quizCompleted?.observe(viewLifecycleOwner) { quizCompleted ->
            if (quizCompleted == false) {
                findNavController().popBackStack()
            }
        }
    }

    private fun observeOnViewErrors(view: View) {
        val button = view.findViewById<Button>(R.id.on_view_errors_button)
        button?.setOnClickListener {
            findNavController().navigate(R.id.viewErrorsFragment)
        }
    }

    private fun observeOnBack(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.quiz_result_toolbar)
        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }
    }

}