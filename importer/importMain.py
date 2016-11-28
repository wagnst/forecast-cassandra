# coding=utf-8
# Anleitung:
# Python 2.7 verwenden
# xlrd muss installiert werden!
# todo man könnte eventuell noch abfangen, dass man keine zwei gleichen Tabellen importieren kann
# todo evaluieren ob Konstanten auch in ein Configfile ausgelagert werden könnten

import argparse
import csv
import os
import xlrd
import subprocess
import time
import shutil
import multiprocessing
import json

with open('config.json') as json_data_file:
    config = json.load(json_data_file)

# Konstanten, die je nachdem wo das Script liegt befüllt werden müssen:
CQLSH = config['cqlsh_binary']

# Konstanten
INTERACTIVE_MODE = "-e"
USED_KEYSPACE = '-k'
USER = '--username='
PASSWORD = '--password='

CREATE_KEY_SPACE = "CREATE KEYSPACE IF NOT EXISTS"
CREATE_KEY_SPACE_SUB = "WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 }"
CREATE_TABLE = "CREATE TABLE IF NOT EXISTS"
IMPORT_FILE = 'COPY'
IMPORT_FILE_SUB = 'FROM'
IMPORT_FILE_SUB_SUB = 'WITH HEADER = TRUE;'
DELETE_KEYSPACE = 'DROP KEYSPACE IF EXISTS'
DELETE_TABLE = 'DROP TABLE IF EXISTS'

# Allgemeine Parameter

PATH_CSV_OUTPUT = os.path.dirname(__file__) + '/csv_output/'


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
parser.add_argument('--keyspace', '-k', required=True, help='keyspace name for creation or keyspace to add data')
parser.add_argument('--dtable', '-t', action='append', help='table witch should be deleted')
parser.add_argument('--dkeyspace', '-d', action="store_true", help='Keyspace should be deleted')
parser.add_argument('--inputfile', '-i', action='append', type=inputpath_and_tablename, nargs='*',
                    help='Input files for tables. Format inputpath,tablename. Following Tablenames are valid: %s'
                    % config['available_params'])

args = parser.parse_args()


# Allgemeine Funktionen
def check_verbose():
    if args.verbose is None:
        return 0
    elif args.verbose == 1:
        return 1
    else:
        return 2


def run_query(query):
    if args.user is None and args.password is None:
        return subprocess.call([CQLSH, args.ip, query])
    else:
        return subprocess.call(CQLSH + ' ' + args.ip + ' ' + query, shell=True)


def validate_path(file_path):
    try:
        os.stat(file_path)
        if check_verbose() >= 1:
            print("The path %s exists." % file_path)
    except OSError:
        print("The path %s does not exist." % file_path)
        exit('Canceled: path %s for import does not exist or is not valid (whitespaces or special characters)' % file_path)
    if file_path[-4:] == '.xls' or file_path[-5:] == '.xlsx':
        if check_verbose() >= 1:
            print("The filetyp is valid.")
    else:
        exit('Wrong datatype. Please import .xls or .xlsx .')


def validate_tablenames(tablename):
    try:
        config['table_params'][tablename]
    except KeyError:
        print('The tablename %s is not valid.' % tablename)
        exit('Canceled: please use the following tablenames: %s' % config['available_params'])


def validate_csv(name, reference_file):
    reader = csv.reader(open(PATH_CSV_OUTPUT + name, 'r'), delimiter=',')
    header_from_csv = next(reader)
    if header_from_csv == config['table_params'][reference_file].upper().split(', '):
        if check_verbose() == 1:
            print ('The header from %s.csv is valid.' % name)
        return True

    else:
        exit("Canceled: CSV Header from %s.csv isn't valide!" % name)


def validate_arguments():
    if args.user is None and args.password is not None:
        parser.error('-p password requires -u user! Use --help for more information.')
    if args.password is None and args.user is not None:
        parser.error('-u user requires -p password! Use --help for more information.')
    if args.inputfile is not None and args.keyspace is None:
        parser.error('-i inputfile requires -k keyspace!')
    if args.dtable is not None and args.keyspace is None:
        parser.error('-d dtable requires -k keyspace!')
    if args.inputfile is None and args.dtable is None and args.dkeyspace is None:
        parser.error('-i inputfile or -t dtable or -d dkeyspace required.')


# Quelle: http://stackoverflow.com/questions/9884353/xls-to-csv-converter/9884551#9884551
# Konvertiert eine xls oder xlsx zu einer csv

