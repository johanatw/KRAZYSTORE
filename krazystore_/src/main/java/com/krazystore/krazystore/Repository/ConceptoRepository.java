/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.ConceptoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface ConceptoRepository extends JpaRepository<ConceptoEntity, Long> {
    @Query(
    "SELECT c FROM ConceptoEntity c "
            + "WHERE c.tipo = ?1 and c.descripcion <> 'Venta' and c.descripcion <> 'Compra' and c.descripcion <> 'Anticipo cliente' and c.descripcion <> 'Reembolso cliente' and c.descripcion <> 'Anticipo proveedor' and c.descripcion <> 'Reembolso proveedor' and c.descripcion <> 'Anulación de Factura'"
           )
    public List<ConceptoEntity> getConceptosByTipo(char tipo);

    @Query(
    "SELECT c FROM ConceptoEntity c "
            + "WHERE c.descripcion = 'Ingresos varios' or c.descripcion = 'Egresos varios' "
           )
    public List<ConceptoEntity> getConceptosIngresoEgreso();
}
