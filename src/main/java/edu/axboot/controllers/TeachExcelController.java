package edu.axboot.controllers;

import com.chequer.axboot.core.api.response.ApiResponse;
import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.code.ApiStatus;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.chequer.axboot.core.utils.DateUtils;
import com.chequer.axboot.core.utils.ExcelUtils;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.axboot.domain.education.EducationTeach;
import edu.axboot.domain.education.EducationTeachService;
import edu.axboot.utils.MiscUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/education/teachexcel")
public class TeachExcelController extends BaseController {

    @Inject
    private EducationTeachService educationTeachService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.PageResponse list(RequestParams<EducationTeach> requestParams) {
        List<EducationTeach> list = educationTeachService.getListUsingQueryDsl(requestParams);
        Page<EducationTeach> page = MiscUtils.toPage(list, requestParams.getPageable());
        return Responses.PageResponse.of(page);
    }

    @ApiOperation(value = "엑셀다운로드", notes = "/resources/excel/education_teach.xlsx")
    @RequestMapping(value = "/exceldown", method = {RequestMethod.POST}, produces = APPLICATION_JSON)
    public void excelDown(RequestParams<EducationTeach> requestParams, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<EducationTeach> list = educationTeachService.getListUsingQueryDsl(requestParams);
        ExcelUtils.renderExcel("/excel/education_teach.xlsx", list, "Education_" + DateUtils.getYyyyMMddHHmmssWithDate(), request, response);
    }

    @RequestMapping(value = "/exceldown2", method = {RequestMethod.POST}, produces = APPLICATION_JSON)
    public ResponseEntity<byte[]> excelDown2(RequestParams<EducationTeach> requestParams, HttpServletRequest request, HttpServletResponse response) throws IOException {
/*
        List<EducationExcelResponseDto> list = educationTeachService.getListExcel(requestParams);
        String excelFile = excelService.exportExcel(list, UUIDUtils.shortUUID());
        ResponseEntity<byte[]> res = filedownloadService.getExcel(excelFile, "Education_" + DateUtils.getYyyyMMddHHmmssWithDate() + ".xlsx");
        return res;
*/
        return null;
    }

    /**
     * 엑셀업로드
     * @param : fileId
     * @return : ApiStatus.SUCCESS
     */
    @ApiOperation(value = "엑셀업로드", notes = "/resources/excel/education_upload.xml")
    @RequestMapping(value = "/excelupload", method = {RequestMethod.POST}, produces = APPLICATION_JSON)
    public ApiResponse excel(@RequestParam(value = "formFile") MultipartFile multipartFile) throws Exception{

        String resultMsg = educationTeachService.uploadFileByExcel(multipartFile);

        if (!StringUtils.isEmpty(resultMsg)) {
            return ApiResponse.error(ApiStatus.SYSTEM_ERROR, resultMsg);
        }

        return ok();
    }

}