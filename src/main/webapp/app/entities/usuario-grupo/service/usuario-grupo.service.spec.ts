import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUsuarioGrupo, UsuarioGrupo } from '../usuario-grupo.model';

import { UsuarioGrupoService } from './usuario-grupo.service';

describe('UsuarioGrupo Service', () => {
  let service: UsuarioGrupoService;
  let httpMock: HttpTestingController;
  let elemDefault: IUsuarioGrupo;
  let expectedResult: IUsuarioGrupo | IUsuarioGrupo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UsuarioGrupoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idnVarUsuarioCadastrado: 'AAAAAAA',
      idnVarGrupo: 'AAAAAAA',
      idnVarUsuario: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a UsuarioGrupo', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new UsuarioGrupo()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UsuarioGrupo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarUsuarioCadastrado: 'BBBBBB',
          idnVarGrupo: 'BBBBBB',
          idnVarUsuario: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UsuarioGrupo', () => {
      const patchObject = Object.assign(
        {
          idnVarUsuarioCadastrado: 'BBBBBB',
          idnVarGrupo: 'BBBBBB',
          idnVarUsuario: 'BBBBBB',
        },
        new UsuarioGrupo()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UsuarioGrupo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarUsuarioCadastrado: 'BBBBBB',
          idnVarGrupo: 'BBBBBB',
          idnVarUsuario: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a UsuarioGrupo', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUsuarioGrupoToCollectionIfMissing', () => {
      it('should add a UsuarioGrupo to an empty array', () => {
        const usuarioGrupo: IUsuarioGrupo = { id: 123 };
        expectedResult = service.addUsuarioGrupoToCollectionIfMissing([], usuarioGrupo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(usuarioGrupo);
      });

      it('should not add a UsuarioGrupo to an array that contains it', () => {
        const usuarioGrupo: IUsuarioGrupo = { id: 123 };
        const usuarioGrupoCollection: IUsuarioGrupo[] = [
          {
            ...usuarioGrupo,
          },
          { id: 456 },
        ];
        expectedResult = service.addUsuarioGrupoToCollectionIfMissing(usuarioGrupoCollection, usuarioGrupo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UsuarioGrupo to an array that doesn't contain it", () => {
        const usuarioGrupo: IUsuarioGrupo = { id: 123 };
        const usuarioGrupoCollection: IUsuarioGrupo[] = [{ id: 456 }];
        expectedResult = service.addUsuarioGrupoToCollectionIfMissing(usuarioGrupoCollection, usuarioGrupo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(usuarioGrupo);
      });

      it('should add only unique UsuarioGrupo to an array', () => {
        const usuarioGrupoArray: IUsuarioGrupo[] = [{ id: 123 }, { id: 456 }, { id: 76248 }];
        const usuarioGrupoCollection: IUsuarioGrupo[] = [{ id: 123 }];
        expectedResult = service.addUsuarioGrupoToCollectionIfMissing(usuarioGrupoCollection, ...usuarioGrupoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const usuarioGrupo: IUsuarioGrupo = { id: 123 };
        const usuarioGrupo2: IUsuarioGrupo = { id: 456 };
        expectedResult = service.addUsuarioGrupoToCollectionIfMissing([], usuarioGrupo, usuarioGrupo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(usuarioGrupo);
        expect(expectedResult).toContain(usuarioGrupo2);
      });

      it('should accept null and undefined values', () => {
        const usuarioGrupo: IUsuarioGrupo = { id: 123 };
        expectedResult = service.addUsuarioGrupoToCollectionIfMissing([], null, usuarioGrupo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(usuarioGrupo);
      });

      it('should return initial array if no UsuarioGrupo is added', () => {
        const usuarioGrupoCollection: IUsuarioGrupo[] = [{ id: 123 }];
        expectedResult = service.addUsuarioGrupoToCollectionIfMissing(usuarioGrupoCollection, undefined, null);
        expect(expectedResult).toEqual(usuarioGrupoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
