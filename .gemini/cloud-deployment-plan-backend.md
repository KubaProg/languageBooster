Create a **single “staging” backend** on **AWS eu-central-1**, using **AWS App Runner** as the compute platform,  
**ECR** for the Spring Boot container image, and **AWS Secrets Manager** for all sensitive configuration.  
Everything will be managed through **Terraform**, with deployments triggered from **GitHub Actions** using  
a dedicated **CI IAM user** that has long-lived AWS access keys stored in **GitHub Secrets**.

---

### Terraform Remote State Setup

Start by preparing Terraform remote state:
- Provision an **S3 bucket** (with versioning and server-side encryption enabled).
- Provision a **DynamoDB table** for state locking.
- Once created, configure Terraform’s backend block to use this S3 bucket and DynamoDB table so that  
  future applies are consistent and state conflicts are prevented.

Example naming:
- S3 bucket: `lb-tfstate-staging`
- DynamoDB table: `lb-tf-lock-staging`

Use consistent tags across all resources, for example:


Project = "lb"
Env = "staging"
Region = "eu-central-1"
Owner = "you"


---

### ECR Setup

Create an **ECR repository** named `lb-staging-api` with image scanning enabled on push.  
You will push a single tag, e.g. **staging-latest**, from the CI pipeline to trigger App Runner deployments.

Keep the image retention policy simple for now (default behavior).  
ECR will store the container images built from your Spring Boot project.

---

### Secrets Manager Setup

Prepare **AWS Secrets Manager** to hold all sensitive backend configuration.  
Create six secrets under a predictable path, e.g.:



lb/staging/SPRING_DATASOURCE_URL
lb/staging/SPRING_DATASOURCE_USERNAME
lb/staging/SPRING_DATASOURCE_PASSWORD
lb/staging/SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI
lb/staging/OPENROUTER_API_KEY
lb/staging/OPENROUTER_API_URL


Values come from:
- Supabase → Database connection URL, username, password.
- Supabase → JWT issuer URI (Auth → Settings).
- OpenRouter → API key (personal) and API URL (`https://openrouter.ai/api/v1`).

Do **not** store plaintext values in Terraform variables — instead,  
create empty secret containers via Terraform and populate values through the AWS Console or CLI.  
This keeps Terraform state free from sensitive content.

---

### IAM Setup

Create an **IAM role** for App Runner so it can:
1. **Pull images** from ECR (attach the policy `AWSAppRunnerServicePolicyForECRAccess`).
2. **Read secrets** from Secrets Manager (custom inline policy granting `secretsmanager:GetSecretValue`  
   only for your six specific secret ARNs).

Also create an **IAM user** for CI (e.g., `ci-staging`) with an inline policy that permits:
- ECR authentication (`ecr:GetAuthorizationToken`)
- ECR push (`ecr:PutImage`, `ecr:BatchCheckLayerAvailability`, etc.)
- Optionally, read/write access to the Terraform state S3 bucket and DynamoDB lock if you plan to run Terraform from CI.

Store that user’s AWS access key and secret key in **GitHub Secrets** (`AWS_ACCESS_KEY_ID` / `AWS_SECRET_ACCESS_KEY`).

---

### App Runner Service Setup

Create an **App Runner service** named `lb-staging-api` in `eu-central-1`.

Configure it to:
- Pull the image from the **ECR** repository (`:staging-latest`).
- Enable **auto deployments** so every new image automatically rolls out.
- Expose port **8080**.
- Start with **1 vCPU / 2 GB RAM** (the smallest instance) for cost efficiency.
- Run **a single instance** (min=1, max=1) to minimize spend.
- Use the **public endpoint** (no VPC ingress) — AWS will assign an HTTPS URL like  
  `https://xxxxxxxx.eu-central-1.awsapprunner.com`.

Inject secrets as runtime environment variables by referencing the corresponding  
**Secrets Manager ARNs** (App Runner fetches and exposes them securely at runtime).

Example mapping:
- `SPRING_DATASOURCE_URL` → Secret ARN for database URL
- `SPRING_DATASOURCE_USERNAME` → Secret ARN for database username
- `SPRING_DATASOURCE_PASSWORD` → Secret ARN for database password
- `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI` → Secret ARN for issuer
- `OPENROUTER_API_KEY` → Secret ARN for API key
- `OPENROUTER_API_URL` → Secret ARN for OpenRouter URL

Public values (like `SPRING_PROFILES_ACTIVE=prod`) can be injected directly as plain environment variables.

---

### GitHub Actions Integration

Set up a **backend CI pipeline** (`.github/workflows/api.yml`):

1. On push to `master`, checkout the repo.
2. Build the Spring Boot JAR (`mvn package -DskipTests`).
3. Build and tag a Docker image (`staging-latest`).
4. Log in to AWS ECR using credentials from GitHub Secrets.
5. Push the image to the ECR repository.
6. App Runner auto-deploys the new image once detected.

Required GitHub Secrets:

AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_REGION (eu-central-1)


App Runner will automatically redeploy with the latest image —  
no manual API calls or deploy commands are needed.

---

### Application Configuration Notes

- App Runner runs the container in a stateless environment;  
  persist all data in external services (Supabase database and storage).
- CORS: Allow all origins temporarily in Spring Boot for staging simplicity.
- Health checks: Configure Spring Boot’s `/actuator/health` endpoint  
  so App Runner can properly check container health.

---

### Runbook and Operations

- **First deployment**:
    1. Populate all Secrets Manager entries.
    2. Run `terraform apply` to create infrastructure.
    3. Push to `master` to trigger CI → build → push → auto-deploy.
    4. Wait until App Runner shows “Deployed successfully”.

- **Changing a secret**:  
  Update the secret in AWS Console → trigger a new App Runner deployment  
  (secrets are loaded at startup).

- **Rolling back**:  
  Re-push the previous image tag to ECR.

- **Pausing spend**:  
  Reduce to one small instance; App Runner cannot scale to zero but this keeps cost minimal.

- **Inspecting logs**:  
  Use the App Runner console → Logs tab (integrated CloudWatch logs).

- **Monitoring cost**:  
  One small instance (~$25/month), Secrets Manager (~$0.40/month total),  
  ECR minimal (~$1/month).

---

### Validation Steps

After the first deployment:
1. Visit the **App Runner public URL** (e.g., `https://xxxx.eu-central-1.awsapprunner.com/actuator/health`)  
   to confirm it returns `{"status":"UP"}`.
2. Test database connectivity by hitting an endpoint that performs a DB query.
3. Verify JWT validation with a Supabase-issued access token.
4. Trigger the OpenRouter endpoint to confirm outbound API connectivity.
5. Share the App Runner URL with the Vercel frontend to configure its `apiBaseUrl`.

---

### Future Improvements

- Restrict CORS to Vercel domain once stable.
- Move CI → AWS authentication to **OIDC** to remove long-lived keys.
- Introduce secret rotation policy if required later.
- Add CloudWatch alarms and metrics for uptime and errors.
- Optionally, add a custom domain (e.g., `api.example.com`) once production setup is needed.

---

This plan gives you a fully self-contained backend infrastructure:
- Terraform-managed, reproducible, low-cost environment.
- Secure secrets stored in AWS Secrets Manager.
- Automatic deploys through GitHub Actions and App Runner.
- Zero manual operations after initial setup.
