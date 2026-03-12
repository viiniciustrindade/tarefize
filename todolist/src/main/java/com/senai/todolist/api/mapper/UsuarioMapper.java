package com.senai.todolist.api.mapper;

import com.senai.todolist.api.dto.usuario.cadastro.UsuarioRequisicaoDto;
import com.senai.todolist.api.dto.usuario.cadastro.UsuarioRespostaDto;
import com.senai.todolist.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public Usuario toEntity(UsuarioRequisicaoDto requisicaoDto){
        return new Usuario(requisicaoDto.nome(), requisicaoDto.senha());
    }

    public Usuario toUpdateDto(UsuarioRequisicaoDto requisicaoDto, Usuario usuario){
        usuario.setNome(requisicaoDto.nome());
        usuario.setSenha(requisicaoDto.senha());
        return usuario;
    }

    public UsuarioRespostaDto toRespostaDto(Usuario usuario){
        return new UsuarioRespostaDto(usuario.getId(),
                usuario.getNome(),
                usuario.getEmail());
    }
}
