var fnObj = {},
    CODE = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        var paramObj = $.extend({}, this.searchView.getData(), this.gridView01.getPageData());

        axboot.ajax({
            type: 'GET',
            url: '/api/v1/dhyunGridForm/myBatis',
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
            if (!item.id) item.__created__ = true;

            axboot.ajax({
                type: 'POST',
                url: '/api/v1/dhyunGridForm/myBatis',
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
            url: '/api/v1/dhyunGridForm/myBatis/' + id,
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
            url: '/api/v1/dhyunGridForm/myBatis?ids=' + ids.join(','),
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
    this.formView01.initView();

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
        var data = this.modelFormatter.getClearData(this.model.get());
        return data;
    },
    setData: function (data) {
        this.model.setModel(data);
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
    initView: function () {
        var _this = this; // fnObj.formView01

        _this.target = $('.js-form');

        this.model = new ax5.ui.binder();
        this.model.setModel(this.getDefaultData(), this.target);
        this.modelFormatter = new axboot.modelFormatter(this.model);

        this.initEvent();
    },
    initEvent: function () {
        axboot.buttonClick(this, 'data-grid-view-01-btn', {
            clear: function () {
                ACTIONS.dispatch(ACTIONS.FORM_CLEAR);
            },
        });
    },
});
