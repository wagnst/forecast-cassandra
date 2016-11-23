# coding=utf-8
# Anleitung:
# xlrd muss installiert werden!
# der Ordner csv_output muss erstellt werden
# todo Ordner csv_output nur temporär anlegen
# todo Funktionalität Tabelle erst löschen, dann neu anlegen
# todo Funktionalität Keyspace löschen
# todo verbose
# todo timer einbauen um zu schauen was wie lange dauert

import argparse
import csv
import os
import xlrd
import subprocess


# Funktion für Parser
def inputpath_and_tablename(s):
    try:
        x, y = map(str, s.split(','))
        return x, y
    except:
        raise argparse.ArgumentTypeError("Error: format must be inputpath,tablename")

# Parser
parser = argparse.ArgumentParser()
parser.add_argument('--verbose', '-v', action='count', help='verbose level')
parser.add_argument('--password', '-p', help='password for the database')
parser.add_argument('--user', '-u', help='user for the database')
parser.add_argument('--ip', '-n', required=True, help='IP Address of the database')
parser.add_argument('--keyspace', '-k', help='keyspace name for creation or keyspace to add data')
parser.add_argument('--inputfile', '-i', action='append', type=inputpath_and_tablename, nargs='*',
                    help='Input files for tables. Format inputpath,tablename')

args = parser.parse_args()

# todo evaluieren ob Konstanten auch in ein Configfile ausgelagert werden könnten
# Konstanten, die je nachdem wo das Script liegt befüllt werden müssen:
PATH_CSV_OUTPUT = '/Users/katharinaspinner/IdeaProjects/fourschlag/importer/csv_output/'
CQLSH_BINARY = "/usr/local/bin/cqlsh"

# Konstanten
INTERACTIVE_MODE = "-e"
USED_KEYSPACE = '-k'

ORG_STRUCTURE_PARAMS = 'bu, sbu, product_main_group'
REGIONS_PARAMS = 'region, subregion'
EXCHANGE_RATE_PARAMS = 'period, period_year, period_month, from_currency, to_currency, rate, userid, entry_ts'
ACTUAL_SALES_PARAMS = 'sales_volumes, net_sales, cm1, product_main_group, region, sbu, sales_type, data_source, period,' \
                      'period_year, period_half_year, period_quarter, period_month, currency, userid, entry_ts'
FORECAST_SALES_PARAMS = 'sales_volumes, net_sales, cm1, topdown_adjust_sales_volumes, topdown_adjust_net_sales,' \
                        'topdown_adjust_cm1, product_main_group, region, sales_type, entry_type, period, period_year, ' \
                        'period_month, plan_period, plan_year, plan_half_year, plan_quarter, plan_month, currency, status, ' \
                        'usercomment, userid, entry_ts'
ACTUAL_FIXED_COSTS_PARAMS = "fix_pre_man_cost, ship_cost, sell_cost, diff_act_pre_man_cost, idle_equip_cost, rd_cost, " \
                            "admin_cost_bu, admin_cost_od, other_op_cost_bu, other_op_cost_od, spec_items, provisions, " \
                            "currency_gains, val_adjust_inventories, other_fix_cost, depreciation, cap_cost, sbu, " \
                            "region, subregion, period, period_year, period_half_year, period_quarter, period_month, " \
                            "currency, userid, entry_ts, admin_cost_company, other_op_cost_company, equity_income"
FORECAST_FIXED_COSTS_PARAMS = "fix_pre_man_cost, ship_cost, sell_cost, diff_act_pre_man_cost, idle_equip_cost, " \
                              "rd_cost, admin_cost_bu, admin_cost_od, other_op_cost_bu, other_op_cost_od, spec_items, " \
                              "provisions, currency_gains, val_adjust_inventories, other_fix_cost, " \
                              "topdown_adjust_fix_costs, depreciation, cap_cost, sbu, region, subregion, " \
                              "entry_type, period, period_year, period_month, plan_period, plan_year, plan_half_year, " \
                              "plan_quarter, plan_month, currency, status, usercomment, userid, entry_ts, " \
                              "admin_cost_company, other_op_cost_company, equity_income"

