CREATE TABLE token_usuarios(
id INT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY ,
token  VARCHAR(400)  NOT NULL,
usuario_id  INT NOT NULL
);

CREATE TABLE token_clientes(
id int UNSIGNED auto_increment not null primary key,
token varchar(400)not null,
claveCliente_id int not null
);

CREATE TABLE buzon_sugerencia( 	
		id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
		mensaje VARCHAR(300) NOT NULL,
    	fecha DATETIME,
    	activo BOOL NOT NULL,
    	clavecliente_id INT NOT NULL REFERENCES clave_cliente.id ON UPDATE CASCADE
);

CREATE TABLE buzon_respuestas(
		id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
		respuesta VARCHAR(300) NOT NULL,
    	fecha DATETIME,
    	activo BOOL NOT NULL,
    	buzonSugerencia_id INT NOT NULL REFERENCES buzon_sugerencia.id ON UPDATE CASCADE

);

CREATE TABLE manual_usuario
(	id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	url VARCHAR(300) NOT NULL,
	acceso_id INT NOT NULL REFERENCES acceso.id ON UPDATE CASCADE
);

CREATE TABLE informacion_contacto
(	id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
   	telefono VARCHAR (20) NOT NULL,
    	correo VARCHAR (200) NOT NULL,
    	usuario_id INT NOT NULL REFERENCES usuario.id ON UPDATE CASCADE,
    	puntoVenta_id INT NOT NULL REFERENCES punto_venta.id ON UPDATE CASCADE
);

CREATE TABLE articulo_imagen
(	id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	url VARCHAR(300) NOT NULL,
	articulo_id INT NOT NULL REFERENCES articulo.id ON UPDATE CASCADE
);

