#!/bin/bash

# Определяем имя JAR-файла из файла pom.xml
JAR_FILE=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.build.finalName}.jar' --non-recursive exec:exec)

# Выполняем сборку проекта с помощью Maven
mvn clean install

# Запускаем приложение
java -jar target/$JAR_FILE

