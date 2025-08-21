# AmceatPortal

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.2.13.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a package that implements end-to-end testing capabilities.

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.

## Build With docker
Open a terminal in the "acmeat-portal/" folder and then run the following commands:

<code> ng build --configuration=production</code>

In this way you will be able to build the front end in production configuration (it includes also the proxy).
Ater that use:

<code>docker build -t your-image-name .</code>

To build the docker image. Then if the build is successfull push the image to docker-hub with the following command:

<code>docker push your-image-name </code>

## Deploy on Kubernetes
To deploy on kubernetes just apply these two yaml files:

<code> kubectl apply -f deploy.yaml -f service.yaml </code>

or with microk8s:

<code> microk8s kubectl apply -f deploy.yaml -f service.yaml </code>
