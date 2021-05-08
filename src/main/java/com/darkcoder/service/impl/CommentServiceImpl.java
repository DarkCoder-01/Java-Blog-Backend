package com.darkcoder.service.impl;

import com.darkcoder.entity.Comment;
import com.darkcoder.mapper.CommentMapper;
import com.darkcoder.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Darkcoder
 * @since 2021-05-03
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
