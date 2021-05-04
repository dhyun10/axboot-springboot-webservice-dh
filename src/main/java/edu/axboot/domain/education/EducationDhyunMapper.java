package edu.axboot.domain.education;

import com.chequer.axboot.core.mybatis.MyBatisMapper;

import java.util.List;
import java.util.Map;

public interface EducationDhyunMapper extends MyBatisMapper {
    List<EducationDhyun> selectList(EducationDhyun educationDhyun);
    List<EducationDhyun> selectListPage(Map<String, Object> map);
    int dataCount(Map<String, Object> map);

    List<EducationDhyun> select(Map<String, Object> map);
    EducationDhyun selectOne(Long id);
    void insert(EducationDhyun educationDhyun);
    void update(EducationDhyun educationDhyun);
    void delete(Long id);
}
