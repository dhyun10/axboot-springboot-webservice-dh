var fnObj = {},
    CODE = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        var paramObj = $.extend({}, this.searchView.getData(), this.gridView01.getPageData());

        axboot.ajax({
            type: 'GET',
            url: '/api/v1/dhyunGridForm',
            data: paramObj,
            callback: function (res) {
                caller.gridView01.setData(res);
                caller.formView01.clear();
            },
        });
    },
    PAGE_SAVE: function (caller, act, data) {
        if (caller.formView01.validate()) {
            var item = caller.formView01.getData();

            var fileIds = [];
            var files = ax5.util.deepCopy(caller.formView01.UPLOAD.uploadedFiles);
            $.each(files, function (idx, o) {
                fileIds.push(o.id);
            });
            item.fileIdList = fileIds;

            if (!item.id) item.__created__ = true;

            axboot.ajax({
                type: 'POST',
                url: '/api/v1/dhyunGridForm',
                data: JSON.stringify(item),
                callback: function (res) {
                    ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                    axToast.push('저장되었습니다.');
                },
            });
        }
    },
    FORM_CLEAR: function (caller, act, data) {
        caller.formView01.clear();
    },
    ITEM_CLICK: function (caller, act, data) {
        var id = data.id;
        axboot.ajax({
            type: 'GET',
            url: '/api/v1/dhyunGridForm/' + id,
            callback: function (res) {
                caller.formView01.setData(res);
            },
        });
    },
    DATA_DELETE: function (caller, act, data) {
        var item = caller.gridView01.getData('selected');
        var ids = item.map(function (value) {
            return value.id;
        });
        axboot.ajax({
            type: 'DELETE',
            url: '/api/v1/dhyunGridForm?ids=' + ids.join(','),
            callback: function (res) {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                axToast.push('삭제되었습니다.');
            },
        });
    },
});

fnObj.pageStart = function () {
    this.pageButtonView.initView();
    this.searchView.initView();
    this.gridView01.initView();
    this.formView01.initView({
        viewMode: false,
        editorReady: function () {},
    });

    ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
};

fnObj.pageResize = function () {};

fnObj.pageButtonView = axboot.viewExtend({
    initView: function () {
        axboot.buttonClick(this, 'data-page-btn', {
            search: function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            },
            save: function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SAVE);
            },
            fn1: function () {
                ACTIONS.dispatch(ACTIONS.DATA_DELETE);
            },
        });
    },
});

//== view 시작
/**
 * searchView
 */
fnObj.searchView = axboot.viewExtend(axboot.searchView, {
    initView: function () {
        this.target = $(document['searchView0']);
        this.target.attr('onsubmit', 'return ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);');
        this.useYn = $('.js-useYn').on('change', function () {
            ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
        });
        this.filter = $('.js-searchFilter');
    },
    getData: function () {
        return {
            pageNumber: this.pageNumber || 0,
            pageSize: this.pageSize || 3,
            filter: this.filter.val(),
            useYn: this.useYn.val(),
        };
    },
});

/**
 * gridView01
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    page: {
        pageNumber: 0,
        pageSize: 3,
    },
    initView: function () {
        var _this = this;

        this.target = axboot.gridBuilder({
            showRowSelector: true,
            frozenColumnIndex: 0,
            multipleSelect: true,
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                { key: 'companyNm', label: COL('company.name'), width: 110, align: 'left' },
                { key: 'ceo', label: COL('company.ceo'), width: 80, align: 'left' },
                {
                    key: 'useYn',
                    label: COL('use.or.not'),
                    align: 'center',
                },
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex, { selectedClear: true });
                    ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.item);
                },
            },
            onPageChange: function (pageNumber) {
                _this.setPageData({ pageNumber: pageNumber });
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            },
        });
    },
});

/**
 * formView01
 */