ORG_STRUCTURE_TABLE_CREATION = '(BU varchar, SBU varchar, PRODUCT_MAIN_GROUP varchar PRIMARY KEY);'
REGIONS_TABLE_CREATION = '(REGION varchar, SUBREGION varchar PRIMARY KEY)'
EXCHANGE_RATE_TABLE_CREATION = '(PERIOD int, PERIOD_YEAR int, PERIOD_MONTH int, FROM_CURRENCY varchar, ' \
                               'TO_CURRENCY varchar, RATE double, USERID varchar, ENTRY_TS varchar, ' \
                               'PRIMARY KEY (PERIOD, FROM_CURRENCY, TO_CURRENCY));'
ACTUAL_SALES_TABLE_CREATION = '(SALES_VOLUMES double, NET_SALES double, CM1 double, PRODUCT_MAIN_GROUP varchar, ' \
                              'REGION varchar, SBU varchar, SALES_TYPE varchar, DATA_SOURCE varchar, PERIOD int, ' \
                              'PERIOD_YEAR int, PERIOD_HALF_YEAR int, PERIOD_QUARTER int, PERIOD_MONTH int, ' \
                              'CURRENCY varchar, USERID varchar, ENTRY_TS varchar, ' \
                              'PRIMARY KEY ((PRODUCT_MAIN_GROUP, Region), PERIOD, SALES_TYPE, DATA_SOURCE));'
FORECAST_SALES_TABLE_CREATION = 'CREATE COLUMNFAMILY forecast_sales(SALES_VOLUMES double, NET_SALES double, ' \
                                'CM1 double, PRODUCT_MAIN_GROUP varchar, REGION varchar, SALES_TYPE varchar, ' \
                                'PERIOD int, PERIOD_YEAR int, PERIOD_MONTH int, CURRENCY varchar, USERID varchar, ' \
                                'ENTRY_TS varchar, TOPDOWN_ADJUST_SALES_VOLUMES double, ' \
                                'TOPDOWN_ADJUST_NET_SALES double, TOPDOWN_ADJUST_CM1 double, PLAN_PERIOD int, ' \
                                'PLAN_MONTH int, PLAN_YEAR int, PLAN_HALF_YEAR int, PLAN_QUARTER int, STATUS varchar, ' \
                                'USERCOMMENT text, ENTRY_TYPE varchar, PRIMARY KEY ((PRODUCT_MAIN_GROUP, REGION), ' \
                                'PERIOD, SALES_TYPE, PLAN_PERIOD, ENTRY_TYPE));'
ACTUAL_FIXED_COSTS_TABLE_CREATION = '(FIX_PRE_MAN_COST double, SHIP_COST double, SELL_COST double, ' \
                                    'DIFF_ACT_PRE_MAN_COST double, IDLE_EQUIP_COST double, RD_COST double, ' \
                                    'ADMIN_COST_BU double, ADMIN_COST_OD double, OTHER_OP_COST_BU double, ' \
                                    'OTHER_OP_COST_OD double, SPEC_ITEMS double, PROVISIONS double, ' \
                                    'CURRENCY_GAINS double, VAL_ADJUST_INVENTORIES double, OTHER_FIX_COST double, ' \
                                    'DEPRECIATION double, CAP_COST double, SBU varchar, REGION varchar, ' \
                                    'SUBREGION varchar, PERIOD int, PERIOD_YEAR int, PERIOD_HALF_YEAR int, ' \
                                    'PERIOD_QUARTER int, PERIOD_MONTH int, CURRENCY varchar, USERID varchar, ' \
                                    'ENTRY_TS varchar, ADMIN_COST_COMPANY double, OTHER_OP_COST_COMPANY double, ' \
                                    'EQUITY_INCOME double, PRIMARY KEY (SBU, PERIOD, SUBREGION));'
