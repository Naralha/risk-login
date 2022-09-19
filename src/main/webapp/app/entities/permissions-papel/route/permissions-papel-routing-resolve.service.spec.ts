import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPermissionsPapel, PermissionsPapel } from '../permissions-papel.model';
import { PermissionsPapelService } from '../service/permissions-papel.service';

import { PermissionsPapelRoutingResolveService } from './permissions-papel-routing-resolve.service';

describe('PermissionsPapel routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PermissionsPapelRoutingResolveService;
  let service: PermissionsPapelService;
  let resultPermissionsPapel: IPermissionsPapel | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(PermissionsPapelRoutingResolveService);
    service = TestBed.inject(PermissionsPapelService);
    resultPermissionsPapel = undefined;
  });

  describe('resolve', () => {
    it('should return IPermissionsPapel returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPermissionsPapel = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPermissionsPapel).toEqual({ id: 123 });
    });

    it('should return new IPermissionsPapel if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPermissionsPapel = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPermissionsPapel).toEqual(new PermissionsPapel());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PermissionsPapel })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPermissionsPapel = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPermissionsPapel).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
