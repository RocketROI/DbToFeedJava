Generador de Feeds de RocketROI a partir de una base de datos
==================================
Se trata de un ejecutable en Java que dada una consulta en SQL, genera un archivo de con todos los resultados de forma bien estructurada. 

El ejecutable contiene todas las librerias necesarias para ejecutarse. Solo necesita como parámetro el path de un archivo de propiedades.

## Requisitos

  * Java 1.6+
  * Maven 3.0+

## Empezando

El ejecutable ha de recibir un archivo de propiedades con los siguientes parametros:

* Tipo y datos de acceso a la base de datos
* Sentencia SQL para generar los datos.
* Archivo de salida de datos.

Actualmente soporta las siguientes bases de datos:

* PostgreSql
* MySql

y los siguientes formatos de salida:

* CSV

## Ejemplos de uso

Ejemplo de archivo de propiedades para PostgreSql:

```
database=postgres
databaseServer=127.0.0.1
databasePort=5432
databaseName=test
user=test
password=test
select=select * from test_table
output=output.out

```

Si usas MySql:
```
database=mysql
databaseServer=127.0.0.1
databasePort=3306
databaseName=test
user=test
password=test
select=select * from test_table
output=output.out

```

El proceso empezará a hacer consultas incrementales de 500 registros hasta que haya acabado de generar todo el archivo de salida.

Puedes controlar el número de registros por consulta añadiendo la siguiente linea al archivo de propiedades:

```
limit=100
```

Para llamar al ejecutable pasando como parametro el archivo de propiedades:

```
java -jar DbToFeedJava-0.1.jar -file="/path/to/archive.properties"
```