FORECAST_FIXED_COSTS_TABLE_CREATION = '(FIX_PRE_MAN_COST double, SHIP_COST double, SELL_COST double, ' \
                                'DIFF_ACT_PRE_MAN_COST double, IDLE_EQUIP_COST double, RD_COST double, ' \
                                'ADMIN_COST_BU double, ADMIN_COST_OD double, OTHER_OP_COST_BU double, ' \
                                'OTHER_OP_COST_OD double, SPEC_ITEMS double, PROVISIONS double, CURRENCY_GAINS double, ' \
                                'VAL_ADJUST_INVENTORIES double, OTHER_FIX_COST double, TOPDOWN_ADJUST_FIX_COSTS double, ' \
                                'DEPRECIATION double, CAP_COST double, SBU varchar, REGION varchar, SUBREGION varchar, ' \
                                'ENTRY_TYPE varchar, PERIOD int, PERIOD_YEAR int, PERIOD_MONTH int, PLAN_PERIOD int, ' \
                                'PLAN_YEAR int, PLAN_HALF_YEAR int, PLAN_QUARTER int, PLAN_MONTH int, CURRENCY varchar, ' \
                                'STATUS varchar, USERCOMMENT text, USERID varchar, ENTRY_TS varchar, ' \
                                'ADMIN_COST_COMPANY double, OTHER_OP_COST_COMPANY double, EQUITY_INCOME double, ' \
                                'PRIMARY KEY(SBU, SUBREGION, PERIOD, ENTRY_TYPE, PLAN_PERIOD));'

CREATE_KEY_SPACE = "CREATE KEYSPACE IF NOT EXISTS"
CREATE_KEY_SPACE_SUB = "WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };"
CREATE_TABLE = "CREATE TABLE IF NOT EXISTS"
IMPORT_FILE = 'COPY'
IMPORT_FILE_SUB = 'FROM'
IMPORT_FILE_SUB_SUB = 'WITH HEADER = TRUE;'

# Allgemeine Parameter
table_params = {
    'ORG_STRUCTURE': ORG_STRUCTURE_PARAMS,
    'REGIONS': REGIONS_PARAMS,
    'EXCHANGE_RATE': EXCHANGE_RATE_PARAMS,
    'ACTUAL_SALES': ACTUAL_SALES_PARAMS,
    'FORECAST_SALES': FORECAST_SALES_PARAMS,
    'FORECAST_FIXED_COSTS': FORECAST_FIXED_COSTS_PARAMS,
    'ACTUAL_FIXED_COSTS': ACTUAL_FIXED_COSTS_PARAMS,
}

table_creation_params = {
    'ORG_STRUCTURE': ORG_STRUCTURE_TABLE_CREATION,
    'REGIONS': REGIONS_TABLE_CREATION,
    'EXCHANGE_RATE': EXCHANGE_RATE_TABLE_CREATION,
    'ACTUAL_SALES': ACTUAL_SALES_TABLE_CREATION,
    'FORECAST_SALES': FORECAST_SALES_TABLE_CREATION,
    'FORECAST_FIXED_COSTS': FORECAST_FIXED_COSTS_TABLE_CREATION,
    'ACTUAL_FIXED_COSTS': ACTUAL_FIXED_COSTS_TABLE_CREATION,
}

firstlistelement_of_inputfiles = args.inputfile[0]


# Allgemeine Funktionen
def validate_path(s):
    try:
        os.stat(s)
        print "The path %s exists." % s
    except OSError:
        print "The path %s does not exist." % s
        exit('Canceled: path %s for import does not exist or is not valid (whitespaces or special characters)' % s)
    if s[-4:] == '.xls' or s[-5:] == '.xlsx':
        print "The filetyp is valid."
    else:
        exit('Wrong datatype. Please import .xls or .xlsx .')


def validate_csv(s, t):
    reader = csv.reader(open(PATH_CSV_OUTPUT + s, 'r'), delimiter=',')
    headerfromcsv = next(reader)
    validation = cmp(headerfromcsv, table_params[t].upper().split(', '))
    if validation == 0:
        return True
    else:
        print
        exit('Canceled: CSV Header from %s isn\'t valide!' % s)


