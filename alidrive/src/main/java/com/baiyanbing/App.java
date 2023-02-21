package com.baiyanbing;


public class App 
{
    public static void main( String[] args) {
        String refreshToken = args[0];
        AliDriveApi api = new AliDriveApi();
        String accessToken = api.getAccessToken(refreshToken);
        boolean signRet = api.sign(refreshToken, accessToken);
        System.out.println(signRet? "签到成功" : "签到失败");
    }
}
