package vn.com.baselibextension;

/**
 * Created by giaan on 11/24/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */
public class Test<T extends Object> {

    private Something<T> testMe;

    private T doaa(){
        T a = (T) new Object();
        return a;
    }

    interface Something<T> {
        T returnSome();
    }
}
