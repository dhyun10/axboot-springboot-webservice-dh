package edu.axboot.controllers;

import com.chequer.axboot.core.api.response.ApiResponse;
import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import edu.axboot.controllers.dto.EducationListResponseDto;
import edu.axboot.controllers.dto.EducationResponseDto;
import edu.axboot.controllers.dto.EducationUpdateRequestDto;
import edu.axboot.domain.education.book.EducationBookService;
import edu.axboot.controllers.dto.EducationSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DahyunGridBookController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(DahyunGridBookController.class);

    private final EducationBookService educationBookService;

    @PostMapping("/api/v1/education/dhyunGridBook")
    public ApiResponse save(@RequestBody EducationSaveRequestDto requestDto) {
        educationBookService.save(requestDto);
        return ok();
    }
    @PutMapping("/api/v1/education/dhyunGridBook/{id}")
    public ApiResponse update(@PathVariable Long id, @RequestBody EducationUpdateRequestDto requestDto) {
        educationBookService.update(id, requestDto);
        return ok();
    }

    @GetMapping("/api/v1/education/dhyunGridBook/{id}")
    public EducationResponseDto findById(@PathVariable Long id) {
        return educationBookService.findById(id);
    }

    @GetMapping("/api/v1/education/dhyunGridBook")
    public Responses.ListResponse list(
            @RequestParam(value = "companyNm", required = false) String companyNm,
            @RequestParam(value = "ceo", required = false) String ceo,
            @RequestParam(value = "bizno", required = false) String bizno,
            @RequestParam(value = "useYn", required = false) String useYn
    ) {
        List<EducationListResponseDto> list=educationBookService.findBy(companyNm, ceo, bizno, useYn);
        return Responses.ListResponse.of(list);
    }

}
