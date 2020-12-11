// sadrzi sva stanja koja ce biti obuhvacena state managmentom za sve feature

import { Customer } from 'src/app/core/model/customer';
import { CustomerGroup } from 'src/app/core/model/customerGroup';

export interface StoreState {

    // --------- LOADER SHOW --------------

    showLoader : boolean;

    // --------- CUSTOMER GROUP --------------

    customerGroups : CustomerGroup[];
    customerGroupsQueryOptions : QueryOptions;
    selectedCustomerGroup : CustomerGroup;
    activeTab : string;

    // ------------ CUSTOMER -----------------

    customers : Customer[];
    selectedCustomer : Customer;

}

export interface QueryOptions {
    filter : string;
    currentPage : number;
    pageSize : number;
}