FROM jolielang/jolie
EXPOSE 9002
COPY ../ /
COPY ../jdbc-mysql.jar /usr/lib/jolie/lib/jdbc-mysql.jar
WORKDIR /payments
CMD ["jolie", "bankPayments.ol"]