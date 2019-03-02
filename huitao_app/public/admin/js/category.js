var category_tab = function () {
    element.tabDelete('tab', nav_name);
    var admin_tab_html = $('#category_tab').html();
    element.tabAdd('tab', {title: nav_name, content: admin_tab_html, id: nav_name});
    element.tabChange('tab', nav_name);
    category_table();
}

var category_table = function (param) {
    $.post('/admin/category/list', param, function (result, status) {
        table.render(table_param('category', result.data.array));
    })
}

form.on('submit(category_search)', function (data) {
    category_table(data.field);
});

form.on('submit(category_form)', function () {
    var d = {categoryId: '', name: '', rank: ''};
    var admin_form_html = laytpl($('#category_form').html()).render(d);
    layer_index = layer.open({
        type: 1,
        area: area_7_6,
        content: admin_form_html
    });
});
form.on('submit(category_add)', function (data) {
    var param = data.field;
    $.post('/admin/category/add', param, function (result, status) {
        category_table();
    });
});

table.on('tool(category_table)', function (obj) {
    var e = obj.event, d = obj.data;
    if (e === 'edit') {
        var admin_form_html = laytpl($('#category_form').html()).render(d);
        layer_index = layer.open({
            type: 1,
            area: area_8_6,
            content: admin_form_html
        });
        form.on('submit(category_edit)', function (data) {
            var param = data.field;
            $.post('/admin/category/edit', param, function (result, status) {
                obj.update(param);
            });
        });
    } else if (e === 'del') {
        layer.confirm('确定删除目录', function (index) {
            $.post('/admin/category/delete', d, function (result, status) {
                obj.del();
            });
        });
    }
});