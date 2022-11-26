package cn.magicalsheep;

import java.util.Map;

/**
 * For UI
 */
public interface CallBackInterface {

    void success(Map<String, Object> params);

    void failed(Map<String, Object> params);

}
