import { Component, OnInit } from '@angular/core';
import { AuthClientConfig } from '@auth0/auth0-angular';
import { ApiService } from 'src/app/api.service';
import { AuthService } from '@auth0/auth0-angular';

@Component({
  selector: 'app-external-api',
  templateUrl: './external-api.component.html',
  styleUrls: ['./external-api.component.css'],
})

export class ExternalApiComponent implements OnInit {
  responseJson: string;
  audience = this.configFactory.get()?.audience;
  hasApiError = false;
  isForbidden = false;
  userProfile:string = null;

  constructor(
    private api: ApiService,
    private configFactory: AuthClientConfig,
    public auth: AuthService
  ) {}

  ngOnInit() {
    this.auth.getAccessTokenSilently().subscribe({
      next: (res) => {
        console.log(res);
        this.ping(res);
      }
    })
  }

  ping(token){
    this.api.ping$(token).subscribe({
      next: (res) => {
        this.hasApiError = false;
        this.responseJson = JSON.stringify(res, null, 2).trim();
      },
      error: (error) => {
        console.log(error);
        if(error.status == 403 && error.headers.get("x-user-email-verified") != undefined){
          this.hasApiError = false;
          this.isForbidden = true;
        }else{
          this.hasApiError = true
        }
      }
    });
  }

  verifyEmail(userId:string, toVerify:boolean){
    console.log(this.userProfile);
    this.api.verifyEmail$(userId, toVerify).subscribe({
      next: (res) => {
        this.isForbidden = false;
        window.location.reload();
      }
    })
  }
}
