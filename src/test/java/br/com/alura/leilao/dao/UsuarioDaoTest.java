package br.com.alura.leilao.dao;

import br.com.alura.leilao.dao.util.builder.UsuarioBuilder;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import org.junit.Assert;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class UsuarioDaoTest {

    private UsuarioDao dao;
    private EntityManager em;

    @BeforeEach
    public void beforeEach() {
        this.em = JPAUtil.getEntityManager();
        this.dao = new UsuarioDao(em);
        em.getTransaction().begin();
    }

    @AfterEach
    public void afterEach() {
        em.getTransaction().rollback();
    }

    @Test
    public void deveriaEncontrarUsuarioCadastrado() {
        Usuario usuario = new UsuarioBuilder()
                .comNome("Fulano")
                .comEmail("fulano@email.com")
                .comSenha("12345678")
                .criar();
        em.persist(usuario);
        Usuario encontrado = this.dao.buscarPorUsername(usuario.getNome());
        Assert.assertNotNull(encontrado);
    }

    @Test
    public void naoDeveriaEncontrarUsuarioNaoCadastrado() {
        criarUsuario();
        Assert.assertThrows(NoResultException.class,
                () ->this.dao.buscarPorUsername("teste"));
    }

    @Test
    public void deveriaRemoverUsuario() {
        Usuario usuario = new UsuarioBuilder()
                .comNome("Fulano")
                .comEmail("fulano@email.com")
                .comSenha("12345678")
                .criar();
        em.persist(usuario);
        dao.deletar(usuario);
        Assert.assertThrows(NoResultException.class,
                () ->this.dao.buscarPorUsername(usuario.getNome()));

    }

    private Usuario criarUsuario() {
        Usuario usuario = new UsuarioBuilder()
                .comNome("Fulano")
                .comEmail("fulano@email.com")
                .comSenha("12345678")
                .criar();
        em.persist(usuario);
        em.persist(usuario);
        return usuario;
    }

}