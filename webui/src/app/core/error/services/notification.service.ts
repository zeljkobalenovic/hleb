import { JsonPipe } from '@angular/common';
import { Injectable } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { ModalNotificationComponent } from 'src/app/shared/components/modal-notification.component';
import { MyError } from '../../model/myError';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  bsModalRef: BsModalRef;
  constructor(private modalService: BsModalService) {}
 
  showErrorNotification(error: MyError) {
    const initialState = {
      title: 'Error Notification',
      btnName: 'Close',
      message: error.message,
      subErrors: error.subErrors,
      stack: error.stack
    };
    this.bsModalRef = this.modalService.show(ModalNotificationComponent, {initialState});
  }
}
