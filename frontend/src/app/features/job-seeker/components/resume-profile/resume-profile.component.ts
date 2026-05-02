import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from '../../../../shared/components/navbar/navbar.component';
import { FooterComponent } from '../../../../shared/components/footer/footer.component';
import { ResumeService } from '../../services/resume.service';
import { AuthService } from '../../../../core/services/auth.service';
import { AiService } from '../../services/ai.service';

@Component({
  selector: 'app-resume-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarComponent, FooterComponent],
  template: `
<div class="min-h-screen flex flex-col bg-gray-50">
  <app-navbar />

  <!-- Toast Popup -->
  <div *ngIf="toastMessage" class="fixed top-6 left-1/2 -translate-x-1/2 z-50 bg-white border border-green-200 shadow-xl rounded-2xl px-6 py-4 flex items-center gap-4 min-w-80">
    <div class="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center text-green-600 text-xl shrink-0">✅</div>
    <div>
      <p class="font-semibold text-gray-900 text-sm">{{ toastMessage }}</p>
      <p class="text-xs text-gray-400 mt-0.5">{{ toastSub }}</p>
    </div>
    <button (click)="toastMessage = ''" class="ml-auto text-gray-300 hover:text-gray-500 text-xl leading-none">&times;</button>
  </div>

  <main class="flex-1 max-w-6xl mx-auto w-full px-6 py-8">
    <div class="flex items-center justify-between mb-6">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Resume &amp; Profile</h1>
        <p class="text-sm text-gray-500 mt-1">Manage your professional details and application documents.</p>
      </div>
      <button (click)="showPublicProfile = true" class="border border-gray-200 text-gray-700 px-4 py-2 rounded-full text-sm font-medium hover:bg-gray-50">View Public Profile</button>
    </div>

    <!-- Public Profile Modal -->
    <div *ngIf="showPublicProfile" class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 px-4">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-lg overflow-hidden">
        <div class="bg-blue-700 px-6 py-5 flex items-center justify-between">
          <div class="flex items-center gap-4">
            <div class="w-14 h-14 bg-white/20 rounded-full flex items-center justify-center text-white text-xl font-bold">
              {{ profile?.name?.substring(0,2)?.toUpperCase() || 'U' }}
            </div>
            <div>
              <p class="text-white font-bold text-lg">{{ profile?.name }}</p>
              <p class="text-blue-200 text-sm">{{ profile?.email }}</p>
            </div>
          </div>
          <button (click)="showPublicProfile = false" class="text-white/70 hover:text-white text-2xl leading-none">&times;</button>
        </div>
        <div class="p-6 space-y-4">
          <div *ngIf="headline">
            <p class="text-xs font-semibold text-gray-400 uppercase mb-1">Headline</p>
            <p class="text-sm text-gray-800">{{ headline }}</p>
          </div>
          <div *ngIf="skills">
            <p class="text-xs font-semibold text-gray-400 uppercase mb-2">Skills</p>
            <div class="flex flex-wrap gap-2">
              <span *ngFor="let skill of skills.split(',')" class="bg-blue-50 text-blue-700 text-xs px-3 py-1 rounded-full border border-blue-100">{{ skill.trim() }}</span>
            </div>
          </div>
          <div *ngIf="profile?.mobile">
            <p class="text-xs font-semibold text-gray-400 uppercase mb-1">Contact</p>
            <p class="text-sm text-gray-800">{{ profile?.mobile }}</p>
          </div>
          <div *ngIf="activeResume">
            <p class="text-xs font-semibold text-gray-400 uppercase mb-2">Resume</p>
            <div class="flex items-center gap-3 p-3 bg-gray-50 rounded-lg cursor-pointer" (click)="openResume(activeResume.fileUrl)">
              <span class="text-red-500 text-xl">📄</span>
              <div>
                <p class="text-sm font-medium text-blue-600 hover:underline">{{ getFileName(activeResume.fileUrl) }}</p>
                <p class="text-xs text-gray-400">Uploaded {{ activeResume.uploadedAt | date:'MMM dd, yyyy' }}</p>
              </div>
            </div>
          </div>
          <div *ngIf="!headline && !skills && !profile?.mobile && !activeResume" class="text-center py-4 text-gray-400 text-sm">
            Your profile is empty. Add your headline, skills and resume to make it visible to recruiters.
          </div>
        </div>
        <div class="px-6 pb-5">
          <button (click)="showPublicProfile = false" class="w-full bg-blue-700 hover:bg-blue-800 text-white py-2.5 rounded-xl text-sm font-medium">Close</button>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <div class="bg-white rounded-xl border border-gray-100 p-6">
        <div class="flex items-center justify-between mb-5">
          <h2 class="font-bold text-gray-900">Profile Information</h2>
          <button (click)="saveProfile()" [disabled]="saving" class="text-blue-600 text-sm font-medium hover:underline disabled:opacity-50">{{ saving ? 'Saving...' : 'Save Changes' }}</button>
        </div>
        <div class="mb-4">
          <label class="block text-sm font-medium text-blue-600 mb-1">Professional Headline</label>
          <input [(ngModel)]="headline" type="text" placeholder="e.g. Senior Frontend Developer | React | TypeScript" class="w-full border border-gray-200 rounded-lg px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
        <div class="mb-4">
          <label class="block text-sm font-medium text-blue-600 mb-1">Professional Summary</label>
          <textarea [(ngModel)]="summary" rows="4" placeholder="Write a brief professional summary..." class="w-full border border-gray-200 rounded-lg px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"></textarea>
        </div>
        <div>
          <label class="block text-sm font-medium text-blue-600 mb-1">Top Skills</label>
          <input [(ngModel)]="skills" type="text" placeholder="e.g. React, TypeScript, Node.js" class="w-full border border-gray-200 rounded-lg px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 mb-2" />
          <div *ngIf="skills" class="flex flex-wrap gap-2">
            <span *ngFor="let skill of skills.split(',')" class="bg-gray-100 text-gray-700 text-xs px-3 py-1 rounded-full">{{ skill.trim() }}</span>
          </div>
        </div>
      </div>

      <div class="space-y-5">
        <div class="bg-white rounded-xl border border-gray-100 p-6">
          <div class="flex items-center justify-between mb-4">
            <h2 class="font-bold text-gray-900">Current Resume</h2>
            <span *ngIf="activeResume" class="bg-green-100 text-green-700 text-xs font-semibold px-2.5 py-1 rounded-full">ACTIVE</span>
          </div>
          <div *ngIf="activeResume" class="flex items-center justify-between p-3 bg-gray-50 rounded-lg mb-4">
            <div class="flex items-center gap-3 cursor-pointer" (click)="openResume(activeResume.fileUrl)">
              <span class="text-red-500 text-xl">📄</span>
              <div>
                <p class="text-sm font-medium text-blue-600 hover:underline">{{ getFileName(activeResume.fileUrl) }}</p>
                <p class="text-xs text-gray-400">Uploaded {{ activeResume.uploadedAt | date:'MMM dd, yyyy' }}</p>
              </div>
            </div>
            <div class="flex gap-2">
              <button (click)="analyzeResume()" [disabled]="analyzing"
                class="bg-purple-600 hover:bg-purple-700 text-white text-xs px-3 py-1.5 rounded-full font-medium disabled:opacity-50 flex items-center gap-1">
                {{ analyzing ? 'Analyzing...' : 'Analyze' }}
              </button>
              <button (click)="deleteResume(activeResume.resumeId)" class="text-gray-400 hover:text-red-500 text-sm">🗑</button>
            </div>
          </div>

          <!-- AI Analysis Result -->
          <div *ngIf="analysisResult" class="mt-3 border border-purple-200 rounded-xl overflow-hidden">
            <div class="bg-purple-600 px-4 py-3 flex items-center justify-between">
              <span class="text-white font-semibold text-sm">AI Resume Analysis</span>
              <button (click)="analysisResult = null" class="text-purple-200 hover:text-white">&times;</button>
            </div>
            <div class="p-4 space-y-4">
              <!-- Score -->
              <div class="flex items-center gap-4">
                <div class="w-16 h-16 rounded-full flex items-center justify-center text-xl font-bold shrink-0"
                  [class]="analysisResult.score >= 75 ? 'bg-green-100 text-green-700' : analysisResult.score >= 50 ? 'bg-yellow-100 text-yellow-700' : 'bg-red-100 text-red-600'">
                  {{ analysisResult.score }}
                </div>
                <div>
                  <p class="text-sm font-semibold text-gray-900">Resume Score</p>
                  <p class="text-xs text-gray-500">{{ analysisResult.summary }}</p>
                </div>
              </div>
              <!-- Strengths -->
              <div *ngIf="analysisResult.strengths?.length">
                <p class="text-xs font-semibold text-green-700 uppercase mb-2">✅ Strengths</p>
                <ul class="space-y-1">
                  <li *ngFor="let s of analysisResult.strengths" class="text-xs text-gray-600 flex gap-2"><span class="text-green-500">•</span>{{ s }}</li>
                </ul>
              </div>
              <!-- Improvements -->
              <div *ngIf="analysisResult.improvements?.length">
                <p class="text-xs font-semibold text-yellow-700 uppercase mb-2">⚠️ Improvements</p>
                <ul class="space-y-1">
                  <li *ngFor="let i of analysisResult.improvements" class="text-xs text-gray-600 flex gap-2"><span class="text-yellow-500">•</span>{{ i }}</li>
                </ul>
              </div>
              <!-- Missing Keywords -->
              <div *ngIf="analysisResult.missingKeywords?.length">
                <p class="text-xs font-semibold text-red-600 uppercase mb-2">🔍 Missing Keywords</p>
                <div class="flex flex-wrap gap-1">
                  <span *ngFor="let k of analysisResult.missingKeywords"
                    class="bg-red-50 border border-red-200 text-red-600 text-xs px-2 py-0.5 rounded-full">{{ k }}</span>
                </div>
              </div>
              <!-- Recommendation -->
              <div *ngIf="analysisResult.recommendation" class="bg-blue-50 rounded-lg p-3">
                <p class="text-xs font-semibold text-blue-700 mb-1">💡 Recommendation</p>
                <p class="text-xs text-blue-600">{{ analysisResult.recommendation }}</p>
              </div>
            </div>
          </div>
          <div *ngIf="!activeResume" class="text-center py-4 text-gray-400 text-sm">No resume uploaded yet</div>
        </div>

        <div class="bg-white rounded-xl border border-gray-100 p-6">
          <h2 class="font-bold text-gray-900 mb-4">Upload New Resume</h2>
          <label class="block border-2 border-dashed border-gray-200 rounded-xl p-8 text-center cursor-pointer hover:border-blue-400 transition">
            <input type="file" accept=".pdf,.doc,.docx" (change)="onFileSelected($event)" class="hidden" />
            <div *ngIf="!uploading">
              <div class="text-3xl mb-2">⬆️</div>
              <p class="text-sm font-medium text-gray-700">Click to upload or drag and drop</p>
              <p class="text-xs text-gray-400 mt-1">PDF or DOCX (Max. 5MB)</p>
            </div>
            <div *ngIf="uploading" class="flex items-center justify-center gap-2">
              <div class="animate-spin rounded-full h-5 w-5 border-b-2 border-blue-700"></div>
              <span class="text-sm text-gray-600">Uploading...</span>
            </div>
          </label>
        </div>

        <div *ngIf="historyResumes.length > 0" class="bg-white rounded-xl border border-gray-100 p-6">
          <div class="flex items-center justify-between mb-4">
            <h2 class="font-bold text-gray-900">Version History</h2>
            <span class="text-xs text-gray-400">Past {{ historyResumes.length }} uploads</span>
          </div>
          <div class="space-y-3">
            <div *ngFor="let r of historyResumes" class="flex items-center justify-between">
              <div class="flex items-center gap-3 cursor-pointer" (click)="openResume(r.fileUrl)">
                <span class="text-blue-500">📄</span>
                <div>
                  <p class="text-sm text-blue-600 hover:underline">{{ getFileName(r.fileUrl) }}</p>
                  <p class="text-xs text-gray-400">{{ r.uploadedAt | date:'MMM dd, yyyy' }}</p>
                </div>
              </div>
              <button (click)="deleteResume(r.resumeId)" class="text-gray-400 hover:text-red-500">🗑</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
  <app-footer />
</div>
  `
})
export class ResumeProfileComponent implements OnInit {

