import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IGrupoPapel, GrupoPapel } from '../grupo-papel.model';
import { GrupoPapelService } from '../service/grupo-papel.service';

import { GrupoPapelRoutingResolveService } from './grupo-papel-routing-resolve.service';

describe('GrupoPapel routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: GrupoPapelRoutingResolveService;
  let service: GrupoPapelService;
  let resultGrupoPapel: IGrupoPapel | undefined;

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
    routingResolveService = TestBed.inject(GrupoPapelRoutingResolveService);
    service = TestBed.inject(GrupoPapelService);
    resultGrupoPapel = undefined;
  });

  describe('resolve', () => {
    it('should return IGrupoPapel returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGrupoPapel = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGrupoPapel).toEqual({ id: 123 });
    });

    it('should return new IGrupoPapel if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGrupoPapel = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultGrupoPapel).toEqual(new GrupoPapel());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as GrupoPapel })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGrupoPapel = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGrupoPapel).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
