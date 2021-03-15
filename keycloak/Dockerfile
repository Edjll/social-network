FROM jboss/keycloak:7.0.1
COPY *.json social-network-realm.json

ENV KEYCLOAK_USER admin
ENV KEYCLOAK_PASSWORD admin
ENV KEYCLOAK_IMPORT social-network-realm.json -Dkeycloak.profile.feature.upload_scripts=enabled