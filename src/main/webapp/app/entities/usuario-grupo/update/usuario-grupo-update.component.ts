import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUsuarioGrupo, UsuarioGrupo } from '../usuario-grupo.model';
import { UsuarioGrupoService } from '../service/usuario-grupo.service';
import { IGrupo } from 'app/entities/grupo/grupo.model';
import { GrupoService } from 'app/entities/grupo/service/grupo.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-usuario-grupo-update',
  templateUrl: './usuario-grupo-update.component.html',
})
export class UsuarioGrupoUpdateComponent implements OnInit {
  isSaving = false;

  gruposSharedCollection: IGrupo[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    idnVarUsuarioCadastrado: [null, [Validators.required]],
    idnVarGrupo: [null, [Validators.required]],
    idnVarUsuario: [null, [Validators.required]],
    grupo: [],
    usuario: [],
  });

  constructor(
    protected usuarioGrupoService: UsuarioGrupoService,
    protected grupoService: GrupoService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuarioGrupo }) => {
      this.updateForm(usuarioGrupo);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuarioGrupo = this.createFromForm();
    if (usuarioGrupo.id !== undefined) {
      this.subscribeToSaveResponse(this.usuarioGrupoService.update(usuarioGrupo));
    } else {
      this.subscribeToSaveResponse(this.usuarioGrupoService.create(usuarioGrupo));
    }
  }

  trackGrupoById(_index: number, item: IGrupo): number {
    return item.id!;
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuarioGrupo>>): void {
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

  protected updateForm(usuarioGrupo: IUsuarioGrupo): void {
    this.editForm.patchValue({
      id: usuarioGrupo.id,
      idnVarUsuarioCadastrado: usuarioGrupo.idnVarUsuarioCadastrado,
      idnVarGrupo: usuarioGrupo.idnVarGrupo,
      idnVarUsuario: usuarioGrupo.idnVarUsuario,
      grupo: usuarioGrupo.grupo,
      usuario: usuarioGrupo.usuario,
    });

    this.gruposSharedCollection = this.grupoService.addGrupoToCollectionIfMissing(this.gruposSharedCollection, usuarioGrupo.grupo);
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      usuarioGrupo.usuario
    );
  }

  protected loadRelationshipsOptions(): void {
    this.grupoService
      .query()
      .pipe(map((res: HttpResponse<IGrupo[]>) => res.body ?? []))
      .pipe(map((grupos: IGrupo[]) => this.grupoService.addGrupoToCollectionIfMissing(grupos, this.editForm.get('grupo')!.value)))
      .subscribe((grupos: IGrupo[]) => (this.gruposSharedCollection = grupos));

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): IUsuarioGrupo {
    return {
      ...new UsuarioGrupo(),
      id: this.editForm.get(['id'])!.value,
      idnVarUsuarioCadastrado: this.editForm.get(['idnVarUsuarioCadastrado'])!.value,
      idnVarGrupo: this.editForm.get(['idnVarGrupo'])!.value,
      idnVarUsuario: this.editForm.get(['idnVarUsuario'])!.value,
      grupo: this.editForm.get(['grupo'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
    };
  }
}
