import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUsuario, Usuario } from '../usuario.model';
import { UsuarioService } from '../service/usuario.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';

@Component({
  selector: 'jhi-usuario-update',
  templateUrl: './usuario-update.component.html',
})
export class UsuarioUpdateComponent implements OnInit {
  isSaving = false;

  empresasSharedCollection: IEmpresa[] = [];

  editForm = this.fb.group({
    id: [],
    idnVarUsuario: [null, [Validators.required]],
    nVarNome: [null, [Validators.required]],
    idnVarEmpresa: [],
    idnVarUsuarioCadastro: [],
    nVarSenha: [null, [Validators.required]],
    empresa: [],
  });

  constructor(
    protected usuarioService: UsuarioService,
    protected empresaService: EmpresaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuario }) => {
      this.updateForm(usuario);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuario = this.createFromForm();
    if (usuario.id !== undefined) {
      this.subscribeToSaveResponse(this.usuarioService.update(usuario));
    } else {
      this.subscribeToSaveResponse(this.usuarioService.create(usuario));
    }
  }

  trackEmpresaById(_index: number, item: IEmpresa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuario>>): void {
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

  protected updateForm(usuario: IUsuario): void {
    this.editForm.patchValue({
      id: usuario.id,
      idnVarUsuario: usuario.idnVarUsuario,
      nVarNome: usuario.nVarNome,
      idnVarEmpresa: usuario.idnVarEmpresa,
      idnVarUsuarioCadastro: usuario.idnVarUsuarioCadastro,
      nVarSenha: usuario.nVarSenha,
      empresa: usuario.empresa,
    });

    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing(this.empresasSharedCollection, usuario.empresa);
  }

  protected loadRelationshipsOptions(): void {
    this.empresaService
      .query()
      .pipe(map((res: HttpResponse<IEmpresa[]>) => res.body ?? []))
      .pipe(
        map((empresas: IEmpresa[]) => this.empresaService.addEmpresaToCollectionIfMissing(empresas, this.editForm.get('empresa')!.value))
      )
      .subscribe((empresas: IEmpresa[]) => (this.empresasSharedCollection = empresas));
  }

  protected createFromForm(): IUsuario {
    return {
      ...new Usuario(),
      id: this.editForm.get(['id'])!.value,
      idnVarUsuario: this.editForm.get(['idnVarUsuario'])!.value,
      nVarNome: this.editForm.get(['nVarNome'])!.value,
      idnVarEmpresa: this.editForm.get(['idnVarEmpresa'])!.value,
      idnVarUsuarioCadastro: this.editForm.get(['idnVarUsuarioCadastro'])!.value,
      nVarSenha: this.editForm.get(['nVarSenha'])!.value,
      empresa: this.editForm.get(['empresa'])!.value,
    };
  }
}
