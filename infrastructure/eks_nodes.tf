# EKS Managed Node Group
# This creates the actual EC2 instances that join the cluster.
resource "aws_eks_node_group" "main" {
  cluster_name    = aws_eks_cluster.main.name
  node_group_name = "language-booster-nodes"
  node_role_arn   = aws_iam_role.eks_nodes.arn
  
  # The subnets where our servers will live.
  subnet_ids      = [
    aws_subnet.public_a.id,
    aws_subnet.public_b.id
  ]

  # CHEAPEST TIER SETTINGS:
  # 1. Use 't3.small' (cheaper than medium, stable enough for EKS)
  instance_types = ["t3.small"]
  
  # 2. Use 'SPOT' instances for ~70% savings vs On-Demand
  capacity_type  = "SPOT"

  scaling_config {
    desired_size = 1
    max_size     = 1 # Restricted to 1 to keep costs low
    min_size     = 1
  }

  # These depends_on blocks are critical for clean creation/deletion.
  depends_on = [
    aws_iam_role_policy_attachment.eks_worker_node_policy,
    aws_iam_role_policy_attachment.eks_cni_policy,
    aws_iam_role_policy_attachment.eks_ecr_policy,
  ]

  tags = {
    Name = "language-booster-worker-node"
  }
}
