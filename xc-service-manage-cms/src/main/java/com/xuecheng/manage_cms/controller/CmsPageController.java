package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 25131 on 2019/3/6.
 */
@RestController
@RequestMapping("cms/page")
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    PageService pageService;
    @Override
    @GetMapping("/list/{page}/{size}")
    //http://localhost:31001/cms/page/list/1/10
    public QueryResponseResult findList(@PathVariable("page") int page,@PathVariable("size")  int size, QueryPageRequest queryPageRequest) {
        //暂时采用测试数据，测试接口是否可以正常运行
//        QueryResult queryResult = new QueryResult();
//        queryResult.setTotal(2);
////静态数据列表
//        List list = new ArrayList();
//        CmsPage cmsPage = new CmsPage();
//        cmsPage.setPageName("测试页面");
//        list.add(cmsPage);
//        queryResult.setList(list);
//        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
//        return queryResponseResult;
        return pageService.findList(page,size,queryPageRequest);
    }

    //添加页面
    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return pageService.add(cmsPage);
    }
}
