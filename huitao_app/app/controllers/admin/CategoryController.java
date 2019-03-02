package controllers.admin;

import annotations.ActionMethod;
import models.news.Category;
import vos.CategoryVO;
import vos.PageData;
import vos.Result;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryController extends ApiController {

    @ActionMethod(name = "目录列表", param = "page,size,-name", clazz = {PageData.class, CategoryVO.class})
    public static void list(CategoryVO vo) {
        int total = Category.count(vo);
        List<Category> categories = Category.fetch(vo);
        List<CategoryVO> vos = categories.stream().map(category -> new CategoryVO(category)).collect(Collectors.toList());
        renderJSON(Result.succeed(new PageData(vo.page, vo.size, total, vos)));
    }

    @ActionMethod(name = "目录详情", param = "categoryId", clazz = CategoryVO.class)
    public static void info(CategoryVO vo) {
        Category category = Category.findByID(vo.categoryId);
        renderJSON(Result.succeed(new CategoryVO(category)));
    }

    @ActionMethod(name = "目录添加", param = "name,rank", clazz = {CategoryVO.class})
    public static void add(CategoryVO vo) {
        Category category = Category.add(vo);
        renderJSON(Result.succeed(new CategoryVO(category)));
    }

    @ActionMethod(name = "目录编辑", param = "categoryId,name,rank")
    public static void edit(CategoryVO vo) {
        Category category = Category.findByID(vo.categoryId);
        category.edit(vo);
        renderJSON(Result.succeed());
    }

    @ActionMethod(name = "目录删除", param = "categoryId")
    public static void delete(CategoryVO vo) {
        Category category = Category.findByID(vo.categoryId);
        category.del();
        renderJSON(Result.succeed());
    }
}
