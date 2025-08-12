#!/bin/bash
# This script will backup the postgresql database
# and store it in a specified directory

# Constants

USER = "user_name_of_db"
DATABASE = "name"
HOST = "localhost"
BACKUP_DIRECTORY = "/root/backup_db"

# Date stamp (formated YYYYMMDD)
# just used in file name
CURRENT_DATE = $(date "+%Y%m%d")

# Database named (command line argument) use pg_dump for targed backup
pg_dump -U $USER $DATABASE -h $HOST | gzip - > $BACKUP_DIRECTORY/$DATABASE\_$CURRENT_DATE.sql.gz

# Cleanup old backups

find $BACKUP_DIRECTORY/* -mtime +7 -exec rm {} \;
