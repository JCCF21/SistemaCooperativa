package com.inf514.sistemacooperativa.dao;

import com.inf514.sistemacooperativa.modelo.Miembro;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author jfria
 */
//pasos
//crear un  campo entity manager factory -> emf
public class MiembroDAO {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("CooperativaPU");
    
    
    public void guardar(Miembro miembro){
        EntityManager em = emf.createEntityManager();
        
        try{
            em.getTransaction().begin();
            em.persist(miembro);//persist es el metodo que se usa para guardar un objeto en la base de datos;
            em.getTransaction().commit();//commit es el metodo que se usa para guardar los cambios 
        }catch(Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();//en caso de que una transaction falle cancela los cambios y regresa al estado que estaba la base de datos.
            }
            throw e;
        }finally{
            em.close();
        }
        
    }
    
    public List<Miembro> obtenerMiembros(){
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery<Miembro> query = em.createQuery("SELECT m FROM Miembro m",Miembro.class);
            return query.getResultList();
        }finally{
            em.close();
        }
    }
    
    public void modificar(Miembro miembro){
         EntityManager em = emf.createEntityManager();
         
         try{
             em.getTransaction().begin();
             em.merge(miembro);
             em.getTransaction().commit();
         }catch(Exception e){
             if(em.getTransaction().isActive()){
                 em.getTransaction().rollback();
             }
             throw e;
         }finally{
             em.close();
         }
    }
    
    public List<Miembro> buscarPorCedula(String cedula){
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery<Miembro> query = em.createQuery("SELECT m FROM Miembro m WHERE m.cedula LIKE :cedula",Miembro.class);
            query.setParameter("cedula","%"+ cedula +"%");
            return query.getResultList();
        }finally{
            em.close();
        }
    }
    public void cambiarEstado(int id, boolean nuevoEstado) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Miembro miembro = em.find(Miembro.class, id);
            
            if (miembro != null) {
                miembro.setActivo(nuevoEstado);
                em.merge(miembro);
            }
            
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
}
