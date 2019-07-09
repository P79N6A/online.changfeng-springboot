package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;

    /**
     * 页面列表分页查询
     *
     * @param page             当前页码
     * @param size             页面显示个数
     * @param queryPageRequest 查询条件
     * @return 页面列表
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }

        CmsPage cmsPage = new CmsPage();
        //站点
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //模板id
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //别名
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        ExampleMatcher matching = ExampleMatcher.matching()
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains())
                ;
        Example<CmsPage> example = Example.of(cmsPage, matching);

        if (page <= 0) {
            page = 1;
        }
        page = page-1; //为了适应mongodb的接口将页码减1
        if (size <= 0) {
            size = 20;
        }
//分页对象
        Pageable pageable = PageRequest.of(page,size);
//        Pageable pageable = new PageRequest(page, size);
//分页查询
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
        QueryResult<CmsPage> cmsPageQueryResult = new QueryResult<CmsPage>();
        cmsPageQueryResult.setList(all.getContent());
        cmsPageQueryResult.setTotal(all.getTotalElements());
//返回结果
        return new QueryResponseResult(CommonCode.SUCCESS, cmsPageQueryResult);
    }

    public CmsPageResult add(CmsPage cmsPage) {
        CmsPage existCmsPage = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        CmsPage save=null;
        if (existCmsPage==null){
            cmsPage.setPageId(null);//添加页面主键由spring data 自动生成
            cmsPageRepository.save(cmsPage);
            //返回结果
            CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS,cmsPage);
            return cmsPageResult;
        }
        return  new CmsPageResult(CommonCode.FAIL,null);
    }

}