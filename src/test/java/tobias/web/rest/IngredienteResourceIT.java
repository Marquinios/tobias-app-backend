package tobias.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tobias.IntegrationTest;
import tobias.domain.Ingrediente;
import tobias.repository.IngredienteRepository;

/**
 * Integration tests for the {@link IngredienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IngredienteResourceIT {

    private static final Float DEFAULT_CANTIDAD = 1F;
    private static final Float UPDATED_CANTIDAD = 2F;

    private static final String DEFAULT_UNIDAD = "AAAAAAAAAA";
    private static final String UPDATED_UNIDAD = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ingredientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIngredienteMockMvc;

    private Ingrediente ingrediente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingrediente createEntity(EntityManager em) {
        Ingrediente ingrediente = new Ingrediente().cantidad(DEFAULT_CANTIDAD).unidad(DEFAULT_UNIDAD).nombre(DEFAULT_NOMBRE);
        return ingrediente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingrediente createUpdatedEntity(EntityManager em) {
        Ingrediente ingrediente = new Ingrediente().cantidad(UPDATED_CANTIDAD).unidad(UPDATED_UNIDAD).nombre(UPDATED_NOMBRE);
        return ingrediente;
    }

    @BeforeEach
    public void initTest() {
        ingrediente = createEntity(em);
    }

    @Test
    @Transactional
    void createIngrediente() throws Exception {
        int databaseSizeBeforeCreate = ingredienteRepository.findAll().size();
        // Create the Ingrediente
        restIngredienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingrediente)))
            .andExpect(status().isCreated());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeCreate + 1);
        Ingrediente testIngrediente = ingredienteList.get(ingredienteList.size() - 1);
        assertThat(testIngrediente.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testIngrediente.getUnidad()).isEqualTo(DEFAULT_UNIDAD);
        assertThat(testIngrediente.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createIngredienteWithExistingId() throws Exception {
        // Create the Ingrediente with an existing ID
        ingrediente.setId(1L);

        int databaseSizeBeforeCreate = ingredienteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIngredienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingrediente)))
            .andExpect(status().isBadRequest());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIngredientes() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get all the ingredienteList
        restIngredienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingrediente.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].unidad").value(hasItem(DEFAULT_UNIDAD)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getIngrediente() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        // Get the ingrediente
        restIngredienteMockMvc
            .perform(get(ENTITY_API_URL_ID, ingrediente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ingrediente.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.unidad").value(DEFAULT_UNIDAD))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getNonExistingIngrediente() throws Exception {
        // Get the ingrediente
        restIngredienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIngrediente() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();

        // Update the ingrediente
        Ingrediente updatedIngrediente = ingredienteRepository.findById(ingrediente.getId()).get();
        // Disconnect from session so that the updates on updatedIngrediente are not directly saved in db
        em.detach(updatedIngrediente);
        updatedIngrediente.cantidad(UPDATED_CANTIDAD).unidad(UPDATED_UNIDAD).nombre(UPDATED_NOMBRE);

        restIngredienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIngrediente.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIngrediente))
            )
            .andExpect(status().isOk());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
        Ingrediente testIngrediente = ingredienteList.get(ingredienteList.size() - 1);
        assertThat(testIngrediente.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testIngrediente.getUnidad()).isEqualTo(UPDATED_UNIDAD);
        assertThat(testIngrediente.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingIngrediente() throws Exception {
        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();
        ingrediente.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingrediente.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingrediente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIngrediente() throws Exception {
        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();
        ingrediente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingrediente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIngrediente() throws Exception {
        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();
        ingrediente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredienteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingrediente)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIngredienteWithPatch() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();

        // Update the ingrediente using partial update
        Ingrediente partialUpdatedIngrediente = new Ingrediente();
        partialUpdatedIngrediente.setId(ingrediente.getId());

        partialUpdatedIngrediente.cantidad(UPDATED_CANTIDAD);

        restIngredienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngrediente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngrediente))
            )
            .andExpect(status().isOk());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
        Ingrediente testIngrediente = ingredienteList.get(ingredienteList.size() - 1);
        assertThat(testIngrediente.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testIngrediente.getUnidad()).isEqualTo(DEFAULT_UNIDAD);
        assertThat(testIngrediente.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdateIngredienteWithPatch() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();

        // Update the ingrediente using partial update
        Ingrediente partialUpdatedIngrediente = new Ingrediente();
        partialUpdatedIngrediente.setId(ingrediente.getId());

        partialUpdatedIngrediente.cantidad(UPDATED_CANTIDAD).unidad(UPDATED_UNIDAD).nombre(UPDATED_NOMBRE);

        restIngredienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngrediente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngrediente))
            )
            .andExpect(status().isOk());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
        Ingrediente testIngrediente = ingredienteList.get(ingredienteList.size() - 1);
        assertThat(testIngrediente.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testIngrediente.getUnidad()).isEqualTo(UPDATED_UNIDAD);
        assertThat(testIngrediente.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingIngrediente() throws Exception {
        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();
        ingrediente.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ingrediente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingrediente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIngrediente() throws Exception {
        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();
        ingrediente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingrediente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIngrediente() throws Exception {
        int databaseSizeBeforeUpdate = ingredienteRepository.findAll().size();
        ingrediente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredienteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ingrediente))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ingrediente in the database
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIngrediente() throws Exception {
        // Initialize the database
        ingredienteRepository.saveAndFlush(ingrediente);

        int databaseSizeBeforeDelete = ingredienteRepository.findAll().size();

        // Delete the ingrediente
        restIngredienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, ingrediente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ingrediente> ingredienteList = ingredienteRepository.findAll();
        assertThat(ingredienteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
