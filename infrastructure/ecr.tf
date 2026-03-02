# ECR Repositories for Docker Images

# 1. Backend Repository
resource "aws_ecr_repository" "backend" {
  name                 = "language-booster-backend"
  image_tag_mutability = "MUTABLE"

  # Scan for vulnerabilities on push
  image_scanning_configuration {
    scan_on_push = true
  }
}

# 2. Frontend Repository
resource "aws_ecr_repository" "frontend" {
  name                 = "language-booster-frontend"
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }
}

# 3. Lifecycle Policy (Keep only the last 5 images to save costs)
resource "aws_ecr_lifecycle_policy" "backend" {
  repository = aws_ecr_repository.backend.name

  policy = jsonencode({
    rules = [
      {
        rulePriority = 1
        description  = "Keep last 5 images"
        selection = {
          tagStatus     = "any"
          countType     = "imageCountMoreThan"
          countNumber   = 5
        }
        action = {
          type = "expire"
        }
      }
    ]
  })
}

resource "aws_ecr_lifecycle_policy" "frontend" {
  repository = aws_ecr_repository.frontend.name

  policy = jsonencode({
    rules = [
      {
        rulePriority = 1
        description  = "Keep last 5 images"
        selection = {
          tagStatus     = "any"
          countType     = "imageCountMoreThan"
          countNumber   = 5
        }
        action = {
          type = "expire"
        }
      }
    ]
  })
}

# Outputs for easier use in CI/CD or CLI
output "ecr_backend_url" {
  value = aws_ecr_repository.backend.repository_url
}

output "ecr_frontend_url" {
  value = aws_ecr_repository.frontend.repository_url
}
