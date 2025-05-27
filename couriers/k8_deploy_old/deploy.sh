#!/bin/sh

files="mysql-secrets.yaml mysql-configmap.yaml mysql-deployment.yaml app-configmap.yaml app-secrets.yaml courierAllocation-deployment.yaml courierTracking-deployment.yaml gateway/gateway.yaml gateway/allocationRoute.yaml gateway/trackingRoute.yaml"

for file in $files; do
  if [ -e "$file" ]; then
    echo "Applying $file..."
    kubectl apply -f "$file"
  else
    echo "File not found: $file"
  fi
done