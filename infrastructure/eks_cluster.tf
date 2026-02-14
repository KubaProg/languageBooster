# The EKS Cluster (Control Plane)
resource "aws_eks_cluster" "main" {
  name     = "language-booster-cluster"
  role_arn = aws_iam_role.eks_cluster.arn

  vpc_config {
    # The cluster needs to be connected to at least two subnets in different AZs.
    # We use our public subnets.
    subnet_ids = [
      aws_subnet.public_a.id,
      aws_subnet.public_b.id
    ]
    
    # For simplicity and cost-saving in this project, we'll keep the 
    # API server endpoint public.
    endpoint_public_access = true
  }

  # Ensure that IAM Role permissions are created before and deleted after EKS Cluster handling.
  # Otherwise, EKS will not be able to properly delete EKS managed EC2 infrastructure such as Security Groups.
  depends_on = [
    aws_iam_role_policy_attachment.eks_cluster_policy
  ]
}
