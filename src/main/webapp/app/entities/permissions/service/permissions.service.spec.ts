import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPermissions, Permissions } from '../permissions.model';

import { PermissionsService } from './permissions.service';

describe('Permissions Service', () => {
  let service: PermissionsService;
  let httpMock: HttpTestingController;
  let elemDefault: IPermissions;
  let expectedResult: IPermissions | IPermissions[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PermissionsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idnVarPermissions: 'AAAAAAA',
      nVarNome: 'AAAAAAA',
      nVarTipoPermissao: 'AAAAAAA',
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

    it('should create a Permissions', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Permissions()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Permissions', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarPermissions: 'BBBBBB',
          nVarNome: 'BBBBBB',
          nVarTipoPermissao: 'BBBBBB',
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

    it('should partial update a Permissions', () => {
      const patchObject = Object.assign(
        {
          idnVarPermissions: 'BBBBBB',
          nVarTipoPermissao: 'BBBBBB',
          idnVarUsuario: 'BBBBBB',
        },
        new Permissions()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Permissions', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarPermissions: 'BBBBBB',
          nVarNome: 'BBBBBB',
          nVarTipoPermissao: 'BBBBBB',
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

    it('should delete a Permissions', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPermissionsToCollectionIfMissing', () => {
      it('should add a Permissions to an empty array', () => {
        const permissions: IPermissions = { id: 123 };
        expectedResult = service.addPermissionsToCollectionIfMissing([], permissions);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(permissions);
      });

      it('should not add a Permissions to an array that contains it', () => {
        const permissions: IPermissions = { id: 123 };
        const permissionsCollection: IPermissions[] = [
          {
            ...permissions,
          },
          { id: 456 },
        ];
        expectedResult = service.addPermissionsToCollectionIfMissing(permissionsCollection, permissions);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Permissions to an array that doesn't contain it", () => {
        const permissions: IPermissions = { id: 123 };
        const permissionsCollection: IPermissions[] = [{ id: 456 }];
        expectedResult = service.addPermissionsToCollectionIfMissing(permissionsCollection, permissions);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(permissions);
      });

      it('should add only unique Permissions to an array', () => {
        const permissionsArray: IPermissions[] = [{ id: 123 }, { id: 456 }, { id: 17645 }];
        const permissionsCollection: IPermissions[] = [{ id: 123 }];
        expectedResult = service.addPermissionsToCollectionIfMissing(permissionsCollection, ...permissionsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const permissions: IPermissions = { id: 123 };
        const permissions2: IPermissions = { id: 456 };
        expectedResult = service.addPermissionsToCollectionIfMissing([], permissions, permissions2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(permissions);
        expect(expectedResult).toContain(permissions2);
      });

      it('should accept null and undefined values', () => {
        const permissions: IPermissions = { id: 123 };
        expectedResult = service.addPermissionsToCollectionIfMissing([], null, permissions, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(permissions);
      });

      it('should return initial array if no Permissions is added', () => {
        const permissionsCollection: IPermissions[] = [{ id: 123 }];
        expectedResult = service.addPermissionsToCollectionIfMissing(permissionsCollection, undefined, null);
        expect(expectedResult).toEqual(permissionsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
