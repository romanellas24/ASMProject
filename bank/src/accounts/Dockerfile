FROM jolielang/jolie
EXPOSE 9003
COPY ../ /
COPY ../jdbc-mysql.jar /usr/lib/jolie/lib/jdbc-mysql.jar
WORKDIR /accounts
CMD ["jolie", "bankAccounts.ol"]