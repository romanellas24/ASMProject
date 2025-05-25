NAME="braciebasilico"
echo "start deploy of $NAME..."

kubectl apply -f "./overlays/$NAME/namespace.yaml"
kubectl apply -f "./overlays/$NAME/ingress.yaml"
kubectl apply -k "./overlays/$NAME"

echo "Deploy of $NAME completed."