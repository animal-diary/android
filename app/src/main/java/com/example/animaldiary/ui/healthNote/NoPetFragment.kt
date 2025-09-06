// NoPetFragment.kt

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.animaldiary.databinding.FragmentNoPetBinding

class NoPetFragment : Fragment() {

    interface OnPetAddedListener {
        fun onAddPetClicked()
    }

    private var _binding: FragmentNoPetBinding? = null
    private val binding get() = _binding!!
    private var listener: OnPetAddedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnPetAddedListener) {
            listener = parentFragment as OnPetAddedListener
        } else {
            throw RuntimeException("$parentFragment must implement OnPetAddedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddPet.setOnClickListener {
            listener?.onAddPetClicked()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}