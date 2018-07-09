resource "digitalocean_ssh_key" "tech_key" {
  name       = "Admin SSH Key"
  public_key = "${file("keys/id_rsa.pub")}"
}

resource "google_service_account" "site" {
  account_id   = "bmunsite"
  display_name = "Website VM System Account"
}

resource "google_storage_bucket_iam_member" "site_account_registry_access" {
  bucket = "artifacts.${var.gcp_project_id}.appspot.com"
  role   = "roles/storage.objectViewer"
  member = "serviceAccount:${google_service_account.site.email}"
}

resource "google_project_iam_member" "site_account_storage_access" {
  role   = "roles/storage.objectViewer"
  member = "serviceAccount:${google_service_account.site.email}"
}

resource "google_service_account_key" "site_account_key" {
  service_account_id = "${google_service_account.site.name}"
  pgp_key            = "keybase:bnavetta"
}

output "google_site_account_key" {
  value = "${google_service_account_key.site_account_key.private_key_encrypted}"
}
