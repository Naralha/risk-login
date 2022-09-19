import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGrupo, Grupo } from '../grupo.model';

import { GrupoService } from './grupo.service';

describe('Grupo Service', () => {
  let service: GrupoService;
  let httpMock: HttpTestingController;
  let elemDefault: IGrupo;
  let expectedResult: IGrupo | IGrupo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GrupoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idnVarGrupo: 'AAAAAAA',
      nVarNome: 'AAAAAAA',
      idnVarUsuario: 'AAAAAAA',
      idnVarEmpresa: 'AAAAAAA',
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

    it('should create a Grupo', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Grupo()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Grupo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarGrupo: 'BBBBBB',
          nVarNome: 'BBBBBB',
          idnVarUsuario: 'BBBBBB',
          idnVarEmpresa: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Grupo', () => {
      const patchObject = Object.assign(
        {
          idnVarGrupo: 'BBBBBB',
        },
        new Grupo()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Grupo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarGrupo: 'BBBBBB',
          nVarNome: 'BBBBBB',
          idnVarUsuario: 'BBBBBB',
          idnVarEmpresa: 'BBBBBB',
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

    it('should delete a Grupo', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGrupoToCollectionIfMissing', () => {
      it('should add a Grupo to an empty array', () => {
        const grupo: IGrupo = { id: 123 };
        expectedResult = service.addGrupoToCollectionIfMissing([], grupo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(grupo);
      });

      it('should not add a Grupo to an array that contains it', () => {
        const grupo: IGrupo = { id: 123 };
        const grupoCollection: IGrupo[] = [
          {
            ...grupo,
          },
          { id: 456 },
        ];
        expectedResult = service.addGrupoToCollectionIfMissing(grupoCollection, grupo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Grupo to an array that doesn't contain it", () => {
        const grupo: IGrupo = { id: 123 };
        const grupoCollection: IGrupo[] = [{ id: 456 }];
        expectedResult = service.addGrupoToCollectionIfMissing(grupoCollection, grupo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(grupo);
      });

      it('should add only unique Grupo to an array', () => {
        const grupoArray: IGrupo[] = [{ id: 123 }, { id: 456 }, { id: 45074 }];
        const grupoCollection: IGrupo[] = [{ id: 123 }];
        expectedResult = service.addGrupoToCollectionIfMissing(grupoCollection, ...grupoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const grupo: IGrupo = { id: 123 };
        const grupo2: IGrupo = { id: 456 };
        expectedResult = service.addGrupoToCollectionIfMissing([], grupo, grupo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(grupo);
        expect(expectedResult).toContain(grupo2);
      });

      it('should accept null and undefined values', () => {
        const grupo: IGrupo = { id: 123 };
        expectedResult = service.addGrupoToCollectionIfMissing([], null, grupo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(grupo);
      });

      it('should return initial array if no Grupo is added', () => {
        const grupoCollection: IGrupo[] = [{ id: 123 }];
        expectedResult = service.addGrupoToCollectionIfMissing(grupoCollection, undefined, null);
        expect(expectedResult).toEqual(grupoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
