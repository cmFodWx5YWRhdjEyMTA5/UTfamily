package com.ufotech.ufo.utfamily.service.message;

/**
 * -----------------------------------------------------------------
 * @author ：UFO（陳俊融）Github：https://github.com/jyunrong
 * @date ：2018/5/15 上午 11:58
 * @description ：接收從推播過來的訊息，並且把它呈現在頁面上
 * -----------------------------------------------------------------
 */
public class MyNotice {

    private static final MyNotice ourInstance = new MyNotice();

    public static MyNotice getInstance() {
        return ourInstance;
    }

    private MyNotice() {
    }

    private OnMessageReceivedListener mOnMessageReceivedListener;
    public interface OnMessageReceivedListener{
        void onMessageReceived(String s);
    }
    public void setOnMessageReceivedListener(OnMessageReceivedListener listener){
        mOnMessageReceivedListener = listener;
    }
    public void notifyOnMessageReceived(String s){
        if(mOnMessageReceivedListener != null){
            mOnMessageReceivedListener.onMessageReceived(s);
        }
    }
}