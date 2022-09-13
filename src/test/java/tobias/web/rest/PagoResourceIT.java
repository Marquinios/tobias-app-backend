package tobias.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
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
import tobias.domain.Pago;
import tobias.repository.PagoRepository;

/**
 * Integration tests for the {@link PagoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PagoResourceIT {

    private static final LocalDate DEFAULT_FECHA_PAGO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_PAGO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TIPO_PAGO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_PAGO = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO_TRANSACCION = 1;
    private static final Integer UPDATED_NUMERO_TRANSACCION = 2;

    private static final String ENTITY_API_URL = "/api/pagos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPagoMockMvc;

    private Pago pago;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pago createEntity(EntityManager em) {
        Pago pago = new Pago().fechaPago(DEFAULT_FECHA_PAGO).tipoPago(DEFAULT_TIPO_PAGO).numeroTransaccion(DEFAULT_NUMERO_TRANSACCION);
        return pago;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pago createUpdatedEntity(EntityManager em) {
        Pago pago = new Pago().fechaPago(UPDATED_FECHA_PAGO).tipoPago(UPDATED_TIPO_PAGO).numeroTransaccion(UPDATED_NUMERO_TRANSACCION);
        return pago;
    }

    @BeforeEach
    public void initTest() {
        pago = createEntity(em);
    }

    @Test
    @Transactional
    void createPago() throws Exception {
        int databaseSizeBeforeCreate = pagoRepository.findAll().size();
        // Create the Pago
        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isCreated());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeCreate + 1);
        Pago testPago = pagoList.get(pagoList.size() - 1);
        assertThat(testPago.getFechaPago()).isEqualTo(DEFAULT_FECHA_PAGO);
        assertThat(testPago.getTipoPago()).isEqualTo(DEFAULT_TIPO_PAGO);
        assertThat(testPago.getNumeroTransaccion()).isEqualTo(DEFAULT_NUMERO_TRANSACCION);
    }

    @Test
    @Transactional
    void createPagoWithExistingId() throws Exception {
        // Create the Pago with an existing ID
        pago.setId(1L);

        int databaseSizeBeforeCreate = pagoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaPagoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().size();
        // set the field null
        pago.setFechaPago(null);

        // Create the Pago, which fails.

        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isBadRequest());

        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPagos() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList
        restPagoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pago.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaPago").value(hasItem(DEFAULT_FECHA_PAGO.toString())))
            .andExpect(jsonPath("$.[*].tipoPago").value(hasItem(DEFAULT_TIPO_PAGO)))
            .andExpect(jsonPath("$.[*].numeroTransaccion").value(hasItem(DEFAULT_NUMERO_TRANSACCION)));
    }

    @Test
    @Transactional
    void getPago() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get the pago
        restPagoMockMvc
            .perform(get(ENTITY_API_URL_ID, pago.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pago.getId().intValue()))
            .andExpect(jsonPath("$.fechaPago").value(DEFAULT_FECHA_PAGO.toString()))
            .andExpect(jsonPath("$.tipoPago").value(DEFAULT_TIPO_PAGO))
            .andExpect(jsonPath("$.numeroTransaccion").value(DEFAULT_NUMERO_TRANSACCION));
    }

    @Test
    @Transactional
    void getNonExistingPago() throws Exception {
        // Get the pago
        restPagoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPago() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();

        // Update the pago
        Pago updatedPago = pagoRepository.findById(pago.getId()).get();
        // Disconnect from session so that the updates on updatedPago are not directly saved in db
        em.detach(updatedPago);
        updatedPago.fechaPago(UPDATED_FECHA_PAGO).tipoPago(UPDATED_TIPO_PAGO).numeroTransaccion(UPDATED_NUMERO_TRANSACCION);

        restPagoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPago.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPago))
            )
            .andExpect(status().isOk());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
        Pago testPago = pagoList.get(pagoList.size() - 1);
        assertThat(testPago.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
        assertThat(testPago.getTipoPago()).isEqualTo(UPDATED_TIPO_PAGO);
        assertThat(testPago.getNumeroTransaccion()).isEqualTo(UPDATED_NUMERO_TRANSACCION);
    }

    @Test
    @Transactional
    void putNonExistingPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pago.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pago))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pago))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePagoWithPatch() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();

        // Update the pago using partial update
        Pago partialUpdatedPago = new Pago();
        partialUpdatedPago.setId(pago.getId());

        partialUpdatedPago.fechaPago(UPDATED_FECHA_PAGO).tipoPago(UPDATED_TIPO_PAGO);

        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPago.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPago))
            )
            .andExpect(status().isOk());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
        Pago testPago = pagoList.get(pagoList.size() - 1);
        assertThat(testPago.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
        assertThat(testPago.getTipoPago()).isEqualTo(UPDATED_TIPO_PAGO);
        assertThat(testPago.getNumeroTransaccion()).isEqualTo(DEFAULT_NUMERO_TRANSACCION);
    }

    @Test
    @Transactional
    void fullUpdatePagoWithPatch() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();

        // Update the pago using partial update
        Pago partialUpdatedPago = new Pago();
        partialUpdatedPago.setId(pago.getId());

        partialUpdatedPago.fechaPago(UPDATED_FECHA_PAGO).tipoPago(UPDATED_TIPO_PAGO).numeroTransaccion(UPDATED_NUMERO_TRANSACCION);

        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPago.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPago))
            )
            .andExpect(status().isOk());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
        Pago testPago = pagoList.get(pagoList.size() - 1);
        assertThat(testPago.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
        assertThat(testPago.getTipoPago()).isEqualTo(UPDATED_TIPO_PAGO);
        assertThat(testPago.getNumeroTransaccion()).isEqualTo(UPDATED_NUMERO_TRANSACCION);
    }

    @Test
    @Transactional
    void patchNonExistingPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pago.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pago))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pago))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePago() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        int databaseSizeBeforeDelete = pagoRepository.findAll().size();

        // Delete the pago
        restPagoMockMvc
            .perform(delete(ENTITY_API_URL_ID, pago.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
