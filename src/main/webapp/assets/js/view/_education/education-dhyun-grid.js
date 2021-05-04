var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        var paramObj = $.extend({}, this.searchView.getData(), this.gridView01.getPageData());

        axboot.ajax({
            type: 'GET',
            url: '/api/v1/dhyunGrid/listPage',
            data: paramObj,
            callback: function (res) {
                caller.gridView01.setData(res);
            },
            options: {
                // axboot.ajax 함수에 2번째 인자는 필수가 아닙니다. ajax의 옵션을 전달하고자 할때 사용합니다.
                onError: function (err) {
                    console.log(err);
                },
            },
        });

        return false;
    },
    PAGE_SAVE: function (caller, act, data) {
        var saveList = [].concat(caller.gridView01.getData());
        saveList = saveList.concat(caller.gridView01.getData('deleted'));

        axboot.ajax({
            type: 'PUT',
            url: '/api/v1/dhyunGrid/QueryDsl',
            data: JSON.stringify(saveList),
            callback: function (res) {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                axToast.push('저장 되었습니다');
            },
        });
    },
    ITEM_CLICK: function (caller, act, data) {},
    ITEM_ADD: function (caller, act, data) {
        caller.gridView01.addRow();
    },
    ITEM_DEL: function (caller, act, data) {
        caller.gridView01.delRow('selected');
    },
    EXCEL_DOWN: function (caller, act, data) {
        let frm = document['excelForm'];
        frm.action = '/api/v1/dhyunGrid/excelDown';
        // frm.parentKey.value=fnObj.gridView01.pKey;
        frm.submit();
    },
    dispatch: function (caller, act, data) {
        var result = ACTIONS.exec(caller, act, data);
        if (result != 'error') {
            return result;
        } else {
            // 직접코딩
            return false;
        }
    },
});

// fnObj 기본 함수 스타트와 리사이즈
fnObj.pageStart = function () {
    this.pageButtonView.initView();
    this.searchView.initView();
    this.gridView01.initView();

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
            excel: function () {
                ACTIONS.dispatch(ACTIONS.EXCEL_DOWN);
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
        this.companyNm = $('#companyNm');
        this.ceo = $('#ceo');
        this.bizno = $('#bizno');
        this.useYn = $('#useYn');
        this.useYnAx5 = $('[data-ax5select="useYn"]').ax5select({
            columnKeys: {
                optionValue: 'value',
                optionText: 'text',
            },
            options: [
                { value: '', text: '전체' },
                { value: 'Y', text: '사용' },
                { value: 'N', text: '사용안함' },
            ],
        });
    },
    getData: function () {
        return {
            pageNumber: this.pageNumber || 0,
            pageSize: this.pageSize || 2,
            sort: this.id,
            companyNm: this.companyNm.val(),
            ceo: this.ceo.val(),
            bizno: this.bizno.val(),
            useYn: this.useYn.val(),
            useYnAx5: ($('[data-ax5select="useYn"]').ax5select('getValue')[0] || {}).value,
        };
    },
});

/**
 * gridView
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    page: {
        pageNumber: 0,
        pageSize: 2,
    },
    initView: function () {
        var _this = this;

        this.target = axboot.gridBuilder({
            showRowSelector: true,
            frozenColumnIndex: 0,
            multipleSelect: true,
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                { key: 'companyNm', label: COL('company.name'), width: 200, align: 'left', editor: 'text' },
                { key: 'ceo', label: COL('company.ceo'), width: 100, align: 'left', editor: 'text' },
                {
                    key: 'bizno',
                    label: COL('company.bizno'),
                    width: 100,
                    align: 'center',
                    editor: 'number',
                },
                {
                    key: 'tel',
                    label: COL('company.tel'),
                    width: 100,
                    align: 'center',
                    editor: 'number',
                    formatter: function () {
                        var value = this.value;
                        return value;
                    },
                },
                {
                    key: 'email',
                    label: COL('company.email'),
                    width: 150,
                    align: 'center',
                    editor: 'text',
                },
                {
                    key: 'useYn',
                    label: COL('use.or.not'),
                    align: 'center',
                    editor: {
                        type: 'select',
                        config: {
                            columnKeys: {
                                optionValue: 'value',
                                optionText: 'text',
                            },
                            options: [
                                { value: 'Y', text: '사용' },
                                { value: 'N', text: '사용안함' },
                            ],
                        },
                    },
                },
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex, { selectedClear: true });
                },
            },
            onPageChange: function (pageNumber) {
                _this.setPageData({ pageNumber: pageNumber });
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            },
        });

        axboot.buttonClick(this, 'data-grid-view-01-btn', {
            add: function () {
                ACTIONS.dispatch(ACTIONS.ITEM_ADD);
            },
            delete: function () {
                ACTIONS.dispatch(ACTIONS.ITEM_DEL);
            },
        });
    },
    getData: function (_type) {
        var list = [];
        var _list = this.target.getList(_type);

        if (_type == 'modified' || _type == 'deleted') {
            list = ax5.util.filter(_list, function () {
                //                delete this.deleted;
                //                return this.key;
                return this.id;
            });
        } else {
            list = _list;
        }
        return list;
    },
    addRow: function () {
        this.target.addRow({ __created__: true }, 'last');
    },
});
