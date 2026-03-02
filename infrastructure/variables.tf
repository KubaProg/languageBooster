# variables.tf
variable "region" {
  type    = string
  default = "eu-central-1"
}

variable "db_username" {
  type      = string
  default   = "dbadmin"
  sensitive = true
}

variable "db_password" {
  type      = string
  default   = "Password123!" # In production, use secrets manager or env vars
  sensitive = true
}
