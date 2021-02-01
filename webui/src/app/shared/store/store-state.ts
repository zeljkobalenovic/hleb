// sadrzi sva stanja koja ce biti obuhvacena state managmentom za sve feature

import { Customer } from 'src/app/core/model/customer';
import { CustomerGroup } from 'src/app/core/model/customerGroup';

export interface StoreState {

    // --------- LOADER SHOW -----------------
    showLoader : boolean;
    // ---- CUSTOMER AND CUSTOMER GROUP ------
    activeTab : string;

    customerGroups : CustomerGroup[];
    customers : Customer[];
    selectedCustomerGroup : CustomerGroup;
    selectedCustomer : Customer;
    customerGroupsQueryOptions : customerGroupQueryOptions;
    customerQueryOptions : customerQueryOptions;

    // ---- PRODUCT AND PRODUCT GROUP --------  
    

}

export interface customerGroupQueryOptions {
    filter : string;
    currentPage : number;
    pageSize : number;
}

export interface customerQueryOptions {
    filter : string[];
    currentPage : number;
    pageSize : number;
}