package vn.com.baselibextension.setup_retrofit

/**
 * Created by giaan on 11/18/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */
class Repo(val headers: Map<String, String>,
           val request: KeyRequest,
           val message: Any? = null,
           val codeRequired: Any,
           val typeRepo: TypeRepo = TypeRepo.GET) {
}