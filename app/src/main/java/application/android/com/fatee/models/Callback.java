package application.android.com.fakee.models;

public interface Callback<T> {
    void onSuccess(T result);

    void onError(Throwable what);
}
