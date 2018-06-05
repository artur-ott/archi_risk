#!/bin/bash

su - postgres -c "/usr/lib/postgresql/9.6/bin/postgres -D /usr/lib/postgresql/9.6/bin/postgres -c config_file=/etc/postgresql/9.6/main/postgresql.conf" 
