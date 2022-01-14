import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as config from '../../auth_config.json';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private http: HttpClient) {}

  ping$(token:string): Observable<any> {
    // console.log(config.apiUri);
    return this.http.get(`${config.apiUri}/home/isalive`, {
      'headers' : {
        'Authorization' : token
      }
    });
  }

  verifyEmail$(userId, verifyFlag): Observable<any> {
    return this.http.post(`${config.apiUri}/identity/updateEmailVerification`, 
      JSON.stringify({
        "userId" : userId,
        "emailVerified" : verifyFlag
      }),
      {
        'headers' : {
          "Content-Type" : "application/json"
        }
      }
    );
  }
}
