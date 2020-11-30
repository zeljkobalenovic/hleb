export interface Customer {
    id : number;
    name : string;
    code : string;
    streetAndNumber : string;
    postcode : number;
    city : string;
    customerGroupId : number;  
    customerGroupName? : string;
}