def xls_to_csv((file_path, tablename)):
    workbook = xlrd.open_workbook(file_path)
    all_worksheets = workbook.sheet_names()
    csv_file = open(PATH_CSV_OUTPUT + tablename + '.csv', 'wb')
    wr = csv.writer(csv_file, quoting=csv.QUOTE_NONE)
    for worksheet_name in all_worksheets:
        worksheet = workbook.sheet_by_name(worksheet_name)
        for rownum in range(worksheet.nrows):
            wr.writerow([unicode(entry).encode("utf-8") for entry in worksheet.row_values(rownum)])
        csv_file.close()
    if check_verbose() >= 1:
        print ('File %s was successfully converted to .csv' % file_path)


def test_databaseconnection():
    if args.user is None and args.password is None:
        connection_test_param = INTERACTIVE_MODE + 'EXIT'
        databaseconnection_exit_code = run_query(connection_test_param)
        if databaseconnection_exit_code == 0:
            if check_verbose() >= 1:
                print('Database connection to %s is running.' % args.ip)
        else:
            exit('Database connection to %s is not possible!' % args.ip)
    else:
        connection_test_param = USER + args.user + ' ' + PASSWORD + args.password + ' ' + INTERACTIVE_MODE + 'EXIT'
        databaseconnection_exit_code = run_query(connection_test_param)
        if databaseconnection_exit_code == 0:
            if check_verbose() >= 1:
                print('Database connection to %s is running.' % args.ip)
        else:
            exit('Database connection to %s is not possible!' % args.ip)


def create_keyspace():
    if args.user is None and args.password is None:
        temp_parameters = INTERACTIVE_MODE + ' ' + CREATE_KEY_SPACE + ' ' + args.keyspace + ' ' + CREATE_KEY_SPACE_SUB
        run_query(temp_parameters)
        if check_verbose() >= 1:
            print('Datatbase %s was created or already exists.' % args.keyspace)
    else:
        temp_parameters = USER + '"' + args.user + '"' + ' ' + PASSWORD + '"' + args.password + '"' + ' ' + \
                          INTERACTIVE_MODE + ' ' + '"' + CREATE_KEY_SPACE + ' ' + args.keyspace + ' ' + \
                          CREATE_KEY_SPACE_SUB + '"'
        run_query(temp_parameters)
        if check_verbose() >= 1:
            print('Datatbase %s was created or already exists.' % args.keyspace)


def delete_keyspace():
    if args.user is None and args.password is None:
        temp_parameters = INTERACTIVE_MODE + ' ' + DELETE_KEYSPACE + ' ' + args.keyspace
        run_query(temp_parameters)
        if check_verbose() >= 1:
            print('Keyspace %s was deleted.' % args.keyspace)
    else:
        temp_parameters = USER + '"' + args.user + '"' + ' ' + PASSWORD + '"' + args.password + '"' + ' ' + \
                          INTERACTIVE_MODE + ' ' + '"' + DELETE_KEYSPACE + ' ' + args.keyspace + '"'
        run_query(temp_parameters)
        if check_verbose() >= 1:
            print('Keyspace %s was deleted.' % args.keyspace)


def create_table(tablename, table_parameter):
    if args.user is None and args.password is None:
        temp_parameters = INTERACTIVE_MODE + ' ' + CREATE_TABLE + ' ' + args.keyspace + '.' + \
                          tablename + table_parameter
        run_query(temp_parameters)
        if check_verbose() >= 1:
            print('Table %s was created or already exists.' % tablename)
    else:
        temp_parameters = USER + '"' + args.user + '"' + ' ' + PASSWORD + '"' + args.password + '"' + ' ' + '"' \
                          + INTERACTIVE_MODE + ' ' + CREATE_TABLE + ' ' + args.keyspace + '.' + \
                          tablename + table_parameter + '"'
        run_query(temp_parameters)
        if check_verbose() >= 1:
            print('Table %s was created or already exists.' % tablename)


def delete_table(tablename):
    if args.user is None and args.password is None:
        temp_parameters = INTERACTIVE_MODE + ' ' + DELETE_TABLE + ' ' + args.keyspace + '.' + tablename
        run_query(temp_parameters)
        if check_verbose() >= 1:
            print('Table %s was deleted.' % tablename)
    else:
        temp_parameters = USER + '"' + args.user + '"' + ' ' + PASSWORD + '"' + args.password + '"' + ' ' + '"' +\
                          INTERACTIVE_MODE + ' ' + DELETE_TABLE + ' ' + args.keyspace + '.' + tablename
        run_query(temp_parameters)
        if check_verbose() >= 1:
            print('Table %s was deleted.' % tablename)


