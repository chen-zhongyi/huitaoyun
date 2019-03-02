package controllers.user;

import annotations.ActionMethod;
import models.news.News;
import vos.NewsVO;
import vos.PageData;
import vos.Result;

import java.util.List;
import java.util.stream.Collectors;

public class NewsController extends ApiController {

    @ActionMethod(name = "新闻列表", param = "page,size,-title,-categoryId", clazz = {PageData.class, NewsVO.class})
    public static void list(NewsVO vo) {
        int total = News.count(vo);
        List<News> categories = News.fetch(vo);
        List<NewsVO> vos = categories.stream().map(News -> new NewsVO(News)).collect(Collectors.toList());
        renderJSON(Result.succeed(new PageData(vo.page, vo.size, total, vos)));
    }

    @ActionMethod(name = "新闻详情", param = "newsId", clazz = NewsVO.class)
    public static void info(NewsVO vo) {
        News news = News.findByID(vo.newsId);
        renderJSON(Result.succeed(new NewsVO(news)));
    }
}
