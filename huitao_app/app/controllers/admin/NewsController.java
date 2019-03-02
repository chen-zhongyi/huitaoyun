package controllers.admin;

import annotations.ActionMethod;
import models.news.News;
import vos.NewsVO;
import vos.PageData;
import vos.Result;

import java.util.List;
import java.util.stream.Collectors;

public class NewsController extends ApiController {

    @ActionMethod(name = "目录列表", param = "page,size,-name", clazz = {PageData.class, NewsVO.class})
    public static void list(NewsVO vo) {
        int total = News.count(vo);
        List<News> categories = News.fetch(vo);
        List<NewsVO> vos = categories.stream().map(News -> new NewsVO(News)).collect(Collectors.toList());
        renderJSON(Result.succeed(new PageData(vo.page, vo.size, total, vos)));
    }

    @ActionMethod(name = "目录详情", param = "newsId", clazz = NewsVO.class)
    public static void info(NewsVO vo) {
        News news = News.findByID(vo.newsId);
        renderJSON(Result.succeed(new NewsVO(news)));
    }

    @ActionMethod(name = "目录添加", param = "title,subTitle,images,content,source", clazz = {NewsVO.class})
    public static void add(NewsVO vo) {
        News news = News.add(vo);
        renderJSON(Result.succeed(new NewsVO(news)));
    }

    @ActionMethod(name = "目录编辑", param = "newsId,title,subTitle,images,content,source")
    public static void edit(NewsVO vo) {
        News news = News.findByID(vo.newsId);
        news.edit(vo);
        renderJSON(Result.succeed());
    }

    @ActionMethod(name = "目录删除", param = "NewsId")
    public static void delete(NewsVO vo) {
        News news = News.findByID(vo.newsId);
        news.del();
        renderJSON(Result.succeed());
    }
}
