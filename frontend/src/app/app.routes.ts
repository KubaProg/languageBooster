import { Routes } from '@angular/router';

export const routes: Routes = [

    {
        path: 'app',
        loadComponent: () => import('./shared/components/main-layout/main-layout.component').then(m => m.MainLayoutComponent),
        // TODO: Add an AuthGuard here with canActivate
        children: [
            {
                path: 'collections',
                loadComponent: () => import('./features/collection-list/collection-list.component').then(m => m.CollectionListComponent)
            },
            {
                path: 'collections/new',
                loadComponent: () => import('./features/generation-form/generation-form.component').then(m => m.GenerationFormComponent)
            },
            // TODO: Add dashboard and collection list routes here
            {
                path: '',
                redirectTo: 'collections/new',
                pathMatch: 'full'
            }
        ]
    },
    {
        path: '',
        redirectTo: '/app',
        pathMatch: 'full'
    }
    // TODO: Add login/register routes here
];
