/**
 * Created by katharinaspinner on 05.12.16.
 */

$(document).ready(function() {
    $('#example').DataTable( {
        "ajax": {
            "url": "fourschlag/api/forecast/"
        },
        "columns": [
            { "data": "PERIOD" },
            { "data": "REGION" },
            { "data": "PERIOD_YEAR" },
            { "data": "PERIOD_MONTH" },
            { "data": "CURRENCY" },
            { "data": "USERID" }
        ]
    } );
} );
/*
$(document).ready(function() {
    $('#example').dataTable({
        "ajax": function (data, callback, settings) {
            callback(
                JSON.parse(localStorage.getItem('test.json'))
            );
        },
        "columns": [
            {"data": "PERIOD"},
            {"data": "REGION"},
            {"data": "PERIOD_YEAR"},
            {"data": "PERIOD_MONTH"},
            {"data": "CURRENCY"},
            {"data": "USERID"}
        ]
    });
});*/
