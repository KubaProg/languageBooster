variable "region" {
  type    = string
  default = "eu-central-1"
}

variable "state_bucket_name" {
  type        = string
  description = "S3 bucket name for Terraform remote state (must be globally unique)."
}

variable "lock_table_name" {
  type        = string
  description = "DynamoDB table name for Terraform state locking."
  default     = "language-booster-infra-lock-table"
}

# Best practice: keep backend resources hard to delete by accident.
# If you really want 'nuke everything', set this to false.
variable "prevent_backend_destroy" {
  type    = bool
  default = false
}
