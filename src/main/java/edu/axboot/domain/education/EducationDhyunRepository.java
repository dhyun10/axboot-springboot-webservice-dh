package edu.axboot.domain.education;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationDhyunRepository extends AXBootJPAQueryDSLRepository<EducationDhyun, Long>, PagingAndSortingRepository<EducationDhyun, Long> {

/*
    List<EducationDhyun> findByCompanyNmContains(String companyNm);
    List<EducationDhyun> findByCeoContains(String ceo);
    List<EducationDhyun> findByBiznoContains(String bizno);
    List<EducationDhyun> findByUseYnContains(String useYn);
    EducationDhyun findByIdLike(Long id);
*/

//    List<DhyunGrid> findAll(Specification<DhyunGrid> spec);
    List<EducationDhyun> findByCompanyNmLikeAndCeoLikeAndBiznoLikeAndUseYnLike(String companyNm, String ceo, String bizno, String useYn);

//    @ApiImplicitParam(name = "companyNm", value = "회사명", dataType = "String", paramType = "query"),
//    @ApiImplicitParam(name = "ceo", value = "대표자", dataType = "String", paramType = "query"),
//    @ApiImplicitParam(name = "bizno", value = "사업자번호", dataType = "String", paramType = "query"),
//    @ApiImplicitParam(name = "useYn", value = "사용유무", dataType = "String", paramType = "query")
}
