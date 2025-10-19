terraform {
  backend "s3" {
    bucket         = "lb-tfstate-staging"
    key            = "terraform.tfstate"
    region         = "eu-central-1"
    dynamodb_table = "lb-tf-lock-staging"
  }

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "eu-central-1"
}
