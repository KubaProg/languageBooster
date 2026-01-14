# Data source to fetch the list of available AWS availability zones in the current region.
# This ensures we distribute our subnets across different physical locations for high availability.
data "aws_availability_zones" "available" {
  state = "available"
}

# 1. VPC (Virtual Private Cloud)
# The isolated network environment where all our resources will live.
# CIDR 10.0.0.0/16 provides up to 65,536 private IP addresses.
resource "aws_vpc" "main" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true # Required for EKS and RDS to resolve internal DNS names.
  enable_dns_support   = true

  tags = {
    Name = "language-booster-vpc"
  }
}

# 2. Internet Gateway (IGW)
# This resource allows communication between the VPC and the public internet.
# It is the "door" that our public subnets will use to reach the world.
resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "language-booster-igw"
  }
}

# 3. Subnets
# We divide our VPC into subnets. Following the "no NAT" plan:
# - Public subnets will host EKS worker nodes and the Load Balancer (ALB).
# - Private subnets will host the RDS Postgres database.

# Public Subnet AZ-a
resource "aws_subnet" "public_a" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = data.aws_availability_zones.available.names[0]
  map_public_ip_on_launch = true # Instances in this subnet get a public IP by default.

  tags = {
    Name                     = "language-booster-public-a"
    "kubernetes.io/role/elb" = "1" # Tag required by AWS Load Balancer Controller to identify public subnets for internet-facing ALBs.
  }
}

# Public Subnet AZ-b
resource "aws_subnet" "public_b" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = data.aws_availability_zones.available.names[1]
  map_public_ip_on_launch = true

  tags = {
    Name                     = "language-booster-public-b"
    "kubernetes.io/role/elb" = "1" # Tag required by AWS Load Balancer Controller to identify public subnets for internet-facing ALBs.
  }
}

# Private Subnet AZ-a
resource "aws_subnet" "private_a" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.101.0/24"
  availability_zone = data.aws_availability_zones.available.names[0]

  tags = {
    Name                              = "language-booster-private-a"
    "kubernetes.io/role/internal-elb" = "1" # Tag for internal Load Balancers discovery.
  }
}

# Private Subnet AZ-b
resource "aws_subnet" "private_b" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.102.0/24"
  availability_zone = data.aws_availability_zones.available.names[1]

  tags = {
    Name                              = "language-booster-private-b"
    "kubernetes.io/role/internal-elb" = "1" # Tag for internal Load Balancers discovery.
  }
}

# 4. Route Tables
# These tables define where network traffic from the subnets is directed.

# Public Route Table
# This table has a route to the Internet Gateway, making any associated subnet "Public".
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id

  # 0.0.0.0/0 means "all traffic not matching local VPC CIDR".
  # We send it to the IGW.
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }

  tags = {
    Name = "language-booster-public-rt"
  }
}

# Private Route Table
# This table only has the default local route (10.0.0.0/16 -> local).
# No route to 0.0.0.0/0 (no NAT Gateway), so resources here cannot be reached from OR reach the internet.
resource "aws_route_table" "private" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "language-booster-private-rt"
  }
}

# 5. Route Table Associations
# Explicitly linking our subnets to their respective route tables.

# Link Public Subnets to Public Route Table
resource "aws_route_table_association" "public_a" {
  subnet_id      = aws_subnet.public_a.id
  route_table_id = aws_route_table.public.id
}

resource "aws_route_table_association" "public_b" {
  subnet_id      = aws_subnet.public_b.id
  route_table_id = aws_route_table.public.id
}

# Link Private Subnets to Private Route Table
resource "aws_route_table_association" "private_a" {
  subnet_id      = aws_subnet.private_a.id
  route_table_id = aws_route_table.private.id
}

resource "aws_route_table_association" "private_b" {
  subnet_id      = aws_subnet.private_b.id
  route_table_id = aws_route_table.private.id
}