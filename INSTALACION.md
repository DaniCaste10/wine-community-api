# Instalación y ejecución

Guía paso a paso para instalar, ejecutar y probar la **Wine Community API** desde cero, tanto en **Windows** como en **Linux**. No hace falta ninguna experiencia previa con Spring Boot: basta con seguir los pasos en orden.

> Los pasos de Linux están redactados para **Ubuntu y Debian**. En otras distribuciones los paquetes son equivalentes, pero los comandos de instalación y la configuración de acceso a PostgreSQL pueden variar.

Para conocer el diseño de la API (recursos, endpoints, parámetros y arquitectura), consulta el [README](README.md).

## Índice

- [Resumen rápido](#resumen-rápido)
- [Requisitos](#requisitos)
- [Instalar el software](#instalar-el-software)
- [Descargar el proyecto](#descargar-el-proyecto)
- [Crear la base de datos](#crear-la-base-de-datos)
- [Configurar la contraseña](#configurar-la-contraseña)
- [Ejecutar la API](#ejecutar-la-api)
- [Probar la API](#probar-la-api)
- [Detener, reiniciar y restablecer](#detener-reiniciar-y-restablecer)
- [Solución de problemas](#solución-de-problemas)

## Resumen rápido

Si ya tienes instalados **Git**, **Java 17 o superior** y **PostgreSQL**, estos son todos los pasos.

**Windows (PowerShell)**

```powershell
git clone https://github.com/DaniCaste10/wine-community-api.git
cd wine-community-api\winecommunity
setx DB_PASSWORD "tu_contraseña_de_postgres"
```

Después, crea la base de datos `wine_community`, **cierra y vuelve a abrir la terminal** y ejecuta:

```powershell
.\mvnw spring-boot:run
```

**Linux (Ubuntu/Debian)**

```bash
git clone https://github.com/DaniCaste10/wine-community-api.git
cd wine-community-api/winecommunity
sudo -u postgres psql -c "CREATE DATABASE wine_community;"
export DB_PASSWORD='tu_contraseña_de_postgres'
chmod +x mvnw
./mvnw spring-boot:run
```

Cuando la consola muestre `Started WinecommunityApplication`, la API está disponible en `http://localhost:8080`. Si es la primera vez que haces esto, sigue la guía completa a continuación.

## Requisitos

| Software | Versión | Para qué se usa | Dónde descargarlo |
|---|---|---|---|
| **Git** | Cualquiera reciente | Descargar el proyecto (opcional: también se puede bajar como ZIP) | https://git-scm.com/downloads |
| **Java JDK** | 17 o superior | Compilar y ejecutar la aplicación | https://adoptium.net/ |
| **PostgreSQL** | Desarrollado con la 18 | Base de datos | https://www.postgresql.org/download/ |
| **Postman** | Cualquiera reciente | Enviar peticiones a la API (alternativa en Linux: `curl`) | https://www.postman.com/downloads/ |

Además:

- **No hace falta instalar Maven.** El proyecto incluye el Maven Wrapper (`mvnw`), que descarga la versión necesaria la primera vez que se ejecuta.
- **Se necesita conexión a Internet** en el primer arranque, para que el wrapper descargue Maven y las dependencias del proyecto.
- **Dos puertos libres**: el `5432` (PostgreSQL) y el `8080` (la API).

## Instalar el software

### Git

**Windows**

1. Descarga el instalador desde https://git-scm.com/download/win.
2. Ejecútalo y acepta las opciones que propone por defecto.
3. Abre una terminal **nueva** (PowerShell) y comprueba la instalación:

```powershell
git --version
```

**Linux (Ubuntu/Debian)**

```bash
sudo apt update
sudo apt install -y git
git --version
```

### Java (JDK 17 o superior)

Se necesita el **JDK** (kit de desarrollo), no solo el JRE (el entorno de ejecución).

**Windows**

1. Entra en https://adoptium.net/ y descarga **Temurin 17 (LTS)** para Windows, en formato `.msi`.
2. Ejecuta el instalador. En la pantalla de características (*Custom Setup*), activa las opciones **Add to PATH** y **Set JAVA_HOME variable**.
3. Termina la instalación y abre una terminal **nueva**. Comprueba que está instalado:

```powershell
java -version
javac -version
```

Ambos comandos deben mostrar una versión 17 o superior.

**Linux (Ubuntu/Debian)**

```bash
sudo apt update
sudo apt install -y openjdk-17-jdk
java -version
javac -version
```

Si tu distribución no incluye la versión 17 en sus repositorios, instala `openjdk-21-jdk`, que también es compatible con el proyecto.

### PostgreSQL

**Windows**

1. Entra en https://www.postgresql.org/download/windows/ y pulsa **Download the installer**. Descarga la versión más reciente (el proyecto se desarrolló con la 18).
2. Ejecuta el instalador y avanza por el asistente:
   - **Installation Directory**: deja la ruta por defecto.
   - **Select Components**: deja marcados *PostgreSQL Server*, *pgAdmin 4* y *Command Line Tools*. *Stack Builder* no hace falta.
   - **Data Directory**: deja la ruta por defecto.
   - **Password**: elige la contraseña del usuario administrador `postgres` y **anótala**: la necesitarás más adelante.
   - **Port**: deja `5432`.
   - **Locale**: deja el valor por defecto.
3. Al terminar, el instalador puede ofrecer abrir **Stack Builder**. Ciérralo con **Cancel**: no es necesario para este proyecto.
4. PostgreSQL se instala como un servicio de Windows y arranca automáticamente. Para comprobarlo, abre `services.msc` y busca un servicio llamado `postgresql-x64-18` (el número final es la versión) con el estado **En ejecución**.

**Linux (Ubuntu/Debian)**

```bash
sudo apt update
sudo apt install -y postgresql postgresql-contrib
```

El servicio arranca automáticamente. Comprueba que el clúster está activo:

```bash
pg_lsclusters
```

La columna `Status` debe mostrar `online`. Si no es así, arráncalo con `sudo systemctl start postgresql`.

A continuación, asigna una contraseña al usuario `postgres` (sustituye `tu_contraseña` por la que quieras usar) y anótala:

```bash
sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'tu_contraseña';"
```

### Postman

Postman permite enviar peticiones HTTP a la API sin escribir código.

**Windows**

1. Descarga el instalador desde https://www.postman.com/downloads/ y ejecútalo.
2. Puedes usarlo **sin crear una cuenta**: en la pantalla de inicio busca la opción para omitir el inicio de sesión (suele aparecer como *Skip and go to the app*).

**Linux**

Descarga el paquete desde https://www.postman.com/downloads/ o, en Ubuntu, instálalo con:

```bash
sudo snap install postman
```

Si prefieres no instalar Postman, en Linux también puedes probar la API con `curl` (véase [Probar la API](#probar-la-api)).

## Descargar el proyecto

El código está en un repositorio de GitHub. Hay dos formas de obtenerlo.

### Opción A: clonar el repositorio con Git (recomendada)

1. Abre la página del repositorio en GitHub, pulsa el botón verde **Code**, selecciona la pestaña **HTTPS** y copia la URL. Tiene la forma `https://github.com/DaniCaste10/wine-community-api.git`.
2. Elige una carpeta donde guardar el proyecto. Es preferible que la ruta **no contenga espacios ni acentos** y que **no esté dentro de una carpeta sincronizada con OneDrive** (como `Escritorio` o `Documentos`), porque la sincronización puede bloquear ficheros.
3. Clona el repositorio.

**Windows (PowerShell)**

```powershell
mkdir C:\proyectos
cd C:\proyectos
git clone https://github.com/DaniCaste10/wine-community-api.git
cd wine-community-api\winecommunity
```

**Linux**

```bash
mkdir -p ~/proyectos
cd ~/proyectos
git clone https://github.com/DaniCaste10/wine-community-api.git
cd wine-community-api/winecommunity
```

### Opción B: descargar un ZIP (sin Git)

1. En la página del repositorio en GitHub, pulsa **Code** y después **Download ZIP**.
2. Descomprime el archivo en una carpeta que cumpla las recomendaciones anteriores. Se creará una carpeta llamada `wine-community-api-main`.
   - **Windows**: clic derecho sobre el ZIP y **Extraer todo...**.
   - **Linux**: `sudo apt install -y unzip` y después `unzip wine-community-api-main.zip`.
3. Entra en la subcarpeta `winecommunity`.

### Estructura del repositorio

```
wine-community-api/
├── README.md                 Diseño de la API
├── INSTALACION.md            Esta guía
└── winecommunity/            Proyecto de la API
    ├── pom.xml
    ├── mvnw / mvnw.cmd       Maven Wrapper
    └── src/
```

**Todos los comandos de las secciones siguientes se ejecutan desde la carpeta `winecommunity`**, la que contiene el fichero `pom.xml`.

## Crear la base de datos

La aplicación se conecta a una base de datos llamada **`wine_community`**. Hay que crearla vacía: las tablas las genera la propia aplicación al arrancar.

### Con pgAdmin (Windows)

1. Abre **pgAdmin 4** desde el menú de inicio. La primera vez te pide una contraseña maestra, que solo protege pgAdmin y puede ser cualquiera.
2. En el árbol de la izquierda, expande **Servers** y haz clic en tu servidor PostgreSQL. Introduce la contraseña del usuario `postgres` que elegiste al instalar.
3. Haz clic derecho sobre **Databases** y selecciona **Create** y después **Database...**.
4. En el campo **Database** escribe `wine_community` y pulsa **Save**.

### Con la línea de comandos

**Windows**

1. Abre **SQL Shell (psql)** desde el menú de inicio.
2. Pulsa **Enter** en las preguntas `Server`, `Database`, `Port` y `Username` para aceptar los valores por defecto (`localhost`, `postgres`, `5432` y `postgres`).
3. Introduce la contraseña del usuario `postgres` (no se muestra mientras escribes).
4. Ejecuta:

```sql
CREATE DATABASE wine_community;
```

5. Para salir, escribe `\q`.

**Linux**

```bash
sudo -u postgres psql -c "CREATE DATABASE wine_community;"
```

### Comprobarlo

En SQL Shell (Windows) escribe `\l`; en Linux ejecuta:

```bash
sudo -u postgres psql -c "\l"
```

En la lista de bases de datos debe aparecer `wine_community`.

## Configurar la contraseña

La aplicación lee la contraseña de PostgreSQL de una **variable de entorno** llamada `DB_PASSWORD`, de modo que la contraseña nunca aparece escrita en el código. La configuración de conexión está en `winecommunity/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/wine_community
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

Si tu PostgreSQL usa otro usuario, otro puerto o una base de datos con otro nombre, modifica las líneas `url` y `username`. No escribas la contraseña directamente en este fichero.

### Windows (PowerShell)

Para que la variable quede guardada de forma permanente (sustituye el valor por tu contraseña):

```powershell
setx DB_PASSWORD "tu_contraseña_de_postgres"
```

Debe responder `CORRECTO: se guardó el valor especificado.` La variable **no estará disponible en las terminales que ya estén abiertas**: cierra todas las terminales y Visual Studio Code, abre una nueva y comprueba que se ha guardado:

```powershell
echo $env:DB_PASSWORD
```

Debe mostrar tu contraseña. Si solo quieres definirla para la terminal actual, sin guardarla, usa en su lugar:

```powershell
$env:DB_PASSWORD = "tu_contraseña_de_postgres"
```

En ese caso, arranca la API desde esa misma terminal.

### Linux

Para la terminal actual (sustituye el valor por tu contraseña):

```bash
export DB_PASSWORD='tu_contraseña_de_postgres'
```

Para que se mantenga en futuras sesiones, añádela a tu perfil (si usas `zsh`, cambia `~/.bashrc` por `~/.zshrc`):

```bash
echo "export DB_PASSWORD='tu_contraseña_de_postgres'" >> ~/.bashrc
source ~/.bashrc
```

Comprueba que está definida:

```bash
echo $DB_PASSWORD
```

## Ejecutar la API

Desde la carpeta `winecommunity` (la que contiene `pom.xml`):

**Windows (PowerShell)**

```powershell
.\mvnw spring-boot:run
```

En el símbolo del sistema clásico (`cmd`), el comando es `mvnw.cmd spring-boot:run`. En PowerShell hay que escribir el `.\` delante.

**Linux**

La primera vez, da permiso de ejecución al wrapper (los ficheros descargados pueden no conservarlo):

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

### Qué ocurre al arrancar

- **La primera vez** tarda varios minutos: el wrapper descarga Maven y todas las dependencias del proyecto. Las siguientes veces arranca en unos segundos.
- La aplicación se conecta a PostgreSQL y **crea las tablas automáticamente** (`usuarios`, `vinos`, `vino_uvas`, `usuario_vinos`, `seguidores` y `solicitudes_seguimiento`).
- La consola muestra las consultas SQL que ejecuta, porque está activada la opción `spring.jpa.show-sql`.

El arranque ha terminado correctamente cuando aparecen líneas como estas (los tiempos varían):

```
Tomcat started on port 8080 (http) with context path '/'
Started WinecommunityApplication in 4.057 seconds (process running for 4.471)
```

La terminal se queda ocupada mientras la API está en marcha. Es lo normal: **no la cierres**. Si necesitas ejecutar otros comandos, abre una terminal nueva.

### Comprobar que responde

Con la API en marcha, abre una **segunda terminal** y ejecuta:

**Windows**

```powershell
curl.exe -i http://localhost:8080/usuarios
```

**Linux**

```bash
curl -i http://localhost:8080/usuarios
```

La respuesta esperada es `HTTP/1.1 404`. Como la base de datos está vacía, el listado de usuarios no encuentra resultados y la API responde con un `404 Not Found`. Es el comportamiento correcto: significa que la API funciona.

### Alternativa: generar y ejecutar el JAR

También se puede compilar la aplicación en un único fichero y ejecutarlo con Java:

```bash
./mvnw package -DskipTests
java -jar target/winecommunity-0.0.1-SNAPSHOT.jar
```

En Windows, usa `.\mvnw package -DskipTests` y `java -jar target\winecommunity-0.0.1-SNAPSHOT.jar`. La opción `-DskipTests` omite el test del proyecto. Ese test arranca la aplicación completa, por lo que necesita PostgreSQL y la variable `DB_PASSWORD`. Con la base de datos configurada puedes ejecutarlo con `./mvnw test` (en Windows, `.\mvnw test`).

## Probar la API

Con la API en marcha, ya se pueden enviar peticiones a `http://localhost:8080`. En esta sección hay una prueba guiada con **11 ejemplos** que recorren todas las funcionalidades.

### Cómo enviar una petición con Postman

1. Abre una pestaña de petición nueva.
2. Elige el **método** (`GET`, `POST`, `PUT` o `DELETE`) en el desplegable situado a la izquierda de la barra de dirección y escribe la **URL** completa, por ejemplo `http://localhost:8080/usuarios`. Los parámetros de consulta (`?nombre=mar`) se escriben directamente en la URL, y Postman codifica los espacios automáticamente.
3. En las peticiones con cuerpo (`POST` y `PUT`): abre la pestaña **Body**, marca **raw**, elige **JSON** en el desplegable de la derecha y pega el JSON del ejemplo.
4. Pulsa **Send**.
5. En el panel inferior verás el **código de estado** (arriba a la derecha) y, en la pestaña **Body**, la respuesta. Las cabeceras, como `Location`, están en la pestaña **Headers**.

### Cómo enviar una petición con curl (Linux)

El primer ejemplo del apartado siguiente sería:

```bash
curl -i -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombreUsuario": "mario123", "fechaNacimiento": "1995-06-15", "email": "mario@email.com"}'
```

La opción `-i` muestra el código de estado y las cabeceras. Para una petición `GET`, basta con `curl -i "http://localhost:8080/usuarios?nombre=mar"` (con comillas cuando la URL lleva `?` o `&`). En Windows recomendamos usar Postman, porque el entrecomillado de JSON en PowerShell es delicado.

### Antes de empezar

Los ejemplos suponen una **base de datos recién creada**, porque se apoyan unos en otros y dependen de los identificadores que asigna la base de datos (`1`, `2`...). Si ya has enviado peticiones antes, vacía las tablas y reinicia los identificadores ejecutando esta sentencia SQL en pgAdmin (clic derecho sobre `wine_community` y **Query Tool**), en SQL Shell o con `psql`:

```sql
TRUNCATE usuarios, vinos, vino_uvas, usuario_vinos, seguidores, solicitudes_seguimiento RESTART IDENTITY CASCADE;
```

En Linux, el mismo comando en una sola línea:

```bash
sudo -u postgres psql -d wine_community -c "TRUNCATE usuarios, vinos, vino_uvas, usuario_vinos, seguidores, solicitudes_seguimiento RESTART IDENTITY CASCADE;"
```

Tres consideraciones para comparar tus resultados con los de los ejemplos:

- **Sigue el orden** de los ejemplos, del 1 al 11.
- Las fechas (`fechaAdicion` y `fechaSolicitud`) de tus respuestas serán **la fecha del día en que las ejecutes**, no la que aparece en los ejemplos.
- Las respuestas largas se muestran **resumidas con `...`**, y el orden de las propiedades del JSON puede variar.

### 1. Alta de usuarios

```http
POST /usuarios
Content-Type: application/json

{
    "nombreUsuario": "mario123",
    "fechaNacimiento": "1995-06-15",
    "email": "mario@email.com"
}
```

```http
HTTP/1.1 201 Created
Location: http://localhost:8080/usuarios/1
```

La respuesta no lleva cuerpo. Si se repite la petición con `ana456` (`1998-03-20`, `ana@email.com`), el usuario creado recibe el identificador 2.

**Alta de un menor de edad.** Con una fecha de nacimiento que no alcanza los 18 años:

```http
POST /usuarios
Content-Type: application/json

{
    "nombreUsuario": "joven01",
    "fechaNacimiento": "2015-01-01",
    "email": "joven@email.com"
}
```

```http
HTTP/1.1 400 Bad Request

El usuario debe ser mayor de edad
```

### 2. Consultar un usuario

```http
GET /usuarios/1
```

```json
{
    "_links": {
        "self":        { "href": "http://localhost:8080/usuarios/1" },
        "vinos":       { "href": "http://localhost:8080/usuarios/1/vinos?page=0&size=10{&desde,hasta,tipo,bodega,origen,anada,uva}", "templated": true },
        "seguidos":    { "href": "http://localhost:8080/usuarios/1/seguidos?page=0&size=10{&nombre}", "templated": true },
        "solicitudes": { "href": "http://localhost:8080/usuarios/1/solicitudes?page=0&size=10" }
    },
    "email": "mario@email.com",
    "fechaNacimiento": "1995-06-15",
    "id": 1,
    "nombreUsuario": "mario123"
}
```

Si el usuario no existe, por ejemplo con `GET /usuarios/99`, la respuesta es `404 Not Found` sin cuerpo.

### 3. Buscar usuarios por nombre

```http
GET /usuarios?nombre=ana
```

```json
{
    "_embedded": {
        "usuarioList": [
            {
                "_links": { ... },
                "email": "ana@email.com",
                "fechaNacimiento": "1998-03-20",
                "id": 2,
                "nombreUsuario": "ana456"
            }
        ]
    },
    "_links": {
        "self": { "href": "http://localhost:8080/usuarios?nombre=ana&page=0&size=10" }
    },
    "page": { "number": 0, "size": 10, "totalElements": 1, "totalPages": 1 }
}
```

Si ningún usuario contiene el texto buscado (por ejemplo, `GET /usuarios?nombre=zzz`), la respuesta es `404 Not Found`.

### 4. Añadir un vino a la lista y consultarlo

```http
POST /usuarios/1/vinos?puntuacion=8.5
Content-Type: application/json

{
    "nombre": "Vega Sicilia Único",
    "bodega": "Vega Sicilia",
    "anada": 2015,
    "origen": "Ribera del Duero",
    "tipo": "Tinto",
    "uvas": [
        { "nombreUva": "Tempranillo", "porcentaje": 85.0 },
        { "nombreUva": "Cabernet Sauvignon", "porcentaje": 15.0 }
    ]
}
```

```http
HTTP/1.1 201 Created
```

La respuesta incluye la cabecera `Location` y no lleva cuerpo. Para consultar la lista:

```http
GET /usuarios/1/vinos
```

```json
{
    "_embedded": {
        "usuarioVinoList": [
            {
                "_links": {
                    "self": { "href": "http://localhost:8080/usuarios/1/vinos?page=0&size=10{&desde,hasta,tipo,bodega,origen,anada,uva}", "templated": true }
                },
                "fechaAdicion": "2026-04-14",
                "id": 1,
                "puntuacion": 8.5,
                "usuario": {
                    "email": "mario@email.com",
                    "fechaNacimiento": "1995-06-15",
                    "id": 1,
                    "nombreUsuario": "mario123"
                },
                "vino": {
                    "anada": 2015,
                    "bodega": "Vega Sicilia",
                    "id": 1,
                    "nombre": "Vega Sicilia Único",
                    "origen": "Ribera del Duero",
                    "tipo": "Tinto",
                    "uvas": [
                        { "nombreUva": "Tempranillo", "porcentaje": 85.0 },
                        { "nombreUva": "Cabernet Sauvignon", "porcentaje": 15.0 }
                    ]
                }
            }
        ]
    },
    "_links": {
        "self": { "href": "http://localhost:8080/usuarios/1/vinos?page=0&size=10" }
    },
    "page": { "number": 0, "size": 10, "totalElements": 1, "totalPages": 1 }
}
```

Observa que el campo `id` de la entrada de la lista (`1`) es el que se usa para modificar la puntuación o eliminar el vino de la lista.

**Con filtros.** `GET /usuarios/1/vinos?tipo=Tinto&anada=2015` devuelve el mismo resultado, porque el vino cumple ambas condiciones. En cambio, `GET /usuarios/1/vinos?tipo=Blanco` no encuentra ningún vino y devuelve una página vacía:

```json
{
    "_links": {
        "self": { "href": "http://localhost:8080/usuarios/1/vinos?tipo=Blanco&page=0&size=10" }
    },
    "page": { "number": 0, "size": 10, "totalElements": 0, "totalPages": 0 }
}
```

### 5. Modificar la puntuación de un vino

```http
PUT /usuarios/1/vinos/1/puntuacion?puntuacion=9.5
```

```json
{
    "_links": {
        "self": { "href": "http://localhost:8080/usuarios/1/vinos?page=0&size=10{&desde,hasta,tipo,bodega,origen,anada,uva}", "templated": true }
    },
    "fechaAdicion": "2026-04-14",
    "id": 1,
    "puntuacion": 9.5,
    "usuario": { ... },
    "vino": { ... }
}
```

La puntuación pasa de `8.5` a `9.5`, y la fecha de adición y el resto de datos no cambian. El segundo `1` de la URI es el identificador de la entrada de la lista.

### 6. Estadísticas de puntuaciones

```http
GET /usuarios/1/vinos/estadisticas
```

```
9.5
```

La respuesta es la nota media de los vinos del usuario, que en este caso es la del único vino puntuado. Con un filtro que no coincide con ningún vino, como `GET /usuarios/1/vinos/estadisticas?tipo=Blanco`, la respuesta es `0.0`.

### 7. Solicitar, consultar y aceptar un seguimiento

**Mario solicita seguir a Ana:**

```http
POST /usuarios/1/solicitudes
Content-Type: application/json

{
    "seguidoId": 2
}
```

```http
HTTP/1.1 201 Created
```

**Ana consulta sus solicitudes pendientes:**

```http
GET /usuarios/2/solicitudes
```

```json
{
    "_embedded": {
        "solicitudSeguimientoList": [
            {
                "_links": {
                    "self": { "href": "http://localhost:8080/usuarios/2/solicitudes?page=0&size=10" }
                },
                "estado": "PENDIENTE",
                "fechaSolicitud": "2026-04-14",
                "id": 1,
                "receptor": {
                    "email": "ana@email.com",
                    "fechaNacimiento": "1998-03-20",
                    "id": 2,
                    "nombreUsuario": "ana456"
                },
                "solicitante": {
                    "email": "mario@email.com",
                    "fechaNacimiento": "1995-06-15",
                    "id": 1,
                    "nombreUsuario": "mario123"
                }
            }
        ]
    },
    "_links": {
        "self": { "href": "http://localhost:8080/usuarios/2/solicitudes?page=0&size=10" }
    },
    "page": { "number": 0, "size": 10, "totalElements": 1, "totalPages": 1 }
}
```

**Ana acepta la solicitud** (el `1` final de la URI es el `id` de la solicitud):

```http
PUT /usuarios/2/solicitudes/1/aceptar
```

```json
{
    "estado": "ACEPTADA",
    "fechaSolicitud": "2026-04-14",
    "id": 1,
    "receptor": {
        "email": "ana@email.com",
        "fechaNacimiento": "1998-03-20",
        "id": 2,
        "nombreUsuario": "ana456"
    },
    "solicitante": {
        "email": "mario@email.com",
        "fechaNacimiento": "1995-06-15",
        "id": 1,
        "nombreUsuario": "mario123"
    }
}
```

**Mario ya sigue a Ana**, así que ella aparece en su lista de seguidos:

```http
GET /usuarios/1/seguidos
```

```json
{
    "_embedded": {
        "usuarioList": [
            {
                "_links": { ... },
                "email": "ana@email.com",
                "fechaNacimiento": "1998-03-20",
                "id": 2,
                "nombreUsuario": "ana456"
            }
        ]
    },
    "_links": {
        "self": { "href": "http://localhost:8080/usuarios/1/seguidos?page=0&size=10" }
    },
    "page": { "number": 0, "size": 10, "totalElements": 1, "totalPages": 1 }
}
```

### 8. Rechazar una solicitud

Ahora es Ana quien solicita seguir a Mario, y Mario rechaza la solicitud.

```http
POST /usuarios/2/solicitudes
Content-Type: application/json

{
    "seguidoId": 1
}
```

```http
HTTP/1.1 201 Created
```

Mario consulta sus solicitudes pendientes (`GET /usuarios/1/solicitudes`), donde aparece la nueva solicitud con `id` 2, y la rechaza:

```http
PUT /usuarios/1/solicitudes/2/rechazar
```

```json
{
    "estado": "RECHAZADA",
    "fechaSolicitud": "2026-04-14",
    "id": 2,
    "receptor": { "id": 1, "nombreUsuario": "mario123", ... },
    "solicitante": { "id": 2, "nombreUsuario": "ana456", ... }
}
```

La solicitud pasa a `RECHAZADA` y no se crea ninguna relación de seguimiento: `GET /usuarios/1/solicitudes` devuelve ahora una página vacía y Ana no aparece como seguidora de Mario.

### 9. Recomendaciones personalizadas

```http
GET /usuarios/1/vinos/recomendaciones
```

```json
{
    "usuario": {
        "email": "mario@email.com",
        "fechaNacimiento": "1995-06-15",
        "id": 1,
        "nombreUsuario": "mario123"
    },
    "ultimosVinos": [
        {
            "fechaAdicion": "2026-04-14",
            "id": 1,
            "puntuacion": 9.5,
            "usuario": { ... },
            "vino": { "nombre": "Vega Sicilia Único", ... }
        }
    ],
    "topVinos": [
        {
            "fechaAdicion": "2026-04-14",
            "id": 1,
            "puntuacion": 9.5,
            "usuario": { ... },
            "vino": { "nombre": "Vega Sicilia Único", ... }
        }
    ],
    "topVinosAmigos": []
}
```

Mario solo tiene un vino, por lo que aparece tanto entre sus últimos vinos como entre los mejor puntuados. La lista `topVinosAmigos` está vacía porque Ana, a quien sigue, todavía no ha puntuado ningún vino. En cuanto lo haga, sus mejores vinos aparecerán en esa lista.

### 10. Paginación y navegación

Con los dos usuarios creados, se pide una página de un solo elemento:

```http
GET /usuarios?size=1
```

```json
{
    "_embedded": { "usuarioList": [ { "id": 1, "nombreUsuario": "mario123", ... } ] },
    "_links": {
        "first": { "href": "http://localhost:8080/usuarios?page=0&size=1" },
        "self":  { "href": "http://localhost:8080/usuarios?page=0&size=1" },
        "next":  { "href": "http://localhost:8080/usuarios?page=1&size=1" },
        "last":  { "href": "http://localhost:8080/usuarios?page=1&size=1" }
    },
    "page": { "number": 0, "size": 1, "totalElements": 2, "totalPages": 2 }
}
```

Al seguir el enlace `next` se obtiene la segunda página, con `ana456`. En ella aparece el enlace `prev` y desaparece `next`, porque ya es la última página. El cliente puede recorrer todo el listado sin construir ninguna URL.

### 11. Borrar un vino y dejar de seguir a un usuario

**Borrar un vino de la lista:**

```http
DELETE /usuarios/1/vinos/1
```

```http
HTTP/1.1 204 No Content
```

Si la entrada no existe, la respuesta es `404 Not Found`.

**Dejar de seguir a un usuario** (Mario deja de seguir a Ana):

```http
DELETE /usuarios/1/seguidos/2
```

```http
HTTP/1.1 204 No Content
```

Ahora `GET /usuarios/1/seguidos` devuelve una página vacía, porque Mario ya no sigue a nadie.


## Detener, reiniciar y restablecer

**Detener la API.** Haz clic en la terminal donde se está ejecutando y pulsa `Ctrl + C`. En PowerShell puede preguntar `¿Desea terminar el trabajo por lotes (S/N)?`: responde `S` y pulsa Enter.

**Volver a arrancarla.** Repite el comando de [Ejecutar la API](#ejecutar-la-api). Los datos se conservan, porque están en PostgreSQL y no en la aplicación.

**Borrar todos los datos** (sin eliminar la base de datos). Ejecuta la sentencia `TRUNCATE` de [Antes de empezar](#antes-de-empezar). Puede hacerse con la API en marcha.

**Recrear la base de datos desde cero.** Detén la API y ejecuta, como administrador de PostgreSQL:

```sql
DROP DATABASE wine_community;
CREATE DATABASE wine_community;
```

Al volver a arrancar la API, se crearán de nuevo las tablas.

**Actualizar el proyecto** si hay cambios nuevos en GitHub (desde la carpeta del repositorio):

```bash
git pull
```

## Solución de problemas

| Síntoma o mensaje | Causa probable | Solución |
|---|---|---|
| `Could not resolve placeholder 'DB_PASSWORD'` | La variable de entorno no está definida en la terminal desde la que arrancas la API. | Define la variable según [Configurar la contraseña](#configurar-la-contraseña). En Windows, después de `setx` hay que **cerrar y volver a abrir** la terminal y Visual Studio Code. Comprueba con `echo $env:DB_PASSWORD` (Windows) o `echo $DB_PASSWORD` (Linux). |
| `FATAL: password authentication failed for user "postgres"` | La contraseña de `DB_PASSWORD` no coincide con la del usuario `postgres`. | Corrige la variable de entorno. Si no recuerdas la contraseña, cámbiala: en Linux, `sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'nueva';"`; en Windows, con la herramienta SQL Shell o con pgAdmin. |
| `FATAL: database "wine_community" does not exist` | La base de datos no se ha creado. | Créala según [Crear la base de datos](#crear-la-base-de-datos). |
| `Connection to localhost:5432 refused` | PostgreSQL no está en marcha o usa otro puerto. | **Windows**: abre `services.msc`, busca `postgresql-x64-NN` y pulsa **Iniciar**. **Linux**: `sudo systemctl start postgresql`. Si usa otro puerto, cámbialo en `spring.datasource.url`. |
| `Port 8080 was already in use` | Otro programa (o una ejecución anterior de la API) ocupa el puerto 8080. | Busca el proceso y ciérralo. **Windows**: `netstat -ano \| findstr :8080` y después `taskkill /PID <PID> /F`. **Linux**: `ss -ltnp \| grep :8080` y después `kill <PID>`. También puedes cambiar el puerto añadiendo `server.port=8081` a `application.properties` (y usar `8081` en las URL). |
| `'java' no se reconoce...` o `java: command not found` | Java no está instalado o la terminal no lo encuentra. | Instala el JDK según [Instalar el software](#instalar-el-software) y abre una terminal nueva. Comprueba con `java -version`. |
| `JAVA_HOME is not defined correctly` | Maven no encuentra el JDK. | **Windows**: reinstala Temurin con la opción *Set JAVA_HOME variable* activada. **Linux**: `export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which javac))))`. |
| `release version 17 not supported` o `invalid target release: 17` | La versión de Java instalada es anterior a la 17. | Instala el JDK 17 o superior. |
| `mvnw : El término 'mvnw' no se reconoce...` (Windows) | Estás en una carpeta incorrecta o falta el `.\` delante. | Entra en la carpeta `winecommunity` (la que contiene `pom.xml`) y escribe `.\mvnw spring-boot:run`. |
| `./mvnw: Permission denied` (Linux) | El fichero `mvnw` no tiene permiso de ejecución. | `chmod +x mvnw` |
| `Could not transfer artifact` o `Connection timed out` al arrancar | Maven no puede descargar las dependencias. | Comprueba la conexión a Internet, el proxy o el cortafuegos. Se necesita acceso a `https://repo.maven.apache.org`. |
| `mvnw test` o `mvnw package` fallan con errores de conexión | El test del proyecto arranca la aplicación completa y necesita la base de datos. | Configura PostgreSQL y `DB_PASSWORD`, o usa `-DskipTests`. |
| `415 Unsupported Media Type` al enviar un `POST` o `PUT` | Falta indicar que el cuerpo es JSON. | En Postman: **Body**, **raw**, **JSON**. Con curl: `-H "Content-Type: application/json"`. |
| `400 Bad Request` al enviar un cuerpo | El JSON está mal formado (comas, comillas o llaves). | Revisa el JSON. Cópialo tal como aparece en los ejemplos. |
| `GET /usuarios` devuelve `404` | La base de datos está vacía. | Es el comportamiento esperado cuando no hay usuarios. |
