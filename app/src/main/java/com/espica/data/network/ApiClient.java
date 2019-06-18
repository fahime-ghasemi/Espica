package com.espica.data.network;

import com.espica.data.network.response.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public Observable<VideoListResponse> getAllVideos() {
        return networkApiService.getAllVideos().compose(configureApiCallObserver());
    }

    @SuppressWarnings("unchecked")
    private <T> ObservableTransformer<T, T> configureApiCallObserver() {
        return (ObservableTransformer<T, T>) apiCallTransformer;
    }

    @NotNull
    public Observable<DefaultResponse<RegisterResponse>> registerDevice(String randomUUID) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("device_id",randomUUID);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonObject.toString());
        return networkApiService.registerDevice(requestBody).compose(configureApiCallObserver());
    }

    @NotNull
    public Observable<DefaultResponse<OTPResponse>> sendOtp(@NotNull String phone, @Nullable String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token",token);
        jsonObject.addProperty("mobile",phone);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonObject.toString());

        return networkApiService.sendOtp(requestBody).compose(configureApiCallObserver());
    }

    public Observable<DefaultResponse<AddToLeitnerResponse>> addToLeitner(RequestBody requestBody)
    {
        return networkApiService.addToLeitner(requestBody).compose(configureApiCallObserver());
    }

    @NotNull
    public Observable<DefaultResponse<VerifyCodeResponse>> verifyCode(@NotNull String code, @Nullable String mobile) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("otp",code);
        jsonObject.addProperty("mobile",mobile);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonObject.toString());

        return networkApiService.verifyCode(requestBody).compose(configureApiCallObserver());
    }

    @NotNull
    public Observable<DefaultResponse<LeitnerDataResponse>> getLeitnerData(RequestBody requestBody) {

        return networkApiService.getLeitnerData(requestBody).compose(configureApiCallObserver());
    }

    @NotNull
    public Observable<DefaultResponse<ReviewResponse>> review(RequestBody requestBody) {

        return networkApiService.review(requestBody).compose(configureApiCallObserver());
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
