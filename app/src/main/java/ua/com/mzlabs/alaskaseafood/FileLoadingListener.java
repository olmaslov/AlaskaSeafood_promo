package ua.com.mzlabs.alaskaseafood;

/**
 * Created by omaslov on 16.02.2018.
 */

public interface FileLoadingListener {

    void onBegin();

    void onSuccess();

    void onFailure(Throwable cause);

    void onEnd();

}