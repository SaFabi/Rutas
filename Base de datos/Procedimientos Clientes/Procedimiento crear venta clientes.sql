use marquesada;
DROP PROCEDURE IF EXISTS procesoOrdenClientes;

/*PROCEDIMIENTO PARA GENERAR UNA ORDEN NUEVA*/
DELIMITER //
CREATE PROCEDURE procesoOrdenClientes(In folio varchar(20),In IDpuntoventa int, In clienteID int)

	BEGIN
    /* CONSULTA PARA SACAR EL ID DEL USUARIO  RESPONSABLE DEL PUNTO DE VENTA */
		SET @usuarioID := (SELECT usuario_id FROM puntoventa_usuario WHERE puntoVenta_id =IDpuntoventa);
		
	/* INSERTAR EN LA TABLA ORDEN */
       INSERT INTO orden(folio,fecha,cliente_id,puntoVenta_id,usuario_id)VALUES(folio,now(),clienteID,IDpuntoventa,@usuarioID);
END
//