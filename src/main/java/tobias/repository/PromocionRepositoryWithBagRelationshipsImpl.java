package tobias.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import tobias.domain.Promocion;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PromocionRepositoryWithBagRelationshipsImpl implements PromocionRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Promocion> fetchBagRelationships(Optional<Promocion> promocion) {
        return promocion.map(this::fetchProductos);
    }

    @Override
    public Page<Promocion> fetchBagRelationships(Page<Promocion> promocions) {
        return new PageImpl<>(fetchBagRelationships(promocions.getContent()), promocions.getPageable(), promocions.getTotalElements());
    }

    @Override
    public List<Promocion> fetchBagRelationships(List<Promocion> promocions) {
        return Optional.of(promocions).map(this::fetchProductos).orElse(Collections.emptyList());
    }

    Promocion fetchProductos(Promocion result) {
        return entityManager
            .createQuery(
                "select promocion from Promocion promocion left join fetch promocion.productos where promocion is :promocion",
                Promocion.class
            )
            .setParameter("promocion", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Promocion> fetchProductos(List<Promocion> promocions) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, promocions.size()).forEach(index -> order.put(promocions.get(index).getId(), index));
        List<Promocion> result = entityManager
            .createQuery(
                "select distinct promocion from Promocion promocion left join fetch promocion.productos where promocion in :promocions",
                Promocion.class
            )
            .setParameter("promocions", promocions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
