import { TestBed } from '@angular/core/testing';

import { LocalsService } from './locals.service';

describe('LocalsService', () => {
  let service: LocalsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LocalsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
