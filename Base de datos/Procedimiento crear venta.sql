use marquesada;
DROP PROCEDURE IF EXISTS procesoOrden;

/*PROCEDIMIENTO PARA GENERAR UNA ORDEN NUEVA*/
DELIMITER //
CREATE PROCEDURE procesoOrden(In folio varchar(20),In puntoVentaID int, In clienteID int)

	BEGIN
    /* CONSULTA PARA SACAR EL ID DEL USUARIO  RESPONSABLE DEL PUNTO DE VENTA */
		SET @usuarioID := (SELECT usuario_id FROM puntoventa_usuario WHERE puntoVenta_id =puntoVentaID);
		
	/* INSERTAR EN LA TABLA ORDEN */
       INSERT INTO orden(folio,fecha,cliente_id,puntoVenta_id,usuario_id)VALUES(folio,now(),clienteID,puntoVentaID,@usuarioID);
       
	/* CONSULTA PARA  SACAR  EL ID DE LA ULTIMA ORDEN REGISTRADA DE ESE PUNTO DE ESE PUNTO DE VENTA*/
       SET @ordenID := (SELECT id FROM orden where puntoVenta_id = puntoVentaID  order by id desc limit 1);
    
    SELECT @ordenID;
END
//

