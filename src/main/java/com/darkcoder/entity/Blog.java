package com.darkcoder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>
 * 
 * </p>
 *
 * @author Darkcoder
 * @since 2021-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m_blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 作者id
     */
    private Long userId;

    /**
     * 作者
     */
    private String author;

    /**
     * 文章标题
     */
    @NotBlank(message = "文章标题不能为空")
    private String title;

    /**
     * 封面url
     */
    @NotBlank(message = "文章封面不能为空")
    private String coverUrl;

    /**
     * 文章内容
     */
    @NotBlank(message = "文章内容不能为空")
    private String content;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    /**
     * 0代表原创，1代表转载，2代表翻译
     */
    @NotBlank(message = "文章来源不能为空")
    private String source;

    /**
     * 博客标签
     */
    @NotBlank(message = "文章标签不能为空")
    private String tags;

    /**
     * 点赞量
     */
    private Integer likes;

    /**
     * 浏览量
     */
    private Integer view;

    /**
     * 文章分类
     */
    @NotBlank(message = "文章分类不能为空")
    private String classification;

    @TableField(exist = false)
    private Integer totalCount;

}
