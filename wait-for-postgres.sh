#!/bin/sh
echo "Esperando PostgreSQL iniciar em postgres:5432..."
while ! nc -z postgres 5432; do
  sleep 1
done
echo "PostgreSQL está ativo!"

exec "$@"
