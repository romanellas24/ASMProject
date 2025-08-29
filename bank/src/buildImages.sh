docker build --tag romanellas/joliebank-gateway -f gateway/Dockerfile . && docker push romanellas/joliebank-gateway &&
docker build --tag romanellas/joliebank-static -f static/Dockerfile . && docker push romanellas/joliebank-static &&
docker build --tag romanellas/joliebank-http-gw -f super-gateway/Dockerfile . && docker push romanellas/joliebank-http-gw &&
docker build --tag romanellas/joliebank-accounts -f accounts/Dockerfile . && docker push romanellas/joliebank-accounts &&
docker build --tag romanellas/joliebank-payments -f payments/Dockerfile . && docker push romanellas/joliebank-payments