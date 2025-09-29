#!/bin/bash

OVERLAY_DIR="./overlays"
# INGRESS="./super-gateway-ingress.yaml"

echo "deploy ingress controller nginx"

if not kubectl get deployment ingress-nginx-controller -n ingress-nginx >/dev/null 2>&1; then
  microk8s kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.10.1/deploy/static/provider/cloud/deploy.yaml
fi

echo "start deploy overlay"

for dir in "$OVERLAY_DIR"/*; do
  if [ -d "$dir" ]; then
    NAME=$(basename "$dir")
    echo "Deploy for $NAME"

    echo "create namespace: $NAME"
    microk8s kubectl apply -f "$dir/namespace.yaml"

	microk8s kubectl apply -f "$dir/ingress.yaml"

    echo "user kustomize overlay of $NAME"
    microk8s kubectl apply -k "$dir"
  fi
done

# echo "Deploy super gateway ..."
# kubectl apply -f "$INGRESS"

echo "Deploy completato con successo."
