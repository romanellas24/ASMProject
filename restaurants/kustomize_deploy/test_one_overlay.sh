NAME="braciebasilico"
echo "start deploy of $NAME..."

microk8s kubectl apply -f "./overlays/$NAME/namespace.yaml"
microk8s kubectl apply -f "./overlays/$NAME/ingress.yaml"
microk8s kubectl apply -k "./overlays/$NAME"

echo "Deploy of $NAME completed."
