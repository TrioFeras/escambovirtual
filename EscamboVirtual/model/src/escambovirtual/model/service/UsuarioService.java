package escambovirtual.model.service;

import escambovirtual.model.base.service.BaseUsuarioService;
import escambovirtual.model.criteria.UsuarioCriteria;
import escambovirtual.model.entity.Administrador;
import escambovirtual.model.entity.Anunciante;
import escambovirtual.model.entity.Usuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Joao
 */
public class UsuarioService implements BaseUsuarioService {

    @Override
    public Usuario login(String email, String senha) throws Exception {
        Usuario usuario = null;
        Map<Long, Object> criteria = new HashMap<>();
        criteria.put(UsuarioCriteria.USUARIO_EMAIL_EQ, email);
        criteria.put(UsuarioCriteria.USUARIO_SENHA_EQ, senha);
        AnuncianteService as = new AnuncianteService();
        List<Usuario> usuarioList = new ArrayList<>();
        usuarioList = (List) as.readByCriteria(criteria);

        if (usuarioList != null && usuarioList.size() == 1) {
            usuario = new Anunciante();
            Anunciante aux = new Anunciante();
            aux = (Anunciante) usuarioList.get(0);
            if (aux.getEmail().equals(email) && aux.getSenha().equals(senha)) {
                usuario = aux;
            }
        } else {
            AdministradorService ads = new AdministradorService();
            usuarioList = (List) ads.readByCriteria(criteria);
            if (usuarioList != null && usuarioList.size() == 1) {
                usuario = new Administrador();
                Administrador aux = new Administrador();
                aux = (Administrador) usuarioList.get(0);
                if (aux.getEmail().equals(email) && aux.getSenha().equals(senha)) {
                    usuario = aux;
                }
            }
        }

        return usuario;
    }

    @Override
    public Usuario recuperarSenha(String email) throws Exception {
        Map<Long, Object> criteria = new HashMap<>();
        criteria.put(UsuarioCriteria.USUARIO_EMAIL_EQ, email);

        Usuario usuario = null;
        List<Usuario> usuarioList = new ArrayList<>();
        AnuncianteService as = new AnuncianteService();
        usuarioList = (List) as.readByCriteria(criteria);
        SenhaService senhaService = new SenhaService();
        String senha = senhaService.gerarSenhaAleatoria();
        String senhaMD5 = senhaService.convertPasswordToMD5(senha);

        if (usuarioList != null && usuarioList.size() == 1) {
            usuario = new Anunciante();
            usuario = usuarioList.get(0);
            usuario.setSenha(senhaMD5);
            as.update((Anunciante) usuario);
        } else {
            AdministradorService ads = new AdministradorService();
            usuarioList = (List) ads.readByCriteria(criteria);
            if (usuarioList != null && usuarioList.size() == 1) {
                usuario = new Administrador();
                usuario = usuarioList.get(0);
                usuario.setSenha(senhaMD5);
                ads.update((Administrador) usuario);
            }
        }

        if (usuario != null) {
            String texto = "Olá, você recebeu uma nova senha para acessar o Escambo Virtual. Sua nova senha se encontra logo abaixo, sugerimos que após o primeiro Login, você altere sua senha novamente."
                    + "Sua nova senha é: " + senha;
            String assunto = "Recuperação de senha";
            EmailService es = new EmailService();
            es.sendEmail(email, assunto, texto);
        }

        return usuario;
    }

}
