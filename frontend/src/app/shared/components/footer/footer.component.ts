import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [RouterLink],
  template: `
    <footer class="bg-white border-t border-gray-200 py-4 px-6 flex items-center justify-between text-xs text-gray-400 mt-auto">
      <div class="flex items-center gap-2">
        <span class="text-lg">🧭</span>
        <span class="font-black tracking-widest uppercase text-xs"><span class="text-gray-700">JOB</span><span class="text-red-600">COMPASS</span></span>
      </div>
      <span>© 2026 JobCompass Inc. All rights reserved.</span>
      <div class="flex gap-4">
        <a routerLink="/about" class="hover:text-gray-600">About</a>
        <a routerLink="/careers" class="hover:text-gray-600">Careers</a>
        <a routerLink="/privacy" class="hover:text-gray-600">Privacy</a>
        <a routerLink="/terms" class="hover:text-gray-600">Terms</a>
        <a routerLink="/help" class="hover:text-gray-600">Help</a>
      </div>
    </footer>
  `
})
export class FooterComponent {}
