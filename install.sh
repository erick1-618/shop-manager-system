#!/bin/bash

echo "Compiling..."
javac -cp "lib/postgresql-42.7.5.jar" $(find . -name "*.java") -d bin

if [ $? -eq 0 ]; then
    echo "Source sucessfully compiled."
else
    echo "Compiling error"
    exit 1
fi

echo "Setting the database properties"
echo "Obs: app will use the default postgres port [5432]"

read -p "User's name: " usr
read -s -p "User's password: " psk
echo 
read -p "Set the database name: " dbn

mkdir -p resources

echo "user=$usr" > resources/config.properties
echo "pwk=$psk" >> resources/config.properties
echo "db=$dbn" >> resources/config.properties
echo "db_url=jdbc:postgresql://localhost:5432/" >> resources/config.properties

echo "Trying connection..."

PGPASSWORD=$psk psql -U $usr -c "CREATE DATABASE $dbn;" 2>/dev/null

if [ $? -eq 0 ]; then
    echo "Database $dbc created or already exists."
else
    echo "Warn: found an error when creating the database (maybe it's already exists)"
fi

touch run.sh

echo java -cp "lib/postgresql-42.7.5.jar:bin" br.com.erick.sms.vision.Application > run.sh

chmod +x run.sh

exit