fnObj.formView01 = axboot.viewExtend(axboot.formView, {
    getDefaultData: function () {
        return {
            useYn: 'Y',
        };
    },
    getData: function () {
        var _this = this;

        var remark = '';
        if (_this.editor) {
            remark = _this.editor.getData();
        }

        var data = this.modelFormatter.getClearData(this.model.get());
        data.remark = remark;
        return $.extend({}, data);
    },
    setData: function (data) {
        if (typeof data === 'undefined') data = this.getDefaultData();
        data = $.extend({}, data);

        this.model.setModel(data);
        this.modelFormatter.formatting();

        var _this = this;

        setTimeout(function () {
            _this.editor.setData(data.remark);
        }, 100);

        if (typeof data.fileList != 'undefined' && data.fileList.length > 0) {
            _this.UPLOAD.setUploadedFiles(data.fileList);
        }
    },
    clear: function () {
        fnObj.formView01.setData(this.getDefaultData());
    },
    validate: function () {
        var item = this.model.get();
        var pattern;

        if (item.email) {
            pattern = /^[A-Za-z0-9]([-_.]?[A-Za-z0-9])*@[A-Za-z0-9]([-_.]?[A-Za-z0-9])*\.(?:[A-Za-z0-9]{2,}?)$/i;
            if (!pattern.test(item.email)) {
                alert('이메일 형식을 확인하세요');
                $('[data-ax-path="email"]').focus();
                return false;
            }
        }

        if (item.bizno && !(pattern = /^([0-9]{3})\-?([0-9]{2})\-?([0-9]{5})$/).test(item.bizno)) {
            alert('사업자번호 형식을 확인하세요');
            $('[data-ax-path="bizno"').focus();
            return false;
        }

        return true;
    },
    initEditor: function (obj) {
        var _this = this;
        var readOnly = obj.viewMode ? true : false;
        this.editor = CKEDITOR.replace('editor1', {
            filebrowserBrowseUrl: CONTEXT_PATH + '/ckeditor/fileBrowser?targetType=' + fnObj.formView01.getData().bbsId + '&targetId=' + UUID,
            filebrowserWindowWidth: '960',
            filebrowserWindowHeight: '600',
            imageUploadUrl: CONTEXT_PATH + '/ckeditor/uploadImage?targetType=' + fnObj.formView01.getData().bbsId + '&targetId=' + UUID,
            toolbarGroups: (function () {
                var viewmode_groups = [
                    { name: 'insert', groups: ['others', 'insert'] },
                    { name: 'tools', groups: ['tools'] },
                    { name: 'styles', groups: ['styles'] },
                ];
                var groups = [
                    { name: 'others', groups: ['others'] },
                    { name: 'insert', groups: ['insert'] },
                    { name: 'document', groups: ['mode', 'document', 'doctools'] },
                    { name: 'links', groups: ['links'] },
                    '/',
                    { name: 'basicstyles', groups: ['basicstyles', 'cleanup'] },
                    { name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi', 'paragraph'] },
                    '/',
                    { name: 'styles', groups: ['styles'] },
                    { name: 'colors', groups: ['colors'] },
                ];
                return readOnly ? viewmode_groups : groups;
            })(),
            readOnly: readOnly,
        });
        this.editor.once('instanceReady', function () {
            if (obj && obj.editorReady) {
                obj.editorReady();
            }
        });
        this.editor.on('notificationShow', function (evt) {
            evt.cancel();
        });
        this.editor.on('notificationUpdate', function (evt) {
            evt.cancel();
        });
        var form = '';
        if (!obj.viewMode) {
            CKEDITOR.instances.editor1.setData(form);
        }
    },
    initUploader: function () {
        var _this = this;
        _this.UPLOAD = new ax5.ui.uploader({
            //debug: true,
            target: $('[data-ax5uploader="upload1"]'),
            form: {
                action: '/api/v1/files/upload',
                fileName: 'file',
            },
            multiple: true,
            manualUpload: false,
            progressBox: true,
            progressBoxDirection: 'left',
            dropZone: {
                target: $('[data-uploaded-box="upload1"]'),
            },
            uploadedBox: {
                target: $('[data-uploaded-box="upload1"]'),
                icon: {
                    download: '<i class="cqc-download" aria-hidden="true"></i>',
                    delete: '<i class="cqc-minus" aria-hidden="true"></i>',
                },
                columnKeys: {
                    name: 'fileNm',
                    type: 'extension',
                    size: 'fileSize',
                    uploadedName: 'saveName',
                    uploadedPath: '',
                    downloadPath: '',
                    previewPath: '',
                    thumbnail: '',
                },
                lang: {
                    supportedHTML5_emptyListMsg: '<div class="text-center" style="padding-top: 30px;">첨부파일이 없습니다. </div>',
                    emptyListMsg: '<div class="text-center" style="padding-top: 30px;">Empty of List.</div>',
                },
                onchange: function () {},
                onclick: function () {
                    var fileIndex = this.fileIndex;
                    var file = this.uploadedFiles[fileIndex];

                    switch (this.cellType) {
                        case 'delete':
                            axDialog.confirm(
                                {
                                    theme: 'danger',
                                    msg: '삭제하시겠습니까?',
                                },
                                function () {
                                    if (this.key == 'ok') {
                                        $.ajax({
                                            contentType: 'application/json',
                                            method: 'get',
                                            url: '/api/v1/files/delete',
                                            /*
                                        data: JSON.stringify([{
                                            id: file.id
                                        }]),
                                        */
                                            data: { id: file.id },
                                            success: function (res) {
                                                if (res.error) {
                                                    alert(res.error.message);
                                                    return;
                                                }
                                                _this.UPLOAD.removeFile(fileIndex);
                                            },
                                        });
                                    }
                                }
                            );
                            break;

                        case 'download':
                            if (file.download) {
                                location.href = file.download;
                            }
                            break;
                    }
                },
            },
            validateSelectedFiles: function () {
                var limitCount = 5;
                if (this.uploadedFiles.length + this.selectedFiles.length > limitCount) {
                    alert('You can not upload more than ' + limitCount + ' files.');
                    return false;
                }
                return true;
            },
            onprogress: function () {},
            onuploaderror: function () {
                console.log(this.error);
                axDialog.alert(this.error.message);
            },
            onuploaded: function () {},
            onuploadComplete: function () {},
        });
    },
    initView: function () {
        var _this = this; // fnObj.formView01

        _this.target = $('.js-form');

        this.model = new ax5.ui.binder();
        this.model.setModel(this.getDefaultData(), this.target);
        this.modelFormatter = new axboot.modelFormatter(this.model);

        this.initEvent();
        this.initUploader();
        this.initEditor(obj);
    },
    initEvent: function () {
        axboot.buttonClick(this, 'data-grid-view-01-btn', {
            clear: function () {
                ACTIONS.dispatch(ACTIONS.FORM_CLEAR);
            },
        });
    },
});
