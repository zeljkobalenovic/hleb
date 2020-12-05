import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-modal-notification',
  templateUrl: './modal-notification.component.html',
  styleUrls: ['./modal-notification.component.css']
})
export class ModalNotificationComponent implements OnInit {

  title: string;
  btnName: string;
  message: string;
  subErrors: string[];
  stack: any;
 
  constructor(public bsModalRef: BsModalRef) {}
 
  ngOnInit() {
    
  }
}