# Quelle: http://stackoverflow.com/questions/9884353/xls-to-csv-converter/9884551#9884551
# Konvertiert eine xls oder xlsx zu einer csv
# Todo ist threading möglich?
def xls_to_csv(s, t):
    workbook = xlrd.open_workbook(s)
    all_worksheets = workbook.sheet_names()
    for worksheet_name in all_worksheets:
        worksheet = workbook.sheet_by_name(worksheet_name)
        csv_file = open(PATH_CSV_OUTPUT + t + '.csv', 'wb')
        wr = csv.writer(csv_file, quoting=csv.QUOTE_NONE)
        for rownum in xrange(worksheet.nrows):
            wr.writerow([unicode(entry).encode("utf-8") for entry in worksheet.row_values(rownum)])
        csv_file.close()


def clear_csvoutput():
    for the_file in os.listdir(PATH_CSV_OUTPUT):
        file_path = os.path.join(PATH_CSV_OUTPUT, the_file)
        try:
            if os.path.isfile(file_path):
                os.unlink(file_path)
        except Exception as e:
            print(e)


# todo validierung von tabellennamen
# todo user und password einbinden

def test_databaseconnection():
    connection_test_param = INTERACTIVE_MODE + 'EXIT'
    databaseconnection_exit_code = subprocess.call([CQLSH_BINARY, args.ip, connection_test_param])

    if databaseconnection_exit_code == 0:
        print 'Databaseconnection is runnig.'
    else:
        exit('Databaseconnection is not possible!')


def create_keyspace():
    temp_parameters = INTERACTIVE_MODE + ' ' + CREATE_KEY_SPACE + ' ' + args.keyspace + ' ' + CREATE_KEY_SPACE_SUB
    subprocess.call([CQLSH_BINARY, args.ip, temp_parameters])
    print 'Datatbase %s was created or already exists.' % args.keyspace


def create_table(s, t, u):
    temp_parameters = INTERACTIVE_MODE + ' ' + CREATE_TABLE + ' ' + s + '.' + t + u
    subprocess.call([CQLSH_BINARY, args.ip, temp_parameters])
    print 'Table %s was created or already exists.' % t


# Es darf kein newlineelement im string vor kommen.
def import_file(s, t, u, v):
    temp_parameters = INTERACTIVE_MODE + ' ' + IMPORT_FILE + ' ' + s + '.' + t + '(' + u + ') ' + IMPORT_FILE_SUB +\
                      ' \'' + v + '\' ' + IMPORT_FILE_SUB_SUB
    print temp_parameters
    subprocess.call([CQLSH_BINARY, args.ip, temp_parameters])


def transformation_and_validation():
    # Testet die Datenbankconnection
    test_databaseconnection()

    # Validiert den Pfad und den Filetyp
    for val in firstlistelement_of_inputfiles:
        first_value_from_tuple = val[0]
        validate_path(first_value_from_tuple)

    # Konvertiert das xlsx oder xls zu csv
    for val in firstlistelement_of_inputfiles:
        first_value_from_tuple = val[0]
        second_value_from_tuple = val[1]
        xls_to_csv(first_value_from_tuple, second_value_from_tuple)

    # Validiert die csv
    counter = 0
    for val in firstlistelement_of_inputfiles:
        csv_output_list = os.listdir(PATH_CSV_OUTPUT)
        second_value_from_tuple = val[1]
        validate_csv(csv_output_list[counter], second_value_from_tuple)
        counter += 1


def fileimport():
    create_keyspace()
    for val in firstlistelement_of_inputfiles:
        second_value_from_tuple = val[1]
        create_table(args.keyspace, second_value_from_tuple, table_creation_params[second_value_from_tuple])

    counter = 0
    for val in firstlistelement_of_inputfiles:
        csv_output_list = os.listdir(PATH_CSV_OUTPUT)
        second_value_from_tuple = val[1]
        csv_path = PATH_CSV_OUTPUT + csv_output_list[counter]
        import_file(args.keyspace, second_value_from_tuple, table_params[second_value_from_tuple], csv_path)
        counter += 1


def main():
    try:
        transformation_and_validation()
        fileimport()
    finally:
        clear_csvoutput()
        print 'cleaned up!'


# Aufruf von Main
if __name__ == '__main__':
    main()
