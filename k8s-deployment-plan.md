# Action Plan: Wdrożenie Kubernetes (EKS) - Language Booster

Ten dokument opisuje kroki niezbędne do przygotowania manifestów K8s i uruchomienia aplikacji na klastrze EKS zgodnie z architekturą opisaną w `infra-lb.pdf`.

## 1. Przygotowanie Struktury Katalogów
Utworzenie folderu `k8s/` w głównym katalogu projektu, aby utrzymać porządek w manifestach.

## 2. Manifesty Kubernetes (Kolejność tworzenia)
Przygotujemy następujące pliki:

1.  **`namespace.yaml`**: Izolacja zasobów aplikacji (namespace: `app`).
2.  **`secrets.yaml`**: Przechowywanie danych wrażliwych (host DB, login, hasło do RDS). *Uwaga: W środowisku produkcyjnym zalecane jest użycie AWS Secrets Manager z External Secrets Operator.*
3.  **`backend-deployment.yaml`**: 
    *   Definicja Podów dla Spring Boot.
    *   Konfiguracja `readinessProbe` i `livenessProbe`.
    *   Zmienne środowiskowe pobierane z `secrets`.
4.  **`backend-service.yaml`**: Serwis typu `ClusterIP` (komunikacja wewnętrzna).
5.  **`frontend-deployment.yaml`**: 
    *   Definicja Podów dla Angulara (serwowanego np. przez Nginx).
6.  **`frontend-service.yaml`**: Serwis typu `ClusterIP`.
7.  **`ingress.yaml`**: 
    *   Konfiguracja AWS Load Balancer Controller.
    *   Reguły routingu: `/api/*` -> backend, `/*` -> frontend.
    *   Adnotacje dla ALB (scheme: internet-facing, target-type: ip).

## 3. Integracja z ECR (Pobieranie obrazów)
*   Upewnienie się, że `IAM Role` przypisana do `EKS Node Group` posiada uprawnienia `AmazonEC2ContainerRegistryReadOnly`.
*   W Deploymentach użyjemy pełnych ścieżek do obrazów z ECR (np. `ACCOUNT_ID.dkr.ecr.REGION.amazonaws.com/lb-backend:latest`).

## 4. Kroki wdrożeniowe (Operations)
1.  **Połączenie z klastrem**: `aws eks update-kubeconfig --name language-booster-cluster --region <region>`.
2.  **Aplikacja manifestów**: `kubectl apply -f k8s/`.
3.  **Weryfikacja**:
    *   `kubectl get pods -n app`
    *   `kubectl get ingress -n app` (oczekiwanie na adres DNS od ALB).

## 5. Cykl życia aplikacji
*   Aktualizacja aplikacji będzie polegać na wypchnięciu nowego obrazu do ECR i wykonaniu `kubectl rollout restart deployment -n app`.
