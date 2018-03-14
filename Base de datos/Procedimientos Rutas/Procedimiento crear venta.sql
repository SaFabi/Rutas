use marquesada;
DROP PROCEDURE IF EXISTS procesoOrden;

/*PROCEDIMIENTO PARA GENERAR UNA ORDEN NUEVA*/
DELIMITER //
CREATE PROCEDURE procesoOrden(In folio varchar(20),In IDpuntoVentaComisiones int,In IDpuntoVentaInventario int, In clienteID int)

	BEGIN
    /* CONSULTA PARA SACAR EL ID DEL USUARIO  RESPONSABLE DEL PUNTO DE VENTA */
		SET @usuarioID := (SELECT usuario_id FROM puntoventa_usuario WHERE puntoVenta_id =IDpuntoVentaInventario);
		
	/* INSERTAR EN LA TABLA ORDEN */
       INSERT INTO orden(folio,fecha,cliente_id,puntoVenta_id,usuario_id)VALUES(folio,now(),clienteID,IDpuntoVentaComisiones,@usuarioID);
END
//

