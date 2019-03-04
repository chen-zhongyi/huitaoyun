package models.news;

import models.BaseModel;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import utils.BaseUtils;
import vos.NewsVO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class News extends BaseModel {

    @Column(columnDefinition = STRING + "'标题'")
    public String title;
    @Column(columnDefinition = STRING + "'短标题'")
    public String subTitle;
    @Column(columnDefinition = STRING_TEXT + "'内容'")
    public String content;
    @Column(columnDefinition = STRING + "'配图'")
    public String images;
    @Column(columnDefinition = STRING + "'来源'")
    public String source;
    @Column(columnDefinition = STRING + "'code'")
    public String code;
    @Column(columnDefinition = LONG + "'发布时间'")
    public Long publishTime = System.currentTimeMillis();

    @ManyToOne
    public Category category; // 目录

    public static News add(NewsVO vo) {
        News news = new News();
        news.code = RandomStringUtils.randomAlphanumeric(32);
        return news.edit(vo);
    }

    public News edit(NewsVO vo) {
        this.title = vo.title != null ? vo.title : title;
        this.subTitle = vo.subTitle != null ? vo.subTitle : subTitle;
        this.content = vo.content != null ? vo.content : content;
        this.images = vo.images != null ? StringUtils.join(vo.images, ",") : images;
        this.source = vo.source != null ? vo.source : source;
        this.category = vo.categoryId != null ? Category.findByID(vo.categoryId) : category;
        this.publishTime = vo.publishTime != null ? vo.publishTime : publishTime;
        return this.save();
    }

    public Long publishTime() {
        return this.publishTime == null ? this.createTime : this.publishTime;
    }

    public void del() {
        this.logicDelete();
    }

    public static News findByID(Long id) {
        return News.find(defaultSql("id = ?"), id).first();
    }

    public static News findByCode(String code) {
        return News.find(defaultSql("code = ?"), code).first();
    }

    public static List<News> fetchByIds(List<Long> ids) {
        if (BaseUtils.collectionEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }
        return News.find(defaultSql("id in(:ids)")).bind("ids", ids.toArray()).fetch();
    }

    public static List<News> fetchAll() {
        return News.find(defaultSql("")).fetch();
    }

    public static List<News> fetch(NewsVO vo) {
        Object[] data = data(vo);
        List<String> hsql = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return News.find(defaultSql(StringUtils.join(hsql, " and ")) + vo.condition, params.toArray()).fetch();
    }

    public static int count(NewsVO vo) {
        Object[] data = data(vo);
        List<String> hsql = (List<String>) data[0];
        return (int) News.count(defaultSql(StringUtils.join(hsql, " and ")));
    }

    public static Object[] data(NewsVO vo) {
        List<String> hsql = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(vo.title)) {
            hsql.add("title like ?");
            params.add("%" + vo.title + "%");
        }
        /*if (vo.categoryId != null) {
            hsql.add("category.id = ?");
            params.add(vo.categoryId);
        }*/
        return new Object[] {
                hsql, params
        };
    }
}
