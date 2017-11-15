package lee.vioson.xiumm.utils;

/**
 * Created by viosonlee
 * on 2017/10/14.
 * todo
 */

public interface DataHandler<T> {
    void onDataBack(boolean isEmpty, T t);

    void onDocumentNull();
}
