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
import tobias.domain.ListaVenta;
import tobias.repository.ListaVentaRepository;

/**
 * Integration tests for the {@link ListaVentaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ListaVentaResourceIT {

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final Float DEFAULT_SUBTOTAL = 1F;
    private static final Float UPDATED_SUBTOTAL = 2F;

    private static final Float DEFAULT_PRECIO = 1F;
    private static final Float UPDATED_PRECIO = 2F;

    private static final Float DEFAULT_DESCUENTO = 1F;
    private static final Float UPDATED_DESCUENTO = 2F;

    private static final String ENTITY_API_URL = "/api/lista-ventas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ListaVentaRepository listaVentaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restListaVentaMockMvc;

    private ListaVenta listaVenta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ListaVenta createEntity(EntityManager em) {
        ListaVenta listaVenta = new ListaVenta()
            .cantidad(DEFAULT_CANTIDAD)
            .subtotal(DEFAULT_SUBTOTAL)
            .precio(DEFAULT_PRECIO)
            .descuento(DEFAULT_DESCUENTO);
        return listaVenta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ListaVenta createUpdatedEntity(EntityManager em) {
        ListaVenta listaVenta = new ListaVenta()
            .cantidad(UPDATED_CANTIDAD)
            .subtotal(UPDATED_SUBTOTAL)
            .precio(UPDATED_PRECIO)
            .descuento(UPDATED_DESCUENTO);
        return listaVenta;
    }

    @BeforeEach
    public void initTest() {
        listaVenta = createEntity(em);
    }

    @Test
    @Transactional
    void createListaVenta() throws Exception {
        int databaseSizeBeforeCreate = listaVentaRepository.findAll().size();
        // Create the ListaVenta
        restListaVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(listaVenta)))
            .andExpect(status().isCreated());

        // Validate the ListaVenta in the database
        List<ListaVenta> listaVentaList = listaVentaRepository.findAll();
        assertThat(listaVentaList).hasSize(databaseSizeBeforeCreate + 1);
        ListaVenta testListaVenta = listaVentaList.get(listaVentaList.size() - 1);
        assertThat(testListaVenta.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testListaVenta.getSubtotal()).isEqualTo(DEFAULT_SUBTOTAL);
        assertThat(testListaVenta.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testListaVenta.getDescuento()).isEqualTo(DEFAULT_DESCUENTO);
    }

    @Test
    @Transactional
    void createListaVentaWithExistingId() throws Exception {
        // Create the ListaVenta with an existing ID
        listaVenta.setId(1L);

        int databaseSizeBeforeCreate = listaVentaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restListaVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(listaVenta)))
            .andExpect(status().isBadRequest());

        // Validate the ListaVenta in the database
        List<ListaVenta> listaVentaList = listaVentaRepository.findAll();
        assertThat(listaVentaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = listaVentaRepository.findAll().size();
        // set the field null
        listaVenta.setCantidad(null);

        // Create the ListaVenta, which fails.

        restListaVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(listaVenta)))
            .andExpect(status().isBadRequest());

        List<ListaVenta> listaVentaList = listaVentaRepository.findAll();
        assertThat(listaVentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllListaVentas() throws Exception {
        // Initialize the database
        listaVentaRepository.saveAndFlush(listaVenta);

        // Get all the listaVentaList
        restListaVentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(listaVenta.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())))
            .andExpect(jsonPath("$.[*].descuento").value(hasItem(DEFAULT_DESCUENTO.doubleValue())));
    }

    @Test
    @Transactional
    void getListaVenta() throws Exception {
        // Initialize the database
        listaVentaRepository.saveAndFlush(listaVenta);

        // Get the listaVenta
        restListaVentaMockMvc
            .perform(get(ENTITY_API_URL_ID, listaVenta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(listaVenta.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.subtotal").value(DEFAULT_SUBTOTAL.doubleValue()))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()))
            .andExpect(jsonPath("$.descuento").value(DEFAULT_DESCUENTO.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingListaVenta() throws Exception {
        // Get the listaVenta
        restListaVentaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingListaVenta() throws Exception {
        // Initialize the database
        listaVentaRepository.saveAndFlush(listaVenta);

        int databaseSizeBeforeUpdate = listaVentaRepository.findAll().size();

        // Update the listaVenta
        ListaVenta updatedListaVenta = listaVentaRepository.findById(listaVenta.getId()).get();
        // Disconnect from session so that the updates on updatedListaVenta are not directly saved in db
        em.detach(updatedListaVenta);
        updatedListaVenta.cantidad(UPDATED_CANTIDAD).subtotal(UPDATED_SUBTOTAL).precio(UPDATED_PRECIO).descuento(UPDATED_DESCUENTO);

        restListaVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedListaVenta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedListaVenta))
            )
            .andExpect(status().isOk());

        // Validate the ListaVenta in the database
        List<ListaVenta> listaVentaList = listaVentaRepository.findAll();
        assertThat(listaVentaList).hasSize(databaseSizeBeforeUpdate);
        ListaVenta testListaVenta = listaVentaList.get(listaVentaList.size() - 1);
        assertThat(testListaVenta.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testListaVenta.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testListaVenta.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testListaVenta.getDescuento()).isEqualTo(UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void putNonExistingListaVenta() throws Exception {
        int databaseSizeBeforeUpdate = listaVentaRepository.findAll().size();
        listaVenta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restListaVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, listaVenta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(listaVenta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ListaVenta in the database
        List<ListaVenta> listaVentaList = listaVentaRepository.findAll();
        assertThat(listaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchListaVenta() throws Exception {
        int databaseSizeBeforeUpdate = listaVentaRepository.findAll().size();
        listaVenta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restListaVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(listaVenta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ListaVenta in the database
        List<ListaVenta> listaVentaList = listaVentaRepository.findAll();
        assertThat(listaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamListaVenta() throws Exception {
        int databaseSizeBeforeUpdate = listaVentaRepository.findAll().size();
        listaVenta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restListaVentaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(listaVenta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ListaVenta in the database
        List<ListaVenta> listaVentaList = listaVentaRepository.findAll();
        assertThat(listaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateListaVentaWithPatch() throws Exception {
        // Initialize the database
        listaVentaRepository.saveAndFlush(listaVenta);

        int databaseSizeBeforeUpdate = listaVentaRepository.findAll().size();

        // Update the listaVenta using partial update
        ListaVenta partialUpdatedListaVenta = new ListaVenta();
        partialUpdatedListaVenta.setId(listaVenta.getId());

        partialUpdatedListaVenta.cantidad(UPDATED_CANTIDAD).subtotal(UPDATED_SUBTOTAL).precio(UPDATED_PRECIO).descuento(UPDATED_DESCUENTO);

        restListaVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedListaVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedListaVenta))
            )
            .andExpect(status().isOk());

        // Validate the ListaVenta in the database
        List<ListaVenta> listaVentaList = listaVentaRepository.findAll();
        assertThat(listaVentaList).hasSize(databaseSizeBeforeUpdate);
        ListaVenta testListaVenta = listaVentaList.get(listaVentaList.size() - 1);
        assertThat(testListaVenta.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testListaVenta.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testListaVenta.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testListaVenta.getDescuento()).isEqualTo(UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void fullUpdateListaVentaWithPatch() throws Exception {
        // Initialize the database
        listaVentaRepository.saveAndFlush(listaVenta);

        int databaseSizeBeforeUpdate = listaVentaRepository.findAll().size();

        // Update the listaVenta using partial update
        ListaVenta partialUpdatedListaVenta = new ListaVenta();
        partialUpdatedListaVenta.setId(listaVenta.getId());

        partialUpdatedListaVenta.cantidad(UPDATED_CANTIDAD).subtotal(UPDATED_SUBTOTAL).precio(UPDATED_PRECIO).descuento(UPDATED_DESCUENTO);

        restListaVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedListaVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedListaVenta))
            )
            .andExpect(status().isOk());

        // Validate the ListaVenta in the database
        List<ListaVenta> listaVentaList = listaVentaRepository.findAll();
        assertThat(listaVentaList).hasSize(databaseSizeBeforeUpdate);
        ListaVenta testListaVenta = listaVentaList.get(listaVentaList.size() - 1);
        assertThat(testListaVenta.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testListaVenta.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testListaVenta.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testListaVenta.getDescuento()).isEqualTo(UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void patchNonExistingListaVenta() throws Exception {
        int databaseSizeBeforeUpdate = listaVentaRepository.findAll().size();
        listaVenta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restListaVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, listaVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(listaVenta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ListaVenta in the database
        List<ListaVenta> listaVentaList = listaVentaRepository.findAll();
        assertThat(listaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchListaVenta() throws Exception {
        int databaseSizeBeforeUpdate = listaVentaRepository.findAll().size();
        listaVenta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restListaVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(listaVenta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ListaVenta in the database
        List<ListaVenta> listaVentaList = listaVentaRepository.findAll();
        assertThat(listaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamListaVenta() throws Exception {
        int databaseSizeBeforeUpdate = listaVentaRepository.findAll().size();
        listaVenta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restListaVentaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(listaVenta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ListaVenta in the database
        List<ListaVenta> listaVentaList = listaVentaRepository.findAll();
        assertThat(listaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteListaVenta() throws Exception {
        // Initialize the database
        listaVentaRepository.saveAndFlush(listaVenta);

        int databaseSizeBeforeDelete = listaVentaRepository.findAll().size();

        // Delete the listaVenta
        restListaVentaMockMvc
            .perform(delete(ENTITY_API_URL_ID, listaVenta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ListaVenta> listaVentaList = listaVentaRepository.findAll();
        assertThat(listaVentaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
