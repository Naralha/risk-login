import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPapel, Papel } from '../papel.model';

import { PapelService } from './papel.service';

describe('Papel Service', () => {
  let service: PapelService;
  let httpMock: HttpTestingController;
  let elemDefault: IPapel;
  let expectedResult: IPapel | IPapel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PapelService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idnVarPapel: 'AAAAAAA',
      nVarNome: 'AAAAAAA',
      idnVarApp: 'AAAAAAA',
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

    it('should create a Papel', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Papel()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Papel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarPapel: 'BBBBBB',
          nVarNome: 'BBBBBB',
          idnVarApp: 'BBBBBB',
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

    it('should partial update a Papel', () => {
      const patchObject = Object.assign(
        {
          idnVarApp: 'BBBBBB',
        },
        new Papel()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Papel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarPapel: 'BBBBBB',
          nVarNome: 'BBBBBB',
          idnVarApp: 'BBBBBB',
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

    it('should delete a Papel', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPapelToCollectionIfMissing', () => {
      it('should add a Papel to an empty array', () => {
        const papel: IPapel = { id: 123 };
        expectedResult = service.addPapelToCollectionIfMissing([], papel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(papel);
      });

      it('should not add a Papel to an array that contains it', () => {
        const papel: IPapel = { id: 123 };
        const papelCollection: IPapel[] = [
          {
            ...papel,
          },
          { id: 456 },
        ];
        expectedResult = service.addPapelToCollectionIfMissing(papelCollection, papel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Papel to an array that doesn't contain it", () => {
        const papel: IPapel = { id: 123 };
        const papelCollection: IPapel[] = [{ id: 456 }];
        expectedResult = service.addPapelToCollectionIfMissing(papelCollection, papel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(papel);
      });

      it('should add only unique Papel to an array', () => {
        const papelArray: IPapel[] = [{ id: 123 }, { id: 456 }, { id: 47368 }];
        const papelCollection: IPapel[] = [{ id: 123 }];
        expectedResult = service.addPapelToCollectionIfMissing(papelCollection, ...papelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const papel: IPapel = { id: 123 };
        const papel2: IPapel = { id: 456 };
        expectedResult = service.addPapelToCollectionIfMissing([], papel, papel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(papel);
        expect(expectedResult).toContain(papel2);
      });

      it('should accept null and undefined values', () => {
        const papel: IPapel = { id: 123 };
        expectedResult = service.addPapelToCollectionIfMissing([], null, papel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(papel);
      });

      it('should return initial array if no Papel is added', () => {
        const papelCollection: IPapel[] = [{ id: 123 }];
        expectedResult = service.addPapelToCollectionIfMissing(papelCollection, undefined, null);
        expect(expectedResult).toEqual(papelCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
