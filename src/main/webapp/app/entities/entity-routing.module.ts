import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'empresa',
        data: { pageTitle: 'riskcomplianceloginserviceApp.empresa.home.title' },
        loadChildren: () => import('./empresa/empresa.module').then(m => m.EmpresaModule),
      },
      {
        path: 'usuario',
        data: { pageTitle: 'riskcomplianceloginserviceApp.usuario.home.title' },
        loadChildren: () => import('./usuario/usuario.module').then(m => m.UsuarioModule),
      },
      {
        path: 'grupo',
        data: { pageTitle: 'riskcomplianceloginserviceApp.grupo.home.title' },
        loadChildren: () => import('./grupo/grupo.module').then(m => m.GrupoModule),
      },
      {
        path: 'grupo-papel',
        data: { pageTitle: 'riskcomplianceloginserviceApp.grupoPapel.home.title' },
        loadChildren: () => import('./grupo-papel/grupo-papel.module').then(m => m.GrupoPapelModule),
      },
      {
        path: 'app',
        data: { pageTitle: 'riskcomplianceloginserviceApp.app.home.title' },
        loadChildren: () => import('./app/app.module').then(m => m.AppModule),
      },
      {
        path: 'app-empresa',
        data: { pageTitle: 'riskcomplianceloginserviceApp.appEmpresa.home.title' },
        loadChildren: () => import('./app-empresa/app-empresa.module').then(m => m.AppEmpresaModule),
      },
      {
        path: 'features',
        data: { pageTitle: 'riskcomplianceloginserviceApp.features.home.title' },
        loadChildren: () => import('./features/features.module').then(m => m.FeaturesModule),
      },
      {
        path: 'permissions',
        data: { pageTitle: 'riskcomplianceloginserviceApp.permissions.home.title' },
        loadChildren: () => import('./permissions/permissions.module').then(m => m.PermissionsModule),
      },
      {
        path: 'permissions-papel',
        data: { pageTitle: 'riskcomplianceloginserviceApp.permissionsPapel.home.title' },
        loadChildren: () => import('./permissions-papel/permissions-papel.module').then(m => m.PermissionsPapelModule),
      },
      {
        path: 'papel',
        data: { pageTitle: 'riskcomplianceloginserviceApp.papel.home.title' },
        loadChildren: () => import('./papel/papel.module').then(m => m.PapelModule),
      },
      {
        path: 'usuario-grupo',
        data: { pageTitle: 'riskcomplianceloginserviceApp.usuarioGrupo.home.title' },
        loadChildren: () => import('./usuario-grupo/usuario-grupo.module').then(m => m.UsuarioGrupoModule),
      },
      {
        path: 'usuario-papel',
        data: { pageTitle: 'riskcomplianceloginserviceApp.usuarioPapel.home.title' },
        loadChildren: () => import('./usuario-papel/usuario-papel.module').then(m => m.UsuarioPapelModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
