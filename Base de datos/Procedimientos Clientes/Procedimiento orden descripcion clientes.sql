use marquesada;
DROP PROCEDURE IF EXISTS procesoOrdenDescripcionClientes;
/* PROCEDIMIENTO PARA GENERAR LOS DETALLES DE UNA VENTA Y SUS RESPECTIVAS COMISIONES*/
DELIMITER //
CREATE PROCEDURE procesoOrdenDescripcionClientes(In folio varchar(200),In puntoVentaID int ,In clienteID int,In tipoVentaID int,In cantidad int,In precio_final int,In ganancia int, In requerimiento varchar(20))
	BEGIN
    /* CONSULTA PARA SACAR EL ID DE LA ORDEN*/
    SET @ordenid:=(SELECT id FROM orden WHERE folio = folio);
    
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
            INSERT INTO orden_descripcion(tipoVentaId,cantidad,precio_sugerido,precio_final,ganancia,orden_id,requerimiento_id)VALUES(tipoVentaID,cantidad,0,precio_final,ganancia,@ordenid,@requerimientoID);
			
			ELSE 
            INSERT INTO orden_descripcion(tipoVentaId,cantidad,precio_sugerido,precio_final,ganancia,orden_id,requerimiento_id)VALUES(tipoVentaID,cantidad,@precioArticuloCliente,precio_final,ganancia,@ordenid,@requerimientoID);
            
            END IF;
		END IF;
		END IF;  
    
END
//