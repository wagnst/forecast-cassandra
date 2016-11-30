# !/usr/bin/env python2.7
# coding=utf-8

import argparse
import csv
import os
import xlrd
import subprocess
import time
import shutil
import multiprocessing
import json

"""
Opens the Json config file
"""
with open(os.path.dirname(__file__) + '/config.json') as json_data_file:
    config = json.load(json_data_file)

"""
Constants
"""
CQLSH = config['cqlsh_binary']
PATH_CSV_OUTPUT = os.path.dirname(__file__) + '/csv_output/'

INTERACTIVE_MODE = "-e"
USED_KEYSPACE = '-k'
USER = '--username='
PASSWORD = '--password='

CREATE_KEY_SPACE = "CREATE KEYSPACE IF NOT EXISTS"
CREATE_KEY_SPACE_SUB = config['replication_param']
CREATE_TABLE = "CREATE TABLE IF NOT EXISTS"
IMPORT_FILE = 'COPY'
IMPORT_FILE_SUB = 'FROM'
IMPORT_FILE_SUB_SUB = 'WITH HEADER = TRUE;'
DELETE_KEYSPACE = 'DROP KEYSPACE IF EXISTS'
DELETE_TABLE = 'DROP TABLE IF EXISTS'

SOMETING_WENT_WRONG_MESSAGE = "Maybe something went wrong. If it was an session timeout just ignore it otherwise " \
                             "please check your database connection or SQL Syntax."

"""
Functions
"""


def return_parser():
    """
    This funktion returns the parser for the arguments.

    :rtype: parser
    """
    parser = argparse.ArgumentParser()
    parser.add_argument('--verbose', '-v', action='count', help='verbose level')
    parser.add_argument('--password', '-p', help='password of the database server')
    parser.add_argument('--user', '-u', help='user of the database server')
    parser.add_argument('--ip', '-n', required=True, help='ip Address of the database server')
    parser.add_argument('--keyspace', '-k', required=True, help='keyspace name for creation or keyspace to add data')
    parser.add_argument('--dtable', '-t', action='append', help='table which should be deleted')
    parser.add_argument('--dkeyspace', '-d', action="store_true", help='Keyspace should be deleted')
    parser.add_argument('--inputfile', '-i', action='append', type=filepath_and_tablename, nargs='*',
                        help='Input files for tables. Format: filepath,tablename. Following tablenames are valid: %s'
                             % config['available_params'])
    return parser


def filepath_and_tablename(inputargs):
    """
    This funktion creat a new datatype. The tuple for the argument inputfile.

    :rtype: <filepath,tablename>
    :parameter inputargs: comes from the parser
    :raise: ArgumentTypeError
    """
    try:
        x, y = map(str, inputargs.split(','))
        return x, y
    except:
        raise argparse.ArgumentTypeError("Error: format must be filepath,tablename")


def check_verbose():
    """
    This function return the verbose level as int.

    :rtype: int
    """
    if args.verbose is None:
        return 0
    elif args.verbose == 1:
        return 1
    else:
        return 2


def run_query(query):
    """
    This funktion run the query on the database server.
    The parameter query is the sql statement.

    :parameter query: this is the query in the SQL language
    """
    if args.user is None and args.password is None:
        return subprocess.call([CQLSH, args.ip, query])
    else:
        return subprocess.call(CQLSH + ' ' + args.ip + ' ' + query, shell=True)


def validate_path(file_path):
    """
    This funktion validate the file path of the given input files. It checks that it exists and that the file has the
    right ending as .xls or .xlsx . Throws an exception when the path doesn't
    exist and end the script when the path or the file isn't vaild.

    :param file_path: path to the file which should be validated

    Used function:  :func:`importMain.check_verbose`
    """
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
    """
    This function validates the tablename of the inputfile argument. It checks that it exists in the config json.
    End the script when the tablename isn't in the config file.

    :parameter tablename: tablename argument which should be validated
    """
    try:
        config['table_params'][tablename]
    except KeyError:
        print('The tablename %s is not valid.' % tablename)
        exit('Canceled: please use the following tablenames: %s' % config['available_params'])


