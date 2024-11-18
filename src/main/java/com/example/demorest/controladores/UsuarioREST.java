package com.example.demorest.controladores;

import org.springframework.web.bind.annotation.RestController;

import com.example.demorest.daos.CompraDAO;
import com.example.demorest.daos.UsuarioDAO;
import com.example.demorest.dtos.UsuarioDTO;
import com.example.demorest.entidades.Usuario;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api")
public class UsuarioREST {
    
    @Autowired
    UsuarioDAO usuarioDAO;

    @Autowired
    CompraDAO compraDAO;

    Log log = LogFactory.getLog(UsuarioREST.class);

    //ruta http://localhost:8080/usuarios/1?cadena=hola&numero=3
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> obtenUsuario(@PathVariable("id")  Integer idUsuario, String cadena, Integer numero) {

        log.info("buscando usuario con id:" + idUsuario);
        log.info("cadena: " + cadena + "valor: " + numero);
        Optional<Usuario> usuario = usuarioDAO.findById(idUsuario);

        //return new ResponseEntity<>(usuario, HttpStatus.OK);
        //return ResponseEntity.ok(usuario);
        return ResponseEntity.of(usuario);
    }

    @PostMapping("/USUARIOS")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody UsuarioDTO usuario) {

        log.info("creando nuevo usuario con informacion " + usuario);
        
        Usuario usuarioACrear = new Usuario();
        usuarioACrear.setNombre(usuario.getNombre());
        usuarioACrear.setEmail(usuario.getEmail());
        usuarioACrear.setPassword(usuario.getPassword());

        Usuario usuarioNuevo = usuarioDAO.save(usuarioACrear);

        return ResponseEntity.ok(usuarioNuevo);
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable("id") Integer id, UsuarioDTO infoUsuario) {

        Optional<Usuario> usuario = usuarioDAO.findById(id);

        if(usuario.isPresent()) {
            //actualizar
            Usuario usuarioAActualizar = usuario.get();
            usuarioAActualizar.setNombre(infoUsuario.getNombre());
            usuarioAActualizar.setEmail(infoUsuario.getEmail());
            usuarioAActualizar.setPassword(infoUsuario.getPassword());
            usuarioAActualizar = usuarioDAO.save(usuarioAActualizar);
            return ResponseEntity.ok(usuarioAActualizar);

        }
        else {
            //no existe
            return ResponseEntity.of(usuario);
        }
    }
    
    @DeleteMapping("/usuarios/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> borrarUsuario(@PathVariable("id") Integer id) {
        //borramos las compras
        compraDAO.borraPorIdUsuario(id);
        //borramos al usuario
        usuarioDAO.deleteById(id);
        //regresamos respuesta
        Map<String, String> respuesta = Collections.singletonMap("respuesta", "usuario eliminado");
    
        return ResponseEntity.ok(respuesta);
    }
    
}
