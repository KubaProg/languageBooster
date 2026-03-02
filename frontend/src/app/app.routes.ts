import {Routes} from "@angular/router";

export const routes: Routes = [
    {
        path: 'app',
        loadComponent: () => import('./shared/components/main-layout/main-layout.component').then(m => m.MainLayoutComponent),
        children: [
            {
                path: 'collections',
                loadComponent: () => import('./features/collection-list/collection-list.component').then(m => m.CollectionListComponent)
            },
            {
                path: 'collections/new',
                loadComponent: () => import('./features/generation-form/generation-form.component').then(m => m.GenerationFormComponent)
            },
            {
                path: 'collections/:id/study',
                loadComponent: () => import('./features/collection-study/collection-study.component').then(m => m.CollectionStudyComponent)
            },
            {
                path: '',
                redirectTo: 'collections/new',
                pathMatch: 'full'
            }
        ]
    },
    {
        path: 'auth',
        redirectTo: 'app'
    },
    {
        path: '',
        redirectTo: '/app',
        pathMatch: 'full'
    }
];
