# conference-service

This role does the mostly-static work for running one of the conference websites. It sets up the user account,
creates the systemd unit, and so on. What it doesn't do is define *any* configuration, including the image version
to use. That's handled by a separate deployment playbook. This separation means we have a relatively lightweight 
playbook to run in the common case of just deploying a new version.