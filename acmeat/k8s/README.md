## Deploy on Kubernetes

1. Go under "deploy" folder and then open the "linux" folder if you are on linux or "win" folder if you are on windows.

2. To apply deployments:

<code> ./deploy-kubectl.sh</code>

or (if you have microk8s):

<code> ./deploy-kubectl.sh</code>

or if you are on windows:

<code> deploy-kubectl.bat</code>


3. To apply services:

<code>kubectl apply -f service.yaml<code>

or (if you have microk8s)

<code>microk8s kubectl apply -f deployment.yaml<code>

## Port forward

If you want just to start up the api's in local and use the backend services available in the remote machine follow this procedure:

1. Go under "local-port-forward" and then open then open the "linux" or "win" folder if you are on windows.

2. To port-forward with kubectl, open the "kubectl" folder and launch the following command:

<code>./start-server.sh<code>

3. To port-forward with microk8s, open the "microk8s" folder and then launch the following command:

<code>./start-server-microk8s.sh</code>

or if you are on windows, open the win folder and then start manually each (.bat) file inside the directory