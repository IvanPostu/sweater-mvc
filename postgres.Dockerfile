FROM postgres:9.6-alpine
COPY ./init.sql /docker-entrypoint-initdb.d/

