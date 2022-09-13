package tobias.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tobias.IntegrationTest;
import tobias.domain.Promocion;
import tobias.repository.PromocionRepository;
import tobias.service.PromocionService;

/**
 * Integration tests for the {@link PromocionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PromocionResourceIT {

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_PORCENTAJE_DESCUENTO = 1F;
    private static final Float UPDATED_PORCENTAJE_DESCUENTO = 2F;

    private static final String ENTITY_API_URL = "/api/promocions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PromocionRepository promocionRepository;

    @Mock
    private PromocionRepository promocionRepositoryMock;

    @Mock
    private PromocionService promocionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPromocionMockMvc;

    private Promocion promocion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promocion createEntity(EntityManager em) {
        Promocion promocion = new Promocion()
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN)
            .porcentajeDescuento(DEFAULT_PORCENTAJE_DESCUENTO);
        return promocion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promocion createUpdatedEntity(EntityManager em) {
        Promocion promocion = new Promocion()
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .porcentajeDescuento(UPDATED_PORCENTAJE_DESCUENTO);
        return promocion;
    }

    @BeforeEach
    public void initTest() {
        promocion = createEntity(em);
    }

    @Test
    @Transactional
    void createPromocion() throws Exception {
        int databaseSizeBeforeCreate = promocionRepository.findAll().size();
        // Create the Promocion
        restPromocionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promocion)))
            .andExpect(status().isCreated());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeCreate + 1);
        Promocion testPromocion = promocionList.get(promocionList.size() - 1);
        assertThat(testPromocion.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testPromocion.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testPromocion.getPorcentajeDescuento()).isEqualTo(DEFAULT_PORCENTAJE_DESCUENTO);
    }

    @Test
    @Transactional
    void createPromocionWithExistingId() throws Exception {
        // Create the Promocion with an existing ID
        promocion.setId(1L);

        int databaseSizeBeforeCreate = promocionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromocionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promocion)))
            .andExpect(status().isBadRequest());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = promocionRepository.findAll().size();
        // set the field null
        promocion.setFechaInicio(null);

        // Create the Promocion, which fails.

        restPromocionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promocion)))
            .andExpect(status().isBadRequest());

        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = promocionRepository.findAll().size();
        // set the field null
        promocion.setFechaFin(null);

        // Create the Promocion, which fails.

        restPromocionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promocion)))
            .andExpect(status().isBadRequest());

        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPorcentajeDescuentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = promocionRepository.findAll().size();
        // set the field null
        promocion.setPorcentajeDescuento(null);

        // Create the Promocion, which fails.

        restPromocionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promocion)))
            .andExpect(status().isBadRequest());

        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPromocions() throws Exception {
        // Initialize the database
        promocionRepository.saveAndFlush(promocion);

        // Get all the promocionList
        restPromocionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promocion.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].porcentajeDescuento").value(hasItem(DEFAULT_PORCENTAJE_DESCUENTO.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPromocionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(promocionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPromocionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(promocionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPromocionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(promocionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPromocionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(promocionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPromocion() throws Exception {
        // Initialize the database
        promocionRepository.saveAndFlush(promocion);

        // Get the promocion
        restPromocionMockMvc
            .perform(get(ENTITY_API_URL_ID, promocion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(promocion.getId().intValue()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.porcentajeDescuento").value(DEFAULT_PORCENTAJE_DESCUENTO.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPromocion() throws Exception {
        // Get the promocion
        restPromocionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPromocion() throws Exception {
        // Initialize the database
        promocionRepository.saveAndFlush(promocion);

        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();

        // Update the promocion
        Promocion updatedPromocion = promocionRepository.findById(promocion.getId()).get();
        // Disconnect from session so that the updates on updatedPromocion are not directly saved in db
        em.detach(updatedPromocion);
        updatedPromocion.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).porcentajeDescuento(UPDATED_PORCENTAJE_DESCUENTO);

        restPromocionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPromocion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPromocion))
            )
            .andExpect(status().isOk());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
        Promocion testPromocion = promocionList.get(promocionList.size() - 1);
        assertThat(testPromocion.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testPromocion.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testPromocion.getPorcentajeDescuento()).isEqualTo(UPDATED_PORCENTAJE_DESCUENTO);
    }

    @Test
    @Transactional
    void putNonExistingPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();
        promocion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromocionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promocion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promocion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();
        promocion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromocionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promocion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();
        promocion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromocionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promocion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePromocionWithPatch() throws Exception {
        // Initialize the database
        promocionRepository.saveAndFlush(promocion);

        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();

        // Update the promocion using partial update
        Promocion partialUpdatedPromocion = new Promocion();
        partialUpdatedPromocion.setId(promocion.getId());

        partialUpdatedPromocion.fechaInicio(UPDATED_FECHA_INICIO);

        restPromocionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromocion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromocion))
            )
            .andExpect(status().isOk());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
        Promocion testPromocion = promocionList.get(promocionList.size() - 1);
        assertThat(testPromocion.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testPromocion.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testPromocion.getPorcentajeDescuento()).isEqualTo(DEFAULT_PORCENTAJE_DESCUENTO);
    }

    @Test
    @Transactional
    void fullUpdatePromocionWithPatch() throws Exception {
        // Initialize the database
        promocionRepository.saveAndFlush(promocion);

        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();

        // Update the promocion using partial update
        Promocion partialUpdatedPromocion = new Promocion();
        partialUpdatedPromocion.setId(promocion.getId());

        partialUpdatedPromocion
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .porcentajeDescuento(UPDATED_PORCENTAJE_DESCUENTO);

        restPromocionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromocion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromocion))
            )
            .andExpect(status().isOk());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
        Promocion testPromocion = promocionList.get(promocionList.size() - 1);
        assertThat(testPromocion.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testPromocion.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testPromocion.getPorcentajeDescuento()).isEqualTo(UPDATED_PORCENTAJE_DESCUENTO);
    }

    @Test
    @Transactional
    void patchNonExistingPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();
        promocion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromocionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, promocion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promocion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();
        promocion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromocionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promocion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();
        promocion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromocionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(promocion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePromocion() throws Exception {
        // Initialize the database
        promocionRepository.saveAndFlush(promocion);

        int databaseSizeBeforeDelete = promocionRepository.findAll().size();

        // Delete the promocion
        restPromocionMockMvc
            .perform(delete(ENTITY_API_URL_ID, promocion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
