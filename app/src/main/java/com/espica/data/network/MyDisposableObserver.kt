package com.espica.data.network

import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.observers.DisposableObserver

/**
 * Created by IT-10 on 1/7/2018.
 */

abstract class MyDisposableObserver<T> : DisposableObserver<T>() {

    protected abstract fun onSuccess(response: T)

    override fun onNext(response: T) {
        onSuccess(response)
    }

    override fun onError(throwable: Throwable) {
        Log.d(TAG, "onError() called with: throwable = [$throwable]")
        val throwableClass = throwable.javaClass

        if (HttpException::class.java.isAssignableFrom(throwableClass)) {
            //            HttpException exception = (HttpException) throwable;
            //            String error = null;
            //            try {
            //                error = exception.response().errorBody().string();
            //                Gson gson = new Gson();
            //                ErrorResponse errorResponse = gson.fromJson(error, ErrorResponse.class);
            //              if(!errorResponse.getError().getMessage().isEmpty() && errorResponse.getError().getMessage().contains("خطا"))
            //              {
            //                  showAPIProblemMessageDialog(errorResponse.getError().getMessage());
            //              }
            //              else
            //                  showAPIProblemMessageDialog("عملیات با شکست رو به رو شد لطفا مجددا تلاش کنید.");
            //
            //            } catch (Exception e) {
            //                e.printStackTrace();
            //            }
        }
    }

    override fun onComplete() {

    }

    companion object {

        private val TAG = "MyDisposableObserver"
    }

    //    private void showAPIProblemMessageDialog(String message) {
    //        ShowAPIProblemMessageEvent event = new ShowAPIProblemMessageEvent(message);
    //        EventBus.getDefault().post(event);
    //    }

}
