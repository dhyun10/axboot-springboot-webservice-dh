package edu.axboot.domain.education;

import com.chequer.axboot.core.api.ApiException;
import com.chequer.axboot.core.parameter.RequestParams;
import com.chequer.axboot.core.utils.CoreUtils;
import com.querydsl.core.BooleanBuilder;
import edu.axboot.domain.BaseService;
import edu.axboot.domain.file.CommonFile;
import edu.axboot.domain.file.CommonFileService;
import edu.axboot.domain.file.UploadParameters;
import edu.axboot.fileupload.FileUploadService;
import edu.axboot.fileupload.UploadFile;
import org.apache.commons.io.FileUtils;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.ReaderConfig;
import org.jxls.reader.XLSReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EducationDhyunService extends BaseService<EducationDhyun, Long> {
    private static final Logger logger = LoggerFactory.getLogger(EducationDhyunService.class);

    private EducationDhyunRepository educationDhyunRepository;

    @Inject
    private EducationDhyunMapper educationDhyunMapper;

    @Autowired
    private CommonFileService commonFileService;

    @Autowired
    private FileUploadService fileUploadService;

    @Inject
    public EducationDhyunService(EducationDhyunRepository educationDhyunRepository) {
        super(educationDhyunRepository);
        this.educationDhyunRepository = educationDhyunRepository;
    }

    public List<EducationDhyun> gets(RequestParams<EducationDhyun> requestParams) {
        List<EducationDhyun> list1=this.getFilter(findAll(), requestParams.getString("companyNm", ""), 1);
        List<EducationDhyun> list2=this.getFilter(list1, requestParams.getString("ceo", ""), 2);
        List<EducationDhyun> list3=this.getFilter(list2, requestParams.getString("bizno", ""), 3);
        List<EducationDhyun> list4=this.getFilter(list3, requestParams.getString("useYn", ""), 4);

        return list4;
    }

    private List<EducationDhyun> getFilter(List<EducationDhyun> sources, String filter, int typ) {
        List<EducationDhyun> targets=new ArrayList<EducationDhyun>();
        for(EducationDhyun entity : sources) {
            if("".equals(filter)) {
                targets.add(entity);
            } else {
                if(typ == 1) {
                    if(entity.getCompanyNm().contains(filter)) {
                        targets.add(entity);
                    }
                } else if(typ == 2) {
                    if(entity.getCeo().contains(filter)) {
                        targets.add(entity);
                    }
                } else if(typ == 3) {
                    if(entity.getBizno().contains(filter)) {
                        targets.add(entity);
                    }
                } else if(typ == 4) {
                    if(entity.getUseYn().contains(filter)) {
                        targets.add(entity);
                    }
                }
            }
        }
        return targets;
    }

    public List<EducationDhyun> getByQueryDsl(RequestParams<EducationDhyun> requestParams) {
        String companyNm = requestParams.getString("companyNm", "");
        String ceo = requestParams.getString("ceo", "");
        String bizno = requestParams.getString("bizno", "");
        String useYn = requestParams.getString("useYn", "");
        String filter = requestParams.getFilter();

        logger.info("회사명 : "+companyNm);
        logger.info("대표자 : "+ceo);
        logger.info("사업자번호 : "+bizno);
        logger.info("사용여부 : "+useYn);

        BooleanBuilder builder=new BooleanBuilder();

        if (isNotEmpty(companyNm)) {
            builder.and(qEducationDhyun.companyNm.contains(companyNm));
        }
        if (isNotEmpty(ceo)) {
            builder.and(qEducationDhyun.ceo.contains(ceo));
        }
        if (isNotEmpty(bizno)) {
            builder.and(qEducationDhyun.bizno.contains(bizno));
        }
        if (isNotEmpty(useYn)) {
            builder.and(qEducationDhyun.useYn.eq(useYn));
        }
        if (isNotEmpty(filter)) {
            builder.and(qEducationDhyun.companyNm.contains(filter))
                    .or(qEducationDhyun.ceo.contains(filter))
                    .or(qEducationDhyun.bizno.contains(filter));
        }

        List<EducationDhyun> list=select()
                .from(qEducationDhyun)
                .where(builder)
                .orderBy(qEducationDhyun.id.asc())
                .fetch();

        return list;
    }

    public EducationDhyun getByQueryDslOne(Long id) {
        EducationDhyun educationDhyun=select().from(qEducationDhyun).where(qEducationDhyun.id.eq(id)).fetchOne();
        return educationDhyun;
    }

    @Transactional
    public void saveByQueryDsl(List<EducationDhyun> request) {
        for(EducationDhyun educationDhyun : request) {
            saveByQueryDsl(educationDhyun);
        }
    }

    @Transactional
    public void saveByQueryDsl(EducationDhyun educationDhyun) {
        if(educationDhyun.isCreated()) {
            save(educationDhyun);
        } else if(educationDhyun.isModified()) {
            update(qEducationDhyun)
                    .set(qEducationDhyun.companyNm, educationDhyun.getCompanyNm())
                    .set(qEducationDhyun.ceo, educationDhyun.getCeo())
                    .set(qEducationDhyun.bizno, educationDhyun.getBizno())
                    .set(qEducationDhyun.tel, educationDhyun.getTel())
                    .set(qEducationDhyun.email, educationDhyun.getEmail())
                    .set(qEducationDhyun.useYn, educationDhyun.getUseYn())
                    .where(qEducationDhyun.id.eq(educationDhyun.getId()))
                    .execute();
        } else if (educationDhyun.isDeleted()) {
            delete(qEducationDhyun).where(qEducationDhyun.id.eq(educationDhyun.getId())).execute();
        }
    }

    public List<EducationDhyun> selectList(RequestParams<EducationDhyun> requestParams) {
        EducationDhyun educationDhyun=new EducationDhyun();

        educationDhyun.setCompanyNm(requestParams.getString("companyNm", ""));
        educationDhyun.setCeo(requestParams.getString("ceo", ""));
        educationDhyun.setBizno(requestParams.getString("bizno", ""));
        educationDhyun.setUseYn(requestParams.getString("useYn", ""));

        List<EducationDhyun> list=educationDhyunMapper.selectList(educationDhyun);

        return list;
    }

    public Page<EducationDhyun> selectListPage(RequestParams<EducationDhyun> requestParams) {
        Map<String, Object> map=new HashMap<>();

        map.put("companyNm", requestParams.getString("companyNm", ""));
        map.put("ceo", requestParams.getString("ceo", ""));
        map.put("bizno", requestParams.getString("bizno", ""));
        map.put("useYn", requestParams.getString("useYn", ""));

        Pageable pageable=requestParams.getPageable();
        int pageSize=pageable.getPageSize();
        int pageNumber=pageable.getPageNumber();

        int dataCount=educationDhyunMapper.dataCount(map);
        int offset=pageSize*pageNumber;
        if(offset<0) offset=0;

        map.put("offset", offset);
        map.put("pageSize", pageSize);

        List<EducationDhyun> list=educationDhyunMapper.selectListPage(map);

        Page<EducationDhyun> page=new PageImpl<>(list, pageable, dataCount);

        return page;
    }

    public List<EducationDhyun> gets(String companyNm, String ceo, String bizno, String useYn) {
        BooleanBuilder builder=new BooleanBuilder();

        if (isNotEmpty(companyNm)) {
            builder.and(qEducationDhyun.companyNm.contains(companyNm));
        }
        if (isNotEmpty(ceo)) {
            builder.and(qEducationDhyun.ceo.contains(ceo));
        }
        if (isNotEmpty(bizno)) {
            builder.and(qEducationDhyun.bizno.contains(bizno));
        }
        if (isNotEmpty(useYn)) {
            builder.and(qEducationDhyun.useYn.eq(useYn));
        }
        List<EducationDhyun> list=select()
                .from(qEducationDhyun)
                .where(builder)
                .orderBy(qEducationDhyun.id.asc())
                .fetch();

        return list;
    }

    public EducationDhyun getByOne(Long id) {
        EducationDhyun educationDhyun=select()
                .from(qEducationDhyun)
                .where(qEducationDhyun.id.eq(id))
                .fetchOne();

        return educationDhyun;
    }

    @Transactional
    public void saveUsingQueryDsl(EducationDhyun entity) {
        if(entity.getId()==null || entity.getId()==0) {
            if (entity.getFileIdList().size() > 0) {
               entity.setAttachId(CoreUtils.getUUID().replaceAll("-", ""));

               List<CommonFile> commonFileList = new ArrayList<>();

               for(Long fileId : entity.getFileIdList()) {
                   CommonFile commonFile = commonFileService.findOne(fileId);
                   commonFile.setTargetId(entity.getAttachId());
                   commonFileList.add(commonFile);
               }

               entity.setFileList(commonFileList);
            }

            this.educationDhyunRepository.save(entity);
        } else {
            update(qEducationDhyun)
                    .set(qEducationDhyun.companyNm, entity.getCompanyNm())
                    .set(qEducationDhyun.ceo, entity.getCeo())
                    .set(qEducationDhyun.bizno, entity.getBizno())
                    .set(qEducationDhyun.tel, entity.getTel())
                    .set(qEducationDhyun.zip, entity.getZip())
                    .set(qEducationDhyun.address, entity.getAddress())
                    .set(qEducationDhyun.addressDetail, entity.getAddressDetail())
                    .set(qEducationDhyun.email, entity.getEmail())
                    .set(qEducationDhyun.remark, entity.getRemark())
                    .set(qEducationDhyun.useYn, entity.getUseYn())
                    .where(qEducationDhyun.id.eq(entity.getId()))
                    .execute();
        }
    }

    @Transactional
    public void deleteUsingQueryDsl(Long id) {
        delete(qEducationDhyun).where(qEducationDhyun.id.eq(id)).execute();
    }

    @Transactional
    public void deleteUsingQueryDsl(List<Long> ids) {
        for(Long id : ids) {
            deleteUsingQueryDsl(id);
        }
    }

    public List<EducationDhyun> select(String companyNm, String ceo, String bizno, String useYn) {
        if(!"".equals(useYn) && !"Y".equals(useYn) && !"N".equals(useYn)) {
            throw new RuntimeException("Y 아니면 N 입력하세요");
        }
        Map<String, Object> map=new HashMap<>();
        map.put("companyNm", companyNm);
        map.put("ceo", ceo);
        map.put("bizno", bizno);
        map.put("useYn", useYn);

        List<EducationDhyun> list=educationDhyunMapper.select(map);
        return list;
    }

    public EducationDhyun selectOne(Long id) {
        EducationDhyun educationDhyun=educationDhyunMapper.selectOne(id);
        return educationDhyun;
    }

    public void enroll(EducationDhyun educationDhyun) {
        if(educationDhyun.getId()==null || educationDhyun.getId()==0) {
            educationDhyunMapper.insert(educationDhyun);
        } else {
            educationDhyunMapper.update(educationDhyun);
        }
    }

    public void del(Long id) {
        educationDhyunMapper.delete(id);
    }

    public void del(List<Long> ids) {
        for(Long id : ids) {
            del(id);
        }
    }

    @Transactional
    public String saveDataByExcel(UploadFile uploadFile) throws Exception {
        String resultMsg="";

        ReaderConfig.getInstance().setSkipErrors(true);

        XLSReader mainReader = ReaderBuilder.buildFromXML(new ClassPathResource("/excel/education_upload.xml").getInputStream());
        List<EducationDhyun> entities = new ArrayList();

        Map beans = new HashMap();
        beans.put("educationList", entities);

        String excelFile = uploadFile.getSavePath();
        File file = new File(excelFile);
        mainReader.read(FileUtils.openInputStream(file), beans);
        // 일반적인 파일을 읽는 방법

        int rowIndex = 1;

        for (EducationDhyun entity : entities) {
            if (StringUtils.isEmpty(entity.getCompanyNm())) {
                resultMsg = String.format("%d 번째 줄의 회사명이 비어있습니다.", rowIndex);
                throw new ApiException(String.format("%d 번째 줄의 회사명이 비어있습니다.", rowIndex));
            }

            if (StringUtils.isEmpty(entity.getCeo())) {
                resultMsg = String.format("%d 번째 줄의 대표자가 비어있습니다.", rowIndex);
                throw new ApiException(String.format("%d 번째 줄의 대표자가 비어있습니다.", rowIndex));
            }

            if (StringUtils.isEmpty(entity.getUseYn())) {
                resultMsg = String.format("%d 번째 줄의 사용여부가 비어있습니다.", rowIndex);
                throw new ApiException(String.format("%d 번째 줄의 사용여부가 비어있습니다.", rowIndex));
            }

            save(entity);

            rowIndex++;
        }

        return resultMsg;
    }

    @Transactional
    public String uploadFileByExcel(MultipartFile multipartFile) throws Exception {
        UploadParameters uploadParameters = new UploadParameters();
        uploadParameters.setMultipartFile(multipartFile);

        UploadFile uploadFile = fileUploadService.addCommonFile(uploadParameters);
        String result = this.saveDataByExcel(uploadFile);

        fileUploadService.deleteFile(uploadFile.getSavePath());

        return result;
    }

}