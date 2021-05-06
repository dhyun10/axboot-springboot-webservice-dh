package edu.axboot.controllers;

import com.chequer.axboot.core.api.response.ApiResponse;
import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import edu.axboot.domain.education.EducationDhyun;
import edu.axboot.domain.education.EducationDhyunService;
import org.simpleframework.xml.Path;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/v1/dhyunGridForm")
public class DahyunGridFormController extends BaseController {

    @Inject
    private EducationDhyunService educationDhyunService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.PageResponse list(
            RequestParams<EducationDhyun> requestParams
    ) {
        List<EducationDhyun> list=educationDhyunService.getByQueryDsl(requestParams);

        Pageable pageable=requestParams.getPageable();
        int start=(int)pageable.getOffset();
        int end=(start+pageable.getPageSize())>list.size() ? list.size() : (start+ pageable.getPageSize());

        Page<EducationDhyun> page=new PageImpl<>(list.subList(start,end), pageable, list.size());

        return Responses.PageResponse.of(page);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public EducationDhyun view(
            @PathVariable Long id
    ) {
        EducationDhyun educationDhyun=educationDhyunService.getOne(id);

        return educationDhyun;
    }

    @RequestMapping(method = RequestMethod.POST, produces = APPLICATION_JSON)
    public ApiResponse save(
            @RequestBody EducationDhyun educationDhyun
    ) {
        educationDhyunService.persist(educationDhyun);
        return ok();
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    public ApiResponse delete(
            @RequestParam List<Long> ids
    ) {
        educationDhyunService.remove(ids);
        return ok();
    }
}