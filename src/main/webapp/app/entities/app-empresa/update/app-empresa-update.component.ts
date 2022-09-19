import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAppEmpresa, AppEmpresa } from '../app-empresa.model';
import { AppEmpresaService } from '../service/app-empresa.service';
import { IApp } from 'app/entities/app/app.model';
import { AppService } from 'app/entities/app/service/app.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-app-empresa-update',
  templateUrl: './app-empresa-update.component.html',
})
export class AppEmpresaUpdateComponent implements OnInit {
  isSaving = false;

  appsSharedCollection: IApp[] = [];
  empresasSharedCollection: IEmpresa[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    idnVarApp: [null, [Validators.required]],
    idnVarEmpresa: [null, [Validators.required]],
    idnVarUsuario: [null, [Validators.required]],
    app: [],
    empresa: [],
    usuario: [],
  });

  constructor(
    protected appEmpresaService: AppEmpresaService,
    protected appService: AppService,
    protected empresaService: EmpresaService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appEmpresa }) => {
      this.updateForm(appEmpresa);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appEmpresa = this.createFromForm();
    if (appEmpresa.id !== undefined) {
      this.subscribeToSaveResponse(this.appEmpresaService.update(appEmpresa));
    } else {
      this.subscribeToSaveResponse(this.appEmpresaService.create(appEmpresa));
    }
  }

  trackAppById(_index: number, item: IApp): number {
    return item.id!;
  }

  trackEmpresaById(_index: number, item: IEmpresa): number {
    return item.id!;
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppEmpresa>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(appEmpresa: IAppEmpresa): void {
    this.editForm.patchValue({
      id: appEmpresa.id,
      idnVarApp: appEmpresa.idnVarApp,
      idnVarEmpresa: appEmpresa.idnVarEmpresa,
      idnVarUsuario: appEmpresa.idnVarUsuario,
      app: appEmpresa.app,
      empresa: appEmpresa.empresa,
      usuario: appEmpresa.usuario,
    });

    this.appsSharedCollection = this.appService.addAppToCollectionIfMissing(this.appsSharedCollection, appEmpresa.app);
    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing(this.empresasSharedCollection, appEmpresa.empresa);
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, appEmpresa.usuario);
  }

  protected loadRelationshipsOptions(): void {
    this.appService
      .query()
      .pipe(map((res: HttpResponse<IApp[]>) => res.body ?? []))
      .pipe(map((apps: IApp[]) => this.appService.addAppToCollectionIfMissing(apps, this.editForm.get('app')!.value)))
      .subscribe((apps: IApp[]) => (this.appsSharedCollection = apps));

    this.empresaService
      .query()
      .pipe(map((res: HttpResponse<IEmpresa[]>) => res.body ?? []))
      .pipe(
        map((empresas: IEmpresa[]) => this.empresaService.addEmpresaToCollectionIfMissing(empresas, this.editForm.get('empresa')!.value))
      )
      .subscribe((empresas: IEmpresa[]) => (this.empresasSharedCollection = empresas));

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): IAppEmpresa {
    return {
      ...new AppEmpresa(),
      id: this.editForm.get(['id'])!.value,
      idnVarApp: this.editForm.get(['idnVarApp'])!.value,
      idnVarEmpresa: this.editForm.get(['idnVarEmpresa'])!.value,
      idnVarUsuario: this.editForm.get(['idnVarUsuario'])!.value,
      app: this.editForm.get(['app'])!.value,
      empresa: this.editForm.get(['empresa'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
    };
  }
}
