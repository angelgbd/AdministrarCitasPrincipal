package com.hospital.administracion.persistencia;

import Interfaces.IAgendamientoDAO;
import Entidades.Cita;
import Entidades.EstadoCita; // Se importa el Enum de el proyecto Consultar Reportes
import DAOS.EntityManagerUtil; // Se Reusa la conexion de la libreria
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;

public class AgendamientoDAO implements IAgendamientoDAO {

    @Override
    public void agendarCita(Cita cita) throws Exception {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin(); // 1. Se inicia transacción
            em.persist(cita);            // 2. Se guarda la entidad
            em.getTransaction().commit();// 3. Se confirma los cambios
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // 4. Se desahace con el rollback si hay error
            }
            throw new Exception("Error al agendar la cita: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizarCita(Cita cita) throws Exception {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(cita); // Merge actualiza una entidad que ya existe
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("Error al actualizar la cita: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public Cita buscarPorId(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            return em.find(Cita.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existeCitaEnHorario(Long idDoctor, LocalDateTime fechaHora) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            // se cuenta cuantas citas que tenga a esa hora el doctor que no esten canceladas
            String jpql = "SELECT COUNT(c) FROM Cita c "
                        + "WHERE c.doctor.id = :idDoctor "
                        + "AND c.fechaHora = :fechaHora "
                        + "AND c.estado <> :estadoCancelado";

            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("idDoctor", idDoctor);
            query.setParameter("fechaHora", fechaHora);
            query.setParameter("estadoCancelado", EstadoCita.CANCELADA);

            Long conteo = query.getSingleResult();
            
            // si conteo es mayor a 1 ya existe una cita (improvise seguramente hay una mejor forma de hacer esto)
            return conteo > 0;
        } finally {
            em.close();
        }
    }
}
