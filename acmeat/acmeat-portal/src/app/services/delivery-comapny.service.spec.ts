import { TestBed } from '@angular/core/testing';

import { DeliveryComapnyService } from './delivery-comapny.service';

describe('DeliveryComapnyService', () => {
  let service: DeliveryComapnyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeliveryComapnyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