def validate_csv(name, reference_file):
    """
    This function validates the generated csv. It checks that the header of the csv file has the same order of columns
    as the order of arguments of SQL statements which are defined in the section table_params in the json config file.
    This is necessary to guarantee a successful import. It ends the script when the csv header isn't valid.

    :parameter name: name of the table to get the csv file
    :parameter reference_file: variable to find the right table_params in the json config file

    Used function:  :func:`importMain.check_verbose`
    """
    reader = csv.reader(open(PATH_CSV_OUTPUT + name, 'r'), delimiter=',')
    header_from_csv = next(reader)
    if header_from_csv == config['table_params'][reference_file].upper().split(', '):
        if check_verbose() == 1:
            print ('The header from %s.csv is valid.' % name)
        return True

    else:
        exit("Canceled: CSV Header from %s.csv isn't valide!" % name)


def xls_to_csv((file_path, tablename)):
    """
    This function takes an excel file and converts it to a utf8 coded csv file. It get's the file_path and the tablename
    as a map because of the usage of this function with multiprocessing.

    :parameter: map of file path and tablename (file_path, tablename)

    Used function:  :func:`importMain.check_verbose`
    """
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
    """
    This function tests if a cassandra database connection with the given ip address and user credentials is possible.
    It returns a success message when verbose is on.
    Ends the script if it's not possible to connect to the server.

    Used functions:     :func:`importMain.run_query` , :func:`importMain.check_verbose`
    """
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
    """
    This function create a keyspace if it doesn't exist. It returns a success message when verbose is on and returns
    a message if the return code of the query isn't 0.

    Used functions:     :func:`importMain.run_query` , :func:`importMain.check_verbose`
    """
    if args.user is None and args.password is None:
        temp_parameters = INTERACTIVE_MODE + ' ' + CREATE_KEY_SPACE + ' ' + args.keyspace + ' ' + CREATE_KEY_SPACE_SUB
        returncode = run_query(temp_parameters)
        if returncode == 0:
            if check_verbose() >= 1:
                print('Datatbase %s was created or already exists.' % args.keyspace)
        else:
            print SOMETING_WENT_WRONG_MESSAGE
    else:
        temp_parameters = USER + '"' + args.user + '"' + ' ' + PASSWORD + '"' + args.password + '"' + ' ' + \
                          INTERACTIVE_MODE + ' ' + '"' + CREATE_KEY_SPACE + ' ' + args.keyspace + ' ' + \
                          CREATE_KEY_SPACE_SUB + '"'
        returncode = run_query(temp_parameters)
        if returncode == 0:
            if check_verbose() >= 1:
                print('Datatbase %s was created or already exists.' % args.keyspace)
        else:
            print SOMETING_WENT_WRONG_MESSAGE


def delete_keyspace():
    """
    This function delete a keyspace if it exist. It returns a success message when verbose is on and returns
    a message if the return code of the query isn't 0.

    Used functions:     :func:`importMain.run_query` , :func:`importMain.check_verbose`
    """
    if args.user is None and args.password is None:
        temp_parameters = INTERACTIVE_MODE + ' ' + DELETE_KEYSPACE + ' ' + args.keyspace
        returncode = run_query(temp_parameters)
        if returncode == 0:
            if check_verbose() >= 1:
                print('Keyspace %s was deleted.' % args.keyspace)
        else:
            print SOMETING_WENT_WRONG_MESSAGE
    else:
        temp_parameters = USER + '"' + args.user + '"' + ' ' + PASSWORD + '"' + args.password + '"' + ' ' + \
                          INTERACTIVE_MODE + ' ' + '"' + DELETE_KEYSPACE + ' ' + args.keyspace + '"'
        returncode = run_query(temp_parameters)
        if returncode == 0:
            if check_verbose() >= 1:
                print('Keyspace %s was deleted.' % args.keyspace)
        else:
            print SOMETING_WENT_WRONG_MESSAGE


def create_table(tablename, table_parameter):
    """
    This function create a table with the parameters if it doesn't exists. It returns a success message when verbose
    is on and returns a message if the return code of the query isn't 0.

    :parameter tablename: name of the table
    :parameter table_parameter: variable to get the right entry in table_creation_params from the json config file

    Used functions:     :func:`importMain.run_query` , :func:`importMain.check_verbose`
    """
    if args.user is None and args.password is None:
        temp_parameters = INTERACTIVE_MODE + ' ' + CREATE_TABLE + ' ' + args.keyspace + '.' + \
                          tablename + table_parameter
        returncode = run_query(temp_parameters)
        if returncode == 0:
            if check_verbose() >= 1:
                print('Table %s was created or already exists.' % tablename)
        else:
            print SOMETING_WENT_WRONG_MESSAGE
    else:
        temp_parameters = USER + '"' + args.user + '"' + ' ' + PASSWORD + '"' + args.password + '"' + ' ' + '"' \
                          + INTERACTIVE_MODE + ' ' + CREATE_TABLE + ' ' + args.keyspace + '.' + \
                          tablename + table_parameter + '"'
        returncode = run_query(temp_parameters)
        if returncode == 0:
            if check_verbose() >= 1:
                print('Table %s was created or already exists.' % tablename)
        else:
            print SOMETING_WENT_WRONG_MESSAGE


