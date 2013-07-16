package com.mensa.net;

public interface OnRequestListener {
    public void onError(String msg);

    public void onComplete(Object object);

}
