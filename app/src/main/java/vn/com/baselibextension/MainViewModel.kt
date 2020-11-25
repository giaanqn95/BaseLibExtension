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
            onSuccess = { LogCat.d("mergeFuntion onSuccess") },
            onError = { LogCat.d("mergeFunc onError") }
        ).end {

        }.loading().buildMerge()
    }

    suspend fun call1(): ResultWrapper<BaseResponse> {
        val header: HashMap<String, String> = HashMap()
        header["token"] =
            "eyJhdXRoIjp7ImNpZiI6ImlzbHAwMDAwMDAwMDE4MCIsInVzZXJJZCI6ImlzbHAwMDAwMDAwMDE4MCIsImRldmljZUlkIjpudWxsLCJ0b2tlblR5cGUiOjEsImRldmljZVN0YXR1cyI6bnVsbCwiZXhwaXJlQXQiOjE2MDYzNzQxNzQ4ODIsImFjdGl2ZVRpbWUiOjM2MDAwMDAsImNyZWF0ZWRBdCI6MTYwNjI4Nzc3NDg4Mn0sImFsZyI6IlJTMjU2In0.eyJqdGkiOiJjNGFkMGVmYjAwMDFiZjc0ZTljYjAxNzVmZTM2NTRhZSIsInVzZXJJZCI6eyJjaWYiOiJpc2xwMDAwMDAwMDAxODAiLCJ1c2VybmFtZSI6IjA5MDI5NzE3NTEiLCJlbWFpbCI6bnVsbCwicGhvbmUiOm51bGwsImRldmljZUlkIjpudWxsLCJhY2NvdW50VHlwZSI6bnVsbCwibGFuZ3VhZ2UiOm51bGwsImN1c3RvbWVyTmFtZSI6bnVsbCwic2V4IjpudWxsLCJ1c2VySWQiOiJpc2xwMDAwMDAwMDAxODAiLCJtZXJjaGFudElkIjpudWxsLCJyYW5rIjpudWxsLCJzdG9yZUlkIjpudWxsfX0.dyI8w10bFsA4WI30kQ_zeFtbLb6fJ27MPTD8P-5Ma1T-kOUsuacCywSmMkS-1XcSL1aumMC_V1th9aO_ZOu2l5pJvmXTDNNIfGzUQscBhGocYefk0GEvQxmxTLL9d5OIijGo-kkhMPWsA5_3sO9S9LRx6kHlWLXABh1jZapKNFg7V0A6C989twExcuQ3_piNH4a62GJbdAxZGiIdEhG2ZBiFuCkCU2I0haRzzdcg2_EGaz4idsPhJQ2Ur_v1B3zXs7u_7W7zfP11lRN-PxcvI1_EgjcUaH0dcDACOWAwRO2Q8BModcAvsh4shvrQ63MjTa4cgICetC-G-FmJAppHfQ"
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
            "eyJhdXRoIjp7ImNpZiI6ImlzbHAwMDAwMDAwMDE4MCIsInVzZXJJZCI6ImlzbHAwMDAwMDAwMDE4MCIsImRldmljZUlkIjpudWxsLCJ0b2tlblR5cGUiOjEsImRldmljZVN0YXR1cyI6bnVsbCwiZXhwaXJlQXQiOjE2MDYzNzQxNzQ4ODIsImFjdGl2ZVRpbWUiOjM2MDAwMDAsImNyZWF0ZWRBdCI6MTYwNjI4Nzc3NDg4Mn0sImFsZyI6IlJTMjU2In0.eyJqdGkiOiJjNGFkMGVmYjAwMDFiZjc0ZTljYjAxNzVmZTM2NTRhZSIsInVzZXJJZCI6eyJjaWYiOiJpc2xwMDAwMDAwMDAxODAiLCJ1c2VybmFtZSI6IjA5MDI5NzE3NTEiLCJlbWFpbCI6bnVsbCwicGhvbmUiOm51bGwsImRldmljZUlkIjpudWxsLCJhY2NvdW50VHlwZSI6bnVsbCwibGFuZ3VhZ2UiOm51bGwsImN1c3RvbWVyTmFtZSI6bnVsbCwic2V4IjpudWxsLCJ1c2VySWQiOiJpc2xwMDAwMDAwMDAxODAiLCJtZXJjaGFudElkIjpudWxsLCJyYW5rIjpudWxsLCJzdG9yZUlkIjpudWxsfX0.dyI8w10bFsA4WI30kQ_zeFtbLb6fJ27MPTD8P-5Ma1T-kOUsuacCywSmMkS-1XcSL1aumMC_V1th9aO_ZOu2l5pJvmXTDNNIfGzUQscBhGocYefk0GEvQxmxTLL9d5OIijGo-kkhMPWsA5_3sO9S9LRx6kHlWLXABh1jZapKNFg7V0A6C989twExcuQ3_piNH4a62GJbdAxZGiIdEhG2ZBiFuCkCU2I0haRzzdcg2_EGaz4idsPhJQ2Ur_v1B3zXs7u_7W7zfP11lRN-PxcvI1_EgjcUaH0dcDACOWAwRO2Q8BModcAvsh4shvrQ63MjTa4cgICetC-G-FmJAppHfQ"
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