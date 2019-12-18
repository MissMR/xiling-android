package com.xiling.ddmall.shared.contracts;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.contracts
 * @since 2017-03-10
 */
public abstract class RequestListener<E> {
   public abstract void onStart();

    public  void onSuccess(E result){};

    public void onSuccess(E result, String msg) {

    }

    public abstract void onError(Throwable e);

    public abstract void onComplete();
}
