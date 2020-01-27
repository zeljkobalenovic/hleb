import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'webui';
  message = '';

  constructor(private http:HttpClient){};

  ngOnInit(): void {
    this.http.get('/api/hello').subscribe(data=>{
      console.log('DATA', data);
      this.message=data['message'];
    })
    }
    
}
