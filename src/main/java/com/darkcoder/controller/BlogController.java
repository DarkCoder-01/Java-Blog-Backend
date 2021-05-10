package com.darkcoder.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.darkcoder.common.lang.Result;
import com.darkcoder.entity.Blog;
import com.darkcoder.entity.User;
import com.darkcoder.service.BlogService;
import com.darkcoder.service.UserService;
import com.darkcoder.util.ShiroUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class BlogController {

    @Autowired
    BlogService blogService;

    @Autowired
    UserService userService;

    @GetMapping("/blog/search")
    public Result list(@RequestParam String keyWord, @RequestParam(defaultValue = "1") Integer currentPage) {
        Page page = new Page(currentPage, 5);
        IPage iPage;
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        if(keyWord != null) {
            //使用HanLP标准分词器对关键字进行分词，不需要词性
            HanLP.Config.ShowTermNature = false;
            List<Term> termList = StandardTokenizer.segment(keyWord);

            //拼接模糊查询sql
            for(Term word: termList) {
                queryWrapper.like("title", word.toString().trim()).or().like("content", word.toString().trim());
                if(word != termList.get(termList.size()-1)) {
                    queryWrapper.or();
                }
            }
        }
        iPage = blogService.page(page, queryWrapper);
        queryWrapper.orderByDesc("created");

        return Result.succ(iPage);
    }

    @GetMapping("/blog/archive")
    public Result archive() {
        List<Blog> blogs = blogService.list(new QueryWrapper<Blog>().orderByDesc("created"));
        ArrayList<Object> archives = new ArrayList<>();
        for (Blog blog : blogs) {
            Map<Object, Object> map = MapUtil.builder()
                    .put("id", blog.getId())
                    .put("title", blog.getTitle())
                    .put("created", DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(blog.getCreated()))
                    .map();
            archives.add(map);
        }
        return Result.succ(archives);
    }

    @GetMapping("/blog/tags/{tag}")
    public Result tag(@PathVariable(name = "tag") String tag, @RequestParam(defaultValue = "1") Integer currentPage) {
        Page page = new Page(currentPage, 5);
        IPage iPage = blogService.page(page, new QueryWrapper<Blog>().like("tags", tag));
        return Result.succ(iPage);
    }

    @GetMapping("/blog/classify/{classification}")
    public Result classify(@PathVariable(name = "classification") String classification, @RequestParam(defaultValue = "1") Integer currentPage) {
        Page page = new Page(currentPage, 5);
        IPage iPage = blogService.page(page, new QueryWrapper<Blog>().eq("classification", classification));

        return Result.succ(iPage);
    }

    @GetMapping("/blog/tags")
    public Result tags() {
        List<Blog> blogs = blogService.list();

        StringBuilder tags = new StringBuilder();

        for (Blog b : blogs) {
            tags.append(b.getTags());
            if (b != blogs.get(blogs.size() - 1)) {
                tags.append(",");
            }
        }
        Object[] array = Arrays.stream(tags.toString().split(",")).toArray();
        HashSet set = new HashSet();
        set.addAll(Arrays.asList(array));
        return Result.succ(MapUtil.builder()
                .put("allTags", set)
                .map());
    }

    @GetMapping("/blog/classifications")
    public Result classifications() {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.groupBy("classification");
        wrapper.select("classification, count(*) as total_count");
        List<Blog> blogs = blogService.list(wrapper);
        ArrayList<Object> classifications = new ArrayList<>();
        for(Blog b: blogs) {
            classifications.add(
                    MapUtil.builder()
                            .put("count", b.getTotalCount())
                            .put("classification", b.getClassification())
                            .map()
            );
        }
        return Result.succ(classifications);
    }

    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已被删除");

        //增加浏览量
        Blog temp = blog.setView(blog.getView() + 1);
        blogService.saveOrUpdate(temp);

        return Result.succ(blog);
    }

    @GetMapping("/blog/like")
    public Result like(@RequestParam(name = "blogId") Long id) {
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已被删除");

        //增加点赞量
        Blog temp = blog.setLikes(blog.getLikes() + 1);
        blogService.saveOrUpdate(temp);

        return Result.succ(temp);
    }

    @GetMapping("/about")
    public Result about() {
        Blog blog = blogService.getById(1L);
        Assert.notNull(blog, "关于页的id必须为1");

        return Result.succ(blog);
    }

    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {
        Blog temp;
        if(blog.getId() != null) {
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            System.out.println(ShiroUtil.getProfile().getId());
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");
            //更新时间
            temp.setCreated(LocalDateTime.now());
        } else {
            //添加博客
            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            User user = userService.getOne(new QueryWrapper<User>().eq("id", ShiroUtil.getProfile().getId()));
            temp.setAuthor(user.getUsername());
            temp.setCreated(LocalDateTime.now());
            temp.setLikes(0);
            temp.setView(0);
        }

        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "author", "likes", "view");
        blogService.saveOrUpdate(temp);

        return Result.succ(null);
    }

    @RequiresAuthentication
    @GetMapping("/blog/delete")
    public Result edit(@RequestParam Integer blogId) {

        Blog blog = blogService.getById(blogId);
        Assert.notNull(blog, "该博客已被删除");

        blogService.removeById(blogId);

        return Result.succ(null);
    }
}
