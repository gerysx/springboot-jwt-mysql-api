

# Guía explicativa API Rest Springboot + JWT + MySQL



## ***1. Introducción***

Este proyecto es una API REST construida con Spring Boot que ofrece operaciones CRUD sobre entidades Product y User, utilizando:

Spring Security con JWT para autenticación y autorización.

CORS configurado para permitir llamadas desde cualquier origen.

JPA/Hibernate con MySQL para persistencia.

Validaciones a nivel de Bean Validation y validaciones personalizadas.

El siguiente documento detalla cada componente, su propósito y cómo interactúan entre sí.

## ***2. Arquitectura y Flujo General***

[ Cliente ]
    │
    │ HTTP(S) requests (JSON)
    ▼
[ Controladores REST ] ──> [ Servicios ] ──> [ Repositorios ] ──> [ Base de Datos ]
    │                         ▲
    └─ Filtros JWT/CORS       │
                              ▼
                       [ Seguridad (Spring Security) ]

Cliente envía solicitudes HTTP con JSON.

Filtros de Seguridad (JWTAuthentication / JWTValidation) previenen acceso no autorizado.

Controllers exponen endpoints y validan peticiones.

Services implementan lógica de negocio, gestionan transacciones y encriptan contraseñas.

Repositories usan CrudRepository para acceder a la base de datos MySQL.

## ***3. Configuración de la Aplicación***

***3.1 application.properties***

Define conexión MySQL: URL (jdbc:mysql://localhost:3306/db_jpa_crud), usuario, contraseña y driver.

Configura Hibernate: dialecto MySQL y muestra SQL en consola.

***3.2 AppConfig & messages.properties***

AppConfig carga el archivo de mensajes de validación.

messages.properties contiene textos personalizados para validaciones (NotNull.product.price, Min.product.price, etc.).

## ***4. Seguridad: JWT y CORS***

***4.1 SpringSecurityConfig***

Beans:

`AuthenticationManager`: gestiona autenticación por username/password.

``PasswordEncoder`` (BCrypt): encripta contraseñas.

``SecurityFilterChain``:

Permite endpoints públicos (/api/users, /api/users/register).

Requiere autenticación para el resto.

Añade filtros JWT para login y validación.

Deshabilita CSRF y establece sesión stateless.

***4.2 JWT Flow***

Login: JwtAuthenticationFilter lee JSON con credenciales, delega en AuthenticationManager.

Generación: si es exitosa, construye un JWT firmando con TokenJwtConfig.SECRET_KEY, incluye claims (username, roles) y lo envía en cabecera Authorization: Bearer <token>.

Acceso: en peticiones posteriores, JwtValidationFilter extrae token de la cabecera, lo valida y carga autoridad en SecurityContext.

***4.3 CORS***

Configurado en SpringSecurityConfig con CorsConfigurationSource y CorsFilter: permite métodos GET, POST, DELETE y PUT desde cualquier origen.

## ***5. Capa de Entidades y Persistencia***

***5.1 Entidades JPA***

``Product``: campos id, sku, name, price, description. Validaciones con Bean Validation y anotación personalizada @IsRequired.

``User``: campos id, username, password, roles, enabled, admin. Relaciones many-to-many con Role.  PrePersist activa enabled=true.

``Role``: campos id, name, lista de users.

***5.2 Repositorios***

``ProductRepository`` (CRUD) con método adicional ``existsBySku``.

``UserRepository`` con ``existsByUsername`` y ``findByUsername.``

``RoleRepository`` con ``findByName.``

## ***6. Servicios (Lógica de Negocio)***

***6.1 ProductService / ProductServiceImpl***

Métodos CRUD: ``findAll()``, ``findById(id)``, ``save()``, ``update(id, entity)``, ``delete(id)``.

Verificación de existencia de SKU.

***6.2 UserService / UserServiceImpl***

Listar y guardar usuarios.

Durante ``save()``, asigna roles: siempre ``ROLE_USER``, y si admin==true, añade ``ROLE_ADMIN``.

Encripta contraseña con ``PasswordEncoder``.

***6.3 JpaUserDetailsService***

Implementa ``UserDetailsService`` para Spring Security.

En ``loadUserByUsername()``, obtiene User de BD, convierte Role a GrantedAuthority y construye ``UserDetails``.

## ***7. Validaciones***

***7.1 Bean Validation****

Anotaciones estándar: @NotNull, @Size, @Min.

Mensajes toman valores desde ``messages.properties``.

***7.2 Validaciones Personalizadas***

@IsRequired / RequiredValidation: comprueba texto no vacío ni solo espacios.

@ExistsByUsername / ExistsByUsernameValidation: verifica que username es único.

@IsExistsDb / IsExistsDbValidation: asegura que SKU no esté en BD.

***7.3 Validador Manual***

``ProductValidation`` (implementa Validator): reglas adicionales en controlador si se desea validación programática.

## ***8. Controladores REST***

``ProductController`` (/api/products): operaciones CRUD, seguras con @PreAuthorize según roles ADMIN o USER.

``UserController`` (/api/users): listar usuarios; crear usuarios (ADMIN) y registro (/register) con admin=false.

Cada método devuelve ResponseEntity con código HTTP adecuado (200, 201, 400, 404).

## *9. Incio de la aplicación*

Clase ``SpringbootCrudApplication`` con @SpringBootApplication y método main().

Ejecuta con ``mvn spring-boot:run`` o generando JAR con mvn package.


**

## *10. Conclusión*


Este dossier proporciona una visión integral del proyecto, explicando cada capa y componente, su interacción y las razones de diseño. Sirve de guía para mantenimiento, ampliación o enseñanza de las buenas prácticas en APIs REST seguras con Spring Boot.
