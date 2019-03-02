package controllers.user;

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
}
