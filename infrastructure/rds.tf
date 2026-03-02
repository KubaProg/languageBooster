# RDS Database Configuration

# 1. DB Subnet Group
# This defines which subnets the RDS instance can use.
# We use the private subnets to keep the database isolated from the internet.
resource "aws_db_subnet_group" "main" {
  name       = "language-booster-db-subnet-group"
  subnet_ids = [aws_subnet.private_a.id, aws_subnet.private_b.id]

  tags = {
    Name = "language-booster-db-subnet-group"
  }
}

# 2. RDS Postgres Instance
# Cost-saving configuration using t4g.micro.
resource "aws_db_instance" "main" {
  identifier = "language-booster-db"
  
  engine            = "postgres"
  engine_version    = "16.3" # Latest stable major version
  instance_class    = "db.t4g.micro" # Cost efficient ARM-based instance
  allocated_storage = 20
  storage_type      = "gp3"

  db_name  = "languagebooster"
  username = var.db_username
  password = var.db_password

  db_subnet_group_name   = aws_db_subnet_group.main.name
  vpc_security_group_ids = [aws_security_group.rds.id]
  
  # Network settings
  publicly_accessible = false
  skip_final_snapshot = true # Allows easy 'terraform destroy' for dev environments

  # Performance and backup (minimal for cost)
  backup_retention_period = 1
  multi_az               = false

  tags = {
    Name = "language-booster-db"
  }
}

# Output the endpoint for the backend to connect
output "db_endpoint" {
  value = aws_db_instance.main.endpoint
}