def delete_table(tablename):
    """
    This function delete the table if it exists. It returns a success message when verbose is on and returns
    a message if the returncode of the query isn't 0.

    :parameter tablename: name of the table which should be deleted

    Used functions:     :func:`importMain.run_query` , :func:`importMain.check_verbose`
    """
    if args.user is None and args.password is None:
        temp_parameters = INTERACTIVE_MODE + ' ' + DELETE_TABLE + ' ' + args.keyspace + '.' + tablename
        returncode = run_query(temp_parameters)
        if returncode == 0:
            if check_verbose() >= 1:
                print('Table %s was deleted.' % tablename)
        else:
            print SOMETING_WENT_WRONG_MESSAGE
    else:
        temp_parameters = USER + '"' + args.user + '"' + ' ' + PASSWORD + '"' + args.password + '"' + ' ' + '"' +\
                          INTERACTIVE_MODE + ' ' + DELETE_TABLE + ' ' + args.keyspace + '.' + tablename + '"'
        returncode = run_query(temp_parameters)
        if returncode == 0:
            if check_verbose() >= 1:
                print('Table %s was deleted.' % tablename)
        else:
            print SOMETING_WENT_WRONG_MESSAGE


def import_file(tablename, import_params, import_file_path):
    """
    This function imports a file to the keyspace. It returns a success message when verbose is on and returns
    a message if the returncode of the query isn't 0.

    :parameter tablename: name of the table in which data should be imported
    :parameter import_params: variable to get the right entry in table_params from the json config file
    :parameter import_file_path: path of the csv file wich should be imported

    Used functions:     :func:`importMain.run_query` , :func:`importMain.check_verbose`
    """
    if args.user is None and args.password is None:
        temp_parameters = INTERACTIVE_MODE + ' ' + IMPORT_FILE + ' ' + args.keyspace + '.' + tablename + \
                          '(' + import_params + ') ' + IMPORT_FILE_SUB + \
                          "'" + import_file_path + "'" + ' ' + IMPORT_FILE_SUB_SUB
        if check_verbose() == 1:
            with open(os.devnull, "w") as f:
                returncode = subprocess.call([CQLSH, args.ip, temp_parameters], stdout=f)
                if returncode == 0:
                    print('File %s was successfully imported' % tablename)
                else:
                    print SOMETING_WENT_WRONG_MESSAGE
        if check_verbose() == 2:
            returncode = subprocess.call([CQLSH, args.ip, temp_parameters])
            if returncode == 0:
                return
            else:
                print SOMETING_WENT_WRONG_MESSAGE
    else:
        temp_parameters = USER + '"' + args.user + '"' + ' ' + PASSWORD + '"' + args.password + '"' + ' ' + '"' +\
                          INTERACTIVE_MODE + ' ' + IMPORT_FILE + ' ' + args.keyspace + '.' + tablename + \
                          '(' + import_params + ') ' + IMPORT_FILE_SUB + \
                          "'" + import_file_path + "'" + ' ' + IMPORT_FILE_SUB_SUB + '"'
        if check_verbose() == 1:
            with open(os.devnull, "w") as f:
                returncode = subprocess.call(CQLSH + ' ' + args.ip + ' ' + temp_parameters, shell=True, stdout=f)
                if returncode == 0:
                    print('File %s was successfully imported' % tablename)
                else:
                    print SOMETING_WENT_WRONG_MESSAGE
        if check_verbose() == 2:
            returncode = subprocess.call(CQLSH + ' ' + args.ip + ' ' + temp_parameters, shell=True)
            if returncode == 0:
                return
            else:
                print SOMETING_WENT_WRONG_MESSAGE


