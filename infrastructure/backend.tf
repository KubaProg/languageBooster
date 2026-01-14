terraform {
  backend "s3" {
    bucket         = "lbstatebucket"     # np. z outputu state_bucket_name
    key            = "prod/infra/terraform.tfstate"      # ścieżka pliku stanu w buckecie
    region         = "eu-central-1"                      # Twój region
    dynamodb_table = "language-booster-infra-lock-table" # np. z outputu lock_table_name
    encrypt        = true
  }
}
