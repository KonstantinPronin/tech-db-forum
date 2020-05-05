FROM ubuntu:18.04
LABEL maintainer="k.pronin"

ENV TZ=Europe/Moscow
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Обновление списка пакетов
RUN apt-get -y update
RUN apt install -y git wget gcc gnupg

ENV WORK /opt/server
ADD app/ $WORK/app/
ADD scripts/ $WORK/scripts/
WORKDIR $WORK

ENV PGVER 12
RUN wget https://www.postgresql.org/media/keys/ACCC4CF8.asc
RUN apt-key add ACCC4CF8.asc
RUN echo "deb http://apt.postgresql.org/pub/repos/apt/ bionic-pgdg main" > /etc/apt/sources.list.d/pgdg.list
RUN apt-get update
RUN apt-get install -y postgresql-$PGVER

# Run the rest of the commands as the ``postgres`` user created by the ``postgres-$PGVER`` package when it was ``apt-get installed``
USER postgres

RUN /etc/init.d/postgresql start &&\
    psql --command "ALTER ROLE postgres WITH PASSWORD 'postgres';" &&\
    createdb -O postgres tech_db &&\
    psql -d tech_db -f $WORK/scripts/postgre.sql &&\
    /etc/init.d/postgresql stop


# Adjust PostgreSQL configuration so that remote connections to the
# database are possible.
RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/$PGVER/main/pg_hba.conf

# And add ``listen_addresses`` to ``/etc/postgresql/$PGVER/main/postgresql.conf``
RUN echo "listen_addresses='*'" >> /etc/postgresql/$PGVER/main/postgresql.conf

# Expose the PostgreSQL port
EXPOSE 5432

# Add VOLUMEs to allow backup of config, logs and databases
VOLUME  ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql"]

# Back to the root user
USER root

RUN apt-get install -y openjdk-8-jdk-headless

WORKDIR $WORK/app
RUN ./mvnw package
CMD service postgresql start && java -jar -Dfile.encoding=UTF-8 ./target/forum-0.0.1.jar
