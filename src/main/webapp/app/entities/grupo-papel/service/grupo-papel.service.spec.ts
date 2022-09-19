import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGrupoPapel, GrupoPapel } from '../grupo-papel.model';

import { GrupoPapelService } from './grupo-papel.service';

describe('GrupoPapel Service', () => {
  let service: GrupoPapelService;
  let httpMock: HttpTestingController;
  let elemDefault: IGrupoPapel;
  let expectedResult: IGrupoPapel | IGrupoPapel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GrupoPapelService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idnVarGrupo: 'AAAAAAA',
      idnVarPapel: 'AAAAAAA',
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

    it('should create a GrupoPapel', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new GrupoPapel()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GrupoPapel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarGrupo: 'BBBBBB',
          idnVarPapel: 'BBBBBB',
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

    it('should partial update a GrupoPapel', () => {
      const patchObject = Object.assign(
        {
          idnVarGrupo: 'BBBBBB',
          idnVarPapel: 'BBBBBB',
        },
        new GrupoPapel()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GrupoPapel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarGrupo: 'BBBBBB',
          idnVarPapel: 'BBBBBB',
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

    it('should delete a GrupoPapel', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGrupoPapelToCollectionIfMissing', () => {
      it('should add a GrupoPapel to an empty array', () => {
        const grupoPapel: IGrupoPapel = { id: 123 };
        expectedResult = service.addGrupoPapelToCollectionIfMissing([], grupoPapel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(grupoPapel);
      });

      it('should not add a GrupoPapel to an array that contains it', () => {
        const grupoPapel: IGrupoPapel = { id: 123 };
        const grupoPapelCollection: IGrupoPapel[] = [
          {
            ...grupoPapel,
          },
          { id: 456 },
        ];
        expectedResult = service.addGrupoPapelToCollectionIfMissing(grupoPapelCollection, grupoPapel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GrupoPapel to an array that doesn't contain it", () => {
        const grupoPapel: IGrupoPapel = { id: 123 };
        const grupoPapelCollection: IGrupoPapel[] = [{ id: 456 }];
        expectedResult = service.addGrupoPapelToCollectionIfMissing(grupoPapelCollection, grupoPapel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(grupoPapel);
      });

      it('should add only unique GrupoPapel to an array', () => {
        const grupoPapelArray: IGrupoPapel[] = [{ id: 123 }, { id: 456 }, { id: 98365 }];
        const grupoPapelCollection: IGrupoPapel[] = [{ id: 123 }];
        expectedResult = service.addGrupoPapelToCollectionIfMissing(grupoPapelCollection, ...grupoPapelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const grupoPapel: IGrupoPapel = { id: 123 };
        const grupoPapel2: IGrupoPapel = { id: 456 };
        expectedResult = service.addGrupoPapelToCollectionIfMissing([], grupoPapel, grupoPapel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(grupoPapel);
        expect(expectedResult).toContain(grupoPapel2);
      });

      it('should accept null and undefined values', () => {
        const grupoPapel: IGrupoPapel = { id: 123 };
        expectedResult = service.addGrupoPapelToCollectionIfMissing([], null, grupoPapel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(grupoPapel);
      });

      it('should return initial array if no GrupoPapel is added', () => {
        const grupoPapelCollection: IGrupoPapel[] = [{ id: 123 }];
        expectedResult = service.addGrupoPapelToCollectionIfMissing(grupoPapelCollection, undefined, null);
        expect(expectedResult).toEqual(grupoPapelCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
