package com.darkcoder.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.darkcoder.common.lang.Result;
import com.darkcoder.entity.Comment;
import com.darkcoder.service.CommentService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Darkcoder
 * @since 2021-05-03
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @GetMapping("/list")
    //http://localhost:8081/comment/list?blogId=51
    public Result list(@RequestParam Integer blogId) {
        List<Comment> comments = commentService.list(new QueryWrapper<Comment>().eq("blog_id", blogId).orderByDesc("created"));

        return Result.succ(comments);
    }

    @PostMapping("/publish")
    public Result publish(@Validated @RequestBody Comment comment) {
        Comment temp = new Comment();
        temp.setCreated(LocalDateTime.now());
        temp.setStatus(1);
        BeanUtil.copyProperties(comment, temp, "id", "created", "status");

        commentService.saveOrUpdate(temp);

        return Result.succ(null);
    }

    @RequiresAuthentication
    @GetMapping("/delete")
    //http://localhost:8081/comment/delete?commentId=1
    public Result delete(@RequestParam Integer commentId) {
        commentService.removeById(commentId);

        return Result.succ(null);
    }
}
