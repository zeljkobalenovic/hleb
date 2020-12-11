import { Injectable } from '@angular/core';
import { ObservableStore } from '@codewithdan/observable-store';
import { StoreState } from 'src/app/shared/store/store-state';

@Injectable({
  providedIn: 'root'
})
export class LoaderService extends ObservableStore<StoreState> {

  constructor() {
    super({trackStateHistory:true , logStateChanges:true});
  }

  showLoader(showLoader : boolean) {
    this.setState({ showLoader } , 'LOADER_STATE_TOGLE');
  }
}
