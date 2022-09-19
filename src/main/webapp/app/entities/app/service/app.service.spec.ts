import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IApp, App } from '../app.model';

import { AppService } from './app.service';

describe('App Service', () => {
  let service: AppService;
  let httpMock: HttpTestingController;
  let elemDefault: IApp;
  let expectedResult: IApp | IApp[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AppService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idnVarApp: 'AAAAAAA',
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

    it('should create a App', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new App()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a App', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarApp: 'BBBBBB',
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

    it('should partial update a App', () => {
      const patchObject = Object.assign(
        {
          idnVarApp: 'BBBBBB',
          nVarNome: 'BBBBBB',
          idnVarEmpresa: 'BBBBBB',
        },
        new App()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of App', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarApp: 'BBBBBB',
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

    it('should delete a App', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAppToCollectionIfMissing', () => {
      it('should add a App to an empty array', () => {
        const app: IApp = { id: 123 };
        expectedResult = service.addAppToCollectionIfMissing([], app);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(app);
      });

      it('should not add a App to an array that contains it', () => {
        const app: IApp = { id: 123 };
        const appCollection: IApp[] = [
          {
            ...app,
          },
          { id: 456 },
        ];
        expectedResult = service.addAppToCollectionIfMissing(appCollection, app);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a App to an array that doesn't contain it", () => {
        const app: IApp = { id: 123 };
        const appCollection: IApp[] = [{ id: 456 }];
        expectedResult = service.addAppToCollectionIfMissing(appCollection, app);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(app);
      });

      it('should add only unique App to an array', () => {
        const appArray: IApp[] = [{ id: 123 }, { id: 456 }, { id: 66942 }];
        const appCollection: IApp[] = [{ id: 123 }];
        expectedResult = service.addAppToCollectionIfMissing(appCollection, ...appArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const app: IApp = { id: 123 };
        const app2: IApp = { id: 456 };
        expectedResult = service.addAppToCollectionIfMissing([], app, app2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(app);
        expect(expectedResult).toContain(app2);
      });

      it('should accept null and undefined values', () => {
        const app: IApp = { id: 123 };
        expectedResult = service.addAppToCollectionIfMissing([], null, app, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(app);
      });

      it('should return initial array if no App is added', () => {
        const appCollection: IApp[] = [{ id: 123 }];
        expectedResult = service.addAppToCollectionIfMissing(appCollection, undefined, null);
        expect(expectedResult).toEqual(appCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
