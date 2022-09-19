import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IUsuarioGrupo, UsuarioGrupo } from '../usuario-grupo.model';
import { UsuarioGrupoService } from '../service/usuario-grupo.service';

import { UsuarioGrupoRoutingResolveService } from './usuario-grupo-routing-resolve.service';

describe('UsuarioGrupo routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: UsuarioGrupoRoutingResolveService;
  let service: UsuarioGrupoService;
  let resultUsuarioGrupo: IUsuarioGrupo | undefined;

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
    routingResolveService = TestBed.inject(UsuarioGrupoRoutingResolveService);
    service = TestBed.inject(UsuarioGrupoService);
    resultUsuarioGrupo = undefined;
  });

  describe('resolve', () => {
    it('should return IUsuarioGrupo returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUsuarioGrupo = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUsuarioGrupo).toEqual({ id: 123 });
    });

    it('should return new IUsuarioGrupo if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUsuarioGrupo = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultUsuarioGrupo).toEqual(new UsuarioGrupo());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as UsuarioGrupo })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUsuarioGrupo = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUsuarioGrupo).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
