package vos;

import annotations.DataField;
import models.news.Category;

public class CategoryVO extends OneData {

    @DataField(name = "ID")
    public Long categoryId;
    @DataField(name = "名称")
    public String name;
    @DataField(name = "排序")
    public Integer rank;

    public CategoryVO () { condition("order by rank, id desc "); }

    public CategoryVO(Category category) {
        super(category.id);
        this.categoryId = category.id;
        this.name = category.name;
        this.rank = category.rank;
    }
}
