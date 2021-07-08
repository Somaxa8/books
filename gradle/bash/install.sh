#!/usr/bin/env bash

docker network create template
docker run --name templatedb -d --network template -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=templatedb -v /root/template/mysql:/var/lib/mysql --restart always mariadb --character-set-server=utf8 --collation-server=utf8_general_ci
docker run --name templatemongo -d --network template -p 27017:27017 -v /root/template/mongo/:/data/db -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=1234 --restart always mongo

#docker run --name ${project}mongoclient -d -p 3000:3000 --restart always --link ${project}mongo mongoclient/mongoclient