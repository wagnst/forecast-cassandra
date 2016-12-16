/**
 * Created by katharinaspinner on 05.12.16.
 */

const endpointScheme = 'http://';
const fixedcostshtmlurl = 'forecastfixedcosts.html';
const saleshtmlurl = 'forecastsales.html';
var planYear;
var currency;
var period;
var keyspace;
var endpointPath = '/fourschlag/api/';
var kpiType;
var newTab;
var salesurl;
var fixedcosturl;
var query;
var backend;
var region;
var dropdownMisc = '/fourschlag/api/misc/get/';
const sucessmessageInsert = 'Data was successfully transmitted.';
const sucessmessageModal = 'Data was successfully changed.';
const errormessage = 'Error, bad parameters.';
const errormessageDropdown = "Error, dropdown parameters aren't available!";

window.addEventListener('load', function () {

    backend = $(location).attr('host');

    if (document.getElementById("backendServer")){
        document.getElementById("backendServer").value = backend;
    }

    query = window.location.search.substring(1);

    function openNewTab(url) {
        newTab = window.open(url);
        if (newTab) {
            newTab.focus();

        } else {
            //Browser has blocked it
            alert('Please allow popups for this website');
        }
    }


    var indexTable = $('#KpiTable').DataTable({
        "info": false,
        aaSorting: [],
        "lengthMenu": [[50, 100, 500, 1000, 5000, 10000, -1], [50, 100, 500, 1000, 5000, 10000, "All"]],
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
            "defaultContent": '<button type="button" class="btn btn-primary" >Drilldown</button>'
        }]
    });


    var forecastSalesTable = $('#ForecastSalesTable').DataTable({
        "ajax": {
            "url": endpointScheme + query,
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

    var forecastFixedCostsTable = $('#ForecastFixedcostsTable').DataTable({
        "ajax": {
            "url": endpointScheme + query,
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

    $('#KpiTable tbody').on('click', 'button', function () {
        var data = indexTable.row($(this).parents('tr')).data();
        planYear = document.getElementById('datepicker_planYear').value;
        backend = document.getElementById('backendServer').value;
        currency = document.getElementById('currency').value;
        period = document.getElementById('datepicker_period').value;
        keyspace = document.getElementById('keyspace').value;
        endpointPath = '/fourschlag/api/' + keyspace + '/';
        if (data.ENTRY_TYPE == 'budget') {
            if (data.FC_TYPE == 'sales') {

                salesurl = '?' + backend + endpointPath + 'sales/' + 'product_main_group/' + data.PRODUCT_MAIN_GROUP + '/region/'
                    + data.REGION + '/sales_type/' + data.SALES_TYPE + '/entry_type/budget' +
                    '/plan_year/' + planYear;

                openNewTab(saleshtmlurl + salesurl);

            }
            if (data.FC_TYPE == 'fixed costs') {

                fixedcosturl = '?' + backend + endpointPath + 'fixedcosts/' + 'sbu/' + data.SBU + '/subregion/'
                    + data.SUBREGION + '/entry_type/budget' + '/plan_year/' + planYear;

                openNewTab(fixedcostshtmlurl + fixedcosturl);

            }
        }
        else {
            if (data.FC_TYPE == 'sales') {

                salesurl = '?' + backend + endpointPath + 'sales/' + 'product_main_group/' + data.PRODUCT_MAIN_GROUP + '/region/'
                    + data.REGION + '/period/' + period + '/sales_type/' + data.SALES_TYPE + '/entry_type/forecast' +
                    '/plan_year/' + planYear;

                openNewTab(saleshtmlurl + salesurl);

            }
            if (data.FC_TYPE == 'fixed costs') {

                fixedcosturl = '?' + backend + endpointPath + 'fixedcosts/' + 'sbu/' + data.SBU + '/subregion/'
                    + data.SUBREGION + '/period/' + period + '/entry_type/forecast' +
                    '/plan_year/' + planYear;

                openNewTab(fixedcostshtmlurl + fixedcosturl);

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
        endpointPath = '/fourschlag/api/' + keyspace + '/forecast/';
        var mainPath = endpointScheme + backend + endpointPath + 'period/' + period + '/planyear/' + planYear + '/currency/' + currency + '/';

        if (kpiType == 'all') {
            $.ajax({
                url: mainPath,
                type: "GET"
            }).done(function (result) {
                indexTable.clear().draw();
                indexTable.rows.add(result).draw();
            }).fail(function (jqXHR, textStatus, errorThrown) {
                alert("Data can't be loaded!")
            });
        }
        if (kpiType == 'sales' || kpiType == 'fixedcosts') {
            var urlother = mainPath + kpiType;
            $.ajax({
                url: urlother,
                type: "GET"
            }).done(function (result) {
                indexTable.clear().draw();
                indexTable.rows.add(result).draw();
            }).fail(function (jqXHR, textStatus, errorThrown) {
                alert("Data can't be loaded!")
            });
        }

    });


// Dropdowns

    $(function(){
        if($('body').is('.insert')){

            //on change
            $('#region').change(function () {
                region = document.getElementById('region').value;
                $.getJSON( endpointScheme + backend + endpointPath + keyspace + '/org_region/get/subregions/region/' + region, function(data){
                    $("#subRegion").empty();
                    $.each(data ,function(key, value){
                        var option = $('<option />').val(value).text(value);
                        $("#subRegion").append(option);
                    });

                }) .fail(function() {
                    alert( errormessageDropdown );
                });
            });

            $('#keyspace').change(function () {
                keyspace = document.getElementById('keyspace').value;
                $.getJSON( endpointScheme + backend + endpointPath + keyspace + '/org_region/get/regions', function(data){
                    $("#region").empty();
                    $.each(data ,function(key, value){
                        var option = $('<option />').val(value).text(value);
                        $("#region").append(option);
                    });

                }) .fail(function() {
                    alert( errormessageDropdown );
                });
                $.getJSON( endpointScheme + backend + endpointPath + keyspace + '/org_region/get/product_main_groups', function(data){
                    $("#productMainGroup").empty();
                    $.each(data ,function(key, value){
                        var option = $('<option />').val(value).text(value);
                        $("#productMainGroup").append(option);
                    });

                }) .fail(function() {
                    alert( errormessageDropdown );
                });
                $.getJSON( endpointScheme + backend + endpointPath + keyspace + '/org_region/get/sbus', function(data){
                    $("#sbu").empty();
                    $.each(data ,function(key, value){
                        var option = $('<option />').val(value).text(value);
                        $("#sbu").append(option);
                    });

                }) .fail(function() {
                    alert( errormessageDropdown );
                });
            });



            // restliche
            $.getJSON( endpointScheme + backend + dropdownMisc + 'sales_types', function(data){
                $.each(data ,function(key, value){
                    var option = $('<option />').val(value).text(value);
                    $("#salesType").append(option);
                });

            }) .fail(function() {
                alert( errormessageDropdown );
            });

            $.getJSON( endpointScheme + backend + dropdownMisc + 'entry_types', function(data){
                $.each(data ,function(key, value){
                    var option = $('<option />').val(value).text(value);
                    $("#entryType").append(option);
                });

            }) .fail(function() {
                alert( errormessageDropdown );
            });

            $.getJSON( endpointScheme + backend + dropdownMisc + 'currencies', function(data){
                $.each(data ,function(key, value){
                    var option = $('<option />').val(value).text(value);
                    $("#currency").append(option);
                });

            }) .fail(function() {
                alert( errormessageDropdown );
            });

            $.getJSON( endpointScheme + backend + dropdownMisc + 'keyspaces', function(data){
                $.each(data ,function(key, value){
                    var option = $('<option />').val(value).text(value);
                    $("#keyspace").append(option);
                });

            })  .fail(function() {
                alert( errormessageDropdown );
            })  .done(function () {
                keyspace = document.getElementById('keyspace').value;
                $.getJSON( endpointScheme + backend + endpointPath + keyspace + '/org_region/get/regions', function(data){
                    $.each(data ,function(key, value){
                        var option = $('<option />').val(value).text(value);
                        $("#region").append(option);
                    });

                }) .fail(function() {
                    alert( errormessageDropdown );
                }) .done(function () {
                    region = document.getElementById('region').value;
                    $.getJSON( endpointScheme + backend + endpointPath + keyspace + '/org_region/get/subregions/region/' + region, function(data){
                        $("#subRegion").empty();
                        $.each(data ,function(key, value){
                            var option = $('<option />').val(value).text(value);
                            $("#subRegion").append(option);
                        });

                    }) .fail(function() {
                        alert( errormessageDropdown );
                    });
                });

                $.getJSON( endpointScheme + backend + endpointPath + keyspace + '/org_region/get/product_main_groups', function(data){
                    $("#productMainGroup").empty();
                    $.each(data ,function(key, value){
                        var option = $('<option />').val(value).text(value);
                        $("#productMainGroup").append(option);
                    });

                }) .fail(function() {
                    alert( errormessageDropdown );
                });

                $.getJSON( endpointScheme + backend + endpointPath + keyspace + '/org_region/get/sbus', function(data){
                    $("#sbu").empty();
                    $.each(data ,function(key, value){
                        var option = $('<option />').val(value).text(value);
                        $("#sbu").append(option);
                    });

                }) .fail(function() {
                    alert( errormessageDropdown );
                });

            });

        }
    });


    $(function(){
        if($('body').is('.index')){
            $.getJSON( endpointScheme + backend + dropdownMisc + 'keyspaces', function(data){
                $.each(data ,function(key, value){
                    var option = $('<option />').val(value).text(value);
                    $("#keyspace").append(option);
                });

            })  .fail(function() {
                alert( errormessageDropdown );
            })
        }
    });




//Post Forms

    $('#salesInsertForm').submit(function (e) {
        keyspace = document.getElementById('keyspace').value;
        endpointPath = '/fourschlag/api/' + keyspace ;

        $.ajax({
            type: "POST",
            url: endpointScheme + endpointPath + "/sales/",
            data: $('#salesInsertForm').serialize(),
            statusCode: {
                200: function () {
                    alert(sucessmessageInsert);
                },
                400: function () {
                    alert(errormessage);
                }

            }

        });
        e.preventDefault();
    });


    $('#fixedcostsInsertForm').submit(function (e) {
        keyspace = document.getElementById('keyspace').value;
        endpointPath = '/fourschlag/api/' + keyspace ;

        $.ajax({
            type: "POST",
            url: endpointScheme + endpointPath + "/fixedcosts/",
            data: $('#fixedcostsInsertForm').serialize(),
            statusCode: {
                200: function () {
                    alert(sucessmessageInsert);
                },
                400: function () {
                    alert(errormessage);
                }

            }

        });
        e.preventDefault();
    });

    $('#fixedcostsModalForm').submit(function (e) {

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/fourschlag/api/TEST/fixedcosts/",
            data: $('#fixedcostsModalForm').serialize(),
            statusCode: {
                200: function () {
                    alert(sucessmessageModal);
                    location.reload();
                },
                400: function () {
                    alert(errormessage);
                }

            }

        });
        e.preventDefault();
    });

    $('#salesModalForm').submit(function (e) {


        $.ajax({
            type: "POST",
            url: "http://localhost:8080/fourschlag/api/TEST/sales/",
            data: $('#salesModalForm').serialize(),
            statusCode: {
                200: function () {
                    alert(sucessmessageModal);
                    location.reload();
                },
                400: function () {
                    alert(errormessage);
                }

            }

        });
        e.preventDefault();
    });

// Modal

    $('#ForecastSalesTable').on('click', 'tr', function () {
        $('#SalesModal').modal('show');
        var oData = forecastSalesTable.row(this).data();
        $('#period').val(oData["PERIOD"]);
        $('#region').val(oData["REGION"]);
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
        $('#entryType').val(oData["ENTRY_TYPE"]);
        $('#status').val(oData["STATUS"]);
        $('#usercomment').val(oData["USERCOMMENT"]);
    });

    $('#ForecastFixedcostsTable').on('click', 'tr', function () {
        $('#FixedCostModal').modal('show');
        var oData = forecastFixedCostsTable.row(this).data();
        $('#period').val(oData["PERIOD"]);
        $('#region').val(oData["REGION"]);
        $('#currency').val(oData["CURRENCY"]);
        $('#userid').val(oData["USERID"]);
        $('#entryts').val(oData["ENTRY_TS"]);
        $('#sbu').val(oData["SBU"]);
        $('#subRegion').val(oData["SUBREGION"]);
        $('#fixPreManCost').val(oData["FIX_PRE_MAN_COST"]);
        $('#shipCost').val(oData["SHIP_COST"]);
        $('#sellCost').val(oData["SELL_COST"]);
        $('#diffActPreManCost').val(oData["DIFF_ACT_PRE_MAN_COST"]);
        $('#idleEquipCost').val(oData["IDLE_EQUIP_COST"]);
        $('#rdCost').val(oData["RD_COST"]);
        $('#adminCostBu').val(oData["ADMIN_COST_BU"]);
        $('#adminCostOd').val(oData["ADMIN_COST_OD"]);
        $('#adminCostCompany').val(oData["ADMIN_COST_COMPANY"]);
        $('#otherOpCostBu').val(oData["OTHER_OP_COST_BU"]);
        $('#otherOpCostOd').val(oData["OTHER_OP_COST_OD"]);
        $('#otherOpCostCompany').val(oData["OTHER_OP_COST_COMPANY"]);
        $('#specItems').val(oData["SPEC_ITEMS"]);
        $('#provisions').val(oData["OTHER_OP_COST_COMPANY"]);
        $('#currencyGains').val(oData["CURRENCY_GAINS"]);
        $('#valAdjustInventories').val(oData["VAL_ADJUST_INVENTORIES"]);
        $('#otherFixCost').val(oData["OTHER_FIX_COST"]);
        $('#depreciation').val(oData["DEPRECIATION"]);
        $('#capCost').val(oData["CAP_COST"]);
        $('#equityIncome').val(oData["EQUITY_INCOME"]);
        $('#topdownAdjustFixCosts').val(oData["TOPDOWN_ADJUST_FIX_COSTS"]);
        $('#planPeriod').val(oData["PLAN_PERIOD"]);
        $('#entryType').val(oData["ENTRY_TYPE"]);
        $('#status').val(oData["STATUS"]);
        $('#usercomment').val(oData["USERCOMMENT"]);
    });


}, false);



