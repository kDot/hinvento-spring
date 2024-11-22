#!/bin/bash

# Variables
PROJECT_ID="xxx" # Replace with your desired project ID
BILLING_ACCOUNT_ID="xxx" # Replace with your Billing Account ID
SERVICE_ACCOUNT_NAME="cloud-run-deployer"
REGION="europe-west1" # Replace with your preferred region
REPO_NAME="docker-repo" # Replace with your desired repository name

# Create GCP project
gcloud projects create $PROJECT_ID --name="Hinvento Spring"
gcloud beta billing projects link $PROJECT_ID --billing-account=$BILLING_ACCOUNT_ID
gcloud config set project $PROJECT_ID

# Enable required APIs
gcloud services enable run.googleapis.com
gcloud services enable artifactregistry.googleapis.com
gcloud services enable cloudbuild.googleapis.com

# Create Service Account
gcloud iam service-accounts create $SERVICE_ACCOUNT_NAME \
    --description="Service Account for Cloud Run Deployment" \
    --display-name="Cloud Run Deployer"

# Assign roles to Service Account
gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member="serviceAccount:$SERVICE_ACCOUNT_NAME@$PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/run.admin"

gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member="serviceAccount:$SERVICE_ACCOUNT_NAME@$PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/storage.admin"

gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member="serviceAccount:$SERVICE_ACCOUNT_NAME@$PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/artifactregistry.admin"

# Generate Service Account key
gcloud iam service-accounts keys create ./gcp-key.json \
    --iam-account="$SERVICE_ACCOUNT_NAME@$PROJECT_ID.iam.gserviceaccount.com"

gcloud artifacts repositories create $REPO_NAME \
    --repository-format=docker \
    --location=$REGION \
    --description="Docker repository for Cloud Run deployment"

echo "Setup complete. The service account key is saved as gcp-key.json."