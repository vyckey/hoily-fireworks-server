package com.hoily.service.fireworks.acl.wechat.util;

import com.hoily.service.fireworks.acl.wechat.customer.message.CustomerImageMessageDTO;
import com.hoily.service.fireworks.acl.wechat.customer.message.CustomerMessageBaseDTO;
import com.hoily.service.fireworks.acl.wechat.customer.message.CustomerTextMessageDTO;
import com.hoily.service.fireworks.acl.wechat.message.OfficialImageMessageDTO;
import com.hoily.service.fireworks.acl.wechat.message.OfficialMessageDTO;
import com.hoily.service.fireworks.acl.wechat.message.OfficialTextMessageDTO;

import java.util.Map;

/**
 * description is here
 *
 * @author vyckey
 * 2023/3/7 20:32
 */
public abstract class MessageHelper {

    public static Map<String, Object> toCustomerMessage(OfficialMessageDTO messageDTO) {
        if (messageDTO == null) {
            return null;
        }
        CustomerMessageBaseDTO customerMessage = null;
        switch (messageDTO.getMsgType()) {
            case OfficialTextMessageDTO.MSG_TYPE:
                OfficialTextMessageDTO textMessageDTO = (OfficialTextMessageDTO) messageDTO;
                customerMessage = new CustomerTextMessageDTO(textMessageDTO.getContent());
                break;
            case OfficialImageMessageDTO.MSG_TYPE:
                OfficialImageMessageDTO imageMessageDTO = (OfficialImageMessageDTO) messageDTO;
                customerMessage = new CustomerImageMessageDTO(imageMessageDTO.getMediaId().toString());
                break;
            default:
        }
        return customerMessage != null ? customerMessage.wrapAsMessage(messageDTO.getToUserName()) : null;
    }
}
