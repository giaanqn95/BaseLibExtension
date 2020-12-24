# SSRequest

[![](https://jitpack.io/v/giaanqn95/BaseLibExtension.svg)](https://jitpack.io/#giaanqn95/BaseLibExtension)

This library makes it easier and faster for devs to make API calls

- Example
```
        RetrofitService<YourBaseResponseClass>().build(
            Repo(
                headers = header,
                url = "url",
                message = Any(),
                codeRequired = "CodeSuccessAPI",
                typeRepo = TypeRepo.GET
            ),
            Request<Ba cseResponse>().work(
            onSuccess = { //Do something },
            onError = { //Do something }
        ))
   
```
- Or return state request
```
    suspend fun requestReturnState(): ResultWrapper<YourBaseResponseClass> = 
        RetrofitService<YourBaseResponseClass>().build(
            Repo(
                headers = header,
                url = "url",
                message = Any(),
                codeRequired = "CodeSuccessAPI",
                typeRepo = TypeRepo.GET
            ),
            Request<BaseResponse>().work(
            onSuccess = { //Do something },
            onError = { //Do something }
        ))
```
- With multipart
```
    RetrofitService<YourBaseResponseClass>().build(
             Repo(
                headers = header,
                url = "url",
                message = Any(),
                codeRequired = "CodeSuccessAPI",
                typeRepo = TypeRepo.GET,
                multiPart = MultiPart
            ),
            Request<BaseResponse>().work(
            onSuccess = { //Do something },
            onError = { //Do something }
        ))
```

- Merge function
```
 fun mergeFunc() = CoroutineScope(Dispatchers.IO).launch {
        RetrofitService<YourBaseResponseClass>().merge(arrayOf(callFirst, callSecond).toMutableList())
        .work(
            onSuccess = { isSuccess.postValue(true) },
            onError = { isSuccess.postValue(false) },
        ).buildMerge()
    }
    
 val callFirst: suspend () -> ResultWrapper<YourBaseResponseClass> = {
        val header: HashMap<String, String> = HashMap()
        header["token"] = ""
        InjectContet.getRetro().build(
            Repo(
                headers = header,
                url = "url",
                message = Any(),
                codeRequired = "CodeSuccessAPI",
                typeRepo = TypeRepo.GET,
            ), Request<YourBaseResponse>().work(
                onSuccess = { LogCat.d("onWork 1 - ${it.value.data()} ${it.value.message}") },
                onError = { LogCat.d("onWork 1 - ${it.message}") }
            ))
    }
```

You can configure the baseURL and API handling in Application:
```
ApiClientModule.host = "base_url"
```
Configure RetroService: 
  Example:
```
object InjectContext {

    lateinit var retrofitService: RetrofitService<YourBaseResponseClass>

    fun initRetroService(context: Context) {
        retrofitService = RetrofitService(context, YourBaseResponseClass())
        retrofitService.setProcessResponse(...)
    }
}

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ApiClientModule.host = "url"
        InjectContet.initRetroService(this)
    }
}
```
Set up process response:
```
RetrofitService<YourBaseResponseClass>().setProcessResponse(object : RetrofitService.Process<YourBaseResponseClass> {
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
