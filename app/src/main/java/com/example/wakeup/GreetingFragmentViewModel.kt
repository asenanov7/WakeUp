package com.example.wakeup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class GreetingFragmentViewModel : ViewModel() {

     val imagePicked = MutableStateFlow(false)

     val textNotEmpty = MutableStateFlow(false)

     val buttonEnabled = combine(imagePicked, textNotEmpty) { imagePicked, textNotEmpty ->
          imagePicked && textNotEmpty
     }

}