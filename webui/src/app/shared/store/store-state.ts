// sadrzi sva stanja koja ce biti obuhvacena state managmentom za sve feature

import { Customer } from 'src/app/core/model/customer';
import { CustomerGroup } from 'src/app/core/model/customerGroup';

export interface StoreState {

    // --------- CUSTOMER GROUP --------------

    customerGroups : CustomerGroup[];

    // ------------ CUSTOMER -----------------

    customers : Customer[];



}