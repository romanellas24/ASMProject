#!/bin/bash
BASE_DIR="../../base"
OVERLAYS_DIR="./overlays"
DB_DIR="/Users/Francesco/git/ASMProject/couriers/courier/db"
NAMES=("panzafly" "cimangiamo" "famechimica" "toctocgnam")



for NAME in "${NAMES[@]}"; do
  OUTDIR="$OVERLAYS_DIR/$NAME"
  mkdir -p "$OUTDIR"

  echo "Generating overlay for: $NAME"
  

  # generate env for secrets of application
  # distance matrix api key is in .env
  cat <<EOF > "$OUTDIR/secrets.env"
mysql-root-password=$(openssl rand -hex 32)
mysql-user-password=$(openssl rand -hex 32)
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
price-per-km=$(awk -v min=0.2 -v max=0.5 'BEGIN{srand(); printf "%.5f", min+rand()*(max-min)}')
ws-url=$NAME.local
tracking-service=courier-tracking.$NAME.svc.cluster.local:8080
allocation-service=courier-allocation.$NAME.svc.cluster.local:8080
mysql-server=mysql-courier
mysql-database-name=courier_db
mysql-user-username=courier_be
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
                name: gateway-courier
                port:
                  number: 8080
EOF

  # generate kustomization.yaml
  cat <<EOF > "$OUTDIR/kustomization.yaml"
namespace: $NAME

resources:
  - $BASE_DIR

secretGenerator:
  - name: courier-secrets
    type: Opaque
    envs:
      - secrets.env
      - .env

configMapGenerator:
  - name: courier-cm
    envs:
      - config.env

generatorOptions:
  disableNameSuffixHash: true
EOF

  echo "Overlay for '$NAME' generated in $OUTDIR"

done

# aggregate all envs in one file for test
SUMMARY_FILE="./all-envs-summary.env"
echo "# Aggregated secrets and configMap for testing" > "$SUMMARY_FILE"

for NAME in "${NAMES[@]}"; do
  echo -e "\n##### $NAME #####" >> "$SUMMARY_FILE"

  if [[ -f "$OVERLAYS_DIR/$NAME/secrets.env" ]]; then
    echo -e "\n# Secrets" >> "$SUMMARY_FILE"
    sed "s/^/$NAME /" "$OVERLAYS_DIR/$NAME/secrets.env" >> "$SUMMARY_FILE"
  fi

  if [[ -f "$OVERLAYS_DIR/$NAME/config.env" ]]; then
    echo -e "\n# ConfigMap (sensitive or useful fields)" >> "$SUMMARY_FILE"
    grep -E '^\s*(price-per-km|ws-url|mysql-user-username|mysql-database-name|mysql_server|allocation-service|tracking-service)' "$OVERLAYS_DIR/$NAME/config.env" |
    sed "s/^\s*/$NAME /" >> "$SUMMARY_FILE"
  fi
done

if [[ -f "$DB_DIR/pass.env" ]]; then
  echo -e "\n# Secrets (delivery companies passwords hash)" >> "$SUMMARY_FILE"
  cat "$DB_DIR/pass.env" >> "$SUMMARY_FILE"
fi

echo -e "File aggregato creato: $SUMMARY_FILE"