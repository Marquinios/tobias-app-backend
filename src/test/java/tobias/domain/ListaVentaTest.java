package tobias.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tobias.web.rest.TestUtil;

class ListaVentaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ListaVenta.class);
        ListaVenta listaVenta1 = new ListaVenta();
        listaVenta1.setId(1L);
        ListaVenta listaVenta2 = new ListaVenta();
        listaVenta2.setId(listaVenta1.getId());
        assertThat(listaVenta1).isEqualTo(listaVenta2);
        listaVenta2.setId(2L);
        assertThat(listaVenta1).isNotEqualTo(listaVenta2);
        listaVenta1.setId(null);
        assertThat(listaVenta1).isNotEqualTo(listaVenta2);
    }
}
