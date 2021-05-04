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
        var item = caller.formView01.getData();
        axboot.ajax({
            type: 'POST',
            url: '/api/v1/dhyunGridForm',
            data: JSON.stringify(item),
            callback: function (res) {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                axToast.push('저장되었습니다.');
            },
        });
    },
    FORM_CLEAR: function (caller, act, data) {
        caller.formView01.clear();
    },
    ITEM_CLICK: function (caller, act, data) {},
    DATA_DELETE: function (caller, act, data) {
        var id = caller.formView01.delData();
        axboot.ajax({
            type: 'DELETE',
            url: '/api/v1/dhyunGridForm/' + id,
            data: id,
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
            pageSize: this.pageSize || 2,
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
                    fnObj.formView01.setData(this.getDefaultData);
                    fnObj.formView01.setData(this.item);
                },
            },
            onPageChange: function (pageNumber) {
                _this.setPageData({ pageNumber: pageNumber });
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            },
        });

        axboot.buttonClick(this, 'data-grid-view-01-btn', {
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
    delData: function () {
        var id;
    },
});

/**
 * formView01
 */
fnObj.formView01 = axboot.viewExtend(axboot.formView, {
    getDefaultData: function () {
        return {
            id: '',
            useYn: '',
            companyNm: '',
            ceo: '',
            bizno: '',
            tel: '',
            zip: '',
            address: '',
            addressDetail: '',
            remark: '',
        };
    },
    initView: function () {
        var _this = this; // fnObj.formView01

        _this.target = $('.js-form');
        // _this.model = new ax5.ui.binder();
        // _this.model.setModel({}, _this.target);

        // console.log(_this.model.get());

        // setTimeout(function () {
        //     _this.model.useYn = 'N';
        // }, 3000);
        axboot.buttonClick(this, 'data-grid-view-01-btn', {
            clear: function () {
                ACTIONS.dispatch(ACTIONS.FORM_CLEAR);
            },
        });
    },
    getData: function () {
        var item = {};
        this.target.find('input,select').each(function (i, elem) {
            //var $elem = $(elem);
            var $elem = $(this);
            var name = $elem.data('axPath');
            var value = $elem.val() || '';
            item[name] = value;
        });
        return item;
    },
    setData: function (item) {
        var value;
        for (var prop in item) {
            value = item[prop] || '';
            $('[data-ax-path="' + prop + '"]').val(value);
        }
    },
    clear: function () {
        fnObj.formView01.setData(this.getDefaultData());
    },
    delData: function () {
        var id = $('[data-ax-path=id').val();
        return id;
    },
});
