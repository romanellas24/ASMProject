#!/bin/bash

OVERLAY_DIR="./overlays"

echo "start cleanup overlay"

for dir in "$OVERLAY_DIR"/*; do
  if [ -d "$dir" ]; then
    NAME=$(basename "$dir")
    echo "Cleanup for $NAME"

    # echo "delete kustomize overlay of $NAME"
    # kubectl delete -k "$dir" --ignore-not-found

    echo "delete namespace: $NAME"
    microk8s kubectl delete -f "$dir/namespace.yaml" --ignore-not-found
  fi
done

# echo "Delete super gateway ingress ..."
# kubectl delete -f "$INGRESS" --ignore-not-found

echo "Cleanup completato con successo."
