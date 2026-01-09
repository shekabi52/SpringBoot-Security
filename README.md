# Docker Compose connecting to local (host) DB

Steps to run:
1. Open a terminal and cd to the folder:
   cd "/Users/abishekp/Downloads/demo 2"

2. Start the service:
   docker compose up -d

   (If you have older Docker: docker-compose up -d)

3. Enter the running container to install a client and test the DB connection:
   docker compose exec app sh
   # inside container (example for PostgreSQL)
   apk add --no-cache postgresql-client
   PGPASSWORD="$DB_PASS" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME"

Notes:
- host.docker.internal:host-gateway requires Docker Engine 20.10+ / Docker Desktop.
- On Linux, if host-gateway isn't supported, use network_mode: "host" in docker-compose.yml and set DB_HOST=127.0.0.1.
- Replace DB_* env vars in docker-compose.yml with your actual DB port/credentials.

