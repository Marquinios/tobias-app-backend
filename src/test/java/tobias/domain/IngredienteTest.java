package tobias.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tobias.web.rest.TestUtil;

class IngredienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ingrediente.class);
        Ingrediente ingrediente1 = new Ingrediente();
        ingrediente1.setId(1L);
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setId(ingrediente1.getId());
        assertThat(ingrediente1).isEqualTo(ingrediente2);
        ingrediente2.setId(2L);
        assertThat(ingrediente1).isNotEqualTo(ingrediente2);
        ingrediente1.setId(null);
        assertThat(ingrediente1).isNotEqualTo(ingrediente2);
    }
}
