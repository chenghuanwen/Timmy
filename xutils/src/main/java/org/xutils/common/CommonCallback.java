package org.xutils.common;

/**
 * Created by Android004 on 2017/3/21.
 */

public interface CommonCallback<ResultType> extends Callback {
    void onSuccess(ResultType result);

    void onError(Throwable ex, boolean isOnCallback);

    void onCancelled(CancelledException cex);

    void onFinished();
}
