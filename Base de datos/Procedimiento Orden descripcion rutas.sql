use marquesada;
DROP PROCEDURE IF EXISTS procesoOrdenDescripcionRutas;
/* PROCEDIMIENTO PARA GENERAR LOS DETALLES DE UNA VENTA Y SUS RESPECTIVAS COMISIONES*/
DELIMITER //
CREATE PROCEDURE procesoOrdenDescripcionRutas(In ordenID int,In puntoVentaID int ,In clienteID int,In tipoVentaID int,In cantidad int,In precio_final int,In ganancia int, In requerimiento varchar(20))
	BEGIN
    
	/* CONSULTA PARA SACAR EL ID DEL ARTICULO SELECCIONADO*/
    SET @articulo := (SELECT a.id from articulo a, cantidad ca WHERE ca.articulo_id =a.id AND ca.id = tipoVentaID);
    
    /*  CONSULTA PARA SACAR EL PRECIO DEL CLIENTE DE UN ARTICULO*/
    SET @precioArticuloCliente := ( SELECT precio FROM precio_cliente WHERE claveCliente_id= clienteID AND articulo_id= @articulo);
    
    /* CONSULTA PARA SACAR EL ID DEL REQUERIMIENTO */
    SET @requerimientoID :=( SELECT id FROM requerimiento WHERE tipo= requerimiento);
    
    
    /* CONSULTA  PARA VERIFICAR EL PRECIO DEL ARTICULO*/
	SET @precioArticulo :=( SELECT precio FROM articulo WHERE id = @articulo);
    
    /* VERIFICA QUE EL PRECIO DE VENTA NO SEA MENOR AL PRECIO  DE INVENTARIO*/
    IF precio_final >= @precioArticulo THEN
    
		/* CONSULTA PARA VERIFICAR QUE LA CANTIDAD DE VENTA NO SEA MAYOR AL STOCK DISPONIBLE*/
		SET @cantidadExistente :=( SELECT valor FROM cantidad WHERE articulo_id =@articulo and puntoVenta_id = puntoVentaID);
		/* VERIFICA QUE LA CANTIDAD NO SEA MAYOR A LA DISPONIBLE*/
		IF cantidad <= @cantidadExistente THEN
        
			IF @precioArticuloCliente is null THEN
			/* CONSULTA PARA INSERTAR EN ORDEN_DESCRIPCION*/
            INSERT INTO orden_descripcion(tipoVentaId,cantidad,precio_sugerido,precio_final,ganancia,orden_id,requerimiento_id)VALUES(tipoVentaID,cantidad,0,precio_final,ganancia,ordenID,@requerimientoID);
			
			ELSE 
            INSERT INTO orden_descripcion(tipoVentaId,cantidad,precio_sugerido,precio_final,ganancia,orden_id,requerimiento_id)VALUES(tipoVentaID,cantidad,@precioArticuloCliente,precio_final,ganancia,ordenID,@requerimientoID);
            
            END IF;
		 /* CONSULTA PARA DESCONTAR LAS CANTIDAD DEL INVENTARIO*/
         UPDATE cantidad SET valor = @cantidadExistente -cantidad WHERE id= tipoVentaID;
         
         /*CONSULTA  PARA SACAR EL ID DE LA ORDEN_DESCRIPCION */
         SET @ordenDesID :=(SELECT id FROM orden_descripcion WHERE orden_id = ordenID);
         
         /*CONSULTA PARA SACAR EL ID DE LA COLOCACION */
         SET @colocacionID :=(SELECT colocacion_id FROM puntoventa_colocacion WHERE puntoVenta_id =puntoVentaID);
         
         /*CONSULTA PARA SACAR EL ID DEL RANGO */
         SET @rangoID :=(SELECT ran.id
			FROM rango ran, articulo art, articulo_rango ar, colocacion col
			WHERE ar.rango_id = ran.id
			AND ar.articulo_id = art.id
			AND ar.colocacion_id = col.id
			AND col.id = @colocacionID
			AND art.id = @articulo
			AND ran.minimo <= precio_final
			AND precio_final <= ran.maximo);
            
		/* CONSULTA PARA SACAR LA COMSION*/
        SET @comision =(SELECT comision FROM rango WHERE id = @rangoID);
        
        /* SE CALCULA LO COMISION POR ARTICULO*/
        SET @comisionTotal =(@comision * cantidad);
        IF @comisionTotal is not null THEN
		/* INSERTAR EN ARTICULO_COMISION*/
        INSERT INTO articulo_comision(total,exito,rango_id,ordenDescripcion_id)VALUES(@comisionTotal,1,@rangoID,@ordenDesID);
		END IF;
		END IF;
		END IF;
 SELECT @ordenDesID,@comisionTotal;   
    
END
//