package com.espica.data.network;

import com.espica.data.network.NetworkApiService;
import com.espica.data.network.response.DefaultResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by IT-10 on 10/16/2018.
 */

public class ApiClient implements Consumer<Throwable> {
    private final NetworkApiService networkApiService;

    @SuppressWarnings("unchecked")
    private final ObservableTransformer apiCallTransformer =
            observable ->
                    observable.map(new Function<Object, Object>() {
                        @Override
                        public Object apply(Object appResponse) throws Exception {

                            return appResponse;
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .retryWhen(new RetryWithDelay()).doOnError(this)
                            .observeOn(AndroidSchedulers.mainThread());

    public ApiClient(NetworkApiService networkApiService) {
        this.networkApiService = networkApiService;
    }

    @Override
    public void accept(Throwable throwable) throws Exception {
        final Class<?> throwableClass = throwable.getClass();
        if (SocketException.class.isAssignableFrom(throwableClass) || SSLException.class.isAssignableFrom(throwableClass)) {
//            this.showConnectionProblemDialog(R.string.error_internal_api);
        } else if (throwableClass.equals(SocketTimeoutException.class) || UnknownHostException.class.equals(throwableClass)) {
            //            this.showConnectionProblemDialog(R.string.error_connection_msg);
        } else if (JsonSyntaxException.class.isAssignableFrom(throwableClass)) {
        }
    }

    public Observable<DefaultResponse> getAllVideos() {
        return networkApiService.getAllVideos();
    }

    @SuppressWarnings("unchecked")
    private <T> ObservableTransformer<T, T> configureApiCallObserver() {
        return (ObservableTransformer<T, T>) apiCallTransformer;
    }


    class RetryWithDelay implements
            Function<Observable<? extends Throwable>, ObservableSource<?>> {

        private final int maxRetries = 3;
        private int retryCount = 0;

        @Override
        public Observable<?> apply(Observable<? extends Throwable> attempts) throws Exception {
            return attempts
                    .flatMap((Function<Throwable, Observable<?>>) throwable -> {
                        if (throwable instanceof TimeoutException || throwable instanceof SocketTimeoutException) {
                            if (++retryCount < maxRetries) {
                                return Observable.timer(
                                        (long) Math.pow(2, retryCount),
                                        TimeUnit.SECONDS);
                            }
                        }
                        return Observable.error(throwable);
                    });
        }
    }
}
