import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAppEmpresa, AppEmpresa } from '../app-empresa.model';

import { AppEmpresaService } from './app-empresa.service';

describe('AppEmpresa Service', () => {
  let service: AppEmpresaService;
  let httpMock: HttpTestingController;
  let elemDefault: IAppEmpresa;
  let expectedResult: IAppEmpresa | IAppEmpresa[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AppEmpresaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idnVarApp: 'AAAAAAA',
      idnVarEmpresa: 'AAAAAAA',
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

    it('should create a AppEmpresa', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AppEmpresa()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AppEmpresa', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarApp: 'BBBBBB',
          idnVarEmpresa: 'BBBBBB',
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

    it('should partial update a AppEmpresa', () => {
      const patchObject = Object.assign(
        {
          idnVarApp: 'BBBBBB',
          idnVarUsuario: 'BBBBBB',
        },
        new AppEmpresa()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AppEmpresa', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarApp: 'BBBBBB',
          idnVarEmpresa: 'BBBBBB',
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

    it('should delete a AppEmpresa', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAppEmpresaToCollectionIfMissing', () => {
      it('should add a AppEmpresa to an empty array', () => {
        const appEmpresa: IAppEmpresa = { id: 123 };
        expectedResult = service.addAppEmpresaToCollectionIfMissing([], appEmpresa);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appEmpresa);
      });

      it('should not add a AppEmpresa to an array that contains it', () => {
        const appEmpresa: IAppEmpresa = { id: 123 };
        const appEmpresaCollection: IAppEmpresa[] = [
          {
            ...appEmpresa,
          },
          { id: 456 },
        ];
        expectedResult = service.addAppEmpresaToCollectionIfMissing(appEmpresaCollection, appEmpresa);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AppEmpresa to an array that doesn't contain it", () => {
        const appEmpresa: IAppEmpresa = { id: 123 };
        const appEmpresaCollection: IAppEmpresa[] = [{ id: 456 }];
        expectedResult = service.addAppEmpresaToCollectionIfMissing(appEmpresaCollection, appEmpresa);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appEmpresa);
      });

      it('should add only unique AppEmpresa to an array', () => {
        const appEmpresaArray: IAppEmpresa[] = [{ id: 123 }, { id: 456 }, { id: 2114 }];
        const appEmpresaCollection: IAppEmpresa[] = [{ id: 123 }];
        expectedResult = service.addAppEmpresaToCollectionIfMissing(appEmpresaCollection, ...appEmpresaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const appEmpresa: IAppEmpresa = { id: 123 };
        const appEmpresa2: IAppEmpresa = { id: 456 };
        expectedResult = service.addAppEmpresaToCollectionIfMissing([], appEmpresa, appEmpresa2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appEmpresa);
        expect(expectedResult).toContain(appEmpresa2);
      });

      it('should accept null and undefined values', () => {
        const appEmpresa: IAppEmpresa = { id: 123 };
        expectedResult = service.addAppEmpresaToCollectionIfMissing([], null, appEmpresa, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appEmpresa);
      });

      it('should return initial array if no AppEmpresa is added', () => {
        const appEmpresaCollection: IAppEmpresa[] = [{ id: 123 }];
        expectedResult = service.addAppEmpresaToCollectionIfMissing(appEmpresaCollection, undefined, null);
        expect(expectedResult).toEqual(appEmpresaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
