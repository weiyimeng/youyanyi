package app.update.define;

/**
 * Created by banxinyu on 2016/10/14.
 */
public interface AnalyzeCallBack <T> {

    /**
     * 返回结果
     * @param t
     */
    void onAnalyzeCallBack(T t);

    /**
     * 发生异常
     * @param e
     */
    void onAnalyzeErrorCallBack(Exception e);

}
