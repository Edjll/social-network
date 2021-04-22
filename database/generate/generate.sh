#!/usr/bin/env bash

user="admin";
database="social_network";
files=("social_network_public_user_entity.sql"  "social_network_public_user_entity_2.sql"  "social_network_public_user_entity_3.sql"  "social_network_public_user_entity_4.sql"  "social_network_public_user_info.sql"  "social_network_public_user_info_2.sql"  "social_network_public_user_friend.sql");

for file in ${files[*]}; do
  psql -U "$user" -d "$database" -f /generate/"$file";
  echo "inserted ${file}"
done