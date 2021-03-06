package edu.axboot.controllers;

import com.chequer.axboot.core.api.response.ApiResponse;
import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import edu.axboot.domain.education.EducationDhyun;
import edu.axboot.domain.education.EducationDhyunService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/api/v1/dhyunGridForm/myBatis")
public class DahyunGridFormMybatisController extends BaseController {


    @Inject
    private EducationDhyunService educationDhyunService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.PageResponse list(
            RequestParams requestParams
    ) {
        try {
            Page<EducationDhyun> page=educationDhyunService.selectListPage(requestParams);
            return Responses.PageResponse.of(page);
        } catch (BadSqlGrammarException e) {
            log.error("MyBatis 조회 오류. 쿼리 확인 !!!!");
            return Responses.PageResponse.of(null);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return Responses.PageResponse.of(null);
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public EducationDhyun view(
            @PathVariable Long id
    ) {
        EducationDhyun educationDhyun=educationDhyunService.getByOne(id);

        return educationDhyun;
    }

    @RequestMapping(method = RequestMethod.POST, produces = APPLICATION_JSON)
    public ApiResponse save(
            @RequestBody EducationDhyun educationDhyun
    ) {
        educationDhyunService.enroll(educationDhyun);
        return ok();
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    public ApiResponse delete(
            @RequestParam List<Long> ids
    ) {
        educationDhyunService.del(ids);
        return ok();
    }
}