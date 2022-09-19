import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPermissionsPapel, PermissionsPapel } from '../permissions-papel.model';

import { PermissionsPapelService } from './permissions-papel.service';

describe('PermissionsPapel Service', () => {
  let service: PermissionsPapelService;
  let httpMock: HttpTestingController;
  let elemDefault: IPermissionsPapel;
  let expectedResult: IPermissionsPapel | IPermissionsPapel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PermissionsPapelService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idnVarPermissions: 'AAAAAAA',
      idnVarPapel: 'AAAAAAA',
      idnVarFeatures: 'AAAAAAA',
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

    it('should create a PermissionsPapel', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PermissionsPapel()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PermissionsPapel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarPermissions: 'BBBBBB',
          idnVarPapel: 'BBBBBB',
          idnVarFeatures: 'BBBBBB',
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

    it('should partial update a PermissionsPapel', () => {
      const patchObject = Object.assign(
        {
          idnVarPermissions: 'BBBBBB',
          idnVarFeatures: 'BBBBBB',
          idnVarUsuario: 'BBBBBB',
        },
        new PermissionsPapel()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PermissionsPapel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idnVarPermissions: 'BBBBBB',
          idnVarPapel: 'BBBBBB',
          idnVarFeatures: 'BBBBBB',
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

    it('should delete a PermissionsPapel', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPermissionsPapelToCollectionIfMissing', () => {
      it('should add a PermissionsPapel to an empty array', () => {
        const permissionsPapel: IPermissionsPapel = { id: 123 };
        expectedResult = service.addPermissionsPapelToCollectionIfMissing([], permissionsPapel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(permissionsPapel);
      });

      it('should not add a PermissionsPapel to an array that contains it', () => {
        const permissionsPapel: IPermissionsPapel = { id: 123 };
        const permissionsPapelCollection: IPermissionsPapel[] = [
          {
            ...permissionsPapel,
          },
          { id: 456 },
        ];
        expectedResult = service.addPermissionsPapelToCollectionIfMissing(permissionsPapelCollection, permissionsPapel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PermissionsPapel to an array that doesn't contain it", () => {
        const permissionsPapel: IPermissionsPapel = { id: 123 };
        const permissionsPapelCollection: IPermissionsPapel[] = [{ id: 456 }];
        expectedResult = service.addPermissionsPapelToCollectionIfMissing(permissionsPapelCollection, permissionsPapel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(permissionsPapel);
      });

      it('should add only unique PermissionsPapel to an array', () => {
        const permissionsPapelArray: IPermissionsPapel[] = [{ id: 123 }, { id: 456 }, { id: 66519 }];
        const permissionsPapelCollection: IPermissionsPapel[] = [{ id: 123 }];
        expectedResult = service.addPermissionsPapelToCollectionIfMissing(permissionsPapelCollection, ...permissionsPapelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const permissionsPapel: IPermissionsPapel = { id: 123 };
        const permissionsPapel2: IPermissionsPapel = { id: 456 };
        expectedResult = service.addPermissionsPapelToCollectionIfMissing([], permissionsPapel, permissionsPapel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(permissionsPapel);
        expect(expectedResult).toContain(permissionsPapel2);
      });

      it('should accept null and undefined values', () => {
        const permissionsPapel: IPermissionsPapel = { id: 123 };
        expectedResult = service.addPermissionsPapelToCollectionIfMissing([], null, permissionsPapel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(permissionsPapel);
      });

      it('should return initial array if no PermissionsPapel is added', () => {
        const permissionsPapelCollection: IPermissionsPapel[] = [{ id: 123 }];
        expectedResult = service.addPermissionsPapelToCollectionIfMissing(permissionsPapelCollection, undefined, null);
        expect(expectedResult).toEqual(permissionsPapelCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
