package org.example.demojwt.common.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Data
public class ResponseInfoDto {

    private static final int SUCCESS_RESULT = 0;
    private static final int ERROR_RESULT = 1;
    private static final int NOT_FOUND_RESULT = 2;

    int result;

    Object returnObject;

    public void setReturnObject(Object returnObject) {
        // nếu object là null
        if (Objects.isNull(returnObject)){
            return;
        }
        this.returnObject = returnObject;
        String phase = "■■■■■ Phản hồi ResponseDto :";
        try {
            // lưu trữ
            if (returnObject instanceof ArrayList<?>){
                // returnObject
                ArrayList<?> list = (ArrayList<?>) returnObject;
                // Nếu giá trị danh sách tồn tại
                if (!list.isEmpty()){
                    //
                }
            } else if (returnObject instanceof List<?>) {
                //
                List<?> list = (List<?>) returnObject;
                //
                if (!list.isEmpty()){
                    //

                }

            } else if (returnObject instanceof String) {

            }

        }catch (Exception e){
            log.error(e.toString());
        }
    }



}