def convertion_and_validation():
    """
    This function has 4 steps: \n
    1. Validates the paths from the inputfile arguments \n
    2. Validates the tablenames from the inputfile arguments  \n
    3. Converts the xlsx or xls input files with multiprocessing into csv files with  \n
    4. Validates the csv files with

    Used functions: :func:`importMain.validate_path` , :func:`importMain.validate_tablenames` ,
    :func:`importMain.xls_to_csv` , :func:`importMain.validate_csv`
    """
    firstlistelement_of_inputfiles = args.inputfile[0]

    """
    validate the paths
    """
    for val in firstlistelement_of_inputfiles:
        first_value_from_tuple = val[0]

        validate_path(first_value_from_tuple)

    """
    validate the tablenames
    """
    for val in firstlistelement_of_inputfiles:
        second_value_from_tuple = val[1]

        validate_tablenames(second_value_from_tuple)

    """
    convert input files with multiprocessing
    """
    p = multiprocessing.Pool()
    p.map(xls_to_csv, firstlistelement_of_inputfiles)

    """
    validate the csv
    """
    for val in firstlistelement_of_inputfiles:
        second_value_from_tuple = val[1]
        validate_csv(second_value_from_tuple + '.csv', second_value_from_tuple)


def delete():
    """
    This function defines when what should be deleted. \n
    Functions which are used: :func:`delete_keyspace` , :func:`validate_tablenames` , :func:`delete_table`
    """
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


def file_import():
    """
    This function has 3 steps: \n
    1. Creates the keyspace if it doesn't already exist  \n
    2. Creates the tables from the inputfiles argument if they don't already exist  \n
    3. Import the files which are converted to csv

    Used functions: :func:`importMain.create_keyspace` , :func:`importMain.create_table` ,
    :func:`importMain.import_file`
    """
    firstlistelement_of_inputfiles = args.inputfile[0]
    """
    create keyspace
    """
    create_keyspace()

    """
    create tables
    """
    for val in firstlistelement_of_inputfiles:
        second_value_from_tuple = val[1]
        create_table(second_value_from_tuple, config['table_creation_params'][second_value_from_tuple])

    """
    import files
    """
    for val in firstlistelement_of_inputfiles:
        csv_output_list = os.listdir(PATH_CSV_OUTPUT)
        second_value_from_tuple = val[1]
        csv_path = PATH_CSV_OUTPUT + csv_output_list[csv_output_list.index(second_value_from_tuple + '.csv')]
        import_file(second_value_from_tuple, config['table_params'][second_value_from_tuple], csv_path)


def create_csv_folder():
    """
    This function creates a folder named csv_output where the converted csv files would be stored.
    """
    os.makedirs(os.path.dirname(__file__) + '/csv_output')


def delete_csv_folder():
    """
    This function deletes the csv_output where the converted csv files were stored.
    """
    try:
        for the_file in os.listdir(PATH_CSV_OUTPUT):
            file_path = os.path.join(PATH_CSV_OUTPUT, the_file)
            try:
                if os.path.isfile(file_path):
                    os.unlink(file_path)
            except Exception as e:
                print(e)
        shutil.rmtree(os.path.dirname(__file__) + '/csv_output')
    except OSError:
        exit("Folder csv_output doesn't exist!")


def main():
    """
    This function does the program sequence. There are two different cases: \n
    * The argument inputfile isn't used:
        1. Tests whether a connection to the cassandra database is possible  \n
        2. Creates a folder csv_output \n
        3. Deletes the keyspace and/or the table/s which are specified with the arguments.
    * The argument inputfile is used:
        1. Tests whether a connection to the cassandra database is possible  \n
        2. Creates a folder csv_output \n
        3. Deletes the keyspace and/or the table/s which are specified with the arguments.
        4. Validates and converts the inputfile/s
        5. Create the keyspace, create the tables and import the files

    Finally it deletes the csv_output folder even if an error occurs.

    Used functions: :func:`importMain.test_databaseconnection` , :func:`importMain.delete` ,
    :func:`importMain.create_csv_folder` , :func:`importMain.delete_csv_folder` , :func:`importMain.check_verbose` ,
    :func:`importMain.tranformation_and_validation` , :func:`importMain.file_import`
    """
    try:
        test_databaseconnection()
        create_csv_folder()
        if args.inputfile is None:
            delete()
        else:
            delete()
            convertion_and_validation()
            file_import()
    finally:
        delete_csv_folder()
        if check_verbose() >= 1:
            print('cleaned up!')


# Aufruf von Main
if __name__ == '__main__':

    start_time = time.time()

    args = return_parser().parse_args()

    if args.inputfile is None and args.dtable is None and args.dkeyspace is False:
        return_parser().error('-i inputfile or -t dtable or -d dkeyspace required.')
    if args.user is None and args.password is not None:
        return_parser().error('-p password requires -u user! Use --help for more information.')
    if args.password is None and args.user is not None:
        return_parser().error('-u user requires -p password! Use --help for more information.')

    main()

    print("--- %s seconds ---" % (time.time() - start_time))
