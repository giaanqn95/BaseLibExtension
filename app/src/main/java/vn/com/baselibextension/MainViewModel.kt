package vn.com.baselibextension

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vn.com.baselibextension.setup_retrofit.KeyRequest
import vn.com.baselibextension.setup_retrofit.Repo
import vn.com.baselibextension.setup_retrofit.TypeRepo

/**
 * Created by giaan on 11/18/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */
class MainViewModel() : ViewModel() {

    fun callSomething() = CoroutineScope(Dispatchers.IO).launch {
        InjectContext.instance.retrofitService.request(
            Repo(
                headers = HashMap(),
                request = KeyRequest.LOGIN,
                message = LoginBinding("username", "Hash.getPublicKey(password)"),
                codeRequired = "2000",
                typeRepo = TypeRepo.POST
            )
        ).work(
            onSuccess = {},
            onError = {}
        ).loading().build()
    }
}