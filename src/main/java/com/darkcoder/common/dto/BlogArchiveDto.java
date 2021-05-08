package com.darkcoder.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 徐泽爽
 * @version 1.0
 * @date 2021/5/3 14:08
 */
@Data
public class BlogArchiveDto implements Serializable {
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
}
