#!/bin/bash
BASE_DIR="../../base"
OVERLAYS_DIR="./overlays"
NAMES=("saporedipasta" "osteriamareebosco" "ilvicolettosegreto" "laforchettaribelle" "cantinafiordisale" "braciebasilico")

for NAME in "${NAMES[@]}"; do
  OUTDIR="$OVERLAYS_DIR/$NAME"
  mkdir -p "$OUTDIR"

  echo "Generating overlay for: $NAME"

  # generate env for secrets of application
  cat <<EOF > "$OUTDIR/secrets.env"
rabbitPsswd=$(openssl rand -hex 32)
jwtSecret=$(openssl rand -hex 128)
restaurateurPassword=$(openssl rand -hex 32)
mysql-root-password=$(openssl rand -hex 32)
mysql-user-password=$(openssl rand -hex 32)
acmeatPassword=$(openssl rand -hex 32)
EOF

  # generate namespace

  cat <<EOF > "$OUTDIR/namespace.yaml"
apiVersion: v1
kind: Namespace
metadata:
  name: $NAME
EOF

  # generate configMap
  cat <<EOF > "$OUTDIR/config.env"
rabbitPort=5672
rabbitHost=rabbitmq.$NAME.svc.cluster.local
rabbitUser=restaurant
# backendUrl=http://gateway.$NAME.svc.cluster.local:8080
backendUrl=$NAME.local
apiService=http://api-restaurant.$NAME.svc.cluster.local:8080
wsService=ws://app-restaurant.$NAME.svc.cluster.local:8080
feService=http://app-restaurant.$NAME.svc.cluster.local:8080
restaurateurUsername=admin
acmeatUser=acmeat
mysql_server=mysql-restaurant
mysql-database-name=restaurant
mysql-user-username=restaurateur
localName=$NAME
EOF

  #generate ingress.yaml
    cat <<EOF > "$OUTDIR/ingress.yaml"
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: gateway-ingress
  namespace: $NAME
  annotations:
    nginx.ingress.kubernetes.io/proxy-read-timeout: "3600"
    nginx.ingress.kubernetes.io/proxy-send-timeout: "3600"
    nginx.ingress.kubernetes.io/proxy-http-version: "1.1"
    nginx.ingress.kubernetes.io/use-forwarded-headers: "true"
spec:
  ingressClassName: nginx
  rules:
    - host: $NAME.local
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: gateway-restaurant
                port:
                  number: 8080
EOF

  # generate kustomization.yaml
  cat <<EOF > "$OUTDIR/kustomization.yaml"
namespace: $NAME

resources:
  - $BASE_DIR

secretGenerator:
  - name: restaurant-secrets
    type: Opaque
    envs:
      - secrets.env

configMapGenerator:
  - name: restaurant-app-cm
    envs:
      - config.env

generatorOptions:
  disableNameSuffixHash: true
EOF

  echo "Overlay for '$NAME' generated in $OUTDIR"

done

# generate super gateway
# cat <<EOF > super-gateway-ingress.yaml
# apiVersion: networking.k8s.io/v1
# kind: Ingress
# metadata:
#   name: super-gateway-ingress
#   namespace: ingress-nginx
#   annotations:
#     nginx.ingress.kubernetes.io/rewrite-target: /\$2
#     nginx.ingress.kubernetes.io/service-upstream: "true"
# spec:
#   ingressClassName: nginx
#   rules:
#   - host: gateway.local #PER TEST - cambiare con ip server pubblico
#     http:
#       paths:
# EOF

# for NAME in "${NAMES[@]}"; do
#   cat <<EOF >> super-gateway-ingress.yaml
#       - path: /${NAME}(/|$)(.*)
#         pathType: Prefix
#         backend:
#           service:
#             name: ${NAME}/gateway-restaurant
#             port:
#               number: 8080

# EOF
# done

# echo "File super-gateway-ingress.yaml generato con successo."

# aggregate all envs in one file for test
SUMMARY_FILE="./all-envs-summary.env"
echo "# Aggregated secrets and configMap for testing" > "$SUMMARY_FILE"

for NAME in "${NAMES[@]}"; do
  echo -e "\n##### $NAME #####" >> "$SUMMARY_FILE"

  if [[ -f "$OVERLAYS_DIR/$NAME/secrets.env" ]]; then
    echo -e "\n# Secrets" >> "$SUMMARY_FILE"
    sed "s/^/$NAME /" "$OVERLAYS_DIR/$NAME/secrets.env" >> "$SUMMARY_FILE"
  fi

  if [[ -f "$OVERLAYS_DIR/$NAME/configMap.yaml" ]]; then
    echo -e "\n# ConfigMap (sensitive or useful fields)" >> "$SUMMARY_FILE"
    grep -E '^\s*(rabbitUser|rabbitHost|backendUrl|mysql-user-username|mysql-database-name|mysql_server|restaurateurUsername)' "$OVERLAYS_DIR/$NAME/configMap.yaml" |
    sed "s/^\s*/$NAME /" >> "$SUMMARY_FILE"
  fi
done

echo -e "File aggregato creato: $SUMMARY_FILE"