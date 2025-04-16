package com.hoily.service.fireworks.acl.wechat;

import com.hoily.service.fireworks.acl.wechat.base.WechatResponse;
import com.hoily.service.fireworks.acl.wechat.customer.message.MessageTypingRequest;
import com.hoily.service.fireworks.acl.wechat.security.AccessTokenDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Refer to <a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html">微信开发文档 - 接入指南</a>
 *
 * @author vyckey
 */
public interface WechatApi {
    @GET("cgi-bin/token")
    Call<WechatResponse<AccessTokenDTO>> requestToken(@Query("grant_type") String grantType,
                                                      @Query("appid") String appId,
                                                      @Query("secret") String secret);

    @POST("cgi-bin/message/custom/send")
    Call<WechatResponse<Void>> sendMessage(@Query("access_token") String accessToken,
                                           @Body Object customerMessage);

    @POST("cgi-bin/message/custom/typing")
    Call<WechatResponse<Void>> sendTyping(@Query("access_token") String accessToken,
                                          @Body MessageTypingRequest customerMessage);
}
