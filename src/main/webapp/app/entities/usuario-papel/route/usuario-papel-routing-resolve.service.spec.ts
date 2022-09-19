import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IUsuarioPapel, UsuarioPapel } from '../usuario-papel.model';
import { UsuarioPapelService } from '../service/usuario-papel.service';

import { UsuarioPapelRoutingResolveService } from './usuario-papel-routing-resolve.service';

describe('UsuarioPapel routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: UsuarioPapelRoutingResolveService;
  let service: UsuarioPapelService;
  let resultUsuarioPapel: IUsuarioPapel | undefined;

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
    routingResolveService = TestBed.inject(UsuarioPapelRoutingResolveService);
    service = TestBed.inject(UsuarioPapelService);
    resultUsuarioPapel = undefined;
  });

  describe('resolve', () => {
    it('should return IUsuarioPapel returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUsuarioPapel = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUsuarioPapel).toEqual({ id: 123 });
    });

    it('should return new IUsuarioPapel if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUsuarioPapel = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultUsuarioPapel).toEqual(new UsuarioPapel());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as UsuarioPapel })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUsuarioPapel = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUsuarioPapel).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
