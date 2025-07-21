echo "Creating acmeat namespace"
microk8s kubectl create namespace acmeat


echo "Applying DB..."
microk8s kubectl apply -R -f "../../../acmeat-db/db/*.yaml"

echo "Appliying Server..."
microk8s kubectl apply -R -f "../../../acmeat-be/server/acmeat.domains.locals/*.yaml" -R -f "../../../acmeat-be/server/acmeat.domains.order/*.yaml" -R -f "../../../acmeat-be/server/acmeat.domains.deliverycompany/*.yaml" -R -f "../../../acmeat-be/server/acmeat.domains.menu/*.yaml" -R -f "../../../acmeat-be/server/acmeat.domains.dish/*.yaml" -R -f "../../../acmeat-be/server/acmeat.domains.user/*.yaml"




echo "Applying apis..."
microk8s kubectl apply -R -f "../../../acmeat-be/api/acmeat.apilocal/*.yaml" -R -f "../../../acmeat-be/api/acmeat.apiorder/*.yaml" -R -f "../../../acmeat-be/api/acmeat.apideliverycompany/*.yaml" -R -f "../../../acmeat-be/api/acmeat.apimenu/*.yaml" -R -f "../../../acmeat-be/api/acmeat.apidish/*.yaml" -R -f "../../../acmeat-be/api/acmeat.apiuser/*.yaml"


echo "Applying Ingress..."
microk8s kubectl apply -f "../../ingress.yaml"

echo "Applying frontend..."
microk8s kubectl apply -f "../../../acmeat-portal/*.yaml"

echo "DEPLOYMENT DONE!"