def import_file(tablename, import_params, import_path):
    if args.user is None and args.password is None:
        temp_parameters = INTERACTIVE_MODE + ' ' + IMPORT_FILE + ' ' + args.keyspace + '.' + tablename + \
                          '(' + import_params + ') ' + IMPORT_FILE_SUB + \
                          "'" + import_path + "'" + ' ' + IMPORT_FILE_SUB_SUB
        if check_verbose() == 1:
            with open(os.devnull, "w") as f:
                subprocess.call([CQLSH, args.ip, temp_parameters], stdout=f)
                print('File %s was successfully imported' % tablename)
        if check_verbose() == 2:
            subprocess.call([CQLSH, args.ip, temp_parameters])
    else:
        temp_parameters = USER + '"' + args.user + '"' + ' ' + PASSWORD + '"' + args.password + '"' + ' ' + '"' +\
                          INTERACTIVE_MODE + ' ' + IMPORT_FILE + ' ' + args.keyspace + '.' + tablename + \
                          '(' + import_params + ') ' + IMPORT_FILE_SUB + \
                          "'" + import_path + "'" + ' ' + IMPORT_FILE_SUB_SUB + '"'
        if check_verbose() == 1:
            with open(os.devnull, "w") as f:
                subprocess.call(CQLSH + ' ' + args.ip + ' ' + temp_parameters, shell=True, stdout=f)
                print('File %s was successfully imported' % tablename)
        if check_verbose() == 2:
            subprocess.call(CQLSH + ' ' + args.ip + ' ' + temp_parameters, shell=True)


def transformation_and_validation():
    firstlistelement_of_inputfiles = args.inputfile[0]

    # Validiert den Pfad und den Filetyp
    for val in firstlistelement_of_inputfiles:
        first_value_from_tuple = val[0]

        validate_path(first_value_from_tuple)

    # Validiert Tabellennamen
    for val in firstlistelement_of_inputfiles:
        second_value_from_tuple = val[1]

        validate_tablenames(second_value_from_tuple)

    # Konvertiert das xlsx oder xls zu csv
    p = multiprocessing.Pool()
    p.map(xls_to_csv, firstlistelement_of_inputfiles)

    # Validiert die csv
    for val in firstlistelement_of_inputfiles:
        second_value_from_tuple = val[1]
        validate_csv(second_value_from_tuple + '.csv', second_value_from_tuple)


def delete():
    if args.dtable is None and args.dkeyspace is True:
        delete_keyspace()
    elif args.dtable is not None and args.dkeyspace is False:
        for val in args.dtable:
            validate_tablenames(val)
            delete_table(val)
    elif args.dtable is not None and args.dkeyspace is True:
        delete_keyspace()
    else:
        return


def fileimport():
    firstlistelement_of_inputfiles = args.inputfile[0]
    # Legt den Keyspace an
    create_keyspace()

    # Legt die Tabellen an
    for val in firstlistelement_of_inputfiles:
        second_value_from_tuple = val[1]
        create_table(second_value_from_tuple, config['table_creation_params'][second_value_from_tuple])

    # Importiert die Files
    for val in firstlistelement_of_inputfiles:
        csv_output_list = os.listdir(PATH_CSV_OUTPUT)
        second_value_from_tuple = val[1]
        csv_path = PATH_CSV_OUTPUT + csv_output_list[csv_output_list.index(second_value_from_tuple + '.csv')]
        import_file(second_value_from_tuple, config['table_params'][second_value_from_tuple], csv_path)


def create_csv_folder():
    os.makedirs(os.path.dirname(__file__) + '/csv_output')


def delete_csvoutput():
    for the_file in os.listdir(PATH_CSV_OUTPUT):
        file_path = os.path.join(PATH_CSV_OUTPUT, the_file)
        try:
            if os.path.isfile(file_path):
                os.unlink(file_path)
        except Exception as e:
            print(e)
    shutil.rmtree(os.path.dirname(__file__) + '/csv_output')

start_time = time.time()


def main():
    try:
        if args.inputfile is None:
            test_databaseconnection()
            validate_arguments()
            delete()
        else:
            test_databaseconnection()
            validate_arguments()
            delete()
            create_csv_folder()
            transformation_and_validation()
            fileimport()
    finally:
        delete_csvoutput()
        if check_verbose() >= 1:
            print('cleaned up!')


# Aufruf von Main
if __name__ == '__main__':
    main()
    print("--- %s seconds ---" % (time.time() - start_time))
