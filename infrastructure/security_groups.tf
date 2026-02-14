resource "aws_security_group" "alb" {
  name        = "language-booster-alb-sg"
  description = "Security Group for Application Load Balancer"
  vpc_id      = aws_vpc.main.id

  # Inbound: Allow HTTP/HTTPS from the Internet
  ingress {
    description = "Allow HTTP from Internet"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow HTTPS from Internet"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Outbound: Allow all traffic (needed for ALB to reach Nodes)
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "language-booster-alb-sg"
  }
}

resource "aws_security_group" "nodes" {
  name        = "language-booster-nodes-sg"
  description = "Security Group for EKS Worker Nodes"
  vpc_id      = aws_vpc.main.id

  # Inbound: Allow traffic from ALB on app port 8080
  ingress {
    description     = "Allow 8080 from ALB"
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.alb.id]
  }

  # Outbound: Allow all outbound (ECR, RDS, Internet)
  # Nodes need to reach the Internet to pull Docker images (ECR/DockerHub)
  # and reach the RDS database in the private subnet.
  egress {
    description = "Allow all outbound (ECR, RDS, Internet)"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "language-booster-nodes-sg"
  }
}

resource "aws_security_group" "rds" {
  name        = "language-booster-rds-sg"
  description = "Security Group for RDS Postgres"
  vpc_id      = aws_vpc.main.id

  # Inbound: Allow Postgres port 5432 ONLY from Nodes SG
  ingress {
    description     = "Allow 5432 from Nodes"
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [aws_security_group.nodes.id]
  }

  tags = {
    Name = "language-booster-rds-sg"
  }
}
