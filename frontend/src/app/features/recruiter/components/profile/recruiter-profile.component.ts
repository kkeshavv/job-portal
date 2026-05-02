import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { NavbarComponent } from '../../../../shared/components/navbar/navbar.component';
import { FooterComponent } from '../../../../shared/components/footer/footer.component';
import { AuthService } from '../../../../core/services/auth.service';
import { ConfirmModalComponent } from '../../../../shared/components/confirm-modal/confirm-modal.component';
import { environment } from '../../../../../environments/environment';
@Component({
  selector: 'app-recruiter-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarComponent, FooterComponent, ConfirmModalComponent],
  template: `
<div class="min-h-screen flex flex-col bg-gray-50">
  <app-navbar />

  <div *ngIf="toastMessage" class="fixed top-6 left-1/2 -translate-x-1/2 z-50 bg-white border border-green-200 shadow-xl rounded-2xl px-6 py-4 flex items-center gap-4 min-w-80">
    <div class="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center text-green-600 text-xl shrink-0">✅</div>
    <div>
      <p class="font-semibold text-gray-900 text-sm">{{ toastMessage }}</p>
      <p class="text-xs text-gray-400 mt-0.5">Your profile has been updated.</p>
    </div>
    <button (click)="toastMessage = ''" class="ml-auto text-gray-300 hover:text-gray-500 text-xl leading-none">&times;</button>
  </div>

  <main class="flex-1 max-w-3xl mx-auto w-full px-6 py-8">

    <div class="flex items-center gap-4 mb-8">
      <div class="w-16 h-16 bg-blue-600 rounded-full flex items-center justify-center text-white text-2xl font-bold shrink-0">
        {{ initials }}
      </div>
      <div>
        <h1 class="text-2xl font-bold text-gray-900">{{ name || email }}</h1>
        <p class="text-sm text-gray-400">{{ email }}</p>
        <span class="text-xs bg-blue-100 text-blue-700 font-semibold px-2.5 py-0.5 rounded-full mt-1 inline-block">RECRUITER</span>
      </div>
    </div>

    <div class="space-y-5">

      <div class="bg-white rounded-xl border border-gray-100 p-6">
        <h2 class="font-bold text-gray-900 mb-5">Personal Information</h2>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-600 mb-1">Full Name</label>
            <input [(ngModel)]="name" type="text" placeholder="Your full name"
              class="w-full border border-gray-200 rounded-lg px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-600 mb-1">Email Address</label>
            <input [value]="email" type="text" disabled
              class="w-full border border-gray-100 bg-gray-50 rounded-lg px-3 py-2.5 text-sm text-gray-400 cursor-not-allowed" />
            <p class="text-xs text-gray-400 mt-1">Email cannot be changed</p>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-600 mb-1">Mobile Number</label>
            <input [(ngModel)]="mobile" type="tel" placeholder="e.g. +91 9876543210"
              class="w-full border border-gray-200 rounded-lg px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
        </div>
        <button (click)="saveProfile()" [disabled]="saving"
          class="mt-5 bg-blue-700 hover:bg-blue-800 text-white text-sm font-semibold px-6 py-2.5 rounded-full transition disabled:opacity-50">
          {{ saving ? 'Saving...' : 'Save Changes' }}
        </button>
      </div>

      <div class="bg-white rounded-xl border border-gray-100 p-6">
        <h2 class="font-bold text-gray-900 mb-4">Quick Links</h2>
        <div class="grid grid-cols-2 gap-3">
          <button (click)="router.navigate(['/recruiter/dashboard'])"
            class="flex items-center gap-3 p-4 border border-gray-100 rounded-xl hover:bg-blue-50 hover:border-blue-200 transition text-left">
            <span class="text-2xl">📊</span>
            <div>
              <p class="text-sm font-semibold text-gray-800">Dashboard</p>
              <p class="text-xs text-gray-400">View your overview</p>
            </div>
          </button>
          <button (click)="router.navigate(['/recruiter/my-jobs'])"
            class="flex items-center gap-3 p-4 border border-gray-100 rounded-xl hover:bg-blue-50 hover:border-blue-200 transition text-left">
            <span class="text-2xl">💼</span>
            <div>
              <p class="text-sm font-semibold text-gray-800">My Jobs</p>
              <p class="text-xs text-gray-400">Manage your postings</p>
            </div>
          </button>
          <button (click)="router.navigate(['/recruiter/applicants/0'])"
            class="flex items-center gap-3 p-4 border border-gray-100 rounded-xl hover:bg-blue-50 hover:border-blue-200 transition text-left">
            <span class="text-2xl">👥</span>
            <div>
              <p class="text-sm font-semibold text-gray-800">Applicants</p>
              <p class="text-xs text-gray-400">Review candidates</p>
            </div>
          </button>
          <button (click)="router.navigate(['/change-password'])"
            class="flex items-center gap-3 p-4 border border-gray-100 rounded-xl hover:bg-blue-50 hover:border-blue-200 transition text-left">
            <span class="text-2xl">🔒</span>
            <div>
              <p class="text-sm font-semibold text-gray-800">Change Password</p>
              <p class="text-xs text-gray-400">Update your password</p>
            </div>
          </button>
        </div>
      </div>

      <div class="bg-white rounded-xl border border-red-100 p-6">
        <h2 class="font-bold text-gray-900 mb-1">Account</h2>
        <p class="text-xs text-gray-400 mb-4">Manage your account session</p>
        <button (click)="showLogoutModal = true"
          class="flex items-center gap-2 text-red-500 border border-red-200 px-5 py-2.5 rounded-full text-sm font-medium hover:bg-red-50 transition">
          🚪 Sign Out
        </button>
      </div>

    </div>
  </main>
  <app-footer />
</div>

<app-confirm-modal
  [visible]="showLogoutModal"
  (confirmed)="authService.logout()"
  (cancelled)="showLogoutModal = false">
</app-confirm-modal>
  `
})
export class RecruiterProfileComponent implements OnInit {

  name = '';
  mobile = '';
  saving = false;
  toastMessage = '';

  constructor(
    public router: Router,
    public authService: AuthService,
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  showLogoutModal = false;
  get email() { return this.authService.getEmail() || ''; }
  get initials() { return this.email ? this.email.substring(0, 2).toUpperCase() : 'R'; }
  confirmLogout() { this.showLogoutModal = true; }

  ngOnInit() {
    this.http.get<any>(`${environment.apiUrl}/api/users/me`).subscribe({
      next: (res) => { this.name = res.name || ''; this.mobile = res.mobile || ''; this.cdr.detectChanges(); },
      error: () => {}
    });
  }

  saveProfile() {
    if (!this.name.trim()) return;
    this.saving = true;
    this.http.get<any>(`${environment.apiUrl}/api/users/me`).subscribe({
      next: (user) => {
        this.http.put<any>(`${environment.apiUrl}/api/users/${user.id}`, {
          name: this.name.trim(),
          mobile: this.mobile.trim()
        }).subscribe({
          next: () => {
            this.saving = false;
            this.toastMessage = 'Profile updated!';
            this.cdr.detectChanges();
            setTimeout(() => { this.toastMessage = ''; this.cdr.detectChanges(); }, 4000);
          },
          error: () => { this.saving = false; this.cdr.detectChanges(); }
        });
      }
    });
  }
}
