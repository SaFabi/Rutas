use marquesada;
DROP PROCEDURE IF EXISTS terminarVentaCredito;

/*PROCEDIMIENTO PARA TERMINAR UNA VENTA A CREDITO*/
DELIMITER //
CREATE PROCEDURE terminarVentaCredito(In ordenID int,In abono int)
	BEGIN
    /*CONSULTA PARA GENERAR EL TOTAL DE LA ORDEN */
   SET @total:=( SELECT SUM(od.cantidad * od.precio_final)
	FROM orden_descripcion od,orden o
	WHERE od.orden_id = o.id
	AND o.id = ordenID);
    
    /*INSERTAR EN ORDEN_COMPLETA */
    INSERT INTO orden_completa(fecha,total,estado,orden_id)VALUES(now(),@total,1,ordenID);
    
    /*INSERTAR EN CREDITO */
    INSERT INTO credito(total,estado,orden_id)VALUES(@total,1,ordenID);
    
		/* SI SE DEJO UN ABONO INICIAL*/
		IF abono >0 THEN
         /* CONSULTA PARA SACAR EL ID DEL CREDITO*/
		SET @creditoID:=( SELECT cre.id 
         FROM credito cre, orden o 
         WHERE cre.orden_id = o.id
         AND o.id = ordenID);
        
        INSERT INTO bono_credito(cantidad,estado,fecha,credito_id)VALUES(abono,1,now(),@creditoID);
		
		END IF;
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