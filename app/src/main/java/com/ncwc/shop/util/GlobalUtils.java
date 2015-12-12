package com.ncwc.shop.util;

import android.content.Context;

import com.ncwc.shop.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by admin on 2015/9/3.
 */
public class GlobalUtils {

    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.isEmpty() || str.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 解析文件中的json数据
     *
     * @param context
     * @param raw
     * @return
     * @throws JSONException
     */
    public static JSONArray getJobType(Context context, int raw) throws JSONException {
        String result = readFileFromRaw(context, raw);
        return result == null ? null : new JSONArray(result);
    }

    public static String readFileFromRaw(Context context, int resourceId) {
        if (null == context || resourceId < 0) {
            return null;
        }
        String result = null;
        try {
            InputStream input = context.getResources().openRawResource(resourceId);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
            output.close();
            input.close();
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
