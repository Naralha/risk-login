package io.sld.riskcomplianceloginservice.constants;

import io.sld.riskcomplianceloginservice.domain.entity.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class LoginFake {

    public static Optional<Usuario> criarEstruturaFake(){
        Usuario u = new Usuario();
        u.setId(1l);

        Grupo g = new Grupo();
        g.setId(1l);

        UsuarioGrupo ug = new UsuarioGrupo();
        ug.id(1l);
        ug.setUsuario(u);
        ug.setGrupo(g);

        Set<UsuarioGrupo> sug = new HashSet<UsuarioGrupo>();
        Set<GrupoPapel> sgp = new HashSet<GrupoPapel>();
        sug.add(ug);


        u.setUsuarioGrupos(sug);
        ///////////////////////////////

        Papel p = new Papel();
        p.setId(1l);
        p.setnVarNome("admin");

        GrupoPapel gp = new GrupoPapel();
        sgp.add(gp);
        g.setGrupoPapels(sgp);
        p.setGrupoPapels(sgp);
        gp.setId(1l);
        gp.setGrupo(g);
        gp.setPapel(p);

        Permissions p1 = new Permissions();
        Permissions p2 = new Permissions();
        Permissions p3 = new Permissions();
        Permissions p4 = new Permissions();
        p1.setId(1l);
        p1.setnVarNome("read");

        p2.setId(2l);
        p2.setnVarNome("insert");

        p3.setId(3l);
        p3.setnVarNome("update");

        p4.setId(4l);
        p4.setnVarNome("delete");

        PermissionsPapel pp1 = new PermissionsPapel();
        PermissionsPapel pp2 = new PermissionsPapel();
        PermissionsPapel pp3 = new PermissionsPapel();
        PermissionsPapel pp4 = new PermissionsPapel();
        pp1.setId(1l);
        pp1.setPapel(p);
        pp2.setId(2l);
        pp2.setPapel(p);
        pp3.setId(3l);
        pp3.setPapel(p);
        pp4.setId(4l);
        pp4.setPapel(p);

        pp1.setPermissions(p1);
        pp2.setPermissions(p2);
        pp3.setPermissions(p3);
        pp4.setPermissions(p4);


        Set<PermissionsPapel> spp = new HashSet<PermissionsPapel>();
        spp.add(pp1);
        spp.add(pp2);
        spp.add(pp3);
        spp.add(pp4);
        p.setPermissionsPapels(spp);

        ////////////////////////////////////////////

        App a = new App();
        a.setId(1l);
        a.setnVarNome("Aplicacao Bolada e Furiosa");

        Features f1 = new Features();
        Features f2 = new Features();
        Features f3 = new Features();
        Features f4 = new Features();
        f1.setId(1l);
        f1.setnVarNome("Organograma");
        f1.setApp(a);
        f2.setId(2l);
        f2.setnVarNome("Processo");
        f2.setApp(a);
        f3.setId(3l);
        f3.setnVarNome("Risco");
        f3.setApp(a);
        f4.setId(4l);
        f4.setnVarNome("SeiLa");
        f4.setApp(a);

        pp1.setFeatures(f1);
        pp2.setFeatures(f2);
        pp3.setFeatures(f3);
        pp4.setFeatures(f4);
        ///////////////////////////////////////////

        Optional<Usuario> ou = Optional.of(u);
        return  ou;
    }
}
