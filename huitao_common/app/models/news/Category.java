package models.news;

import models.BaseModel;
import org.apache.commons.lang.StringUtils;
import utils.BaseUtils;
import vos.CategoryVO;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Category extends BaseModel {

    @Column(columnDefinition = STRING + "'名称'")
    public String name;
    @Column(columnDefinition = STRING + "'排序'")
    public Integer rank;

    public static Category add(CategoryVO vo) {
        if (StringUtils.isBlank(vo.name))   return null;
        Category category = Category.findByName(vo.name);
        if (category != null)   return category;
        category = new Category();
        return category.edit(vo);
    }

    public Category edit(CategoryVO vo) {
        this.name = vo.name != null ? vo.name : name;
        Category category = Category.findByName(this.name);
        if (category != null)   return null;
        this.rank = vo.rank != null ? vo.rank : rank;
        return this.save();
    }

    public void del() {
        this.logicDelete();
    }

    public static Category findByID(Long id) {
        return Category.find(defaultSql("id = ?"), id).first();
    }

    public static Category findByName(String name) {
        return Category.find(defaultSql("name = ?"), name).first();
    }

    public static List<Category> fetchByIds(List<Long> ids) {
        if (BaseUtils.collectionEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }
        return Category.find(defaultSql("id in(:ids)")).bind("ids", ids.toArray()).fetch();
    }

    public static List<Category> fetchAll() {
        return Category.find(defaultSql("")).fetch();
    }

    public static List<Category> fetch(CategoryVO vo) {
        Object[] data = data(vo);
        List<String> hsql = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return Category.find(defaultSql(StringUtils.join(hsql, " and ")) + vo.condition, params.toArray()).fetch();
    }

    public static int count(CategoryVO vo) {
        Object[] data = data(vo);
        List<String> hsql = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return (int) Category.count(defaultSql(StringUtils.join(hsql, " and ")), params.toArray());
    }

    public static Object[] data(CategoryVO vo) {
        List<String> hsql = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(vo.name)) {
            hsql.add("name like ?");
            params.add("%" + vo.name + "%");
        }
        return new Object[] {
                hsql, params
        };
    }
}
