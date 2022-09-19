import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApp, App } from '../app.model';
import { AppService } from '../service/app.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-app-update',
  templateUrl: './app-update.component.html',
})
export class AppUpdateComponent implements OnInit {
  isSaving = false;

  empresasSharedCollection: IEmpresa[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    idnVarApp: [null, [Validators.required]],
    nVarNome: [null, [Validators.required]],
    idnVarUsuario: [null, [Validators.required]],
    idnVarEmpresa: [null, [Validators.required]],
    empresa: [],
    usuario: [],
  });

  constructor(
    protected appService: AppService,
    protected empresaService: EmpresaService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ app }) => {
      this.updateForm(app);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const app = this.createFromForm();
    if (app.id !== undefined) {
      this.subscribeToSaveResponse(this.appService.update(app));
    } else {
      this.subscribeToSaveResponse(this.appService.create(app));
    }
  }

  trackEmpresaById(_index: number, item: IEmpresa): number {
    return item.id!;
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApp>>): void {
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

  protected updateForm(app: IApp): void {
    this.editForm.patchValue({
      id: app.id,
      idnVarApp: app.idnVarApp,
      nVarNome: app.nVarNome,
      idnVarUsuario: app.idnVarUsuario,
      idnVarEmpresa: app.idnVarEmpresa,
      empresa: app.empresa,
      usuario: app.usuario,
    });

    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing(this.empresasSharedCollection, app.empresa);
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, app.usuario);
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): IApp {
    return {
      ...new App(),
      id: this.editForm.get(['id'])!.value,
      idnVarApp: this.editForm.get(['idnVarApp'])!.value,
      nVarNome: this.editForm.get(['nVarNome'])!.value,
      idnVarUsuario: this.editForm.get(['idnVarUsuario'])!.value,
      idnVarEmpresa: this.editForm.get(['idnVarEmpresa'])!.value,
      empresa: this.editForm.get(['empresa'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
    };
  }
}
