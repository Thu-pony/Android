package com.example.fragmenttest

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class right_Fragment:Fragment() {
    companion object {
        const val TAG = "RightFragment"
        var conut:Int = 0
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG,"onAttach")
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        conut++

        val view = inflater.inflate(R.layout.right_fragment,container,false)
        val textView:TextView = view.findViewById(R.id.num_text)
        textView.text = "This is right fragment " + conut
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG,"onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG,"onDestoryview")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestory")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG,"onDetch")
    }

}