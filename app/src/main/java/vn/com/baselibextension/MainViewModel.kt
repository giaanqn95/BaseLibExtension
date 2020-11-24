package vn.com.baselibextension

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import vn.com.baselibextension.setup_retrofit.*
import vn.com.baselibextension.utils.LogCat

/**
 * Created by giaan on 11/18/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */
class MainViewModel() : ViewModel() {

    fun callSomething() = CoroutineScope(Dispatchers.IO).launch {
        InjectContet.getRetro().request(
            Repo(
                headers = HashMap(),
                url = KeyRequest.LOGIN.url,
                message = LoginBinding("username", "Hash.getPublicKey(password)"),
                codeRequired = "2000",
                typeRepo = TypeRepo.POST
            )
        ).work(
            onSuccess = {},
            onError = {}
        ).loading().build()
    }

    fun mergeFunc() = CoroutineScope(Dispatchers.IO).launch {
        InjectContet.getRetro().merge(call1(), call2()).work(
            onSuccess = { LogCat.d("mergeFunc onSuccess") },
            onError = { LogCat.d("mergeFunc onError") }
        ).end {

        }.loading().buildMerge()
    }

    suspend fun call1(): ResultWrapper<BaseResponse> {
        val header: HashMap<String, String> = HashMap()
        header["token"] =
            "eyJhdXRoIjp7ImNpZiI6ImlzbHAwMDAwMDAwMDE4MCIsInVzZXJJZCI6ImlzbHAwMDAwMDAwMDE4MCIsImRldmljZUlkIjpudWxsLCJ0b2tlblR5cGUiOjEsImRldmljZVN0YXR1cyI6bnVsbCwiZXhwaXJlQXQiOjE2MDU4MzkzNTQ3MDksImFjdGl2ZVRpbWUiOjM2MDAwMDAsImNyZWF0ZWRBdCI6MTYwNTc1Mjk1NDcwOX0sImFsZyI6IlJTMjU2In0.eyJqdGkiOiIwNTExYzhmZTAwMDFiMmJiMjUzNDAxNzVkZTU1OWY1NSIsInVzZXJJZCI6eyJjaWYiOiJpc2xwMDAwMDAwMDAxODAiLCJ1c2VybmFtZSI6IjA5MDI5NzE3NTEiLCJlbWFpbCI6bnVsbCwicGhvbmUiOm51bGwsImRldmljZUlkIjpudWxsLCJhY2NvdW50VHlwZSI6bnVsbCwibGFuZ3VhZ2UiOm51bGwsImN1c3RvbWVyTmFtZSI6bnVsbCwic2V4IjpudWxsLCJ1c2VySWQiOiJpc2xwMDAwMDAwMDAxODAiLCJtZXJjaGFudElkIjpudWxsLCJzdG9yZUlkIjpudWxsfX0.XI11QCu85NehY4yn2r5g7srSDrf3GLH5rV2HXBSw2sFib7q-8ENntmL1rhLqaDqV_aFIwzZiNiX0a_iLDa4ImHdo_X48Ta7XjgpMdRm3AJbQ8ympBMuvM7RPfo2s_uYQ5l29V4rwd7k7B2ffZNhSsk_rk5DZMwagsgTqW6TDWvxpwSSBRWUm1LheruduvvxHO13WZuWhklhWaYHKS-07iFyoQV5OjilgFFDN0y4JKcP19uyaECC1S9p0WVS60SVcTMx8N3wpdHJbXQVuZywP18BfHHspsg6ENfitK0CiZZLrRm0beF-an8RmHXg845I092Ts5DJ5YHrhE9xxNz25qw"
        return InjectContet.getRetro().request(
            Repo(
                headers = header,
                url = KeyRequest.SUBMIT_OTP.url,
                message = null,
                codeRequired = "CARD_2001",
                typeRepo = TypeRepo.GET
            )
        ).build()
    }

    suspend fun call2(): ResultWrapper<BaseResponse> {
        val header: HashMap<String, String> = HashMap()
        header["token"] =
            "eyJhdXRoIjp7ImNpZiI6ImlzbHAwMDAwMDAwMDE4MCIsInVzZXJJZCI6ImlzbHAwMDAwMDAwMDE4MCIsImRldmljZUlkIjpudWxsLCJ0b2tlblR5cGUiOjEsImRldmljZVN0YXR1cyI6bnVsbCwiZXhwaXJlQXQiOjE2MDU4MzkzNTQ3MDksImFjdGl2ZVRpbWUiOjM2MDAwMDAsImNyZWF0ZWRBdCI6MTYwNTc1Mjk1NDcwOX0sImFsZyI6IlJTMjU2In0.eyJqdGkiOiIwNTExYzhmZTAwMDFiMmJiMjUzNDAxNzVkZTU1OWY1NSIsInVzZXJJZCI6eyJjaWYiOiJpc2xwMDAwMDAwMDAxODAiLCJ1c2VybmFtZSI6IjA5MDI5NzE3NTEiLCJlbWFpbCI6bnVsbCwicGhvbmUiOm51bGwsImRldmljZUlkIjpudWxsLCJhY2NvdW50VHlwZSI6bnVsbCwibGFuZ3VhZ2UiOm51bGwsImN1c3RvbWVyTmFtZSI6bnVsbCwic2V4IjpudWxsLCJ1c2VySWQiOiJpc2xwMDAwMDAwMDAxODAiLCJtZXJjaGFudElkIjpudWxsLCJzdG9yZUlkIjpudWxsfX0.XI11QCu85NehY4yn2r5g7srSDrf3GLH5rV2HXBSw2sFib7q-8ENntmL1rhLqaDqV_aFIwzZiNiX0a_iLDa4ImHdo_X48Ta7XjgpMdRm3AJbQ8ympBMuvM7RPfo2s_uYQ5l29V4rwd7k7B2ffZNhSsk_rk5DZMwagsgTqW6TDWvxpwSSBRWUm1LheruduvvxHO13WZuWhklhWaYHKS-07iFyoQV5OjilgFFDN0y4JKcP19uyaECC1S9p0WVS60SVcTMx8N3wpdHJbXQVuZywP18BfHHspsg6ENfitK0CiZZLrRm0beF-an8RmHXg845I092Ts5DJ5YHrhE9xxNz25qw"
        return InjectContet.getRetro().request(
            Repo(
                headers = header,
                url = KeyRequest.SUBMIT_OTP.url,
                message = null,
                codeRequired = "CARD_2001",
                typeRepo = TypeRepo.GET
            )
        ).build()
    }

    fun repeat(): Flow<ResultWrapper<BaseResponse>> = flow {
        emit(call2())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val _stateFlow = MutableStateFlow(BaseResponse())
    @OptIn(ExperimentalCoroutinesApi::class)
    var stateFlow: StateFlow<BaseResponse> = MutableStateFlow(BaseResponse())
}