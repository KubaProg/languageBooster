# Terraform Infrastructure Tutorial

This tutorial explains how to run and destroy the Terraform infrastructure for this project.

## How to run the infrastructure

1.  **Configure AWS Credentials**:
    Make sure you have your AWS credentials configured as environment variables in your terminal:
    ```bash
    export AWS_ACCESS_KEY_ID="your_access_key"
    export AWS_SECRET_ACCESS_KEY="your_secret_key"
    export AWS_DEFAULT_REGION="eu-central-1"
    ```

2.  **Navigate to the infrastructure directory**:
    ```bash
    cd infrastructure
    ```

3.  **Initialize Terraform**:
    This command downloads the necessary providers.
    ```bash
    terraform init
    ```

4.  **Apply the configuration**:
    This command creates the AWS resources defined in the configuration.
    ```bash
    terraform apply
    ```
    You will be prompted to confirm the changes. Type `yes` to proceed.

## How to destroy the infrastructure

1.  **Navigate to the infrastructure directory**:
    ```bash
    cd infrastructure
    ```

2.  **Destroy the resources**:
    This command will destroy all the resources created by Terraform.
    ```bash
    terraform destroy
    ```
    You will be prompted to confirm the destruction. Type `yes` to proceed.
