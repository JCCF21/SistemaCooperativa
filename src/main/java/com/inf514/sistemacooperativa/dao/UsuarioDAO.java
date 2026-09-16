/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inf514.sistemacooperativa.dao;

import com.inf514.sistemacooperativa.modelo.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 *
 * @author jfria
 */
public class UsuarioDAO {
    
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("CooperativaPU");
    
    
    public void guardarUsuario( Usuario usuario){
        EntityManager em = emf.createEntityManager();
        
        try{
            
            em.getTransaction().begin();//indica de se inicia una consulta 
            
            em.persist(usuario);// la consulta seria como un insert en mysql
            em.merge(usuario);
            em.getTransaction().commit();//se confirma la solicitud
        }catch(Exception e){
            em.getTransaction().rollback();//si algo sale mas se deshace los cambios
            e.printStackTrace();
            
        }finally{
            em.close();
        }
    }
    
    public Usuario buscarPorCedula(String cedula){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            return em.createQuery("SELECT U FROM Usuario u WHERE u.cedula = :cedula", Usuario.class)
                    .setParameter("cedula",cedula)
                    .getSingleResult();
            
        }catch(Exception e){
            return null;
            
        }finally{
            em.close();
        }
    }
    
}
