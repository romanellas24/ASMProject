NAME="panzafly"

echo "start deploy of $NAME..."

cp .env  "./overlays/$NAME/.env"

kubectl apply -f "./overlays/$NAME/namespace.yaml"
kubectl apply -f "./overlays/$NAME/ingress.yaml"
kubectl apply -k "./overlays/$NAME"

rm "./overlays/$NAME/.env"

echo "Deploy of $NAME completed."