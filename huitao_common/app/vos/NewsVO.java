package vos;

import annotations.DataField;
import models.news.News;
import utils.BaseUtils;

import java.util.List;

public class NewsVO extends OneData {

    @DataField(name = "ID")
    public Long newsId;
    @DataField(name = "标题")
    public String title;
    @DataField(name = "短标题'")
    public String subTitle;
    @DataField(name = "内容")
    public String content;
    @DataField(name = "配图")
    public List<String> images;
    @DataField(name = "来源")
    public String source;
    @DataField(name = "code")
    public String code;
    @DataField(name = "目录ID")
    public Long categoryId;
    @DataField(name = "目录名称")
    public String categoryName;
    @DataField(name = "发布时间")
    public Long publishTime;

    public NewsVO() {
        this.condition("order by id desc");
    }

    public NewsVO(News news) {
        super(news.id);
        this.newsId = news.id;
        this.title = news.title;
        this.subTitle = news.subTitle;
        this.content = news.content;
        this.images = BaseUtils.strToList(news.images);
        this.source = news.source;
        this.code = news.code;
        this.publishTime = news.publishTime();
        if (news.category != null) {
            this.categoryId = news.category.id;
            this.categoryName = news.category.name;
        }
    }
}
