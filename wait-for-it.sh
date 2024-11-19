#!/bin/bash

host="$1"
port="$2"
shift 2
cmd="$@"

# Kiểm tra xem các biến host và port có giá trị hợp lệ không
if [[ -z "$host" || -z "$port" ]]; then
  echo "Host hoặc Port không hợp lệ, vui lòng kiểm tra lại."
  exit 1
fi

until nc -z "$host" "$port"; do
  >&2 echo "MySQL is unavailable - sleeping"
  sleep 3
done

>&2 echo "MySQL is up - executing command"
exec $cmd
