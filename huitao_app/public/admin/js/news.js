var news_tab = function () {
    element.tabDelete('tab', nav_name);
    var admin_tab_html = $('#news_tab').html();
    element.tabAdd('tab', {title: nav_name, content: admin_tab_html, id: nav_name});
    element.tabChange('tab', nav_name);
    news_table();
}

var news_table = function (param) {
    $.post('/admin/news/list', param, function (result, status) {
        table.render(table_param('news', result.data.array));
    })
}

form.on('submit(news_search)', function (data) {
    news_table(data.field);
});

form.on('submit(news_form)', function () {
    var d = {newsId: '', title: '', subTitle: '', content: '', images: '', source: '', code: ''};
    var admin_form_html = laytpl($('#news_form').html()).render(d);
    layer_index = layer.open({
        type: 1,
        area: area_7_6,
        content: admin_form_html
    });
});
form.on('submit(news_add)', function (data) {
    var param = data.field;
    $.post('/admin/news/add', param, function (result, status) {
        news_table();
    });
});

table.on('tool(news_table)', function (obj) {
    var e = obj.event, d = obj.data;
    if (e === 'edit') {
        var admin_form_html = laytpl($('#news_form').html()).render(d);
        layer_index = layer.open({
            type: 1,
            area: area_8_6,
            content: admin_form_html
        });
        form.on('submit(news_edit)', function (data) {
            var param = data.field;
            $.post('/admin/news/edit', param, function (result, status) {
                obj.update(param);
            });
        });
    } else if (e === 'del') {
        layer.confirm('确定删除目录', function (index) {
            $.post('/admin/news/delete', d, function (result, status) {
                obj.del();
            });
        });
    }
});