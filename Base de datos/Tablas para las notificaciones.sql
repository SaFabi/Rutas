CREATE TABLE token_usuarios(
id int auto_increment not null primary key,
token varchar(400) not null,
usuario_id  int not null
);

CREATE TABLE token_clientes(
id int auto_increment not null primary key,
token varchar(400)not null,
claveCliente_id int not null
);