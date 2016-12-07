/**
 * Created by katharinaspinner on 05.12.16.
 */



window.addEventListener('load', function(){

    var table = $('#KpiTable').DataTable({
        "ajax": {
            "url":"test.json",
            "dataSrc": ""
        },
        "info": false,
        aaSorting: [],
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
        "columnDefs": [ {
            "data": null,
            "targets": -1,
            "defaultContent": "<button>Click!</button>"
        } ]
    });
    
    const startbutton = document.getElementById('startbutton');

    $('#KpiTable tbody').on( 'click', 'button', function () {
        var data = table.row( $(this).parents('tr') ).data();
        const endpointScheme = 'http://';
        const planYear = document.getElementById('datepicker_planYear').value;
        const backend = document.getElementById('backendServer').value;
        const currency = document.getElementById('currency').value;
        const period = document.getElementById('datepicker_period').value;
        const endpointPath = '/fourschlag/api/';
        if (data.FC_TYPE == 'sales'){
            table
                .clear()
                .draw();
            console.log(data.FC_TYPE + ' ich bin sales')
            var urlsales = endpointScheme + backend + endpointPath + 'sales/' + 'product_main_group/' + data.PRODUCT_MAIN_GROUP + '/region/'
            + data.REGION + '/period/' + period + '/sales_type/' + data.SALES_TYPE  + '/entry_type/' + data.ENTRY_TYPE + ''
            table.ajax.url( urlsales ).load();
        }
        if (data.FC_TYPE == 'fixed costs'){
            table
                .clear()
                .draw();
            console.log(data.FC_TYPE + ' ich bin fixed costs')
            var urlfixedcosts =
            table.ajax.url( urlfixedcosts ).load();
        }
    } );

    startbutton.addEventListener('click', function(){
        const endpointScheme = 'http://'
        const planYear = document.getElementById('datepicker_planYear').value;
        const backend = document.getElementById('backendServer').value;
        const kpiType = document.getElementById('kpiType').value;
        const currency = document.getElementById('currency').value;
        const period = document.getElementById('datepicker_period').value;
        const endpointPath = '/fourschlag/api/forecast/';
        if (kpiType == 'all'){
            var urlall = endpointScheme + backend + endpointPath + currency + '/' + planYear + '/' + period + '/';
            table.ajax.url( urlall ).load();
        }
        if(kpiType == 'sales' || kpiType == 'fixedcosts') {
            var urlother = endpointScheme + backend + endpointPath + currency + '/' + planYear + '/' + period + '/' + kpiType;
            table.ajax.url( urlother ).load();
        }

    }, false);
}, false);



