# SSRequest

[![](https://jitpack.io/v/giaanqn95/BaseLibExtension.svg)](https://jitpack.io/#giaanqn95/BaseLibExtension)

This library makes it easier and faster for devs to make API calls

- Example
```bash
fun simpleRequest(merchantId: String, cardNumber: String) = viewModelScope().launch {
        call().request(
            Repo(
                headers = Header(),
                url = url,
                codeRequired = "CodeSuccessAPI",
                message = body(Any),
                typeRepo = TypeRepo.GET,
            )
        ).work(
            onSuccess = { //Do something },
            onError = { //Do something }
        ).loading { boolean }.build()
    }
```
- Or return state request
```
    suspend fun requestReturnState(uri: MultipartBody.Part?): ResultWrapper<BaseResponse> = 
        call().request(
            Repo(
              headers = Header(),
              url = url,
              codeRequired = "CodeSuccessAPI",
              message = body(Any),
              typeRepo = TypeRepo.POST_MULTIPART,
              multiPart = uri
              )
        ).work(
            onSuccess = { //Do something },
            onError = { //Do something }
        ).build()
```
- With multipart
```
suspend fun jobMultiLink(uri: MultipartBody.Part?): ResultWrapper<BaseResponse> = 
    call().request(
        Repo(
            headers = Header(),
            url = url,
            codeRequired = "CodeSuccessAPI",
            message = body(Any),
            typeRepo = TypeRepo.POST_MULTIPART,
            multiPart = uri
        )
    ).work(
        onSuccess = { //Do something },
        onError = { //Do something }
    ).build()
```

You can configure the baseURL and API handling in Application:
```
ApiClientModule.host = "base_url"
```
Configure RetroService: 
```
class InjectRetroService {

    private lateinit var retrofitService: RetrofitService<YourBaseResponseClass>

    companion object {
        val instance by lazy {
            InjectContext()
        }

        fun getRetro(): RetrofitService<YourBaseResponseClass> {
            return instance.retrofitService
        }

        fun initRetroService(context: Context) = apply {
            this.instance.retrofitService = RetrofitService(context, YourBaseResponseClass())
        }
    }
}
```
Set up process response:
```
InjectContext.getRetro().setProcessResponse(object : RetrofitService.Process<YourBaseResponseClass> {
            override fun process(response: String, codeRequire: Any): ResultWrapper<YourBaseResponseClass> {
                //this response is String Json
                val parse = JSON.decode(response, YourBaseResponseClass::class.java)
                //Check success
                    return ResultWrapper.Success(parse)
                //Else
                return ResultWrapper.Error(parse.code, parse.message)
            }
        })
```
