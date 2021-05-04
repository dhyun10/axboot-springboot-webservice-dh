package edu.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.chequer.axboot.core.utils.DateUtils;
import com.chequer.axboot.core.utils.ExcelUtils;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.axboot.domain.education.EducationDhyun;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import com.chequer.axboot.core.api.response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import edu.axboot.domain.education.EducationDhyunService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/v1/dhyunGrid")
public class DahyunGridController extends BaseController {

    @Inject
    private EducationDhyunService educationDhyunService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyNm", value = "회사명", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ceo", value = "대표자", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bizno", value = "사업자번호", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "useYn", value = "사용유무", dataType = "String", paramType = "query")
    })
    public Responses.ListResponse list(RequestParams<EducationDhyun> requestParams) {
        List<EducationDhyun> list = educationDhyunService.gets(requestParams);
        return Responses.ListResponse.of(list);
    }

//    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
//    public Responses.PageResponse page(Pageable pageable, RequestParams<EducationDhyun> requestParams) {
//        Page<EducationDhyun> page=educationDhyunService.findAll(pageable, requestParams);
//        return Responses.PageResponse.of(page);
//    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody List<EducationDhyun> request) {
        educationDhyunService.save(request);
        return ok();
    }

    @RequestMapping(value = "/QueryDsl", method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyNm", value = "회사명", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ceo", value = "대표자", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bizno", value = "사업자번호", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "useYn", value = "사용유무", dataType = "String", paramType = "query")
    })
    public Responses.ListResponse list2(RequestParams<EducationDhyun> requestParams) {
        List<EducationDhyun> list= educationDhyunService.getByQueryDsl(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value = "/QueryDsl/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public EducationDhyun selectOne(@PathVariable Long id) {
        EducationDhyun dhyunGrid= educationDhyunService.getByQueryDslOne(id);
        return dhyunGrid;
    }

    @RequestMapping(value = "/QueryDsl", method = RequestMethod.PUT, produces = APPLICATION_JSON)
    public ApiResponse save2(@RequestBody List<EducationDhyun> request) {
        educationDhyunService.saveByQueryDsl(request);
        return ok();
    }

    @RequestMapping(value = "/QueryDsl/one", method = RequestMethod.PUT, produces = APPLICATION_JSON)
    public ApiResponse saveOne(@RequestBody EducationDhyun request) {
        educationDhyunService.saveByQueryDsl(request);
        return ok();
    }

    @RequestMapping(value = "/myBatis", method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyNm", value = "회사명", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ceo", value = "대표자", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bizno", value = "사업자번호", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "useYn", value = "사용유무", dataType = "String", paramType = "query")
    })
    public Responses.ListResponse list3(RequestParams<EducationDhyun> requestParams) {
        List<EducationDhyun> list= educationDhyunService.selectList(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value = "/listPage", method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyNm", value = "회사명", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ceo", value = "대표자", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bizno", value = "사업자번호", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "useYn", value = "사용유무", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageNumber", value = "페이지", dataType = "int", paramType = "query")
    })
    public Responses.PageResponse listPage(
            RequestParams<EducationDhyun> requestParams) { ;
        Page<EducationDhyun> page=educationDhyunService.selectListPage(requestParams);

        return Responses.PageResponse.of(page);
    }

    @ApiOperation(value = "엑셀다운로드", notes = "/resource/excel/education_dhyun.xlsx")
    @RequestMapping(value = "/excelDown", method = {RequestMethod.POST}, produces = APPLICATION_JSON)
    public void excelDown(
            RequestParams<EducationDhyun> requestParams,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        List<EducationDhyun> list=educationDhyunService.getByQueryDsl(requestParams);
        ExcelUtils.renderExcel("/excel/education_dhyun.xlsx", list, "Education_"+
                DateUtils.getYyyyMMddHHmmssWithDate(), request, response);
    }
}