  profile: any = null;
  resumes: any[] = [];
  loading = true;
  saving = false;
  uploading = false;
  toastMessage = '';
  toastSub = '';

  headline = '';
  summary = '';
  skills = '';

  analyzing = false;
  analysisResult: any = null;
  showPublicProfile = false;

  constructor(
    private resumeService: ResumeService,
    private authService: AuthService,
    private aiService: AiService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadProfile();
    this.loadResumes();
  }

  showToast(message: string, sub: string) {
    this.toastMessage = message;
    this.toastSub = sub;
    this.cdr.detectChanges();
    setTimeout(() => { this.toastMessage = ''; this.cdr.detectChanges(); }, 4000);
  }

  loadProfile() {
    this.resumeService.getProfile().subscribe({
      next: (res) => {
        this.profile = res;
        this.headline = res.headline || res.name || '';
        this.skills = res.skills || '';
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  loadResumes() {
    this.resumeService.getMyResumes().subscribe({
      next: (res) => {
        this.resumes = Array.isArray(res) ? res : (res.content || []);
        this.cdr.detectChanges();
      },
      error: () => { this.cdr.detectChanges(); }
    });
  }

  saveProfile() {
    if (!this.profile) return;
    const nameToSave = (this.headline || this.profile.name || '').trim();
    if (!nameToSave) return;
    this.saving = true;
    this.resumeService.updateProfile(this.profile.id, { name: nameToSave }).subscribe({
      next: () => {
        this.saving = false;
        this.profile.name = nameToSave;
        this.showToast('Profile saved successfully!', 'Your profile has been updated.');
      },
      error: () => {
        this.saving = false;
        this.cdr.detectChanges();
      }
    });
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (!file) return;
    this.uploading = true;
    this.resumeService.uploadResumeFile(file).subscribe({
      next: (res) => {
        this.resumes = [res, ...this.resumes];
        this.uploading = false;
        this.showToast('Resume uploaded successfully!', this.getFileName(res.fileUrl) + ' is now active.');
      },
      error: () => {
        this.uploading = false;
        this.cdr.detectChanges();
      }
    });
  }

  deleteResume(id: number) {
    this.resumeService.deleteResume(id).subscribe({
      next: () => {
        this.resumes = this.resumes.filter(r => r.resumeId !== id);
        this.cdr.detectChanges();
      }
    });
  }

  analyzeResume() {
    if (!this.activeResume || this.analyzing) return;
    this.analyzing = true;
    this.analysisResult = null;
    this.aiService.analyzeResume(this.activeResume.fileUrl).subscribe({
      next: (res) => { this.analysisResult = res; this.analyzing = false; this.cdr.detectChanges(); },
      error: () => { this.analyzing = false; this.showToast('Analysis failed', 'Could not analyze resume.'); }
    });
  }

  get activeResume() { return this.resumes[0] || null; }
  get historyResumes() { return this.resumes.slice(1); }

  getFileName(fileUrl: string): string {
    if (!fileUrl) return 'Resume';
    const parts = fileUrl.split('/');
    const fullName = parts[parts.length - 1];
    return fullName.replace(/_\d{13}(\.[^.]+)$/, '$1');
  }

  openResume(fileUrl: string) {
    if (!fileUrl) return;
    const filename = fileUrl.split('/').pop();
    const url = `http://localhost:8080/api/resumes/download/${filename}`;
    const token = localStorage.getItem('token');
    fetch(url, { headers: { Authorization: `Bearer ${token}` } })
      .then(res => res.blob())
      .then(blob => {
        const blobUrl = window.URL.createObjectURL(blob);
        window.open(blobUrl, '_blank');
      })
      .catch(err => console.error('Failed to open resume:', err));
  }
}
