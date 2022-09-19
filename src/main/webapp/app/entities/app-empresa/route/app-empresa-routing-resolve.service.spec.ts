import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IAppEmpresa, AppEmpresa } from '../app-empresa.model';
import { AppEmpresaService } from '../service/app-empresa.service';

import { AppEmpresaRoutingResolveService } from './app-empresa-routing-resolve.service';

describe('AppEmpresa routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AppEmpresaRoutingResolveService;
  let service: AppEmpresaService;
  let resultAppEmpresa: IAppEmpresa | undefined;

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
    routingResolveService = TestBed.inject(AppEmpresaRoutingResolveService);
    service = TestBed.inject(AppEmpresaService);
    resultAppEmpresa = undefined;
  });

  describe('resolve', () => {
    it('should return IAppEmpresa returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAppEmpresa = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAppEmpresa).toEqual({ id: 123 });
    });

    it('should return new IAppEmpresa if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAppEmpresa = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAppEmpresa).toEqual(new AppEmpresa());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AppEmpresa })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAppEmpresa = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAppEmpresa).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
