package com.example.quiz.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.quiz.R
import com.example.quiz.databinding.FragmentMainBinding
import com.example.quiz.states.UIState
import com.example.quiz.viewmodel.MainViewModel


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.viewmodel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        if (binding.viewmodel?.quizzes?.value == null) {
            binding.viewmodel?.loadQuizzes()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState(view)
        pushItemsToScrollView(view)
    }

    private fun observeUiState(view: View) {

        val layout = view.findViewById<ConstraintLayout>(R.id.main_layout)
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


    private fun pushItemsToScrollView(view: View) {

        val scrollView = view.findViewById<LinearLayout>(R.id.main_scroll_view)
        binding.viewmodel?.quizzes?.observe(viewLifecycleOwner) { quizzes ->
            scrollView.removeAllViews()
            if (quizzes != null) {
                for (quiz in quizzes) {

                    val textView = TextView(view.context)
                    textView.textSize = 22f
                    textView.setPadding(312, 4, 4, 4)
                    textView.text = quiz.name

                    val imageView = ImageView(view.context)
                    val iParams = ConstraintLayout.LayoutParams(300, 300)
                    imageView.layoutParams = iParams
                    imageView.load(quiz.imageUrl)

                    val cardView = CardView(view.context)
                    val cParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        300
                    )
                    cParams.setMargins(15)
                    cardView.layoutParams = cParams
                    cardView.addView(imageView)
                    cardView.addView(textView)

                    cardView.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putString("quiz_id", quiz.id)
                        findNavController().navigate(R.id.quizFragment, bundle)
                    }

                    scrollView.addView(cardView)
                }

            }
        }
    }

}
