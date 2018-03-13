use marquesada;
DROP PROCEDURE IF EXISTS terminarVentaContado;

/*PROCEDIMIENTO PARA TERMINAR UNA VENTA A CONTADO*/
DELIMITER //
CREATE PROCEDURE terminarVentaContadoClientes(In ordenID int)
	BEGIN
    /*CONSULTA PARA GENERAR EL TOTAL DE LA ORDEN */
   SET @total:=( SELECT SUM(od.cantidad * od.precio_final)
	FROM orden_descripcion od,orden o
	WHERE od.orden_id = o.id
	AND o.id = ordenID);
    
    /*INSERTAR EN ORDEN_COMPLETA*/
    INSERT INTO orden_completa(fecha,total,estado,orden_id)VALUES(now(),@total,1,ordenID);
END

//