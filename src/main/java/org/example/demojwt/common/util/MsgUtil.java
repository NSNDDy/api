package org.example.demojwt.common.util;

import org.example.demojwt.common.dto.MessageInfoDto;
import org.springframework.stereotype.Component;

@Component
public class MsgUtil {

    public MessageInfoDto getMessage(String messageInfo,
                                     String messageId,
                                     String checkId,
                                     String message){

        MessageInfoDto messageInfoDto = new MessageInfoDto();
        messageInfoDto.setMessageInfo(messageInfo);
        messageInfoDto.setMessageId(messageId);
        messageInfoDto.setCheckId(checkId);
        messageInfoDto.setMessage(message);

        return messageInfoDto;
    }
}
