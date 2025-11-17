# ========================================================

# Proyecto TPI Programación II

# ========================================================

# 

# \# TPI – Programación II  

# \## Sistema de Gestión de Usuarios y Credenciales (Java + MySQL)

# 

# Este proyecto forma parte del Trabajo Práctico Integrador de la materia Programación II.  

# Implementa un sistema de gestión de usuarios basado en arquitectura por capas, con persistencia en MySQL y manejo de transacciones.

# 

# ------------------------------------

# FUNCIONALIDADES PRINCIPALES

# ------------------------------------

# ✔ Crear Usuario + CredencialAcceso  

# ✔ Listar usuarios activos  

# ✔ Buscar por email  

# ✔ Buscar por username  

# ✔ Buscar por ID  

# ✔ Actualizar datos  

# ✔ Eliminar (baja lógica)  

# ✔ Validaciones de negocio  

# ✔ Transacciones (commit/rollback)  

# ✔ Interfaz por consola (AppMenu)

# 

# ------------------------------------

# ARQUITECTURA

# ------------------------------------

# La estructura sigue el patrón de capas:

# 

# AppMenu → Service → DAO → DatabaseConnection → MySQL

# 

# CARPETAS:

# src/

# &nbsp;├── main/AppMenu.java

# &nbsp;├── service/ (reglas de negocio + transacciones)

# &nbsp;├── dao/ (JDBC + consultas SQL)

# &nbsp;├── entities/ (clases del dominio)

# &nbsp;└── config/DatabaseConnection.java

# 

# ------------------------------------

# MODELO DE DATOS

# ------------------------------------

# Tablas:

# \- Usuario

# \- CredencialAcceso (FK UNIQUE usuario\_id)

# 

# Ambas poseen el campo 'eliminado' para baja lógica.

# 

# Scripts incluidos:

# \- creacion\_bd.sql

# \- inserts\_tabla\_usuario.sql

# \- inserts\_tabla\_credencialacceso.sql

# 

# ------------------------------------

# REQUISITOS

# ------------------------------------

# \- Java 17+

# \- NetBeans 18+

# \- MySQL Server 8.x

# \- MySQL Workbench

# \- mysql-connector-j-9.0+

# 

# ------------------------------------

# CÓMO EJECUTAR

# ------------------------------------

# 1\. Crear base de datos con creacion\_bd.sql

# 2\. Ejecutar inserts iniciales

# 3\. Configurar credenciales en DatabaseConnection.java:

# &nbsp;  URL, USER, PASS

# 4\. Agregar mysql-connector-j a las Libraries del proyecto

# 5\. Ejecutar AppMenu.java

# 

# ------------------------------------

# PRUEBAS RECOMENDADAS

# ------------------------------------

# \- Crear usuario + credencial  

# \- Listar usuarios  

# \- Buscar por campos  

# \- Actualizar  

# \- Eliminar (baja lógica)  

# \- Verificar rollback ante error  

# 

# ------------------------------------

# TECNOLOGÍAS

# ------------------------------------

# Java – JDBC – MySQL – NetBeans – UMLet – GitHub – SQL



# ------------------------------------

# EQUIPO

# ------------------------------------

# AMIRA YASMIN ELIZABETH ESPER – Diseño y Entidades 

# LUCAS NAHUEL DUGO – Base de Datos (MySQL) 

# MANUEL DA CORTA – DAO

# AGUSTIN ECHEVERRIA ARAYA – Service y AppMenu

# 

# ------------------------------------

# INTELIGENCIA ARTIFICIAL UTILIZADA

# ------------------------------------

# Se utilizó ChatGPT como herramienta de apoyo en explicación teórica, 

# formateo del informe, armado del README y revisión del código.  

# La implementación final es del equipo.

# 

# ------------------------------------

# LICENCIA

# ------------------------------------

# Uso académico

