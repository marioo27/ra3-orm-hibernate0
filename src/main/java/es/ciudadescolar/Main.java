package es.ciudadescolar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ciudadescolar.instituto.Alumno;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("PersistenciaInstituto");

    public static void main(String[] args) {

        EntityManager em = null;
        EntityTransaction trans = null;

        Alumno al = new Alumno("Juan", "juan2@ciudadescolar.es");
        try {
            em = EMF.createEntityManager();
            trans = em.getTransaction();
            // Siempre necesito una transaccion para modificar la BD (altas, bajas y
            // modificaciones)

            trans.begin();

            em.persist(al);
            LOG.debug("La instancia de alumno pasa a estar administrada o persistente");

            trans.commit();
            // A partir de aqui (porque se ha cerrado la sesion) la instancia pasa a estar
            // separada (detached) Cambios posteriores no se vuelcana a la BD

        } catch (RuntimeException e) {
            // Las excepciones propagadas por motivos de persistencia extienden de Runtime
            // Exception (no poner Exception), dichas excepciones son NO comprobadas
            LOG.error("Error durante la persistencia de datos [" + e.getMessage() + "]");
            trans.rollback();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
                LOG.debug("Se ha cerrado la conexion con la BD");
            }
        }

    }
}