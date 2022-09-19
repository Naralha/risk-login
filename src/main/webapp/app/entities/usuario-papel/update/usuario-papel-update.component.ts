import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUsuarioPapel, UsuarioPapel } from '../usuario-papel.model';
import { UsuarioPapelService } from '../service/usuario-papel.service';
import { IPapel } from 'app/entities/papel/papel.model';
import { PapelService } from 'app/entities/papel/service/papel.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-usuario-papel-update',
  templateUrl: './usuario-papel-update.component.html',
})
export class UsuarioPapelUpdateComponent implements OnInit {
  isSaving = false;

  papelsSharedCollection: IPapel[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    idnVarUsuarioCadastrado: [null, [Validators.required]],
    idnVarPapel: [null, [Validators.required]],
    idnVarUsuario: [null, [Validators.required]],
    papel: [],
    usuario: [],
  });

  constructor(
    protected usuarioPapelService: UsuarioPapelService,
    protected papelService: PapelService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuarioPapel }) => {
      this.updateForm(usuarioPapel);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuarioPapel = this.createFromForm();
    if (usuarioPapel.id !== undefined) {
      this.subscribeToSaveResponse(this.usuarioPapelService.update(usuarioPapel));
    } else {
      this.subscribeToSaveResponse(this.usuarioPapelService.create(usuarioPapel));
    }
  }

  trackPapelById(_index: number, item: IPapel): number {
    return item.id!;
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuarioPapel>>): void {
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

  protected updateForm(usuarioPapel: IUsuarioPapel): void {
    this.editForm.patchValue({
      id: usuarioPapel.id,
      idnVarUsuarioCadastrado: usuarioPapel.idnVarUsuarioCadastrado,
      idnVarPapel: usuarioPapel.idnVarPapel,
      idnVarUsuario: usuarioPapel.idnVarUsuario,
      papel: usuarioPapel.papel,
      usuario: usuarioPapel.usuario,
    });

    this.papelsSharedCollection = this.papelService.addPapelToCollectionIfMissing(this.papelsSharedCollection, usuarioPapel.papel);
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      usuarioPapel.usuario
    );
  }

  protected loadRelationshipsOptions(): void {
    this.papelService
      .query()
      .pipe(map((res: HttpResponse<IPapel[]>) => res.body ?? []))
      .pipe(map((papels: IPapel[]) => this.papelService.addPapelToCollectionIfMissing(papels, this.editForm.get('papel')!.value)))
      .subscribe((papels: IPapel[]) => (this.papelsSharedCollection = papels));

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): IUsuarioPapel {
    return {
      ...new UsuarioPapel(),
      id: this.editForm.get(['id'])!.value,
      idnVarUsuarioCadastrado: this.editForm.get(['idnVarUsuarioCadastrado'])!.value,
      idnVarPapel: this.editForm.get(['idnVarPapel'])!.value,
      idnVarUsuario: this.editForm.get(['idnVarUsuario'])!.value,
      papel: this.editForm.get(['papel'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
    };
  }
}
