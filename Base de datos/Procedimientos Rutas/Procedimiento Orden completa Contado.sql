use marquesada;
DROP PROCEDURE IF EXISTS terminarVentaContado;

/*PROCEDIMIENTO PARA TERMINAR UNA VENTA A CONTADO*/
DELIMITER //
CREATE PROCEDURE terminarVentaContado(In ordenID int)
	BEGIN
    /*CONSULTA PARA GENERAR EL TOTAL DE LA ORDEN */
   SET @total:=( SELECT SUM(od.cantidad * od.precio_final)
	FROM orden_descripcion od,orden o
	WHERE od.orden_id = o.id
	AND o.id = ordenID);
    
    /*INSERTAR EN ORDEN_COMPLETA*/
    INSERT INTO orden_completa(fecha,total,estado,orden_id)VALUES(now(),@total,1,ordenID);
    
    /*CONSULTA PARA GENERAR EL TOTAL DE LA COMISION */
    SET @totalComision:=( SELECT sum(artc.total)
	FROM articulo_comision artc, orden o, orden_descripcion od
	WHERE artc.ordenDescripcion_id = od.id
	AND od.orden_id = o.id
	AND o.id = ordenID);
    
    /*INSERTAR EN TOTAL ARTICULO COMISION*/
    INSERT INTO totalarticulo_comision(total,orden_id)VALUES(@totalComision,ordenID);
END

//