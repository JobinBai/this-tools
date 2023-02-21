package com.baiyanbing;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author baiyanbing
 * @Date 2023/2/21 17:17
 */
public class AliDriveApi {

    private static final String REFRESH_TOKEN_URL = "https://auth.aliyundrive.com/v2/account/token";

    private static final String SIGN_URL = "https://member.aliyundrive.com/v1/activity/sign_in_list";

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * 获取AccessToken
     *
     * @param refreshToken
     * @return
     */
    public String getAccessToken(String refreshToken) {
        JSONObject body = new JSONObject();
        body.put("grant_type", "refresh_token");
        body.put("refresh_token", refreshToken);

        Request request = new Request.Builder()
                .url(REFRESH_TOKEN_URL)
                .post(RequestBody.create(body.toJSONString(), MediaType.parse("application/json")))
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            String ret = response.body().string();
            System.out.println("-->获取令牌：" + ret);
            return JSON.parseObject(ret).getString("access_token");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean sign(String refreshToken, String accessToken) {
        JSONObject body = new JSONObject();
        body.put("grant_type", "refresh_token");
        body.put("refresh_token", refreshToken);

        RequestBody requestBody = RequestBody.create(body.toJSONString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(SIGN_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("content-type", "application/json")
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            String ret = response.body().string();
            System.out.println("-->签到：" + ret);
            JSONObject retJSON = JSON.parseObject(ret);
            boolean signRet = retJSON.getBooleanValue("success", false);
            if (!signRet) {
                throw new RuntimeException(retJSON.getString("message"));
            }
            return signRet;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * bark通知
     */
    public void noticeBark() {

    }

}
