package com.example.demorest.daos;

import org.springframework.data.repository.CrudRepository;
import com.example.demorest.entidades.Usuario;

//DAO = DATA ACCES OBJECT
public interface UsuarioDAO extends CrudRepository<Usuario, Integer>{
    
}
