#
# Scala and sbt Dockerfile
#
# https://github.com/hseeberger/scala-sbt
#

# Pull base image
FROM ubuntu:latest

# Env variables
ENV POSTGRES_VERSION 9.6

# Install Postgres
RUN \
    apt-get update && \
    apt-get -y install software-properties-common && \
    apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 7FCC7D46ACCC4CF8 && \
    add-apt-repository "deb http://apt.postgresql.org/pub/repos/apt/ xenial-pgdg main" && \
    apt-get -y install wget && \
    wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add - && \
    apt-get update && \
    apt-get -y install postgresql-$POSTGRES_VERSION && \
    /etc/init.d/postgresql start && \
    su - postgres -c "psql --command \"CREATE USER archi WITH SUPERUSER PASSWORD 'risk';\"" && \
    su - postgres -c "createdb -O archi archidb" && \
    echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/$POSTGRES_VERSION/main/pg_hba.conf && \
    echo "listen_addresses='*'" >> /etc/postgresql/$POSTGRES_VERSION/main/postgresql.conf

EXPOSE 5432

# Install MongoDB
RUN \
    apt-get install -y mongodb && \
    apt-get install -y netcat

EXPOSE 27017:27017

ADD run_docker.sh /run.sh
ADD run_mongo.sh /run_mongo.sh
ADD run_postgres.sh /run_postgres.sh
ADD mongodb.conf /etc/mongodb.conf

CMD ["sh", "/run.sh"]
