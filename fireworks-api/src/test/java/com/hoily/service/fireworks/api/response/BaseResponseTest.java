package com.hoily.service.fireworks.api.response;

import org.junit.Assert;
import org.junit.Test;

/**
 * description is here
 *
 * @author vyckey
 */
public class BaseResponseTest {
    @Test
    public void buildTest() {
        BaseResponse<?> response1 = BaseResponse.successResponse();
        Assert.assertTrue(response1.isSuccess());

        BaseResponse<?> response2 = BaseResponse.failResponse(404, "page not found");
        Assert.assertFalse(response2.isSuccess());
        Assert.assertEquals("page not found", response2.getMessage());

        BaseResponse<?> response3 = BaseResponse.fail().code(404).message("page not found").build();
        Assert.assertFalse(response3.isSuccess());
        Assert.assertEquals(response2.getCode(), response3.getCode());
        Assert.assertEquals(response2.getMessage(), response3.getMessage());

        BaseResponse<String> response4 = BaseResponse.successResponse("hello");
        Assert.assertTrue(response4.isSuccess());
        Assert.assertEquals("hello", response4.getResult());

        BaseResponse<?> response5 = response3.toBuilder().message("page 404").build();
        Assert.assertEquals(response3.getCode(), response5.getCode());
        Assert.assertEquals("page 404", response5.getMessage());
    }
}