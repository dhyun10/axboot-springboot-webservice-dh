<%@ page contentType="text/html; charset=UTF-8" %> <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="ax"
tagdir="/WEB-INF/tags" %>

<ax:set key="title" value="${pageName}" />
<ax:set key="page_desc" value="${pageRemark}" />
<ax:set key="page_auto_height" value="true" />

<ax:layout name="base">
    <jsp:attribute name="script">
        <ax:script-lang key="ax.script" />
        <ax:script-lang key="ax.base" var="COL" />
        <script type="text/javascript" src="https://cdn.rawgit.com/ax5ui/ax5ui-uploader/master/dist/ax5uploader.js"></script>
        <script type="text/javascript" src="<c:url value='/assets/js/view/_education/education-dhyun-grid-form.js' />"></script>
    </jsp:attribute>
    <jsp:body>
        <ax:page-buttons></ax:page-buttons>

        <div role="page-header">
            <form name="searchView0" id="searchView0" method="post" onsubmit="return ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);">
                <div data-ax-tbl class="ax-search-tbl">
                    <div data-ax-tr>
                        <div data-ax-td style="width:200px">
                            <div data-ax-td-label>사용여부</div>
                            <div data-ax-td-wrap>
                                <select class="form-control js-useYn ">
                                    <option value="">전체</option>
                                    <option value="Y">사용</option>
                                    <option value="N">사용안함</option>
                                </select>
                            </div>
                        </div>
                        <div data-ax-td style="width:300px">
                            <div data-ax-td-label>검색</div>
                            <div data-ax-td-wrap>
                                <input type="text" class="js-searchFilter form-control" placeholder="회사명 / 대표자 / 사업자번호">
                            </div>
                        </div>
                    </div>
                </div>
            </form>
            <div class="H10"></div>
        </div>

        <div class="container-fluid">
            <div class="row">
                <div class="col-md-4">
                    <div class="ax-button-group" data-fit-height-aside="grid-view-01">
                        <div class="left">
                            <h2><i class="cqc-list"></i> <ax:lang id="ax.admin.program.title" /></h2>
                        </div>
                    </div>
                    <div data-ax5grid="grid-view-01" data-fit-height-content="grid-view-01" style="height: 400px"></div>
                </div>
                <div class="col-md-8">
                    <div class="ax-button-group" data-fit-height-aside="grid-view-01">
                        <div class="left">
                            <h2><i class="cqc-uh-page-view"></i> 상세 정보</h2>
                        </div>
                        <div class="right">
                            <button type="button" class="btn btn-default" data-grid-view-01-btn="clear">
                                <i class="cqc-circle-with-plus"></i> <ax:lang id="ax.admin.clear" />
                            </button>
                        </div>
                    </div>
                    <form name="form" class="js-form">
                        <div data-ax-tbl class="ax-form-tbl" >
                            <div data-ax-tr>
                                <div data-ax-td style="width:50%">
                                    <div data-ax-td-label style="width:150px">ID</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" data-ax-path="id" class="form-control" value="" readonly="readonly">
                                    </div>
                                </div>
                                <div data-ax-td id style="width:50%">
                                    <div data-ax-td-label style="width:150px">사용여부</div>
                                    <div data-ax-td-wrap>                                
                                        <select class="form-control null " data-ax-path="useYn"><option value="">전체</option><option value="Y">사용</option><option value="N">사용안함</option></select>                      
                                    </div>
                                </div>                
                            </div>
                            <div data-ax-tr>
                                <div data-ax-td style="width:50%">
                                    <div data-ax-td-label style="width:150px">사업자명</div>
                                    <div data-ax-td-wrap="">    
                                        <input type="text" data-ax-path="companyNm" class="form-control" value="">                      
                                    </div>
                                </div>
                                <div data-ax-td style="width:50%">
                                    <div data-ax-td-label="" class="" style="width:150px">대표자</div>
                                    <div data-ax-td-wrap="">
                                        <input type="text" data-ax-path="ceo" class="form-control" value="">    
                                    </div>
                                </div>                   
                            </div>
                            <div data-ax-tr>
                                <div data-ax-td style="width:50%">
                                    <div data-ax-td-label style="width:150px">사업자번호</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" data-ax-path="bizno" data-ax5formatter="bizno" class="form-control" placeholder="000-00-00000">                   
                                    </div>
                                </div>
                                <div data-ax-td style="width:50%">
                                    <div data-ax-td-label style="width:150px">전화번호</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" data-ax-path="tel" class="form-control" value="">
                                    </div>
                                </div>            
                            </div>
                            <div data-ax-tr>
                                <div data-ax-td style="width:50%">
                                    <div data-ax-td-label style="width:150px">이메일</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" data-ax-path="email" class="form-control" placeholder="aa@bb.com">
                                    </div>
                                </div>            
                                <div data-ax-td style="width:50%">
                                    <div data-ax-td-label style="width:150px">우편번호</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" data-ax-path="zip" class="form-control" value="">                   
                                    </div>
                                </div>    
                            </div>
                            <div data-ax-tr>
                                <div data-ax-td style="width:100%">
                                    <div data-ax-td-label style="width:150px">주소</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" data-ax-path="address" class="form-control" value="">
                                    </div>
                                </div>        
                            </div>
                            <div data-ax-tr>
                                <div data-ax-td style="width:100%">
                                    <div data-ax-td-label style="width:150px">상세 주소</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" data-ax-path="addressDetail" class="form-control" value="">                   
                                    </div>
                                </div>          
                            </div>
                            <div data-ax-tr>
                                <div data-ax-td style="width:100%">
                                    <div data-ax-td-label style="width:150px">비고</div>
                                    <div data-ax-td-wrap>
                                        <textarea rows="5" data-ax-path="remark" name="editor1" id="editor1" class="form-control"></textarea>                   
                                    </div>
                                </div>          
                            </div>
                            <ax:tr labelWidth="150px">
                                <ax:td label="첨부파일 <i class='icon-info-circled cp'></i>" width="100%">
                                    <div data-ax5uploader="upload1">
                                        <input type="hidden" id="targetType" name="targetType" value="EDUCATION_DHYUN" />
                                        <button data-ax5uploader-button="selector" class="btn btn-primary">Select File (*/*)</button>
                                        (Upload Max fileSize 100MB)
                                        <div data-uploaded-box="upload1" data-ax5uploader-uploaded-box="inline"></div>
                                    </div>
                                </ax:td>
                            </ax:tr>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </jsp:body>
</ax:layout>
