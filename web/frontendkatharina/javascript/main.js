/**
 * Created by katharinaspinner on 05.12.16.
 */

const endpointScheme = 'http://';
const fixedcostshtmlurl = 'forecastfixedcosts.html';
const saleshtmlurl = 'forecastsales.html';
var planYear;
var backend;
var currency;
var period;
var keyspace;
var endpointPath = '/fourschlag/api/' + keyspace + '/' ;
var kpiType;
var newTab;
var salesurl;
var fixedcosturl;
var forecastSalesTable = '################';
var forecastFixedCostsTable;



window.addEventListener('load', function () {

    function openNewTab(url) {
        newTab = window.open(url);
        if (newTab) {
            newTab.focus();

        } else {
            //Browser has blocked it
            alert('Please allow popups for this website');
        }
    }


    function loadTable (type ,request){
        newTab.addEventListener('load', function () {
            console.log(forecastSalesTable + ' bevor table inizialisiert')
            if (type == 'sales'){
                forecastSalesTable = newTab.$('#ForecastSalesTable').DataTable({
                    "ajax": {
                        "url": request,
                        "dataSrc": ""
                    },
                    "info": false,
                    "paging": false,
                    aaSorting: [],
                    "columns": [
                        {"data": "PERIOD"},
                        {"data": "REGION"},
                        {"data": "PERIOD_YEAR"},
                        {"data": "PERIOD_MONTH"},
                        {"data": "CURRENCY"},
                        {"data": "USERID"},
                        {"data": "ENTRY_TS"},
                        {"data": "PRODUCT_MAIN_GROUP"},
                        {"data": "SALES_TYPE"},
                        {"data": "SALES_VOLUMES"},
                        {"data": "NET_SALES"},
                        {"data": "CM1"},
                        {"data": "TOPDOWN_ADJUST_SALES_VOLUMES"},
                        {"data": "TOPDOWN_ADJUST_NET_SALES"},
                        {"data": "TOPDOWN_ADJUST_CM1"},
                        {"data": "PLAN_PERIOD"},
                        {"data": "PLAN_YEAR"},
                        {"data": "PLAN_HALF_YEAR"},
                        {"data": "PLAN_QUARTER"},
                        {"data": "PLAN_MONTH"},
                        {"data": "ENTRY_TYPE"},
                        {"data": "STATUS"},
                        {"data": "USERCOMMENT"}
                    ]
                });
            console.log(forecastSalesTable + ' nach table inizialisiert');

            }
            if (type =='fixed costs'){
                forecastFixedCostsTable = newTab.$('#ForecastFixedcostsTable').DataTable({
                    "ajax": {
                        "url": request,
                        "dataSrc": ""
                    },
                    "info": false,
                    "paging": false,
                    aaSorting: [],
                    "columns": [
                        {"data": "PERIOD"},
                        {"data": "REGION"},
                        {"data": "PERIOD_YEAR"},
                        {"data": "PERIOD_MONTH"},
                        {"data": "CURRENCY"},
                        {"data": "USERID"},
                        {"data": "ENTRY_TS"},
                        {"data": "SBU"},
                        {"data": "SUBREGION"},
                        {"data": "FIX_PRE_MAN_COST"},
                        {"data": "SHIP_COST"},
                        {"data": "SELL_COST"},
                        {"data": "DIFF_ACT_PRE_MAN_COST"},
                        {"data": "IDLE_EQUIP_COST"},
                        {"data": "RD_COST"},
                        {"data": "ADMIN_COST_BU"},
                        {"data": "ADMIN_COST_OD"},
                        {"data": "ADMIN_COST_COMPANY"},
                        {"data": "OTHER_OP_COST_BU"},
                        {"data": "OTHER_OP_COST_OD"},
                        {"data": "OTHER_OP_COST_COMPANY"},
                        {"data": "SPEC_ITEMS"},
                        {"data": "PROVISIONS"},
                        {"data": "CURRENCY_GAINS"},
                        {"data": "VAL_ADJUST_INVENTORIES"},
                        {"data": "OTHER_FIX_COST"},
                        {"data": "DEPRECIATION"},
                        {"data": "CAP_COST"},
                        {"data": "EQUITY_INCOME"},
                        {"data": "TOPDOWN_ADJUST_FIX_COSTS"},
                        {"data": "PLAN_PERIOD"},
                        {"data": "PLAN_YEAR"},
                        {"data": "PLAN_HALF_YEAR"},
                        {"data": "PLAN_QUARTER"},
                        {"data": "PLAN_MONTH"},
                        {"data": "STATUS"},
                        {"data": "USERCOMMENT"},
                        {"data": "ENTRY_TYPE"},
                    ]
                });
            }
            console.log(forecastSalesTable + ' am Ende der loadTable');
            return forecastSalesTable
        });

    }

    var indexTable = $('#KpiTable').DataTable({
        "ajax": {
            "url": "emptyindex.json",
            "dataSrc": ""
        },
        "info": false,
        aaSorting: [],
        "lengthMenu": [ [50, 100, 500, 1000, 5000, 10000, -1], [50, 100, 500, 1000, 5000, 10000,  "All"] ],
        "columns": [
            {"data": "ENTRY_TYPE"},
            {"data": "REGION"},
            {"data": "SUBREGION"},
            {"data": "SBU"},
            {"data": "PRODUCT_MAIN_GROUP"},
            {"data": "SALES_TYPE"},
            {"data": "FC_TYPE"},
            {"data": "ORDER_NUMBER"},
            {"data": "KPI"},
            {"data": "UNIT"},
            {"data": "M01"},
            {"data": "M02"},
            {"data": "M03"},
            {"data": "M04"},
            {"data": "M05"},
            {"data": "M06"},
            {"data": "M07"},
            {"data": "M08"},
            {"data": "M09"},
            {"data": "M10"},
            {"data": "M11"},
            {"data": "M12"},
            {"data": "M13"},
            {"data": "M14"},
            {"data": "M15"},
            {"data": "M16"},
            {"data": "M17"},
            {"data": "M18"},
            {"data": "BJ2"},
            {"data": "BJ3"},
            {"data": "BJ4"},
            {"data": "Edit"}
        ],
        "columnDefs": [{
            "data": null,
            "targets": -1,
            "defaultContent": '<button type="button" class="btn btn-primary" >Show Data!</button>'
        }]
    });

    $('#KpiTable tbody').on('click', 'button', function () {
        var data = indexTable.row($(this).parents('tr')).data();
        planYear = document.getElementById('datepicker_planYear').value;
        backend = document.getElementById('backendServer').value;
        currency = document.getElementById('currency').value;
        period = document.getElementById('datepicker_period').value;
        keyspace = document.getElementById('keyspace').value;
        endpointPath = '/fourschlag/api/' + keyspace + '/' ;
        if (data.ENTRY_TYPE == 'budget'){
            if (data.FC_TYPE == 'sales') {

                salesurl = endpointScheme + backend + endpointPath + 'sales/' + 'product_main_group/' + data.PRODUCT_MAIN_GROUP + '/region/'
                    + data.REGION + '/sales_type/' + data.SALES_TYPE + '/entry_type/budget' +
                    '/plan_year/' + planYear;

                openNewTab(saleshtmlurl);
                loadTable(data.FC_TYPE, salesurl);

            }
            if (data.FC_TYPE == 'fixed costs') {

                fixedcosturl = endpointScheme + backend + endpointPath + 'fixedcosts/' + 'sbu/' + data.SBU + '/subregion/'
                    + data.SUBREGION +  '/entry_type/budget' +  '/plan_year/' + planYear;

                openNewTab(fixedcostshtmlurl);
                loadTable(data.FC_TYPE, fixedcosturl)

            }
        }
        else {
            if (data.FC_TYPE == 'sales') {


                salesurl = endpointScheme + backend + endpointPath + 'sales/' + 'product_main_group/' + data.PRODUCT_MAIN_GROUP + '/region/'
                    + data.REGION + '/period/' + period + '/sales_type/' + data.SALES_TYPE + '/entry_type/forecast' +
                    '/plan_year/' + planYear;

                openNewTab(saleshtmlurl);
                console.log(forecastSalesTable + ' vor dem Methondenaufruf');
                var test = loadTable(data.FC_TYPE, salesurl);
                console.log(forecastSalesTable + ' nach dem Methondenaufruf');
                console.log(test);



            }
            if (data.FC_TYPE == 'fixed costs') {

                fixedcosturl = endpointScheme + backend + endpointPath + 'fixedcosts/' + 'sbu/' + data.SBU + '/subregion/'
                    + data.SUBREGION + '/period/' + period + '/entry_type/forecast' +
                    '/plan_year/' + planYear;

                openNewTab(fixedcostshtmlurl);
                loadTable(data.FC_TYPE, fixedcosturl)

            }
        }
    });

    $('#startbutton').on('click', function () {

        planYear = document.getElementById('datepicker_planYear').value;
        backend = document.getElementById('backendServer').value;
        kpiType = document.getElementById('kpiType').value;
        currency = document.getElementById('currency').value;
        period = document.getElementById('datepicker_period').value;
        keyspace = document.getElementById('keyspace').value;
        endpointPath = '/fourschlag/api/' + keyspace + '/forecast/' ;
        var mainPath = endpointScheme + backend + endpointPath + 'period/' + period + '/planyear/' + planYear + '/currency/' + currency + '/';

        if (kpiType == 'all') {
            indexTable.ajax.url(mainPath).load();
        }
        if (kpiType == 'sales' || kpiType == 'fixedcosts') {
            var urlother = mainPath + kpiType;
            indexTable.ajax.url(urlother).load();
        }

    });

        $('#ForecastSalesTable').on('click', 'tr', function() {
        $('#SalesModal').modal('show');
        var oData = forecastSalesTable.row(this).data();
        console.log(oData);
        $('#period').val(oData["PERIOD"]);
        $('#region').val(oData["REGION"]);
        $('#periodYear').val(oData["PERIDO_YEAR"]);
        $('#periodMonth').val(oData["PERIOD_MONTH"]);
        $('#currency').val(oData["CURRENCY"]);
        $('#userid').val(oData["USERID"]);
        $('#entryts').val(oData["ENTRY_TS"]);
        $('#productMainGroup').val(oData["PRODUCT_MAIN_GROUP"]);
        $('#salesType').val(oData["SALES_TYPE"]);
        $('#salesVolumes').val(oData["SALES_VOLUMES"]);
        $('#netSales').val(oData["NET_SALES"]);
        $('#cm1').val(oData["CM1"]);
        $('#topdownSalesVolumes').val(oData["TOPDOWN_ADJUST_SALES_VOLUMES"]);
        $('#topdownNetSales').val(oData["TOPDOWN_ADJUST_NET_SALES"]);
        $('#topdownCm1').val(oData["TOPDOWN_ADJUST_CM1"]);
        $('#planPeriod').val(oData["PLAN_PERIOD"]);
        $('#planYear').val(oData["PLAN_YEAR"]);
        $('#planHalfYear').val(oData["PLAN_HALF_YEAR"]);
        $('#planQuarter').val(oData["PLAN_QUARTER"]);
        $('#planMonth').val(oData["PLAN_MONTH"]);
        $('#entryType').val(oData["ENTRY_TYPE"]);
        $('#status').val(oData["STATUS"]);
        $('#usercomment').val(oData["USERCOMMENT"]);
    });



}, false);



