import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUsuarioPapel, UsuarioPapel } from '../usuario-papel.model';

import { UsuarioPapelService } from './usuario-papel.service';

describe('UsuarioPapel Service', () => {
  let service: UsuarioPapelService;
  let httpMock: HttpTestingController;
  let elemDefault: IUsuarioPapel;
  let expectedResult: IUsuarioPapel | IUsuarioPapel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UsuarioPapelService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idnVarUsuarioCadastrado: 'AAAAAAA',
      idnVarPapel: 'AAAAAAA',
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

    it('should create a UsuarioPapel', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new UsuarioPapel()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UsuarioPapel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarUsuarioCadastrado: 'BBBBBB',
          idnVarPapel: 'BBBBBB',
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

    it('should partial update a UsuarioPapel', () => {
      const patchObject = Object.assign(
        {
          idnVarUsuario: 'BBBBBB',
        },
        new UsuarioPapel()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UsuarioPapel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarUsuarioCadastrado: 'BBBBBB',
          idnVarPapel: 'BBBBBB',
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

    it('should delete a UsuarioPapel', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUsuarioPapelToCollectionIfMissing', () => {
      it('should add a UsuarioPapel to an empty array', () => {
        const usuarioPapel: IUsuarioPapel = { id: 123 };
        expectedResult = service.addUsuarioPapelToCollectionIfMissing([], usuarioPapel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(usuarioPapel);
      });

      it('should not add a UsuarioPapel to an array that contains it', () => {
        const usuarioPapel: IUsuarioPapel = { id: 123 };
        const usuarioPapelCollection: IUsuarioPapel[] = [
          {
            ...usuarioPapel,
          },
          { id: 456 },
        ];
        expectedResult = service.addUsuarioPapelToCollectionIfMissing(usuarioPapelCollection, usuarioPapel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UsuarioPapel to an array that doesn't contain it", () => {
        const usuarioPapel: IUsuarioPapel = { id: 123 };
        const usuarioPapelCollection: IUsuarioPapel[] = [{ id: 456 }];
        expectedResult = service.addUsuarioPapelToCollectionIfMissing(usuarioPapelCollection, usuarioPapel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(usuarioPapel);
      });

      it('should add only unique UsuarioPapel to an array', () => {
        const usuarioPapelArray: IUsuarioPapel[] = [{ id: 123 }, { id: 456 }, { id: 61113 }];
        const usuarioPapelCollection: IUsuarioPapel[] = [{ id: 123 }];
        expectedResult = service.addUsuarioPapelToCollectionIfMissing(usuarioPapelCollection, ...usuarioPapelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const usuarioPapel: IUsuarioPapel = { id: 123 };
        const usuarioPapel2: IUsuarioPapel = { id: 456 };
        expectedResult = service.addUsuarioPapelToCollectionIfMissing([], usuarioPapel, usuarioPapel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(usuarioPapel);
        expect(expectedResult).toContain(usuarioPapel2);
      });

      it('should accept null and undefined values', () => {
        const usuarioPapel: IUsuarioPapel = { id: 123 };
        expectedResult = service.addUsuarioPapelToCollectionIfMissing([], null, usuarioPapel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(usuarioPapel);
      });

      it('should return initial array if no UsuarioPapel is added', () => {
        const usuarioPapelCollection: IUsuarioPapel[] = [{ id: 123 }];
        expectedResult = service.addUsuarioPapelToCollectionIfMissing(usuarioPapelCollection, undefined, null);
        expect(expectedResult).toEqual(usuarioPapelCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
