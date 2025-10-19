resource "aws_s3_bucket" "tfstate" {
  bucket = "lb-tfstate-staging"

  tags = {
    Project = "lb"
    Env     = "staging"
    Region  = "eu-central-1"
    Owner   = "gemini"
  }
}

resource "aws_s3_bucket_versioning" "tfstate" {
  bucket = aws_s3_bucket.tfstate.id
  versioning_configuration {
    status = "Enabled"
  }
}

resource "aws_s3_bucket_server_side_encryption_configuration" "tfstate" {
  bucket = aws_s3_bucket.tfstate.id
  rule {
    apply_server_side_encryption_by_default {
      sse_algorithm = "AES256"
    }
  }
}

resource "aws_dynamodb_table" "tflock" {
  name           = "lb-tf-lock-staging"
  read_capacity  = 1
  write_capacity = 1
  hash_key       = "LockID"

  attribute {
    name = "LockID"
    type = "S"
  }

  tags = {
    Project = "lb"
    Env     = "staging"
    Region  = "eu-central-1"
    Owner   = "gemini"
  }
}
